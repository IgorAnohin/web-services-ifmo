package ru.anokhin.jaxws.cli.util

fun generateHardcodedAuthToken(): String =
    BasicAuthTokenGenerator.generateString(login = "login", password = "password")
