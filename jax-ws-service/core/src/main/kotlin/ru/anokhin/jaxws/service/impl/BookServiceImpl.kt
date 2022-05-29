package ru.anokhin.jaxws.service.impl

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import java.time.LocalDate
import mu.KLogger
import mu.toKLogger
import org.slf4j.LoggerFactory
import ru.anokhin.jaxws.dao.BookDao
import ru.anokhin.jaxws.model.dto.BookDto
import ru.anokhin.jaxws.model.dto.BookFilter
import ru.anokhin.jaxws.model.dto.BookSaveDto
import ru.anokhin.jaxws.model.jpa.Book
import ru.anokhin.jaxws.service.BookService

class BookServiceImpl constructor(

    private val bookDao: BookDao,
) : BookService {

    private val logger: KLogger = LoggerFactory.getLogger(this::class.java).toKLogger()

    override fun save(book: BookSaveDto): BookDto {
        logger.entry(book)
        val entity = Book().apply {
            this.id = book.id
            this.name = book.name
            this.authors = book.authors
            this.pageCount = book.pageCount
            this.publisher = book.publisher
            this.publicationDate = book.publicationDate
        }
        return bookDao.save(entity)
            .let(::toBookDto)
            .also {
                logger.info { "Created [$it]" }
                logger.exit(it)
            }
    }

    override fun findByFilter(filter: BookFilter): List<BookDto> {
        logger.entry(filter)
        val params = toParamsMap(filter)
        val entityList = bookDao.findByCondition(params, createPredicateBuilder(params))
        return entityList.map(::toBookDto)
            .also(logger::exit)
    }

    override fun remove(id: Long): Boolean = bookDao.remove(id).also { removed ->
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
                    cb.lower(root[Book.AUTHORS]),
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
