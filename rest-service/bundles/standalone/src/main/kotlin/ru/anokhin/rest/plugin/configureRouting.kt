package ru.anokhin.rest.plugin

import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import ru.anokhin.core.service.BookService
import ru.anokhin.rest.bookRoutes

fun Application.configureRouting(bookService: BookService) {
    routing {
        bookRoutes(bookService)
        get("/") {
            call.respondRedirect("https://www.linkedin.com/in/160r")
        }
    }
}
