package ru.anokhin.jaxws

import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.Persistence
import jakarta.xml.ws.Endpoint
import mu.KLogger
import mu.KotlinLogging
import ru.anokhin.jaxws.dao.BookDao
import ru.anokhin.jaxws.dao.impl.BookDaoImpl
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

//    val factory: SessionFactory = try {
//        Configuration().configure().buildSessionFactory()
//    } catch (ex: Throwable) {
//        System.err.println("Failed to create sessionFactory object: ${ex.message}")
//        ex.printStackTrace()
//        throw ExceptionInInitializerError(ex)
//    }
//    val entityManager: EntityManager = factory.createEntityManager()

    val emf: EntityManagerFactory = Persistence.createEntityManagerFactory("ru.anokhin.jaxws")
    val entityManager: EntityManager = emf.createEntityManager()

    val bookDao: BookDao = BookDaoImpl(entityManager)
    val bookService: BookService = BookServiceImpl(bookDao)
    val bookSoapService: BookSoapService = BookSoapServiceImpl(bookService)

    val readServiceUrl = "http://127.0.0.1:8080/BookService"
    Endpoint.publish(readServiceUrl, bookSoapService)
    logger.info { "Server started!" }
}
