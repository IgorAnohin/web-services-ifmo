dependencies {
    api(projects.jaxWsService.apiModel)

    implementation(libs.jersey.client)

    testImplementation(libs.kotlin.test)
}

sourceSets.main {
    java.srcDirs("src/main/java", "src/main/kotlin")
}
