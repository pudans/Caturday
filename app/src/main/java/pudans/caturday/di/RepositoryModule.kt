/*
 * Designed and developed by 2020 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pudans.caturday.di

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
		firebaseStorage: FirebaseStorage,
	): FeedRepository {
		return FeedRepository(firebaseStorage)
	}

	@Provides
	@ViewModelScoped
	fun provideUploadFileRepository(
		firebaseStorage: FirebaseStorage,
		firebaseDatabase: FirebaseDatabase
	): UploadFileRepository {
		return UploadFileRepository(firebaseStorage, firebaseDatabase)
	}
}
