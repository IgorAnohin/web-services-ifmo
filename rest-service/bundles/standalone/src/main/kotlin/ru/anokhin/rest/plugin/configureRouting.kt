package ru.anokhin.rest.plugin

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.util.pipeline.PipelineContext
import ru.anokhin.core.service.BookService
import ru.anokhin.rest.api.bookRoutes

private suspend fun PipelineContext<*, ApplicationCall>.redirectToLinkedIn() =
    call.respondRedirect("https://www.linkedin.com/in/160r")

fun Application.configureRouting(bookService: BookService) {
    routing {
        get("/") { redirectToLinkedIn() }
        route("/api/v1") {
            get("/") { redirectToLinkedIn() }
            bookRoutes(bookService)
        }
    }
}
