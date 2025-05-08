plugins {
    kotlin("jvm")
}

group = "za.co.wethinkcode"
val junitVersion = property("junit.version")
val assertJVersion = property("assertj.version")

repositories {
    mavenLocal()
    mavenCentral()
    google()
}

dependencies {
    implementation("commons-cli:commons-cli:1.5.0")
    implementation("za.co.wethinkcode:jltk-io:0.1.0")

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

tasks.jar {
    manifest.attributes["Main-Class"] = "za.co.wethinkcode.nitpick.MainKt"
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree) // OR .map { zipTree(it) }
    from(dependencies).exclude("META-INF/*.RSA", "META-INF/*.SF", "META-INF/*.SF", "META-INF/*.DSA")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}