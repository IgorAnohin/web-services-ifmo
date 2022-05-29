plugins {
    war
}

tasks.withType<War> {
    archiveBaseName.set("jaxws-j2ee")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

dependencies {
    implementation(projects.jaxWsService.api)
}
