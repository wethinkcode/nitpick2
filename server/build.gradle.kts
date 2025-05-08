plugins {
    kotlin("jvm")
    application
}

group = "za.co.wethinkcode"
val junitVersion = property("junit.version")
val assertJVersion = property("assertj.version")

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation("org.slf4j:slf4j-simple:2.0.16")
    implementation("io.javalin:javalin:6.4.0")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testImplementation("org.assertj:assertj-core:$assertJVersion")
}

tasks.test {
    useJUnitPlatform()
}