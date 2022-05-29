package ru.anokhin.rest.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.Charsets
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
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import ru.anokhin.rest.api.model.Book
import ru.anokhin.rest.api.model.BookCreationRequest
import ru.anokhin.rest.api.model.BookFilter
import ru.anokhin.rest.api.model.BookUpdateRequest

class BookClient {

    private val client: HttpClient = HttpClient(CIO) {
        expectSuccess = true
        defaultRequest {
            url {
                protocol = URLProtocol.HTTP
                host = "0.0.0.0"
                port = 4242
                path("books")
            }
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
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
    }

    fun create(request: BookCreationRequest): Book = runBlocking {
        client.post("/books") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    fun update(
        bookId: Long,
        request: BookUpdateRequest,
    ): Book = runBlocking {
        client.put("/books/$bookId") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    fun findByFilter(filter: BookFilter): List<Book> = runBlocking {
        client.post("/find-by-filter") {
            contentType(ContentType.Application.Json)
            setBody(filter)
        }.body()
    }

    fun findById(id: Long): Book = runBlocking {
        client.get("/books/$id").body()
    }

    fun deleteById(id: Long): Boolean = runBlocking {
        val response = client.delete("/books/$id")
        when (response.status) {
            HttpStatusCode.OK -> true
            HttpStatusCode.NotFound -> false
            else -> error("Unknown response status: ${response.status}")
        }
    }
}
