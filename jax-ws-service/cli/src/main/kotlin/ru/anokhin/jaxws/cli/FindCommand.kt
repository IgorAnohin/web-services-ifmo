package ru.anokhin.jaxws.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import ru.anokhin.jaxws.cli.util.stringify
import ru.anokhin.jaxws.cli.util.toDate
import ru.anokhin.jaxws.model.dto.BookSoapDto
import ru.anokhin.jaxws.service.BookSoapService

class FindCommand constructor(
    private val bookSoapService: BookSoapService,
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
        val foundBooks: List<BookSoapDto> = bookSoapService.findByFilter(
            name = name,
            author = author,
            publisher = publisher,
            publicationDateFrom = publicationDateFrom?.toDate(),
            publicationDateTo = publicationDateTo?.toDate(),
            pageCountFrom = pageCountFrom,
            pageCountTo = pageCountTo,
        )
        when {
            foundBooks.isEmpty() -> println("Could not find any book by given filter")
            (foundBooks.size == 1) -> println("Found a single book: ${foundBooks.first().stringify()}")
            else -> println(
                "Found books:\n" + foundBooks.joinToString(
                    separator = "\n",
                    limit = 10,
                    transform = BookSoapDto::stringify
                )
            )
        }
    }
}
