package ru.anokhin.jaxws

import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.Persistence
import jakarta.xml.ws.Endpoint
import mu.KLogger
import mu.KotlinLogging
import ru.anokhin.core.dao.BookDao
import ru.anokhin.core.dao.impl.StandaloneBookDao
import ru.anokhin.core.service.BookService
import ru.anokhin.core.service.impl.BookServiceImpl
import ru.anokhin.jaxws.service.BookSoapService
import ru.anokhin.jaxws.service.impl.BookSoapServiceImpl

private val logger: KLogger = KotlinLogging.logger {}

fun main(args: Array<String>) {
    System.setProperty(
        "com.sun.xml.ws.fault.SOAPFaultBuilder.disableCaptureStackTrace",
        "false"
    )

    val entityManagerFactory: EntityManagerFactory = Persistence.createEntityManagerFactory("ru.anokhin.web")
    val entityManager: EntityManager = entityManagerFactory.createEntityManager()

    val bookDao: BookDao = StandaloneBookDao(entityManager)
    val bookService: BookService = BookServiceImpl(bookDao)
    val bookSoapService: BookSoapService = BookSoapServiceImpl(bookService)

    val readServiceUrl = "http://127.0.0.1:8080/jaxws/BookService"
    Endpoint.publish(readServiceUrl, bookSoapService)
    logger.info { "Server started!" }
}
