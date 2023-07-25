plugins {
    id("java")
}

group = "org.aj"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    // https://mvnrepository.com/artifact/org.testng/testng
    implementation("org.testng:testng:7.8.0")
    // https://mvnrepository.com/artifact/org.seleniumhq.selenium/selenium-java
    implementation("org.seleniumhq.selenium:selenium-java:4.10.0")
    // https://mvnrepository.com/artifact/io.appium/java-client
    implementation("io.appium:java-client:8.5.1")
    // https://mvnrepository.com/artifact/org.json/json
    implementation("org.json:json:20230618")
    implementation("com.github.vidstige:jadb:v1.2.1")
}

tasks.test {
    useTestNG() {
        suites("src/test/resources/testng.xml")
    }
}