plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "library-search"

include(
    "search-api",
    "common",
    "external:naver-client"
)
