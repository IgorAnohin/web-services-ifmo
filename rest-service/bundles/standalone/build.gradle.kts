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

    api(projects.serviceCore.core)
    api(projects.restService.apiModel)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.callLogging)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.contentNegotiation)
    implementation(libs.ktor.server.defaultHeaders)
    implementation(libs.ktor.serialization.json.kotlinx)
}
