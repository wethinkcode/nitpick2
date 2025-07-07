plugins {
    kotlin("jvm")
    id("org.openjfx.javafxplugin") version "0.0.13"
}

group = "za.co.wethinkcode"
val junitVersion = property("junit.version")
val assertJVersion = property("assertj.version")

repositories {
    mavenLocal()
    mavenCentral()
    google()
}

javafx {
    version = "21.0.2"
    modules = listOf(
        "javafx.controls",
        "javafx.base",
        "javafx.graphics",
        "javafx.fxml",
        "javafx.web",
        "javafx.swing",
        "javafx.media"
    )
}

dependencies {
    implementation("no.tornado:tornadofx:1.7.20")
    implementation("za.co.wethinkcode:flow:1.0.4")
    implementation(project(":core"))
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testImplementation("org.assertj:assertj-core:$assertJVersion")
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}