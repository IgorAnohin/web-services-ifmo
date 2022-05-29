@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.jpa)
    application
}

application {
    mainClass.set("ru.anokhin.jaxws.StandaloneMainKt")
}

distributions {
    all {
        contents.duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}

dependencies {
    runtimeOnly(libs.jdbcDrivers.postgresql)
    runtimeOnly(libs.sun.xml.jaxwsRt)

    implementation(projects.jaxWsService.api)
}
