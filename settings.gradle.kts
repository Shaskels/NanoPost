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

rootProject.name = "NanoPost"
include(":app")
include(":util:networkchecker")
include(":util:datetime")
include(":component:uicomponents")
include(":shared:settings")
include(":shared:network")
include(":shared:auth:domain")
include(":shared:auth:remote")
include(":shared:profile:domain")
include(":util:image")
include(":shared:profile:remote")
