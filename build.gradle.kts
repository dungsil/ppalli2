plugins {
  kotlin("jvm") version "1.9.10"
  kotlin("plugin.jpa") version "1.9.0"
  kotlin("plugin.spring") version "1.9.0"
  id("org.jetbrains.dokka") version "1.8.20"

  id("org.springframework.boot") version "3.1.2"
  id("io.spring.dependency-management") version "1.1.3"
}

group = "ppalli"
version = "0.1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

@Suppress("SpellCheckingInspection")
dependencies {
  // Spring boot
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  developmentOnly("org.springframework.boot:spring-boot-devtools")
  developmentOnly("org.springframework.boot:spring-boot-docker-compose")
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
  testImplementation("org.springframework.boot:spring-boot-starter-test")

  // 3rd party spring module
  implementation("me.paulschwarz:spring-dotenv:4.0.0")

  // Hypersistence Utils
  implementation("io.hypersistence:hypersistence-utils-hibernate-62:3.5.2")

  // Liquibase (Database migration)
  implementation("org.liquibase:liquibase-core")

  // Jackson
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

  // Bouncy Castle
  implementation("org.bouncycastle:bcprov-jdk18on:1.75")

  // JDBC Driver
  runtimeOnly("org.postgresql:postgresql") // PostgreSQL
  testRuntimeOnly("com.h2database:h2") // H2

  // Kotlin
  implementation(kotlin("stdlib-jdk8"))
  implementation(kotlin("reflect"))
  implementation("io.github.oshai:kotlin-logging-jvm:4.0.2") // Logging (SLF4j)
  testImplementation(kotlin("test-junit5"))
}

configurations {
  compileOnly {
    extendsFrom(annotationProcessor.get())
  }
}

tasks {
  compileKotlin {
    kotlinOptions {
      jvmTarget = "17"
      freeCompilerArgs = listOf("-Xjsr305=strict")
    }
  }

  test {
    systemProperty("spring.profiles.active", "test") // 테스트 프로필에서 작동

    useJUnitPlatform()
  }
}
