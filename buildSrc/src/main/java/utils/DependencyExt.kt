import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.apis(vararg list: ProjectDependency) {
    list.forEach { dependency ->
        add("api", dependency)
    }
}

fun DependencyHandler.implementations(vararg list: String) {
    list.forEach { dependency ->
        add("implementation", dependency)
    }
}

fun DependencyHandler.debugImplementations(vararg list: String) {
    list.forEach { dependency ->
        add("debugImplementation", dependency)
    }
}

fun DependencyHandler.androidTestImplementations(vararg list: String) {
    list.forEach { dependency ->
        add("androidTestImplementation", dependency)
    }
}

fun DependencyHandler.testImplementations(vararg list: String) {
    list.forEach { dependency ->
        add("testImplementation", dependency)
    }
}