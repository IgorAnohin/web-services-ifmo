dependencies {
    api(projects.jaxWsService.apiModel)

    implementation(libs.jersey.core.client)
    implementation(libs.sun.xml.jaxwsRt)

    implementation(libs.kotlin.reflect)

    testImplementation(libs.kotlin.test)
}

sourceSets.main {
    java.srcDirs("src/main/java", "src/main/kotlin")
}
