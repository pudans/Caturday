buildscript {

	repositories {
		google()
		mavenCentral()
	}
	dependencies {
		classpath("com.android.tools.build:gradle:7.0.0-alpha15")
//		classpath(kotlin("gradle-plugin", version = "1.4.32"))
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.32")
		classpath("com.google.dagger:hilt-android-gradle-plugin:2.34-beta")
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