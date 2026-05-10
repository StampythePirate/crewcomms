pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "CrewComms"
include(
    ":app-phone",
    ":app-watch",
    ":core-model",
    ":core-transport",
    ":core-database",
    ":core-common"
)
