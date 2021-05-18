package pudans.caturday.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import pudans.caturday.repository.MainRepository
import pudans.caturday.network.CatApiClient
import pudans.caturday.repository.FeedRepository
import pudans.caturday.repository.UploadFileRepository

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

	@Provides
	@ViewModelScoped
	fun provideMainRepository(
		client: CatApiClient,
	): MainRepository {
		return MainRepository(client)
	}

	@Provides
	@ViewModelScoped
	fun provideFeedRepository(
		firebaseDatabase: FirebaseDatabase
	): FeedRepository {
		return FeedRepository(firebaseDatabase)
	}

	@Provides
	@ViewModelScoped
	fun provideUploadFileRepository(
		firebaseStorage: FirebaseStorage,
		firebaseDatabase: FirebaseDatabase,
		firebaseAuth: FirebaseAuth
	): UploadFileRepository {
		return UploadFileRepository(firebaseStorage, firebaseDatabase, firebaseAuth)
	}
}
