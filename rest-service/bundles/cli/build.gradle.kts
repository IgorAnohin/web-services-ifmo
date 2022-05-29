plugins {
    application
}

application {
    mainClass.set("ru.anokhin.rest.CliMainKt")
}

dependencies {
    api(libs.kotlinLogging.jvm)
    api(libs.log4j.api)

    implementation(projects.restService.client)
    implementation(libs.clikt)
    implementation(libs.mordant)

    implementation(libs.kotlin.reflect)
    implementation(libs.log4j.core)
    implementation(libs.log4j.slf4jImpl)
}
