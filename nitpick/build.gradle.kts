plugins {
    kotlin("jvm")
}

group = "za.co.wethinkcode"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation("commons-cli:commons-cli:1.5.0")
    implementation("za.co.wethinkcode:jltk-io:0.1.0")

    implementation(project(":core"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("org.assertj:assertj-core:3.24.2")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest.attributes["Main-Class"] = "za.co.wethinkcode.nitpick.MainKt"
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree) // OR .map { zipTree(it) }
    from(dependencies).exclude("META-INF/*.RSA", "META-INF/*.SF", "META-INF/*.SF", "META-INF/*.DSA")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}