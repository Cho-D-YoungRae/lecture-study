tasks.getByName("bootJar") {
    enabled = true
}

dependencies {
    implementation(project(":common"))
    implementation(project(":external:naver-client"))
}