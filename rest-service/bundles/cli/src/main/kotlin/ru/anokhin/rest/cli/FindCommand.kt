package ru.anokhin.rest.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import kotlinx.datetime.LocalDate
import ru.anokhin.core.ErrorCodes
import ru.anokhin.core.exception.ServiceException
import ru.anokhin.rest.api.model.Book
import ru.anokhin.rest.api.model.BookFilter
import ru.anokhin.rest.cli.util.printUnknownError
import ru.anokhin.rest.client.BookClient

class FindCommand constructor(
    private val bookClient: BookClient,
) : CliktCommand(name = "find", help = "Find books by filter") {

    private val name: String? by option(help = "Book name")

    private val author: String? by option(help = "Book author")

    private val publisher: String? by option(help = "Publisher")

    private val publicationDateFrom: LocalDate? by option(
        help = "Publication date in format yyyy-mm-dd (e.g. \"1984-05-27\")"
    ).convert { LocalDate.parse(it) }

    private val publicationDateTo: LocalDate? by option(
        help = "Publication date in format yyyy-mm-dd (e.g. \"2022-04-25\")"
    ).convert { LocalDate.parse(it) }

    private val pageCountFrom: Int? by option(help = "Pages count").int()

    private val pageCountTo: Int? by option(help = "Pages count").int()

    override fun run() {
        val foundBooks: List<Book> = try {
            bookClient.findByFilter(
                BookFilter(
                    name = name,
                    author = author,
                    publisher = publisher,
                    publicationDateFrom = publicationDateFrom,
                    publicationDateTo = publicationDateTo,
                    pageCountFrom = pageCountFrom,
                    pageCountTo = pageCountTo,
                )
            )
        } catch (ex: ServiceException) {
            when (ex.code) {
                ErrorCodes.Books001UnknownError -> printUnknownError()
                else -> printUnknownError()
            }
        }
        when {
            foundBooks.isEmpty() -> println("Could not find any book by given filter")
            (foundBooks.size == 1) -> println("Found a single book: ${foundBooks.first()}")
            else -> println("Found books:\n" + foundBooks.joinToString(separator = "\n", limit = 10))
        }
    }
}
