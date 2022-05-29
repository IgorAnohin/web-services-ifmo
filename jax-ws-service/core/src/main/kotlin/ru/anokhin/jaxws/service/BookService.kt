package ru.anokhin.jaxws.service

import ru.anokhin.jaxws.model.dto.BookDto
import ru.anokhin.jaxws.model.dto.BookFilter
import ru.anokhin.jaxws.model.dto.BookSaveDto

interface BookService {

    fun save(book: BookSaveDto): BookDto

    fun findById(id: Long): BookDto

    fun findByFilter(filter: BookFilter): List<BookDto>

    fun remove(id: Long): Boolean
}
