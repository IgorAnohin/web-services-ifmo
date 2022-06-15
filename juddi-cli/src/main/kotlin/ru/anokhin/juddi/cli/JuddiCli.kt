package ru.anokhin.juddi.cli

import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.context
import ru.anokhin.jaxws.cli.util.ColorHelpFormatter

class JuddiCli : NoOpCliktCommand(
    name = "cli",
    help = "Command line interface for managing jUDDi services"
) {

    init {
        context { helpFormatter = ColorHelpFormatter() }
    }
}
