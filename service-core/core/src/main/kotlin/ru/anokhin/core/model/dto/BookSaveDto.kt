package ru.anokhin.core.model.dto

import java.time.LocalDate

data class BookSaveDto(

    val id: Long? = null,

    val name: String,

    val authors: List<String>,

    val publisher: String,

    val publicationDate: LocalDate,

    val pageCount: Int,
)
