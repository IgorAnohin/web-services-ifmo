package ru.anokhin.jaxws.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.multiple
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.int
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import ru.anokhin.jaxws.cli.util.stringify
import ru.anokhin.jaxws.client.BookService

class CreateCommand constructor(
    private val bookSoapService: BookService,
) : CliktCommand(name = "create", help = "Create a new book") {

    private val name: String by option(help = "Book name").required()

    private val authors: List<String> by option(
        "--author",
        help = "Book authors (use multiple --author arguments to provide a few authors)"
    ).multiple(required = true)

    private val publisher: String by option(help = "Publisher").required()

    private val publicationDate: LocalDate by option(help = "Publication date in format dd-mm-yyyy (e.g. \"27-05-1984\")")
        .convert {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            LocalDate.parse(it, formatter)
        }
        .required()

    private val pageCount: Int by option(help = "Pages count").int().required()

    override fun run() {
        bookSoapService.create(name, authors, publisher, publicationDate.toGregorianCalendar(), pageCount)
            .also { book -> println("Created book: ${book.stringify()}") }
    }
}
