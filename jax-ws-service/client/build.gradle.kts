dependencies {
    api(projects.jaxWsService.model)
    api(libs.jakarta.xml.wsApi)

//    implementation("com.sun.xml.ws:webservices-rt:2.0.1")
    implementation(libs.jersey.client)

    testImplementation(libs.kotlin.test)
}

sourceSets.main {
    java.srcDirs("src/main/java", "src/main/kotlin")
}
