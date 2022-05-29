package ru.anokhin.rest.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.datetime.toKotlinLocalDate
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
        help = "Publication date in format dd-mm-yyyy (e.g. \"27-05-1984\")"
    ).convert {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        LocalDate.parse(it, formatter)
    }

    private val publicationDateTo: LocalDate? by option(
        help = "Publication date in format dd-mm-yyyy (e.g. \"25-04-2022\")"
    ).convert {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        LocalDate.parse(it, formatter)
    }

    private val pageCountFrom: Int? by option(help = "Pages count").int()

    private val pageCountTo: Int? by option(help = "Pages count").int()

    override fun run() {
        val foundBooks: List<Book> = try {
            bookClient.findByFilter(
                BookFilter(
                    name = name,
                    author = author,
                    publisher = publisher,
                    publicationDateFrom = publicationDateFrom?.toKotlinLocalDate(),
                    publicationDateTo = publicationDateTo?.toKotlinLocalDate(),
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
