import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.jvm) apply false
}

allprojects {
    repositories {
        mavenCentral()
    }
}

val javaVersion: String = libs.versions.java.get()

subprojects {
    apply<KotlinPlatformJvmPlugin>()

    tasks {
        withType<Test>() {
            useJUnitPlatform()
        }
        withType<KotlinCompile>() {
            kotlinOptions {
                jvmTarget = javaVersion
                freeCompilerArgs = listOf("-Xjsr305=strict")
            }
        }
    }

    extensions.configure<KotlinJvmProjectExtension> {
        jvmToolchain {
            (this as JavaToolchainSpec)
                .languageVersion
                .set(JavaLanguageVersion.of(javaVersion))
        }
    }
}
