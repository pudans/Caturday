package pudans.caturday.di

import android.app.Application
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

	@Provides
	@Singleton
	fun provideMoshi(): Moshi {
		return Moshi.Builder()
			.addLast(KotlinJsonAdapterFactory())
//			.addLast(CollectionJsonAdapter())
			.build()
	}
//
//	@Provides
//	@Singleton
//	fun provideAppDatabase(
//		application: Application,
//		typeResponseConverter: TypeResponseConverter
//	): AppDatabase {
//		return Room
//			.databaseBuilder(application, AppDatabase::class.java, "Pokedex.db")
//			.fallbackToDestructiveMigration()
//			.addTypeConverter(typeResponseConverter)
//			.build()
//	}
//
//	@Provides
//	@Singleton
//	fun providePokemonDao(appDatabase: AppDatabase): PokemonDao {
//		return appDatabase.pokemonDao()
//	}
//
//	@Provides
//	@Singleton
//	fun providePokemonInfoDao(appDatabase: AppDatabase): PokemonInfoDao {
//		return appDatabase.pokemonInfoDao()
//	}
//
//	@Provides
//	@Singleton
//	fun provideTypeResponseConverter(moshi: Moshi): TypeResponseConverter {
//		return TypeResponseConverter(moshi)
//	}
}
