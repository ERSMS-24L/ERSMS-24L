plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `version-catalog`
    id("pl.edu.pw.ia.convention") version "0.0.1"
}

fun plugin(id: String, version: String) = "$id:$id.gradle.plugin:$version"

dependencies {
    implementation(plugin("io.freefair.lombok", "8.6"))
    implementation(plugin("com.coditory.manifest", "0.2.6"))
    implementation(plugin("org.jetbrains.kotlin.jvm", "1.9.22"))

    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

val publishPluginId = "pl.edu.pw.ia.publish"
val conventionPluginId = "pl.edu.pw.ia.convention"
val libraryPluginId = "pl.edu.pw.ia.library-convention"
val settingsPluginId = "pl.edu.pw.ia.settings"

gradlePlugin {
    plugins {
        create("publishConvention") {
            id = publishPluginId
            implementationClass = "pl.edu.pw.ia.plugins.PublishPlugin"
        }
        create("convention") {
            id = conventionPluginId
            implementationClass = "pl.edu.pw.ia.plugins.ConventionPlugin"
        }
        create("libraryConvention") {
            id = libraryPluginId
            implementationClass = "pl.edu.pw.ia.plugins.LibraryPlugin"
        }
        create("settingsConvention") {
            id = settingsPluginId
            implementationClass = "pl.edu.pw.ia.plugins.SettingsPlugin"
        }
    }
}

tasks.test {
    useJUnitPlatform()
}
