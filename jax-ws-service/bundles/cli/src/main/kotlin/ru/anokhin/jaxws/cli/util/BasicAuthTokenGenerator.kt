package ru.anokhin.jaxws.cli.util

import java.util.Base64

object BasicAuthTokenGenerator {

    fun generateString(
        login: String,
        password: String,
    ): String = buildString {
        append("Basic ")
        append(
            Base64.getEncoder()
                .encode("$login:$password".toByteArray())
                .let(::String)
        )
    }
}
