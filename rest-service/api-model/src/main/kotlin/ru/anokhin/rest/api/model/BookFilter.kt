package ru.anokhin.rest.api.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
class BookFilter(

    val name: String? = null,

    val author: String? = null,

    val publisher: String? = null,

    val publicationDateFrom: LocalDate? = null,

    val publicationDateTo: LocalDate? = null,

    val pageCountFrom: Int? = null,

    val pageCountTo: Int? = null,
) {

    override fun toString(): String = buildString {
        append(
            "BookFilter(",
            "name=", name,
            ", author=", author,
            ", publisher=", publisher,
            ", publicationDateFrom=", publicationDateFrom,
            ", publicationDateTo=", publicationDateTo,
            ", pageCountFrom=", pageCountFrom,
            ", pageCountTo=", pageCountTo,
            ")"
        )
    }
}
