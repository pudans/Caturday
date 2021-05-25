//plugins {
//	id("io.gitlab.arturbosch.detekt").version("1.17.0")
//}

buildscript {

	repositories {
		google()
		mavenCentral()
	}
	dependencies {
		classpath("com.android.tools.build:gradle:7.0.0-beta02")
		classpath(kotlin("gradle-plugin", version = "1.4.32"))
//		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.32")
		classpath("com.google.dagger:hilt-android-gradle-plugin:2.34-beta")
		classpath("com.google.gms:google-services:4.3.8")
	}
}

allprojects {
	repositories {
//		maven {
//			url = uri("http://jcenter.bintray.com")
//		}
		google()
		mavenCentral()
	}
}

tasks.register("clean", Delete::class) {
	delete(rootProject.buildDir)
}

//detekt {
//	toolVersion = "1.17.0"
//	config = files("config/detekt/detekt.yml")
//	buildUponDefaultConfig = true
//}