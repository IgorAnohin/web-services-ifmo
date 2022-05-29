@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    api(projects.serviceCore.coreModel)
    api(libs.kotlinx.serialization.json)
    api(libs.kotlinx.datetime)
}
