package pl.edu.pw.ia.plugins

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

class SettingsPlugin : Plugin<Settings> {

	override fun apply(settings: Settings) {

		@Suppress("UnstableApiUsage")
		settings.dependencyResolutionManagement {
			repositories.addAll(settings.pluginManagement.repositories)
		}

		settings.gradle.allprojects {
			repositories.addAll(settings.pluginManagement.repositories)
		}
	}
}
