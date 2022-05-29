dependencies {
    implementation(projects.restService.client)
    implementation(libs.clikt)
    implementation(libs.mordant)

    implementation(libs.log4j.api)
    implementation(libs.log4j.core)
    implementation(libs.log4j.slf4jImpl)
}
