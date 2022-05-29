package ru.anokhin.rest

import io.ktor.http.HttpStatusCode
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
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import ru.anokhin.core.model.dto.BookSaveDto
import ru.anokhin.core.service.BookService
import ru.anokhin.rest.api.model.Book
import ru.anokhin.rest.api.model.BookCreationRequest
import ru.anokhin.rest.api.model.BookFilter
import ru.anokhin.rest.api.model.BookUpdateRequest

private typealias ApiBookFilter = BookFilter
private typealias ModelBookFilter = ru.anokhin.core.model.dto.BookFilter

private typealias ApiBook = Book
private typealias ModelBook = ru.anokhin.core.model.dto.BookDto

fun toApiBook(entity: ModelBook): ApiBook = ApiBook(
    id = entity.id,
    name = entity.name,
    authors = entity.authors,
    publisher = entity.publisher,
    publicationDate = entity.publicationDate.toKotlinLocalDate(),
    pageCount = entity.pageCount,
)

fun Route.bookRoutes(bookService: BookService) {

    route("/books") {

        post {
            val request = call.receive<BookCreationRequest>()
            val createdBook = bookService.save(
                BookSaveDto(
                    name = request.name,
                    authors = request.authors,
                    publisher = request.publisher,
                    publicationDate = request.publicationDate.toJavaLocalDate(),
                    pageCount = request.pageCount,
                )
            ).let(::toApiBook)

            call.respond(createdBook)
        }

        get("{id?}") {
            val id = call.parameters["id"]?.toLong() ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val foundBook = bookService.findById(id)
                .let(::toApiBook)
            call.respond(foundBook)
        }

        post("find-by-filter") {
            val filter = call.receive<BookFilter>()
            val foundBooks: List<ApiBook> = bookService.findByFilter(
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

            if (foundBooks.isEmpty()) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(foundBooks)
            }
        }

        put("{id?}") {
            val id = call.parameters["id"]?.toLong() ?: return@put call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val request = call.receive<BookUpdateRequest>()

            val updatedBook: ApiBook = bookService.save(
                BookSaveDto(
                    id = id,
                    name = request.name,
                    authors = request.authors,
                    publisher = request.publisher,
                    publicationDate = request.publicationDate.toJavaLocalDate(),
                    pageCount = request.pageCount,
                )
            ).let(::toApiBook)
            call.respond(updatedBook)
        }

        delete("{id?}") {
            val id = call.parameters["id"]?.toLong() ?: return@delete call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val removed = bookService.remove(id)
            if (removed) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}
