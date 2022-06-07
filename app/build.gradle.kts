buildscript {
	dependencies {
		classpath("com.google.protobuf:protobuf-gradle-plugin:0.8.13")
	}
}

plugins {
    val kotlin_version = "1.4.0"

    application
    kotlin("jvm") version kotlin_version
    kotlin("plugin.serialization") version kotlin_version
    id("com.google.protobuf") version "0.8.13"
}

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    val ktor_version = "1.5.0"
    val exposed_version = "0.24.1"

    // kotlin
    implementation(platform(kotlin("bom")))
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0")

    // ktor
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation ("io.ktor:ktor-gson:$ktor_version")
    implementation("com.google.code.gson:gson:2.7")
   // implementation("io.ktor:ktor-client-content-negotiation:2.0.1")
    //implementation("io.ktor:ktor-serialization-kotlinx-json:2.0.1")

    // test
    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
}

application {
    mainClass.set("archstyle.AppKt")
}

tasks {
    test {
        environment("APP_SEED", "false")
    }
}
