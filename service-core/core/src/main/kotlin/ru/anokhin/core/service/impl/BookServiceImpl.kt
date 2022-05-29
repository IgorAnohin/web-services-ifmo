package ru.anokhin.core.service.impl

import jakarta.inject.Inject
import jakarta.inject.Singleton
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import java.time.LocalDate
import mu.KLogger
import mu.toKLogger
import org.slf4j.LoggerFactory
import ru.anokhin.core.ErrorCodes
import ru.anokhin.core.dao.BookDao
import ru.anokhin.core.exception.ServiceException
import ru.anokhin.core.model.dto.BookDto
import ru.anokhin.core.model.dto.BookFilter
import ru.anokhin.core.model.dto.BookSaveDto
import ru.anokhin.core.model.jpa.Book
import ru.anokhin.core.service.BookService

@Singleton
class BookServiceImpl @Inject constructor(
    private val bookDao: BookDao,
) : BookService {

    private val logger: KLogger = LoggerFactory.getLogger(this::class.java).toKLogger()

    override fun save(book: BookSaveDto): BookDto {
        logger.entry(book)

        if (book.name.isBlank()) {
            throw ServiceException(ErrorCodes.Books002NameIsBlank).also(logger::throwing)
        }
        if (book.authors.isEmpty()) {
            throw ServiceException(ErrorCodes.Books003AuthorsListIsEmpty).also(logger::throwing)
        }
        if (book.authors.any(String::isBlank)) {
            throw ServiceException(ErrorCodes.Books004AuthorsIsBlank).also(logger::throwing)
        }
        if (book.publisher.isBlank()) {
            throw ServiceException(ErrorCodes.Books005PublisherIsBlank).also(logger::throwing)
        }
        if (book.pageCount <= 0) {
            throw ServiceException(ErrorCodes.Books006PageCountIsNotPositive).also(logger::throwing)
        }

        val entity = Book().apply {
            this.id = book.id
            this.name = book.name
            this.authors = book.authors
            this.pageCount = book.pageCount
            this.publisher = book.publisher
            this.publicationDate = book.publicationDate
        }
        return withDao { save(entity) }
            .let(::toBookDto)
            .also {
                logger.info { "Created [$it]" }
                logger.exit(it)
            }
    }

    override fun findById(id: Long): BookDto {
        logger.entry(id)
        val entity: Book = withDao { findById(id) } ?: run {
            throw ServiceException(ErrorCodes.Books007EntityNotFound)
                .also(logger::throwing)
        }
        return entity.let(::toBookDto).also(logger::exit)
    }

    override fun findByFilter(filter: BookFilter): List<BookDto> {
        logger.entry(filter)
        val params = toParamsMap(filter)
        val entityList = withDao { findByCondition(params, createPredicateBuilder(params)) }
        return entityList.map(::toBookDto)
            .also(logger::exit)
    }

    override fun remove(id: Long): Boolean = withDao { remove(id) }
        .also { removed ->
            if (removed) {
                logger.info { "Removed Book(id=$id)" }
            }
        }

    private fun createPredicateBuilder(
        params: Map<String, Any>,
    ): (CriteriaBuilder, CriteriaQuery<Book>, Root<Book>) -> Predicate = { cb, cq, root ->
        val predicates = buildList<Predicate> {
            if (Params.NAME_LIKE in params) add(
                cb.like(
                    cb.lower(root[Book.NAME]),
                    cb.parameter(String::class.java, Params.NAME_LIKE)
                )
            )
            if (Params.AUTHORS_LIKE in params) add(
                cb.like(
                    cb.lower(root.get<List<String>>(Book.AUTHORS).`as`(String::class.java)),
                    cb.parameter(String::class.java, Params.AUTHORS_LIKE)
                )
            )
            if (Params.PUBLISHER_LIKE in params) add(
                cb.like(
                    cb.lower(root[Book.PUBLISHER]),
                    cb.parameter(String::class.java, Params.PUBLISHER_LIKE)
                )
            )
            if (Params.PUBLICATION_DATE_FROM in params) add(
                cb.greaterThanOrEqualTo(
                    root[Book.PUBLICATION_DATE],
                    cb.parameter(LocalDate::class.java, Params.PUBLICATION_DATE_FROM)
                )
            )
            if (Params.PUBLICATION_DATE_TO in params) add(
                cb.lessThanOrEqualTo(
                    root[Book.PUBLICATION_DATE],
                    cb.parameter(LocalDate::class.java, Params.PUBLICATION_DATE_TO)
                )
            )
            if (Params.PAGE_COUNT_FROM in params) add(
                cb.greaterThanOrEqualTo(
                    root[Book.PAGE_COUNT],
                    cb.parameter(Int::class.javaPrimitiveType, Params.PAGE_COUNT_FROM)
                )
            )
            if (Params.PAGE_COUNT_TO in params) add(
                cb.lessThanOrEqualTo(
                    root[Book.PAGE_COUNT],
                    cb.parameter(Int::class.javaPrimitiveType, Params.PAGE_COUNT_TO)
                )
            )
        }
        cb.and(*predicates.toTypedArray())
    }

    private fun toBookDto(entity: Book): BookDto = BookDto(
        id = entity.id!!,
        name = entity.name!!,
        authors = entity.authors,
        publisher = entity.publisher!!,
        publicationDate = entity.publicationDate!!,
        pageCount = entity.pageCount!!,
    )

    private fun toParamsMap(filter: BookFilter): Map<String, Any> = buildMap {
        filter.name?.let { put(Params.NAME_LIKE, '%' + it.lowercase() + '%') }
        filter.author?.let { put(Params.AUTHORS_LIKE, '%' + it.lowercase() + '%') }
        filter.publisher?.let { put(Params.PUBLISHER_LIKE, '%' + it.lowercase() + '%') }
        filter.publicationDateFrom?.let { put(Params.PUBLICATION_DATE_FROM, it) }
        filter.publicationDateTo?.let { put(Params.PUBLICATION_DATE_TO, it) }
        filter.pageCountFrom?.let { put(Params.PAGE_COUNT_FROM, it) }
        filter.pageCountTo?.let { put(Params.PAGE_COUNT_TO, it) }
    }

    private fun <T> withDao(closure: BookDao.() -> T): T =
        try {
            closure(bookDao)
        } catch (ex: Exception) {
            throw ServiceException(ErrorCodes.Books001UnknownError, ex)
        }

    private companion object {

        private object Params {

            const val NAME_LIKE: String = "nameLike"

            const val AUTHORS_LIKE: String = "authorsLike"

            const val PUBLISHER_LIKE: String = "publisherLike"

            const val PUBLICATION_DATE_FROM: String = "publicationDateFrom"

            const val PUBLICATION_DATE_TO: String = "publicationDateTo"

            const val PAGE_COUNT_FROM: String = "pageCountFrom"

            const val PAGE_COUNT_TO: String = "pageCountTo"
        }
    }
}
