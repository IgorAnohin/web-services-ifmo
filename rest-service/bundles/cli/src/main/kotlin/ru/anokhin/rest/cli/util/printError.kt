package ru.anokhin.rest.cli.util

import com.github.ajalt.clikt.core.PrintMessage

fun printUnknownError(): Nothing = printError("Unknown error")

fun printError(message: String): Nothing = throw PrintMessage(message, error = true)
