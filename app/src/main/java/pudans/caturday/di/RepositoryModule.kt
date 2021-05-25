package pudans.caturday.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import pudans.caturday.repository.FeedRepository
import pudans.caturday.repository.ProfileLikedRepository
import pudans.caturday.repository.ProfileUploadedRepository
import pudans.caturday.repository.UploadFileRepository

@ExperimentalCoroutinesApi
@Module
@FlowPreview
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

	@Provides
	@ViewModelScoped
	@ExperimentalCoroutinesApi
	fun provideFeedRepository(
		firebaseDatabase: FirebaseDatabase
	): FeedRepository = FeedRepository(firebaseDatabase)

	@Provides
	@ViewModelScoped
	fun provideUploadFileRepository(
		firebaseStorage: FirebaseStorage,
		firebaseDatabase: FirebaseDatabase,
		firebaseAuth: FirebaseAuth
	): UploadFileRepository = UploadFileRepository(firebaseStorage, firebaseDatabase, firebaseAuth)

	@Provides
	@ViewModelScoped
	@ExperimentalCoroutinesApi
	fun provideProfileLikedRepository(
		firebaseDatabase: FirebaseDatabase,
		firebaseAuth: FirebaseAuth
	): ProfileLikedRepository = ProfileLikedRepository(firebaseDatabase, firebaseAuth)

	@Provides
	@ViewModelScoped
	@ExperimentalCoroutinesApi
	fun provideProfileUploadedRepository(
		firebaseDatabase: FirebaseDatabase,
		firebaseAuth: FirebaseAuth
	): ProfileUploadedRepository = ProfileUploadedRepository(firebaseDatabase, firebaseAuth)
}
