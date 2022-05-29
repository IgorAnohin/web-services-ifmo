package ru.anokhin.jaxws.service.impl

import jakarta.inject.Inject
import jakarta.jws.WebService
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date
import ru.anokhin.core.model.dto.BookDto
import ru.anokhin.core.model.dto.BookFilter
import ru.anokhin.core.model.dto.BookSaveDto
import ru.anokhin.core.service.BookService
import ru.anokhin.jaxws.model.dto.BookSoapDto
import ru.anokhin.jaxws.service.BookSoapService

@WebService(name = "BookService", serviceName = "BookService")
class BookSoapServiceImpl : BookSoapService {

    @Inject
    lateinit var bookService: BookService

    override fun create(
        name: String,
        authors: List<String>,
        publisher: String,
        publicationDate: Date,
        pageCount: Int,
    ): BookSoapDto = bookService.save(
        BookSaveDto(
            id = null,
            name = name,
            authors = authors,
            publisher = publisher,
            publicationDate = toLocalDate(publicationDate),
            pageCount = pageCount
        )
    ).let(::toBookSoapDto)

    override fun findById(id: Long): BookSoapDto = bookService.findById(id).let(::toBookSoapDto)

    override fun findByFilter(
        name: String?,
        author: String?,
        publisher: String?,
        publicationDateFrom: Date?,
        publicationDateTo: Date?,
        pageCountFrom: Int?,
        pageCountTo: Int?,
    ): List<BookSoapDto> = bookService.findByFilter(
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

    override fun update(
        id: Long,
        name: String,
        authors: List<String>,
        publisher: String,
        publicationDate: Date,
        pageCount: Int,
    ): BookSoapDto = bookService.save(
        BookSaveDto(
            id = id,
            name = name,
            authors = authors,
            publisher = publisher,
            publicationDate = toLocalDate(publicationDate),
            pageCount = pageCount
        )
    ).let(::toBookSoapDto)

    override fun deleteById(id: Long): Boolean = bookService.remove(id)

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
}