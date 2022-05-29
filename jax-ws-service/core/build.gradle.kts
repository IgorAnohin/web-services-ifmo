@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.jpa)
}

dependencies {
    api(projects.jaxWsService.model)

    implementation(libs.hibernate.core)
    implementation(libs.hibernate.commons.annotations)

    api(libs.jakarta.annotationApi)
    api(libs.jakarta.persistenceApi)
    api(libs.jakarta.jwsApi)

    api(libs.kotlinLogging.jvm)
    api(libs.kotlinx.serialization.jsonJvm)
    api(libs.bundles.kotlin)

    testImplementation(libs.kotlin.test)
}
