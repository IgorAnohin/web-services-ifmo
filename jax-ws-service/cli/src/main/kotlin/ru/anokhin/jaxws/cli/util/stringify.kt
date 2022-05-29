package ru.anokhin.jaxws.cli.util

import ru.anokhin.jaxws.model.dto.BookSoapDto

fun BookSoapDto.stringify() = buildString {
    append("Book(")
    append("id=", id, ", ")
    append("name=", name, ", ")
    append("authors=", authors.joinToString(prefix = "[", postfix = "]", separator = " "), ", ")
    append("publisher=", publisher, ", ")
    append("publicationDate=", publicationDate, ", ")
    append("pageCount=", pageCount)
    append(")")
}
