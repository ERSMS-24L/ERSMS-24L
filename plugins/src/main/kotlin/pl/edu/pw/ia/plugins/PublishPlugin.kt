package pl.edu.pw.ia.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.getByName

class PublishPlugin : Plugin<Project> {

	override fun apply(project: Project) {
		with(project) {
			apply {
				plugin("org.gradle.maven-publish")
			}

			group = "pl.edu.pw.ia"

			extensions.configure<PublishingExtension>("publishing") {
				repositories {
					maven {
						name = "GitHubPackages"
						url = uri("https://maven.pkg.github.com/ERSMS-24L/ERSMS-24L")
						credentials {
							username = System.getenv("GITHUB_ACTOR") ?: project.findProperty("GITHUB_ACTOR").toString()
							password = System.getenv("GITHUB_TOKEN") ?: project.findProperty("GITHUB_TOKEN").toString()
						}
					}
				}
			}
		}
	}
}

val Project.publishing: PublishingExtension
	get() = extensions.getByName<PublishingExtension>("publishing")
