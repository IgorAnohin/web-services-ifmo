@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.kotlin.allopen)
}

allOpen {
    annotation("jakarta.inject.Singleton")
}

dependencies {
    api(projects.serviceCore.coreModel)

    implementation(libs.hibernate.core)
    implementation(libs.hibernate.commons.annotations)

    api(libs.jakarta.annotationApi)
    api(libs.jakarta.persistenceApi)
    api(libs.jakarta.injectApi)

    api(libs.kotlinLogging.jvm)
    api(libs.log4j.api)
    implementation(libs.log4j.core)
    implementation(libs.log4j.slf4jImpl)

    api(libs.kotlinx.serialization.json)
    api(libs.bundles.kotlin)

    testImplementation(libs.kotlin.test)
}
