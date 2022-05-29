package ru.anokhin.jaxws

import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.Persistence
import jakarta.xml.ws.Endpoint
import mu.KLogger
import mu.KotlinLogging
import ru.anokhin.jaxws.dao.BookDao
import ru.anokhin.jaxws.dao.impl.StandaloneBookDao
import ru.anokhin.jaxws.service.BookService
import ru.anokhin.jaxws.service.BookSoapService
import ru.anokhin.jaxws.service.impl.BookServiceImpl
import ru.anokhin.jaxws.service.impl.BookSoapServiceImpl

private val logger: KLogger = KotlinLogging.logger {}

fun main(args: Array<String>) {
    System.setProperty(
        "com.sun.xml.ws.fault.SOAPFaultBuilder.disableCaptureStackTrace",
        "false"
    )

    val entityManagerFactory: EntityManagerFactory = Persistence.createEntityManagerFactory("ru.anokhin.jaxws")
    val entityManager: EntityManager = entityManagerFactory.createEntityManager()

    val bookDao: BookDao = StandaloneBookDao().apply {
        this.entityManager = entityManager
    }
    val bookService: BookService = BookServiceImpl().apply {
        this.bookDao = bookDao
    }
    val bookSoapService: BookSoapService = BookSoapServiceImpl().apply {
        this.bookService = bookService
    }

    val readServiceUrl = "http://127.0.0.1:8080/jaxws/BookService"
    Endpoint.publish(readServiceUrl, bookSoapService)
    logger.info { "Server started!" }
}
