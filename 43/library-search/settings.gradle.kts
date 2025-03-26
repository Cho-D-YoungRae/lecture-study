plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "library-search"

include("search-api")
include("common")
include("external:naver-cilent")
findProject(":external:naver-cilent")?.name = "naver-cilent"
