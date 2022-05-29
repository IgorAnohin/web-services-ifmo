package ru.anokhin.core.model.dto

import java.time.LocalDate

data class BookDto(

    val id: Long,

    val name: String,

    val authors: List<String>,

    val publisher: String,

    val publicationDate: LocalDate,

    val pageCount: Int,
)
