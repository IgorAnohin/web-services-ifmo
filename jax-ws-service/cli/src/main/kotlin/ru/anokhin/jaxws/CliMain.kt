package ru.anokhin.jaxws

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.multiple
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.int
import java.net.URL
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.GregorianCalendar
import javax.xml.datatype.DatatypeFactory
import ru.anokhin.jaxws.client.BookService
import ru.anokhin.jaxws.client.BookService_Service

private const val WSDL_URL_ENV_VARIABLE: String = "WSDL_URL"

/**
 * Link to standalone WSDL URL
 */
private const val STANDALONE_WSDL_URL: String = "http://localhost:8080/jaxws/BookService?wsdl"

private val DATATYPE_FACTORY: DatatypeFactory = DatatypeFactory.newInstance()

private fun resolveWsdlUrl(): URL =
    System.getenv()
        .getOrDefault(key = WSDL_URL_ENV_VARIABLE, defaultValue = STANDALONE_WSDL_URL)
        .let(::URL)

fun main(args: Array<String>) {
    val bookService = BookService_Service(resolveWsdlUrl())
    val bookSoapService: BookService = bookService.bookServicePort

    BooksCli()
        .subcommands(CreateCommand(bookSoapService))
        .main(args)
}

class BooksCli : NoOpCliktCommand(
    name = "cli",
    help = "An example of a custom help formatter that uses ansi colors"
) {

    init {
        context { helpFormatter = ColorHelpFormatter() }
    }
}

class CreateCommand constructor(
    private val bookSoapService: BookService,
) : CliktCommand(name = "create", help = "Create a new book") {

    private val name: String by option(help = "Book name").required()

    private val authors: List<String> by option(help = "Book authors").multiple(required = true)

    private val publisher: String by option(help = "Publisher").required()

    private val publicationDate: LocalDate by option(help = "Publication date in format dd-mm-yyyy (e.g. \"27-05-1984\")")
        .convert {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            LocalDate.parse(it, formatter)
        }
        .required()

    private val pageCount: Int by option(help = "Pages count").int().required()

    override fun run() {
        val publicationDateVal = GregorianCalendar().apply {
            timeInMillis = publicationDate.atStartOfDay()
                .toInstant(ZoneOffset.UTC).toEpochMilli()
        }.let(DATATYPE_FACTORY::newXMLGregorianCalendar)

        bookSoapService.create(name, authors, publisher, publicationDateVal, pageCount)
            .also { book -> println("Created book: ${book.stringify()}") }
    }
}
