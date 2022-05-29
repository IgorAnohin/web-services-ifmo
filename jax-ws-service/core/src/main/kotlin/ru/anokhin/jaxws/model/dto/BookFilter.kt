package ru.anokhin.jaxws.model.dto

import java.time.LocalDate

data class BookFilter(

    val name: String? = null,

    val author: String? = null,

    val publisher: String? = null,

    val publicationDateFrom: LocalDate? = null,

    val publicationDateTo: LocalDate? = null,

    val pageCountFrom: Int? = null,

    val pageCountTo: Int? = null,
)
