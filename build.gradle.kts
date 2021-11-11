buildscript {

	repositories {
		google()
		mavenCentral()
		gradlePluginPortal()
	}
	dependencies {
		classpath(dependencyNotation = "com.android.tools.build:gradle:7.1.0-alpha03")
		classpath(kotlin(module = "gradle-plugin", version = "1.5.31"))
		classpath(dependencyNotation = "com.google.dagger:hilt-android-gradle-plugin:2.40")
		classpath(dependencyNotation = "com.google.gms:google-services:4.3.10")
	}
}

allprojects {
	repositories {
		google()
		mavenCentral()
	}
}

tasks.register("clean", Delete::class) {
	delete(rootProject.buildDir)
}