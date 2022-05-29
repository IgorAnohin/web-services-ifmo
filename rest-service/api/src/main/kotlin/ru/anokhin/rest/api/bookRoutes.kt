package ru.anokhin.rest.api

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.util.pipeline.PipelineContext
import java.io.PrintWriter
import java.io.StringWriter
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import ru.anokhin.core.ErrorCodes
import ru.anokhin.core.exception.ServiceException
import ru.anokhin.core.model.dto.BookSaveDto
import ru.anokhin.core.service.BookService
import ru.anokhin.rest.api.kotlin.extension.asErrorResponse
import ru.anokhin.rest.api.model.Book
import ru.anokhin.rest.api.model.BookCreationRequest
import ru.anokhin.rest.api.model.BookFilter
import ru.anokhin.rest.api.model.BookUpdateRequest
import ru.anokhin.rest.api.model.ErrorModel
import ru.anokhin.rest.api.model.ErrorResponse

private typealias ApiBookFilter = BookFilter
private typealias ModelBookFilter = ru.anokhin.core.model.dto.BookFilter

private typealias ApiBook = Book
private typealias ModelBook = ru.anokhin.core.model.dto.BookDto

fun Route.bookRoutes(bookService: BookService) {

    route("/books") {

        post {
            runService {
                val request = call.receive<BookCreationRequest>()
                bookService.save(
                    BookSaveDto(
                        name = request.name,
                        authors = request.authors,
                        publisher = request.publisher,
                        publicationDate = request.publicationDate.toJavaLocalDate(),
                        pageCount = request.pageCount,
                    )
                ).let(::toApiBook)
            }
        }

        get("{id?}") {
            runService {
                val id = call.parameters["id"]?.toLong() ?: return@get call.respondText(
                    "Missing id",
                    status = HttpStatusCode.BadRequest
                )
                bookService.findById(id)
                    .let(::toApiBook)
            }
        }

        post("/find-by-filter") {
            callService(
                fn = {
                    val filter = call.receive<ApiBookFilter>()
                    bookService.findByFilter(
                        ModelBookFilter(
                            name = filter.name,
                            author = filter.author,
                            publisher = filter.publisher,
                            publicationDateFrom = filter.publicationDateFrom?.toJavaLocalDate(),
                            publicationDateTo = filter.publicationDateTo?.toJavaLocalDate(),
                            pageCountFrom = filter.pageCountFrom,
                            pageCountTo = filter.pageCountTo,
                        )
                    ).map(::toApiBook)
                },
                callback = { foundBooks: List<ApiBook> ->
                    if (foundBooks.isEmpty()) {
                        call.respond(HttpStatusCode.NotFound)
                    } else {
                        call.respond(foundBooks)
                    }
                }
            )
        }

        put("{id?}") {
            runService {
                val id = call.parameters["id"]?.toLong() ?: return@put call.respondText(
                    "Missing id",
                    status = HttpStatusCode.BadRequest
                )
                val request = call.receive<BookUpdateRequest>()

                bookService.save(
                    BookSaveDto(
                        id = id,
                        name = request.name,
                        authors = request.authors,
                        publisher = request.publisher,
                        publicationDate = request.publicationDate.toJavaLocalDate(),
                        pageCount = request.pageCount,
                    )
                ).let(::toApiBook)
            }
        }

        delete("{id?}") {
            callService(
                fn = {
                    val id = call.parameters["id"]?.toLong() ?: return@delete call.respondText(
                        "Missing id",
                        status = HttpStatusCode.BadRequest
                    )
                    bookService.remove(id)
                },
                callback = { removed ->
                    if (removed) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }
            )
        }
    }
}

private fun toApiBook(entity: ModelBook): ApiBook = ApiBook(
    id = entity.id,
    name = entity.name,
    authors = entity.authors,
    publisher = entity.publisher,
    publicationDate = entity.publicationDate.toKotlinLocalDate(),
    pageCount = entity.pageCount,
)

private suspend inline fun <reified T : Any> PipelineContext<Unit, ApplicationCall>.runService(
    fn: PipelineContext<Unit, ApplicationCall>.() -> T,
): Unit = callService(
    fn = fn,
    callback = { result -> call.respond(result) }
)

private suspend inline fun <T> PipelineContext<Unit, ApplicationCall>.callService(
    fn: PipelineContext<Unit, ApplicationCall>.() -> T,
    callback: PipelineContext<Unit, ApplicationCall>.(T) -> Unit = {},
): Unit = try {
    callback.invoke(this, fn())
} catch (ex: ServiceException) {
    val errorResponse = ex.asErrorResponse()
    call.respond(
        HttpStatusCode.BadRequest,
        errorResponse
    )
} catch (ex: Exception) {
    call.respond(
        HttpStatusCode.InternalServerError,
        ErrorResponse(
            ErrorModel(
                code = ErrorCodes.Books001UnknownError.code,
                message = buildString {
                    append(ex.javaClass.canonicalName)
                    if (ex.message != null) {
                        append(": ", ex.message)
                    }
                    val stackTraceWriter = StringWriter().also { sw ->
                        ex.printStackTrace(
                            PrintWriter(sw)
                        )
                    }
                    append(stackTraceWriter.toString())
                }
            )
        )
    )
}
