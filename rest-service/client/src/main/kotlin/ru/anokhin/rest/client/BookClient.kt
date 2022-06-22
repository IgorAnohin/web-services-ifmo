package ru.anokhin.rest.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.Charsets
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
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
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
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

private suspend fun HttpResponse.getErrorResponseOrNull(): ErrorResponse? =
    try {
        body()
    } catch (ex: Exception) {
        null
    }

class BookClient {

    private val client: HttpClient = HttpClient(CIO) {
        install(Auth) {
            basic {
                credentials {
                    BasicAuthCredentials(
                        username = "login",
                        password = "password",
                    )
                }
            }
        }
        defaultRequest {
            with(url) {
                protocol = URLProtocol.HTTP
                host = "0.0.0.0"
                port = 8080
                path("rest", "api", "v1", "books")
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

        HttpResponseValidator {
            validateResponse block@{ response ->
                if (!isValidatedStatusCode(response.status)) return@block

                response.getErrorResponseOrNull()
                    ?.asServiceException()
                    ?.let { throw it }
            }
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
    }

    fun create(request: BookCreationRequest): Book = runBlocking {
        client.post {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    fun update(
        bookId: Long,
        request: BookUpdateRequest,
    ): Book = runBlocking {
        client.put {
            url.appendPathSegments("books", bookId.toString())
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    fun find(filter: BookFilter): List<Book> = runBlocking {
        val response = client.get {
            url.apply {
                appendPathSegments("books")
                parameters.apply {
                    for ((key, value) in Json.decodeFromString<Map<String, String>>(Json.encodeToString(filter))) {
                        append(key, value)
                    }
                }
            }
            contentType(ContentType.Application.Json)
        }
        if (response.status == HttpStatusCode.NotFound) {
            emptyList()
        } else {
            response.body()
        }
    }

    fun findById(id: Long): Book = runBlocking {
        client.get { url.appendPathSegments("books", id.toString()) }
            .body()
    }

    fun deleteById(id: Long): Boolean = runBlocking {
        val response = client.delete { url.appendPathSegments("books", id.toString()) }
        when (response.status) {
            HttpStatusCode.OK -> true
            HttpStatusCode.NotFound -> false
            else -> error("Unknown response status: ${response.status}")
        }
    }
}
