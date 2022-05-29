package ru.anokhin.rest.api.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
class Book(

    val id: Long,

    val name: String,

    val authors: List<String>,

    val publisher: String,

    val publicationDate: LocalDate,

    val pageCount: Int,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Book

        if (id != other.id) return false
        if (name != other.name) return false
        if (authors != other.authors) return false
        if (publisher != other.publisher) return false
        if (publicationDate != other.publicationDate) return false
        if (pageCount != other.pageCount) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + authors.hashCode()
        result = 31 * result + publisher.hashCode()
        result = 31 * result + publicationDate.hashCode()
        result = 31 * result + pageCount
        return result
    }

    override fun toString(): String = buildString {
        append(
            "Book(",
            "id=", id,
            ", name=", name,
            ", authors=", authors,
            ", publisher=", publisher,
            ", publicationDate=", publicationDate,
            ", pageCount=", pageCount,
            ")"
        )
    }
}
