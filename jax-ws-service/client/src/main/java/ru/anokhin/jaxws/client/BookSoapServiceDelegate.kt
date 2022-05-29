package ru.anokhin.jaxws.client

import com.sun.xml.ws.fault.ServerSOAPFaultException
import java.time.LocalDate
import java.util.Date
import ru.anokhin.core.ErrorCodeRepository
import ru.anokhin.core.exception.ServiceException
import ru.anokhin.jaxws.service.BookSoapService

private typealias JaxWsBookSoapDto = ru.anokhin.jaxws.client.BookSoapDto
private typealias ModelBookSoapDto = ru.anokhin.jaxws.model.dto.BookSoapDto

class BookSoapServiceDelegate(
    private val bookService: BookService,
) : BookSoapService {

    override fun create(
        name: String,
        authors: List<String>,
        publisher: String,
        publicationDate: Date,
        pageCount: Int,
    ): ModelBookSoapDto = interceptException {
        bookService.create(
            name,
            authors,
            publisher,
            publicationDate.toGregorianCalendar(),
            pageCount
        ).let(::toModelDto)
    }

    override fun findById(id: Long): ModelBookSoapDto = interceptException {
        bookService.findById(id).let(::toModelDto)
    }

    override fun findByFilter(
        name: String?,
        author: String?,
        publisher: String?,
        publicationDateFrom: Date?,
        publicationDateTo: Date?,
        pageCountFrom: Int?,
        pageCountTo: Int?,
    ): List<ModelBookSoapDto> = interceptException {
        bookService.findByFilter(
            name,
            author,
            publisher,
            publicationDateFrom?.toGregorianCalendar(),
            publicationDateTo?.toGregorianCalendar(),
            pageCountFrom,
            pageCountTo
        ).map(::toModelDto)
    }

    override fun update(
        id: Long,
        name: String,
        authors: List<String>,
        publisher: String,
        publicationDate: Date,
        pageCount: Int,
    ): ModelBookSoapDto = interceptException {
        bookService.update(
            id,
            name,
            authors,
            publisher,
            publicationDate.toGregorianCalendar(),
            pageCount
        ).let(::toModelDto)
    }

    override fun deleteById(id: Long): Boolean = interceptException {
        bookService.deleteById(id)
    }

    private fun toModelDto(entity: JaxWsBookSoapDto): ModelBookSoapDto = ModelBookSoapDto().apply {
        this.id = entity.id
        this.name = entity.name
        this.authors = entity.authors
        this.publisher = entity.publisher
        this.publicationDate = entity.publicationDate.run { LocalDate.of(year, month, day) }
        this.pageCount = entity.pageCount
    }

    private fun <T> interceptException(fn: () -> T): T =
        try {
            fn()
        } catch (ex: ServerSOAPFaultException) {
            val faultString = ex.fault.faultString ?: throw ex

            val prefix = ServiceException::class.java.canonicalName + "::"
            if (!faultString.startsWith(prefix)) throw ex

            val errorCodeStr = faultString.removePrefix(prefix)
            val errorCode = ErrorCodeRepository.findByCode(errorCodeStr) ?: throw ex
            throw ServiceException(errorCode)
        }
}
