dependencies {
    api(projects.jaxWsService.model)
    api(libs.jakarta.xml.wsApi)

    implementation(libs.jersey.client)

    testImplementation(libs.kotlin.test)
}

sourceSets.main {
    java.srcDirs("src/main/java", "src/main/kotlin")
}
