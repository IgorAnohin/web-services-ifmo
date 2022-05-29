@file:JvmMultifileClass
@file:JvmName("KtorPlugins")

package ru.anokhin.rest.plugin

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.util.pipeline.PipelineContext
import io.ktor.util.pipeline.PipelineInterceptor
import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.Persistence
import ru.anokhin.core.dao.impl.CdiBookDao
import ru.anokhin.core.service.impl.BookServiceImpl
import ru.anokhin.rest.api.bookRoutes

// private suspend fun PipelineInterceptor<Unit, ApplicationCall>.redirectToLinkedIn() =
//    call.respondRedirect("https://www.linkedin.com/in/160r")

val redirectToLinkedIn = object : PipelineInterceptor<Unit, ApplicationCall> {
    override suspend fun invoke(ctx: PipelineContext<Unit, ApplicationCall>, p2: Unit) = with(ctx) {
        call.respondRedirect("https://www.linkedin.com/in/160r")
    }
}

@Suppress("unused")
fun Application.configureRouting() {
    routing {
        get("/", redirectToLinkedIn)
        route("/api/v1") {
            get("/", redirectToLinkedIn)
            bookRoutes(
                run {
                    val entityManagerFactory: EntityManagerFactory =
                        Persistence.createEntityManagerFactory("ru.anokhin.web")
                    val entityManager: EntityManager = entityManagerFactory.createEntityManager()

                    val bookDao = CdiBookDao().apply {
                        this.entityManager = entityManager
                    }
                    BookServiceImpl(bookDao)
                }
            )
        }
    }
}
