package pl.edu.pw.ia.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.withType

class LibraryPlugin : Plugin<Project> {

	override fun apply(project: Project) {
		with(project) {
			apply {
				plugin(ConventionPlugin::class.java)
				plugin("com.coditory.manifest")
				plugin("org.gradle.java-library")
			}

			extensions.configure<JavaPluginExtension>("java") {
				withSourcesJar()
			}

			extensions.configure<PublishingExtension>("publishing") {
				publications {
					create<MavenPublication>("maven") {
						from(components["java"])
					}
				}
			}

			val moduleName = findProperty("pl.edu.pw.ia.module-name") ?: project.name

			tasks.withType<Jar> {
				manifest {
					attributes(mapOf("Automatic-Module-Name" to moduleName))
				}
			}
		}
	}
}
