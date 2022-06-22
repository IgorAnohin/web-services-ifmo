@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.serialization)
    application
}

dependencies {
    api(projects.restService.apiModel)
    api(projects.serviceCore.core)

    api(libs.ktor.server.auth)

    implementation(libs.ktor.server.core)
}
