
plugins {
    kotlin("jvm")
    application
}

group = "za.co.wethinkcode"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation("org.slf4j:slf4j-simple:2.0.16")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("org.assertj:assertj-core:3.24.2")
    implementation("io.javalin:javalin:6.4.0")
}

tasks.test {
    useJUnitPlatform()
}