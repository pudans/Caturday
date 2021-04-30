package pudans.caturday.buildsrc

object Dependencies {

	object AndroidSdk {
		const val min = 29
		const val compile = 30
		const val target = compile
	}

	object Compose {

		const val version = "1.0.0-beta05"
	}

	object Timber {
		private const val version = "4.7.1"
		const val core = "com.jakewharton.timber:timber:$version"
	}

	object Moshi {
		private const val version = "1.11.0"
		const val core = "com.squareup.moshi:moshi-kotlin:$version"
		const val codegen = "com.squareup.moshi:moshi-kotlin-codegen:$version"
	}

	object Lifecycle {
		private const val version = "2.3.1"
		const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
		const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
		const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
	}

	object Room {
		private const val version = "2.3.0-rc01"
		const val core = "androidx.room:room-ktx:$version"
		const val runtime = "androidx.room:room-runtime:$version"
		const val compiler = "androidx.room:room-compiler:$version"

	}
}