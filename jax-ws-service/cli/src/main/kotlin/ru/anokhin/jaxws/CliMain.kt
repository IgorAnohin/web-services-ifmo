package ru.anokhin.jaxws

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.output.CliktHelpFormatter
import com.github.ajalt.clikt.output.HelpFormatter
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.multiple
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.terminal.TerminalColors
import java.net.URL
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.GregorianCalendar
import javax.xml.datatype.DatatypeFactory
import ru.anokhin.jaxws.client.BookService
import ru.anokhin.jaxws.client.BookSoapDto
import ru.anokhin.jaxws.client.BookSoapServiceImplService

fun main(args: Array<String>) {
    val url = URL("http://localhost:8080/BookService?wsdl")
    val bookService = BookSoapServiceImplService(url)
    val bookSoapService: BookService = bookService.bookServicePort

    BooksCli()
        .subcommands(Create(bookSoapService))
        .main(args)
}

private fun stringify(value: BookSoapDto) = value.run {
    "Book(id=$id, name=$name, authors=$authors, publisher=$publisher, publicationDate=$publicationDate, pageCount=$pageCount)"
}

private val DATATYPE_FACTORY: DatatypeFactory = DatatypeFactory.newInstance()

class Create constructor(
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

        bookSoapService.create(
            name,
            authors,
            publisher,
            publicationDateVal,
            pageCount
        ).also { println("Created book: ${stringify(it)}") }
    }
}

class BooksCli : NoOpCliktCommand(
    name = "cli",
    help = "An example of a custom help formatter that uses ansi colors"
) {

    init {
        context { helpFormatter = ColorHelpFormatter() }
    }
}

class ColorHelpFormatter : CliktHelpFormatter() {

    private val tc: TerminalColors = Terminal().colors

    override fun renderTag(tag: String, value: String) = tc.green(super.renderTag(tag, value))

    override fun renderOptionName(name: String) = tc.yellow(super.renderOptionName(name))

    override fun renderArgumentName(name: String) = tc.yellow(super.renderArgumentName(name))

    override fun renderSubcommandName(name: String) = tc.yellow(super.renderSubcommandName(name))

    override fun renderSectionTitle(title: String) = (tc.bold + tc.underline)(super.renderSectionTitle(title))

    override fun optionMetavar(option: HelpFormatter.ParameterHelp.Option) = tc.green(super.optionMetavar(option))
}
