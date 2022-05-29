package ru.anokhin.jaxws.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.long
import ru.anokhin.jaxws.ErrorCodes
import ru.anokhin.jaxws.cli.util.printError
import ru.anokhin.jaxws.cli.util.printUnknownError
import ru.anokhin.jaxws.exception.ServiceException
import ru.anokhin.jaxws.service.BookSoapService

class RemoveCommand constructor(
    private val bookSoapService: BookSoapService,
) : CliktCommand(name = "remove", help = "Remove an existing book") {

    private val id: Long by option(help = "Book identifier").long().required()

    override fun run() {
        val deleted: Boolean = try {
            bookSoapService.deleteById(id = id)
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
        if (deleted) {
            println("Book with id $id has been removed")
        } else {
            println("Book with id $id was not removed")
        }
    }
}
