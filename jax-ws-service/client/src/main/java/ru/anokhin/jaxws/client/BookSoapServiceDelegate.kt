package ru.anokhin.jaxws.client

import java.time.LocalDate
import java.util.Date
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
    ): ModelBookSoapDto = bookService.create(
        name,
        authors,
        publisher,
        publicationDate.toGregorianCalendar(),
        pageCount
    ).let(::toModelDto)

    override fun findById(id: Long): ModelBookSoapDto = bookService.findById(id).let(::toModelDto)

    override fun findByFilter(
        name: String?,
        author: String?,
        publisher: String?,
        publicationDateFrom: Date?,
        publicationDateTo: Date?,
        pageCountFrom: Int?,
        pageCountTo: Int?,
    ): List<ModelBookSoapDto> = bookService.findByFilter(
        name,
        author,
        publisher,
        publicationDateFrom?.toGregorianCalendar(),
        publicationDateTo?.toGregorianCalendar(),
        pageCountFrom,
        pageCountTo
    ).map(::toModelDto)

    override fun update(
        id: Long,
        name: String,
        authors: List<String>,
        publisher: String,
        publicationDate: Date,
        pageCount: Int,
    ): ModelBookSoapDto = bookService.update(
        id,
        name,
        authors,
        publisher,
        publicationDate.toGregorianCalendar(),
        pageCount
    ).let(::toModelDto)

    override fun deleteById(id: Long): Boolean = bookService.deleteById(id)

    private fun toModelDto(entity: JaxWsBookSoapDto): ModelBookSoapDto = ModelBookSoapDto().apply {
        this.id = entity.id
        this.name = entity.name
        this.authors = entity.authors
        this.publisher = entity.publisher
        this.publicationDate = entity.publicationDate.run { LocalDate.of(year, month, day) }
        this.pageCount = entity.pageCount
    }
}
