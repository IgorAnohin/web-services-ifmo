package ru.anokhin.rest.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.Charsets
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import ru.anokhin.rest.api.kotlin.extension.asServiceException
import ru.anokhin.rest.api.model.Book
import ru.anokhin.rest.api.model.BookCreationRequest
import ru.anokhin.rest.api.model.BookFilter
import ru.anokhin.rest.api.model.BookUpdateRequest
import ru.anokhin.rest.api.model.ErrorResponse

private fun isValidatedStatusCode(status: HttpStatusCode): Boolean = when {
    status.isSuccess() -> false
    (status == HttpStatusCode.NotFound) -> false
    else -> true
}

private fun HttpResponse.hasServiceException(): Boolean {
    if (status == HttpStatusCode.BadRequest) return true
    return false
}

class BookClient {

    private val client: HttpClient = HttpClient(CIO) {
        defaultRequest {
            url("http://0.0.0.0:8080/rest/api/v1/books")
        }
        Charsets {
            register(Charsets.UTF_8)
        }
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                }
            )
        }

        HttpResponseValidator {
            validateResponse block@{ response ->
                if (!isValidatedStatusCode(response.status)) {
                    return@block
                }

                if (response.hasServiceException()) {
                    val errorResponse: ErrorResponse = response.body()
                    throw errorResponse.asServiceException()
                }
            }
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
    }

    fun create(request: BookCreationRequest): Book = runBlocking {
        client.post() {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    fun update(
        bookId: Long,
        request: BookUpdateRequest,
    ): Book = runBlocking {
        client.put("/$bookId") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    fun findByFilter(filter: BookFilter): List<Book> = runBlocking {
        val response = client.post("/find-by-filter") {
            contentType(ContentType.Application.Json)
            setBody(filter)
        }
        if (response.status == HttpStatusCode.NotFound) {
            emptyList()
        } else {
            response.body()
        }
    }

    fun findById(id: Long): Book = runBlocking {
        client.get("/$id").body()
    }

    fun deleteById(id: Long): Boolean = runBlocking {
        val response = client.delete("/$id")
        when (response.status) {
            HttpStatusCode.OK -> true
            HttpStatusCode.NotFound -> false
            else -> error("Unknown response status: ${response.status}")
        }
    }
}
