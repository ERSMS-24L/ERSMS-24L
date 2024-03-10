package pl.edu.pw.ia.plugins

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class ConventionPlugin : Plugin<Project> {

	override fun apply(project: Project) {
		with(project) {
			apply {
				plugin("org.gradle.java")
				plugin("io.freefair.lombok")
				plugin("org.jetbrains.kotlin.jvm")
				plugin(PublishPlugin::class.java)
			}

			extensions.configure<JavaPluginExtension>("java") {
				toolchain {
					languageVersion.set(JavaLanguageVersion.of(21))
				}
				sourceCompatibility = JavaVersion.VERSION_21
				targetCompatibility = JavaVersion.VERSION_21
			}

			tasks.withType<KotlinCompile> {
				kotlinOptions {
					freeCompilerArgs += "-Xjsr305=strict"
					jvmTarget = "21"
				}
			}
		}
	}
}
