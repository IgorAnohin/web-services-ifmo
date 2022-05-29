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
import ru.anokhin.jaxws.cli.util.stringify
import ru.anokhin.jaxws.client.BookService
import ru.anokhin.jaxws.client.BookSoapDto

class UpdateCommand constructor(
    private val bookSoapService: BookService,
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
        val existingBook: BookSoapDto = bookSoapService.findById(id)

        val updatedBook: BookSoapDto = bookSoapService.update(
            id,
            this@UpdateCommand.name ?: existingBook.name,
            this@UpdateCommand.authors.takeIf(List<*>::isNotEmpty) ?: existingBook.authors,
            this@UpdateCommand.publisher ?: existingBook.publisher,
            this@UpdateCommand.publicationDate?.toGregorianCalendar() ?: existingBook.publicationDate,
            this@UpdateCommand.pageCount ?: existingBook.pageCount
        )

        println("Updated book by id $id: ${updatedBook.stringify()}")
    }
}
