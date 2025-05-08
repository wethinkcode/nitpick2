import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose").version("1.6.0")
    id("dev.hydraulic.conveyor") version "1.6"
}

group = "za.co.wethinkcode"
val junitVersion = property("junit.version")
val assertJVersion = property("assertj.version")


repositories {
    mavenLocal()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://jitpack.io")
    google()
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation("com.github.vishna:watchservice-ktx:master-SNAPSHOT")
    implementation(compose.desktop.currentOs)
    implementation("androidx.compose.material:material-icons-extended:1.7.6")
    implementation("br.com.devsrsouza.compose.icons:font-awesome:1.1.0")
    implementation("dev.hydraulic.conveyor:conveyor-control:1.1")
    implementation("za.co.wethinkcode:flow:1.0.4")
    implementation(project(":core"))
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testImplementation("org.assertj:assertj-core:$assertJVersion")
}

compose.desktop {
    application {
        mainClass = "za.co.wethinkcode.vnitpick.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "vnitpick"
            packageVersion = project.version.toString()
        }
    }
}
