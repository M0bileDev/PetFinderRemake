pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

rootProject.name = "PetFinderRemake"
include(":app")
include(":common")
include(":features:search")
include(":features:follow")
include(":logging")
include(":features:discover")
include(":features:details:animals")
include(":features:filter")
include(":features:gallery")
include(":features:notifications")
