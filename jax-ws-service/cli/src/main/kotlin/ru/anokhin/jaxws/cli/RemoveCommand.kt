package ru.anokhin.jaxws.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.long
import ru.anokhin.jaxws.client.BookService

class RemoveCommand constructor(
    private val bookSoapService: BookService,
) : CliktCommand(name = "remove", help = "Remove an existing book") {

    private val id: Long by option(help = "Book identifier").long().required()

    override fun run() {
        val deleted = bookSoapService.deleteBookById(id)
        if (deleted) {
            println("Book with id $id has been removed")
        } else {
            println("Book with id $id was not removed")
        }
    }
}
