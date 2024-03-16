import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
	id("org.springframework.boot") version "3.2.3"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("plugin.spring") version "1.9.23"
	id("pl.edu.pw.ia.library-convention")
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

val axonVersion = "4.9.4"
val springDocVersion = "2.3.0"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	implementation("org.springframework.data:spring-data-commons")

	implementation("org.axonframework:axon-spring-boot-starter")

	implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:$springDocVersion")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("org.hibernate:hibernate-validator:8.0.1.Final")

	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.axonframework.extensions.reactor:axon-reactor-spring-boot-starter")
	testImplementation("org.axonframework:axon-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:mongodb")
}

dependencyManagement {
	imports {
		mavenBom("org.axonframework:axon-bom:${axonVersion}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<BootJar> {
	enabled = false
}
