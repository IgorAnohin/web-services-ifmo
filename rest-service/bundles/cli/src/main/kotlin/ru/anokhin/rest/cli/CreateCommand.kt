package ru.anokhin.rest.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.multiple
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.int
import kotlinx.datetime.LocalDate
import ru.anokhin.core.ErrorCodes
import ru.anokhin.core.exception.ServiceException
import ru.anokhin.rest.api.model.BookCreationRequest
import ru.anokhin.rest.cli.util.printError
import ru.anokhin.rest.cli.util.printUnknownError
import ru.anokhin.rest.client.BookClient

class CreateCommand constructor(
    private val bookClient: BookClient,
) : CliktCommand(name = "create", help = "Create a new book") {

    private val name: String by option(help = "Book name").required()

    private val authors: List<String> by option(
        "--author",
        help = "Book authors (use multiple --author arguments to provide a few authors)"
    ).multiple(required = true)

    private val publisher: String by option(help = "Publisher").required()

    private val publicationDate: LocalDate by option(
        help = "Publication date in format yyyy-mm-dd (e.g. \"1984-05-27\")"
    ).convert { LocalDate.parse(it) }.required()

    private val pageCount: Int by option(help = "Pages count").int().required()

    override fun run() {
        try {
            bookClient.create(
                BookCreationRequest(
                    name = name,
                    authors = authors,
                    publisher = publisher,
                    publicationDate = publicationDate,
                    pageCount = pageCount
                )
            ).also { book -> println("Created book: $book") }
        } catch (ex: ServiceException) {
            when (ex.code) {
                ErrorCodes.Books002NameIsBlank -> printError("Name cannot be blank")
                ErrorCodes.Books003AuthorsListIsEmpty -> printError("Authors list must not be empty")
                ErrorCodes.Books004AuthorsIsBlank -> printError("Author cannot be blank")
                ErrorCodes.Books005PublisherIsBlank -> printError("Publisher cannot be blank")
                ErrorCodes.Books006PageCountIsNotPositive -> printError("Page count must be a positive number")
                ErrorCodes.Books001UnknownError -> printUnknownError()
                else -> printUnknownError()
            }
        }
    }
}
