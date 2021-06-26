import pudans.caturday.buildsrc.Dependencies

plugins {
	id("com.android.application")
	kotlin("android")
	kotlin("kapt")
	id("dagger.hilt.android.plugin")
	id("com.google.gms.google-services")
//	id("io.gitlab.arturbosch.detekt")
}

android {
	compileSdk = Dependencies.AndroidSdk.compileSdk
	buildToolsVersion = Dependencies.AndroidSdk.buildToolsVersion

	defaultConfig {
		applicationId = "pudans.caturday"
		minSdk = Dependencies.AndroidSdk.minSdk
		targetSdk = Dependencies.AndroidSdk.targetSdk
		versionCode = 1
		versionName = "1.0"
	}

	buildTypes {
		getByName("release") {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
		useIR = true
	}
	buildFeatures {
		compose = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = Dependencies.Compose.version
	}
}

dependencies {

	implementation("com.google.android.gms:play-services-auth:19.0.0")

	implementation(Dependencies.Firebase.auth)
	implementation(Dependencies.Firebase.analytics)
	implementation(Dependencies.Firebase.database)
	implementation(Dependencies.Firebase.storage)

	implementation("com.google.mlkit:image-labeling:17.0.3")

	implementation(Dependencies.ExoPlayer.core)

	implementation("com.google.android.material:material:1.3.0")

	implementation("androidx.core:core-ktx:1.3.2")
	implementation("androidx.appcompat:appcompat:1.2.0")
	implementation("androidx.datastore:datastore:1.0.0-beta01")
	implementation("androidx.datastore:datastore-preferences-core:1.0.0-beta01")

	implementation(Dependencies.Compose.ui)
	implementation(Dependencies.Compose.ui_tooling)
	implementation(Dependencies.Compose.material)
	implementation(Dependencies.Compose.material_icons)

	implementation("androidx.activity:activity-compose:1.3.0-beta02")

	implementation("androidx.navigation:navigation-compose:2.4.0-alpha03")
	implementation("androidx.hilt:hilt-navigation-compose:1.0.0-alpha03")

	implementation(Dependencies.Lifecycle.livedata)
	implementation(Dependencies.Lifecycle.runtime)
	implementation(Dependencies.Lifecycle.viewmodel)

//	implementation(Dependencies.Room.core)
//	kapt(Dependencies.Room.runtime)
//	kapt(Dependencies.Room.compiler)

	// hilt
	implementation("com.google.dagger:hilt-android:2.35")
	kapt("com.google.dagger:hilt-compiler:2.35")
	kapt("androidx.hilt:hilt-compiler:1.0.0")

	// coroutines
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0")

	// debugging
	implementation(Dependencies.Timber.core)

	implementation("com.google.accompanist:accompanist-coil:0.12.0")
	implementation("com.google.accompanist:accompanist-pager:0.12.0")
	implementation("com.google.accompanist:accompanist-pager-indicators:0.12.0")

}