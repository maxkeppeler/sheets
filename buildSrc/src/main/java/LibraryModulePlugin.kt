import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.project

class LibraryModulePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        with(project) {
            applyPlugins()
            applyDependencies()
        }
    }

    private fun Project.applyPlugins() {
        plugins.run {
            apply(Plugins.LIBRARY.id)
            apply(Plugins.KOTLIN.id)
            apply(Plugins.DOKKA.id)
            apply(Plugins.MAVEN_PUBLISH.id)
        }
    }

    private fun Project.applyDependencies() {

        dependencies.apply {

            // All modules require the core module

            if (name != Modules.CORE.moduleName) {
                apis(project(Modules.CORE.path))
            }

            // Google libs

            implementations(Dependencies.Google.MATERIAL)

            // Kotlin libs

            implementations(Dependencies.Kotlin.KOTLIN_STD)

            // AndroidX libs

            implementations(
                Dependencies.AndroidX.ANNOTATIONS,
                Dependencies.AndroidX.CORE_KTX,
                Dependencies.AndroidX.CONSTRAINT_LAYOUT,
                Dependencies.AndroidX.RECYCLER_VIEW,
            )
        }
    }
}