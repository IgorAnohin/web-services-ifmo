package ru.anokhin.jaxws

import com.github.ajalt.clikt.core.subcommands
import java.net.URL
import ru.anokhin.jaxws.cli.BooksCli
import ru.anokhin.jaxws.cli.CreateCommand
import ru.anokhin.jaxws.cli.FindCommand
import ru.anokhin.jaxws.cli.RemoveCommand
import ru.anokhin.jaxws.cli.UpdateCommand
import ru.anokhin.jaxws.client.BookService_Service
import ru.anokhin.jaxws.client.BookSoapServiceDelegate
import ru.anokhin.jaxws.service.BookSoapService

private const val WSDL_URL_ENV_VARIABLE: String = "WSDL_URL"

/**
 * Link to standalone WSDL URL
 */
private const val STANDALONE_WSDL_URL: String = "http://localhost:8080/jaxws/BookService?wsdl"

private fun resolveWsdlUrl(): URL =
    System.getenv()
        .getOrDefault(key = WSDL_URL_ENV_VARIABLE, defaultValue = STANDALONE_WSDL_URL)
        .let(::URL)

fun main(args: Array<String>) {
    val bookService = BookService_Service(resolveWsdlUrl())
    val bookServicePort = bookService.bookServicePort
    val bookSoapService: BookSoapService = BookSoapServiceDelegate(bookServicePort)

    BooksCli()
        .subcommands(
            CreateCommand(bookSoapService),
            FindCommand(bookSoapService),
            UpdateCommand(bookSoapService),
            RemoveCommand(bookSoapService),
        )
        .main(args)
}
