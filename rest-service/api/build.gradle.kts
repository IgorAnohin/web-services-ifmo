@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.serialization)
    application
}

dependencies {
    api(projects.restService.apiModel)
    api(projects.serviceCore.core)

    implementation(libs.ktor.server.core)
}
