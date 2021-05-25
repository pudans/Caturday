package pudans.caturday

import android.content.Context
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File

object CacheUtils {

	private lateinit var mCache: SimpleCache

	fun getCache(context: Context): SimpleCache {
		if (!::mCache.isInitialized) {
			val cacheFolder = File(context.cacheDir, "media")
			val cacheEvictor = LeastRecentlyUsedCacheEvictor(1 * 1024 * 1024)
			mCache = SimpleCache(cacheFolder, cacheEvictor)
		}

		return mCache
	}
}