import pudans.caturday.buildsrc.Dependencies

plugins {
	id("com.android.application")
	id("kotlin-android")
	id("kotlin-android-extensions")
	id("kotlin-kapt")
	id("dagger.hilt.android.plugin")
	id("com.google.gms.google-services")
}

android {
	compileSdkVersion(Dependencies.AndroidSdk.compile)
	buildToolsVersion("30.0.3")

	defaultConfig {
		applicationId = "pudans.caturday"
		minSdkVersion(Dependencies.AndroidSdk.min)
		targetSdkVersion(Dependencies.AndroidSdk.target)
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

//	implementation("com.google.firebase:firebase-bom:27.1.0")
//	implementation("com.google.firebase:firebase-analytics-ktx")
	implementation("com.google.firebase:firebase-analytics:19.0.0")
	implementation("com.google.firebase:firebase-storage-ktx:20.0.0")
	implementation("com.google.firebase:firebase-database-ktx:20.0.0")
	implementation("com.google.firebase:firebase-auth-ktx:21.0.1")
	implementation("com.google.android.gms:play-services-auth:19.0.0")

//	implementation("com.google.firebase:firebase-ml-vision:24.1.0")
//	implementation("com.google.firebase:firebase-ml-vision-image-label-model:20.0.2")

	implementation ("com.github.wseemann:FFmpegMediaMetadataRetriever-core:1.0.15")
	implementation ("com.github.wseemann:FFmpegMediaMetadataRetriever-native:1.0.15")

	implementation("com.google.mlkit:image-labeling:17.0.3")

	implementation("com.google.android.exoplayer:exoplayer-core:2.14.0")
	implementation("com.google.android.exoplayer:exoplayer-dash:2.14.0")
	implementation("com.google.android.exoplayer:exoplayer-ui:2.14.0")

	implementation("com.google.android.material:material:1.3.0")

	implementation("androidx.core:core-ktx:1.3.2")
	implementation("androidx.appcompat:appcompat:1.2.0")

	implementation("androidx.compose.ui:ui:1.0.0-beta06")
	implementation("androidx.compose.material:material:1.0.0-beta06")
	implementation("androidx.compose.ui:ui-tooling:1.0.0-beta06")
	implementation("androidx.compose.material:material-icons-extended:1.0.0-beta06")

	implementation("androidx.activity:activity-compose:1.3.0-alpha07")

	implementation(Dependencies.Lifecycle.livedata)
	implementation(Dependencies.Lifecycle.runtime)
	implementation(Dependencies.Lifecycle.viewmodel)

	implementation(Dependencies.Room.core)
	kapt(Dependencies.Room.runtime)
	kapt(Dependencies.Room.compiler)

	// hilt
	implementation("com.google.dagger:hilt-android:2.34-beta")
	kapt("com.google.dagger:hilt-compiler:2.34-beta")
	kapt("androidx.hilt:hilt-compiler:1.0.0")

	// network
	implementation("com.squareup.retrofit2:retrofit:2.9.0")
	implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
	implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")
	implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

	// moshi
	implementation(Dependencies.Moshi.core)
	kapt(Dependencies.Moshi.codegen)

	// coroutines
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.3")

	// debugging
	implementation(Dependencies.Timber.core)

	implementation("io.coil-kt:coil:1.2.1")

	implementation("com.google.accompanist:accompanist-coil:0.8.1")

	implementation("com.google.accompanist:accompanist-pager:0.9.1")
}