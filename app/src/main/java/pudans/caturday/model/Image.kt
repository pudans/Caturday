package pudans.caturday.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
@JsonClass(generateAdapter = true)
data class Image(
	@field:Json(name = "id") @PrimaryKey val name: String,
	@field:Json(name = "url") val url: String
) : Parcelable
