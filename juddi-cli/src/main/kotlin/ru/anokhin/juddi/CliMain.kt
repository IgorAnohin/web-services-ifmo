package ru.anokhin.juddi

import com.github.ajalt.clikt.core.subcommands
import ru.anokhin.juddi.cli.BrowseCommand
import ru.anokhin.juddi.cli.JuddiCli
import ru.anokhin.juddi.cli.PublishCommand

fun main(args: Array<String>) {
    JuddiCli()
        .subcommands(
            BrowseCommand(),
            PublishCommand(),
        )
        .main(args)
}
