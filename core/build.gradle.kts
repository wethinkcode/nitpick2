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
    implementation("org.yaml:snakeyaml:2.0")
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.7.0.202309050840-r")
    implementation("org.slf4j:slf4j-nop:2.0.7")
    implementation("org.buildobjects:jproc:2.8.2")
    implementation("org.yaml:snakeyaml:2.0")
    implementation("org.geepawhill:jltk:0.0.59")
    implementation("org.slf4j:slf4j-nop:2.0.7")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("org.assertj:assertj-core:3.24.2")
}

tasks.test {
    useJUnitPlatform()
}