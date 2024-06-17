plugins {
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.5"
	kotlin("plugin.spring") version "1.9.24"
	id("pl.edu.pw.ia.convention")
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

extra["springCloudVersion"] = "2023.0.0"

val springDocVersion = "2.5.0"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-web")

	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server")
	implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")

	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springDocVersion")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	implementation("io.micrometer:micrometer-registry-prometheus:1.13.1")

	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
