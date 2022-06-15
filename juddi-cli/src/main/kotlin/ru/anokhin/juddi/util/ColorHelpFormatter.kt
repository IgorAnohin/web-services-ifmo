package ru.anokhin.jaxws.cli.util

import com.github.ajalt.clikt.output.CliktHelpFormatter
import com.github.ajalt.clikt.output.HelpFormatter
import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.terminal.TerminalColors

class ColorHelpFormatter : CliktHelpFormatter() {

    private val tc: TerminalColors = Terminal().colors

    override fun renderTag(tag: String, value: String) = tc.green(super.renderTag(tag, value))

    override fun renderOptionName(name: String) = tc.yellow(super.renderOptionName(name))

    override fun renderArgumentName(name: String) = tc.yellow(super.renderArgumentName(name))

    override fun renderSubcommandName(name: String) = tc.yellow(super.renderSubcommandName(name))

    override fun renderSectionTitle(title: String) = (tc.bold + tc.underline)(super.renderSectionTitle(title))

    override fun optionMetavar(option: HelpFormatter.ParameterHelp.Option) = tc.green(super.optionMetavar(option))
}
