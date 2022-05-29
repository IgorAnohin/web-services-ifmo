package ru.anokhin.jaxws

import ru.anokhin.jaxws.client.BookSoapDto

fun BookSoapDto.stringify() = buildString {
    append("Book(")
    append("id=", id, ", ")
    append("name=", name, ", ")
    append("publisher=", publisher, ", ")
    append("publicationDate=", publicationDate, ", ")
    append("pageCount=", pageCount)
    append(")")
}
