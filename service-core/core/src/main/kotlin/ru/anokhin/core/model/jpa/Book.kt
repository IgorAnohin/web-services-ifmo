package ru.anokhin.core.model.jpa

import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate
import ru.anokhin.core.converter.StringListAttributeConverter

@Entity
@Table(name = "book")
open class Book {

    @get:Id
    @get:GeneratedValue(strategy = GenerationType.IDENTITY)
    @get:Column(name = "id", nullable = false)
    open var id: Long? = null

    @get:Column(name = "name", nullable = false)
    open var name: String? = null

    @get:Convert(converter = StringListAttributeConverter::class)
    @get:Column(name = "authors", nullable = false)
    open var authors: List<String> = emptyList()

    @get:Column(name = "publisher", nullable = false)
    open var publisher: String? = null

    @get:Column(name = "publication_date", nullable = false)
    open var publicationDate: LocalDate? = null

    @get:Column(name = "page_count", nullable = false)
    open var pageCount: Int? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Book

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int = if (id != null) id.hashCode() else 0

    override fun toString(): String = "Book(id=$id)"

    companion object {

        const val ID: String = "id"

        const val NAME: String = "name"

        const val AUTHORS: String = "authors"

        const val PUBLISHER: String = "publisher"

        const val PUBLICATION_DATE: String = "publicationDate"

        const val PAGE_COUNT: String = "pageCount"
    }
}
