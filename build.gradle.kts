plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.3.5"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "br.com.fiap.techchallenge"
version = "0.0.1"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

val kotlinLoggingJvmVersion = "7.0.13"
val awsParameterStoreVersion = "3.1.0"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.awspring.cloud:spring-cloud-aws-starter-sqs:${awsParameterStoreVersion}")
	implementation("io.awspring.cloud:spring-cloud-aws-starter-sns:${awsParameterStoreVersion}")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("io.github.oshai:kotlin-logging-jvm:$kotlinLoggingJvmVersion")
	implementation("io.awspring.cloud:spring-cloud-aws-starter-parameter-store:$awsParameterStoreVersion")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
