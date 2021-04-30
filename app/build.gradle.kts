import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
	id("com.android.application")
	kotlin("android")
	kotlin("android.extensions")
	kotlin("kapt")
	id("dagger.hilt.android.plugin")
}

android {
	compileSdkVersion(30)
	buildToolsVersion("30.0.3")

	defaultConfig {
		applicationId = "pudans.caturday"
		minSdkVersion(29)
		targetSdkVersion(30)
		versionCode = 1
		versionName = "1.0"
	}

	buildTypes {
		getByName("release") {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile(
					"proguard-android-optimize.txt"),
					"proguard-rules.pro")
		}
	}
	compileOptions {
		sourceCompatibility(JavaVersion.VERSION_1_8)
		targetCompatibility(JavaVersion.VERSION_1_8)
	}
	kotlinOptions {
		jvmTarget = "1.8"
		useIR = true
	}
//	buildFeatures {
//		compose(true)
//	}
//	composeOptions {
//		kotlinCompilerExtensionVersion("1.0.0-beta05")
//		kotlinCompilerVersion("1.4.32")
//	}
}

dependencies {

	implementation("androidx.core:core-ktx:1.3.2")
	implementation("androidx.appcompat:appcompat:1.2.0")
	implementation("com.google.android.material:material:1.3.0")

	implementation("androidx.compose.ui:ui:1.0.0-beta05")
	implementation("androidx.compose.material:material:1.0.0-beta05")
	implementation("androidx.compose.ui:ui-tooling:1.0.0-beta05")

	implementation("androidx.activity:activity-compose:1.3.0-alpha07")

	implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
	implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")

	implementation("androidx.room:room-runtime:2.3.0-rc01")
	implementation("androidx.room:room-ktx:2.3.0-rc01")
	kapt("androidx.room:room-compiler:2.3.0-rc01")

	// hilt
	implementation("com.google.dagger:hilt-android:2.34-beta")
	kapt("com.google.dagger:hilt-compiler:2.34-beta")
	kapt("androidx.hilt:hilt-compiler:1.0.0-beta01")

	// network
	implementation("com.squareup.retrofit2:retrofit:2.9.0")
	implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
	implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")
	implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

	// moshi
	implementation("com.squareup.moshi:moshi-kotlin:1.11.0")
	kapt("com.squareup.moshi:moshi-kotlin-codegen:1.11.0")

	// coroutines
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.3")

	// debugging
	implementation("com.jakewharton.timber:timber:4.7.1")

	implementation("io.coil-kt:coil:1.2.1")

	implementation("com.google.accompanist:accompanist-coil:0.8.1")
}