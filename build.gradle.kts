plugins {
    kotlin("jvm") version "1.9.22" apply false
    java
}

tasks.withType(Test::class) {
    useJUnitPlatform()
}