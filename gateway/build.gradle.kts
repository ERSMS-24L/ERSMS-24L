plugins {
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.5"
	kotlin("plugin.spring") version "1.9.24"
	id("pl.edu.pw.ia.convention")
}

extra["springCloudVersion"] = "2023.0.0"

val springDocVersion = "2.5.0"

dependencies {
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-webflux")

	implementation("org.springframework.cloud:spring-cloud-starter-gateway")
	implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j")
//	implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
//	implementation("org.springframework.cloud:spring-cloud-starter-config")
	if (project.hasProperty("kubernetes")) {
		implementation("org.springframework.cloud:spring-cloud-starter-kubernetes-client-all")
	} else {
		implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
	}

	implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:$springDocVersion")

	implementation("io.micrometer:micrometer-registry-prometheus:1.12.5")

	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
