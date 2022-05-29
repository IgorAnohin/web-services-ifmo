@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.jpa)
}

dependencies {
    runtimeOnly(libs.jdbcDrivers.postgresql)
    runtimeOnly(libs.sun.xml.jaxwsRt)

    implementation(projects.jaxWsService.core)

//    implementation("com.sun.xml.ws:webservices-rt:2.0.1")
    implementation(libs.sun.xml.jaxbImpl)
    implementation(libs.jakarta.xml.wsApi)
}
