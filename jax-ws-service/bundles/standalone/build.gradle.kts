@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.jpa)
}

dependencies {
    runtimeOnly(libs.jdbcDrivers.postgresql)
    runtimeOnly(libs.sun.xml.jaxwsRt)

    implementation(projects.jaxWsService.api)
}
