plugins {
    application
}

application {
    mainClass.set("ru.anokhin.rest.CliMainKt")
}

dependencies {
    implementation(projects.restService.client)
    implementation(libs.clikt)
    implementation(libs.mordant)

    implementation(libs.kotlin.reflect)
}
