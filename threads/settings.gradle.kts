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

rootProject.name = "ersms-threads"

apply(plugin = "pl.edu.pw.ia.settings")

buildscript {
	repositories.addAll(settings.pluginManagement.repositories)
	dependencies {
		"classpath"("pl.edu.pw.ia.settings:pl.edu.pw.ia.settings.gradle.plugin:0.0.1")
	}
}

include(
	"command",
	"query",
)
