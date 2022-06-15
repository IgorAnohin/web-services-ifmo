plugins {
    application
}

group = "ru.anokhin.juddi"

application {
    mainClass.set("ru.anokhin.juddi.CliMainKt")
}

dependencies {
    implementation("com.sun.xml.ws:jaxws-rt:2.3.2")
    implementation("com.sun.xml.ws:rt:2.3.2")
    implementation("com.sun.xml.ws:jaxws-ri:2.3.2")

    implementation(libs.juddi.uddiWs)
    implementation(libs.juddi.client)

    implementation(libs.clikt)
    implementation(libs.mordant)
}
