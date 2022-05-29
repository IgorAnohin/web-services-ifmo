package ru.anokhin.rest.api.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
class BookUpdateRequest(

    val name: String,

    val authors: List<String>,

    val publisher: String,

    val publicationDate: LocalDate,

    val pageCount: Int,
) {

    override fun toString(): String = buildString {
        append(
            "BookUpdateRequest(",
            ", name='", name, '\'',
            ", authors=", authors,
            ", publisher='", publisher, '\'',
            ", publicationDate=", publicationDate,
            ", pageCount=", pageCount,
            ")"
        )
    }
}
