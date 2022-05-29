package ru.anokhin.core.service

import ru.anokhin.core.model.dto.BookDto
import ru.anokhin.core.model.dto.BookFilter
import ru.anokhin.core.model.dto.BookSaveDto

interface BookService {

    fun save(book: BookSaveDto): BookDto

    fun findById(id: Long): BookDto

    fun findByFilter(filter: BookFilter): List<BookDto>

    fun remove(id: Long): Boolean
}
