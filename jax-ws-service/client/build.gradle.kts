dependencies {
    api(projects.jaxWsService.apiModel)

    implementation(libs.jersey.core.client)

    testImplementation(libs.kotlin.test)
}

sourceSets.main {
    java.srcDirs("src/main/java", "src/main/kotlin")
}
