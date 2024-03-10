rootProject.name = "ersms-plugins"

pluginManagement {
	repositories {
		mavenCentral()
		gradlePluginPortal()
		maven {
			url = uri("https://maven.pkg.github.com/ERSMS-24L/ERSMS-24L")
			credentials {
				username = System.getenv("GITHUB_ACTOR") ?: settings.extra.properties["GITHUB_ACTOR"].toString()
				password = System.getenv("GITHUB_TOKEN") ?: settings.extra.properties["GITHUB_TOKEN"].toString()
			}
		}
	}
}

settings.gradle.allprojects {
	repositories.addAll(settings.pluginManagement.repositories)
}
