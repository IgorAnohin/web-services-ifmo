package ru.anokhin.rest

import com.github.ajalt.clikt.core.subcommands
import ru.anokhin.rest.cli.BooksCli
import ru.anokhin.rest.cli.CreateCommand
import ru.anokhin.rest.cli.FindCommand
import ru.anokhin.rest.cli.RemoveCommand
import ru.anokhin.rest.cli.UpdateCommand
import ru.anokhin.rest.client.BookClient

fun main(args: Array<String>) {
    val bookClient = BookClient()
    BooksCli()
        .subcommands(
            CreateCommand(bookClient),
            FindCommand(bookClient),
            UpdateCommand(bookClient),
            RemoveCommand(bookClient),
        )
        .main(args)
}
