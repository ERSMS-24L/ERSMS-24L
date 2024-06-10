import org.springframework.boot.gradle.tasks.run.BootRun

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

val axonVersion = "4.9.4"
val springDocVersion = "2.5.0"

dependencies {
	implementation("pl.edu.pw.ia:ersms-shared:swagger-fix")

	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
	implementation("org.springframework.boot:spring-boot-starter-validation")

//	implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
//	implementation("org.springframework.cloud:spring-cloud-starter-config")
	if (project.hasProperty("kubernetes")) {
		implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")
		implementation("org.springframework.cloud:spring-cloud-starter-kubernetes-client-all")
	} else {
		implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
	}

	implementation("org.axonframework:axon-spring-boot-starter")
	implementation("org.axonframework.extensions.reactor:axon-reactor-spring-boot-starter")
	implementation("org.axonframework.extensions.kotlin:axon-kotlin")
	implementation("org.axonframework.extensions.mongo:axon-mongo")

	implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:$springDocVersion")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("org.hibernate:hibernate-validator:8.0.1.Final")

	implementation("io.micrometer:micrometer-registry-prometheus:1.12.6")

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
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<Jar> {
	manifest {
		attributes("Add-Opens" to "java.base/java.util")
	}
}

tasks.withType<BootRun> {
	jvmArgs(
		"--add-opens", "java.base/java.util=ALL-UNNAMED"
	)
}
