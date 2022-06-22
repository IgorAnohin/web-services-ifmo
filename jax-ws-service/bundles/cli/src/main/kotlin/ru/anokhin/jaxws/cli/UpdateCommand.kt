package ru.anokhin.jaxws.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.multiple
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.long
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import ru.anokhin.core.ErrorCodes
import ru.anokhin.core.exception.ServiceException
import ru.anokhin.jaxws.cli.util.generateHardcodedAuthToken
import ru.anokhin.jaxws.cli.util.printError
import ru.anokhin.jaxws.cli.util.printUnknownError
import ru.anokhin.jaxws.cli.util.stringify
import ru.anokhin.jaxws.cli.util.toDate
import ru.anokhin.jaxws.model.dto.BookSoapDto
import ru.anokhin.jaxws.service.BookSoapService

class UpdateCommand constructor(
    private val bookSoapService: BookSoapService,
) : CliktCommand(name = "update", help = "Update an existing book") {

    private val id: Long by option(help = "Book identifier").long().required()

    private val name: String? by option(help = "Book name")

    private val authors: List<String> by option(
        "--author",
        help = "Book authors (use multiple --author arguments to provide a few authors)"
    ).multiple(required = false)

    private val publisher: String? by option(help = "Publisher")

    private val publicationDate: LocalDate? by option(
        help = "Publication date in format yyyy-mm-dd (e.g. \"1984-05-27\")"
    ).convert {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        LocalDate.parse(it, formatter)
    }

    private val pageCount: Int? by option(help = "Pages count").int()

    override fun run() {
        val existingBook: BookSoapDto = try {
            bookSoapService.findById(id)
        } catch (ex: ServiceException) {
            when (ex.code) {
                ErrorCodes.Books007EntityNotFound -> printError("Entity with id $id was not found")
                ErrorCodes.Books001UnknownError -> printUnknownError()
                else -> printUnknownError()
            }
        }

        val updatedBook: BookSoapDto = try {
            bookSoapService.update(
                id = id,
                name = this@UpdateCommand.name ?: existingBook.name!!,
                authors = this@UpdateCommand.authors.takeIf(List<*>::isNotEmpty) ?: existingBook.authors,
                publisher = this@UpdateCommand.publisher ?: existingBook.publisher!!,
                publicationDate = this@UpdateCommand.publicationDate?.toDate()
                    ?: existingBook.publicationDate?.toDate()!!,
                pageCount = this@UpdateCommand.pageCount ?: existingBook.pageCount!!,
                authToken = generateHardcodedAuthToken(),
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

        println("Updated book by id $id: ${updatedBook.stringify()}")
    }
}
