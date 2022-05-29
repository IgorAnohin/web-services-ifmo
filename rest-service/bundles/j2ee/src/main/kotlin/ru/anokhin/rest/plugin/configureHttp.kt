@file:JvmMultifileClass
@file:JvmName("KtorPlugins")
package ru.anokhin.rest.plugin

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.defaultheaders.DefaultHeaders

@Suppress("unused")
fun Application.configureHttp() {
    install(DefaultHeaders) {
        header("X-Engine", "Ktor") // will send this header with each response
    }
}
