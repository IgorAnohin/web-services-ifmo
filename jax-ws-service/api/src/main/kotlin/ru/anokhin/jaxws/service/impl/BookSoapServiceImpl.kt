package ru.anokhin.jaxws.service.impl

import jakarta.inject.Inject
import jakarta.jws.WebService
import jakarta.xml.soap.SOAPFactory
import jakarta.xml.soap.SOAPFault
import jakarta.xml.ws.soap.SOAPFaultException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date
import javax.xml.namespace.QName
import ru.anokhin.core.exception.ServiceException
import ru.anokhin.core.model.dto.BookDto
import ru.anokhin.core.model.dto.BookFilter
import ru.anokhin.core.model.dto.BookSaveDto
import ru.anokhin.core.service.BookService
import ru.anokhin.jaxws.model.dto.BookSoapDto
import ru.anokhin.jaxws.service.BookSoapService

@WebService(name = "BookService", serviceName = "BookService")
class BookSoapServiceImpl @Inject constructor(
    private val bookService: BookService,
) : BookSoapService {

    override fun create(
        name: String,
        authors: List<String>,
        publisher: String,
        publicationDate: Date,
        pageCount: Int,
    ): BookSoapDto = interceptServiceException {
        bookService.save(
            BookSaveDto(
                id = null,
                name = name,
                authors = authors,
                publisher = publisher,
                publicationDate = toLocalDate(publicationDate),
                pageCount = pageCount
            )
        ).let(::toBookSoapDto)
    }

    override fun findById(id: Long): BookSoapDto = interceptServiceException {
        bookService.findById(id).let(::toBookSoapDto)
    }

    override fun findByFilter(
        name: String?,
        author: String?,
        publisher: String?,
        publicationDateFrom: Date?,
        publicationDateTo: Date?,
        pageCountFrom: Int?,
        pageCountTo: Int?,
    ): List<BookSoapDto> = interceptServiceException {
        bookService.findByFilter(
            BookFilter(
                name = name,
                author = author,
                publisher = publisher,
                publicationDateFrom = publicationDateFrom?.let(::toLocalDate),
                publicationDateTo = publicationDateTo?.let(::toLocalDate),
                pageCountFrom = pageCountFrom,
                pageCountTo = pageCountTo
            )
        ).map(::toBookSoapDto)
    }

    override fun update(
        id: Long,
        name: String,
        authors: List<String>,
        publisher: String,
        publicationDate: Date,
        pageCount: Int,
    ): BookSoapDto = interceptServiceException {
        bookService.save(
            BookSaveDto(
                id = id,
                name = name,
                authors = authors,
                publisher = publisher,
                publicationDate = toLocalDate(publicationDate),
                pageCount = pageCount
            )
        ).let(::toBookSoapDto)
    }

    override fun deleteById(id: Long): Boolean = interceptServiceException {
        bookService.remove(id)
    }

    private fun toBookSoapDto(entity: BookDto): BookSoapDto = BookSoapDto().apply {
        this.id = entity.id
        this.name = entity.name
        this.authors = entity.authors
        this.publisher = entity.publisher
        this.publicationDate = entity.publicationDate
        this.pageCount = entity.pageCount
    }

    private fun toLocalDate(date: Date): LocalDate = LocalDateTime
        .ofInstant(date.toInstant(), ZoneOffset.UTC)
        .toLocalDate()

    private fun <T> interceptServiceException(fn: () -> T): T =
        try {
            fn()
        } catch (ex: ServiceException) {
            val soapFactory: SOAPFactory = SOAPFactory.newInstance()
            val soapFault: SOAPFault = soapFactory.createFault(
                ServiceException::class.qualifiedName + "::" + ex.code.code,
                QName("http://schemas.xmlsoap.org/soap/envelope/", "Client")
            )
            throw SOAPFaultException(soapFault)
        }
}
