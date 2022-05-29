package ru.anokhin.jaxws.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.long
import ru.anokhin.jaxws.service.BookSoapService

class RemoveCommand constructor(
    private val bookSoapService: BookSoapService,
) : CliktCommand(name = "remove", help = "Remove an existing book") {

    private val id: Long by option(help = "Book identifier").long().required()

    override fun run() {
        val deleted = bookSoapService.deleteById(id = id)
        if (deleted) {
            println("Book with id $id has been removed")
        } else {
            println("Book with id $id was not removed")
        }
    }
}
