import org.gradle.api.JavaVersion

plugins {
    id("profiler.embedded-library")
}

dependencies {
    api(gradleApi())
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<AbstractCompile>().configureEach {
    targetCompatibility = "8"
    sourceCompatibility = "8"
}
