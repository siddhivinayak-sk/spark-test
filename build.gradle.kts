plugins {
	java
	application
}

group = "com.sk.spark.jobs"
version = "0.0.1-SNAPSHOT"
val mainClassCanonicalName: String by project
val sparkCoreVersion: String by project

application {
    mainClass.set(mainClassCanonicalName)
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
	sourceCompatibility = JavaVersion.VERSION_11
	targetCompatibility = JavaVersion.VERSION_11
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.apache.spark:spark-core_2.12:$sparkCoreVersion")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

//tasks.withType<Jar> {
//	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
//    manifest {
//        attributes["Main-Class"] = mainClassCanonicalName
//    }
//    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
//}
