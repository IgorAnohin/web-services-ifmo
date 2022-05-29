package ru.anokhin.rest

import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.Persistence
import mu.KLogger
import mu.KotlinLogging
import ru.anokhin.core.dao.BookDao
import ru.anokhin.core.dao.impl.StandaloneBookDao
import ru.anokhin.core.service.BookService
import ru.anokhin.core.service.impl.BookServiceImpl
import ru.anokhin.rest.plugin.configureHttp
import ru.anokhin.rest.plugin.configureMonitoring
import ru.anokhin.rest.plugin.configureRouting
import ru.anokhin.rest.plugin.configureSerialization

private val logger: KLogger = KotlinLogging.logger {}

fun main() {
    val entityManagerFactory: EntityManagerFactory = Persistence.createEntityManagerFactory("ru.anokhin.jaxws")
    val entityManager: EntityManager = entityManagerFactory.createEntityManager()

    val bookDao: BookDao = StandaloneBookDao().apply {
        this.entityManager = entityManager
    }
    val bookService: BookService = BookServiceImpl().apply {
        this.bookDao = bookDao
    }

    embeddedServer(Netty, port = 4242, host = "0.0.0.0") {
        configureRouting(bookService)
        configureSerialization()
        configureMonitoring()
        configureHttp()
    }.start(wait = true)

    logger.info { "Server started!" }
}
