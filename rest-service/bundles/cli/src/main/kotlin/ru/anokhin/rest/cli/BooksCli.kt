package ru.anokhin.rest.cli

import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.context
import ru.anokhin.rest.cli.util.ColorHelpFormatter

class BooksCli : NoOpCliktCommand(
    name = "cli",
    help = "Command line interface for managing books"
) {

    init {
        context { helpFormatter = ColorHelpFormatter() }
    }
}
