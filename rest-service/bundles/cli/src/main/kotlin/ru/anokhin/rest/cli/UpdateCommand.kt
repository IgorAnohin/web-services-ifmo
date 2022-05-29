package ru.anokhin.rest.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.multiple
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.long
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.datetime.toKotlinLocalDate
import ru.anokhin.core.ErrorCodes
import ru.anokhin.core.exception.ServiceException
import ru.anokhin.rest.api.model.Book
import ru.anokhin.rest.api.model.BookUpdateRequest
import ru.anokhin.rest.cli.util.printError
import ru.anokhin.rest.cli.util.printUnknownError
import ru.anokhin.rest.client.BookClient

class UpdateCommand constructor(
    private val bookClient: BookClient,
) : CliktCommand(name = "update", help = "Update an existing book") {

    private val id: Long by option(help = "Book identifier").long().required()

    private val name: String? by option(help = "Book name")

    private val authors: List<String> by option(
        "--author",
        help = "Book authors (use multiple --author arguments to provide a few authors)"
    ).multiple(required = false)

    private val publisher: String? by option(help = "Publisher")

    private val publicationDate: LocalDate? by option(help = "Publication date in format dd-mm-yyyy (e.g. \"27-05-1984\")")
        .convert {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            LocalDate.parse(it, formatter)
        }

    private val pageCount: Int? by option(help = "Pages count").int()

    override fun run() {
        val existingBook: Book = try {
            bookClient.findById(id)
        } catch (ex: ServiceException) {
            when (ex.code) {
                ErrorCodes.Books007EntityNotFound -> printError("Entity with id $id was not found")
                ErrorCodes.Books001UnknownError -> printUnknownError()
                else -> printUnknownError()
            }
        }

        val updatedBook: Book = try {
            bookClient.update(
                bookId = id,
                BookUpdateRequest(
                    name = this@UpdateCommand.name ?: existingBook.name,
                    authors = this@UpdateCommand.authors.takeIf(List<*>::isNotEmpty)
                        ?: existingBook.authors,
                    publisher = this@UpdateCommand.publisher ?: existingBook.publisher,
                    publicationDate = this@UpdateCommand.publicationDate?.toKotlinLocalDate()
                        ?: existingBook.publicationDate,
                    pageCount = this@UpdateCommand.pageCount ?: existingBook.pageCount
                )
            )
        } catch (ex: ServiceException) {
            when (ex.code) {
                ErrorCodes.Books002NameIsBlank -> printError("Name cannot be blank")
                ErrorCodes.Books003AuthorsListIsEmpty -> printError("Authors list must not be empty")
                ErrorCodes.Books004AuthorsIsBlank -> printError("Author cannot be blank")
                ErrorCodes.Books005PublisherIsBlank -> printError("Publisher cannot be blank")
                ErrorCodes.Books006PageCountIsNotPositive -> printError("Page count must be a positive number")
                ErrorCodes.Books007EntityNotFound -> printError("Entity with id $id was not found")
                ErrorCodes.Books001UnknownError -> printUnknownError()
                else -> printUnknownError()
            }
        }

        println("Updated book by id $id: $updatedBook")
    }
}
