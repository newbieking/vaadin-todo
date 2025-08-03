plugins {
    kotlin("jvm") version "2.2.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    // vaadin
    implementation("com.vaadin:vaadin-core:24.7.2")
    implementation("com.github.mvysny.vaadin-boot:vaadin-boot:13.3")
    implementation("com.github.mvysny.karibudsl:karibu-dsl:2.3.2")

    // exposed
    implementation("org.jetbrains.exposed:exposed-core:0.61.0")
    implementation("org.jetbrains.exposed:exposed-java-time:0.61.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.61.0")

    // database
    implementation("com.zaxxer:HikariCP:6.3.0")
    implementation("org.postgresql:postgresql:42.7.5")

    // rxjava
//    implementation("io.reactivex.rxjava3:rxkotlin:3.0.1")


    testImplementation(kotlin("test"))
}

// build.gradle.kts
//import org.gradle.api.tasks.Copy

tasks.register<Copy>("copyVaadinStyles") {
    from("src/main/frontend/themes/vaadin-app/styles.css")
    into("src/main/resources/META-INF/resources/frontend/styles") // 目标目录
}

// 将任务添加到资源处理流程
tasks.named("processResources") {
    dependsOn("copyVaadinStyles")
}


tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}