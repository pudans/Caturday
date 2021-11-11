package pudans.caturday.buildsrc

object Dependencies {

	object AndroidSdk {
		const val minSdk = 30
		const val compileSdk = 31
		const val buildToolsVersion = "31.0.0"
		const val targetSdk = compileSdk
	}

	object Compose {
		const val version = "1.1.0-beta02"
		const val ui = "androidx.compose.ui:ui:$version"
		const val ui_tooling = "androidx.compose.ui:ui-tooling:$version"
		const val material = "androidx.compose.material:material:$version"
		const val material_icons = "androidx.compose.material:material-icons-extended:$version"
	}

	object Timber {
		private const val version = "4.7.1"
		const val core = "com.jakewharton.timber:timber:$version"
	}

	object Lifecycle {
		private const val version = "2.4.0"
		const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
		const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
		const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
	}

	object Room {
		private const val version = "2.3.0"
		const val core = "androidx.room:room-ktx:$version"
		const val runtime = "androidx.room:room-runtime:$version"
		const val compiler = "androidx.room:room-compiler:$version"

	}
	
	object Firebase {
		const val analytics = "com.google.firebase:firebase-analytics-ktx:20.0.0"
		const val storage = "com.google.firebase:firebase-storage-ktx:20.0.0"
		const val database = "com.google.firebase:firebase-database-ktx:20.0.2"
		const val auth = "com.google.firebase:firebase-auth-ktx:21.0.1"
	}

	object ExoPlayer {
		private const val version = "2.16.0"
		const val core = "com.google.android.exoplayer:exoplayer-core:$version"
	}

	object Accompanist {
		private const val version = "0.21.2-beta"
		const val coil = "com.google.accompanist:accompanist-coil:0.15.0"
		const val pager = "com.google.accompanist:accompanist-pager:$version"
		const val pager_indicator = "com.google.accompanist:accompanist-pager-indicators:$version"
	}
}