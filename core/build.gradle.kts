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
    implementation("org.yaml:snakeyaml:2.0")
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.7.0.202309050840-r")
    implementation("org.slf4j:slf4j-nop:2.0.7")
    implementation("org.buildobjects:jproc:2.8.2")
    implementation("org.yaml:snakeyaml:2.0")
    implementation("org.slf4j:slf4j-nop:2.0.7")
    implementation("za.co.wethinkcode:flow:1.0.4")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testImplementation("org.assertj:assertj-core:$assertJVersion")
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}