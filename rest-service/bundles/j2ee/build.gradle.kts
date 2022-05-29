plugins {
    war
    id("org.gretty") version "3.0.6"
}

tasks.withType<War> {
    archiveBaseName.set("rest-j2ee")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

dependencies {
    implementation(projects.restService.api)
    implementation(libs.jakarta.jakartaee.api)

    implementation(libs.ktor.server.contentNegotiation)
    implementation(libs.ktor.serialization.json.kotlinx)

    implementation(libs.ktor.server.callLogging)
    implementation(libs.ktor.server.servlet)
    implementation(libs.ktor.server.defaultHeaders)

    implementation(libs.jakarta.servletApi)
}
