plugins {
    kotlin("jvm") version "2.2.0" apply false
    java
}

tasks.withType(Test::class) {
    useJUnitPlatform()
}