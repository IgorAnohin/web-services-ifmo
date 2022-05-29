@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.serialization)
    application
}

application {
    mainClass.set("ru.anokhin.rest.StandaloneMainKt")
}

dependencies {
    runtimeOnly(libs.jdbcDrivers.postgresql)

    implementation(projects.restService.api)
    implementation(libs.ktor.server.callLogging)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.contentNegotiation)
    implementation(libs.ktor.server.defaultHeaders)
    implementation(libs.ktor.serialization.json.kotlinx)
}
