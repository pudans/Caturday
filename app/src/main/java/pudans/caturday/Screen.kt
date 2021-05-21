package pudans.caturday

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
	val route: String,
	@StringRes val resourceId: Int,
	val defaultIcon: ImageVector,
	val selectedIcon: ImageVector
) {
	object Feed : Screen("feed", R.string.feed, Icons.Outlined.Home, Icons.Filled.Home)
	object Profile : Screen("profile", R.string.profile, Icons.Outlined.Person, Icons.Filled.Person)
	object UploadVideo : Screen("upload_video", R.string.upload, Icons.Outlined.AddCircleOutline, Icons.Filled.AddCircleOutline)

	companion object {
		val ITEMS = listOf(Feed, UploadVideo, Profile)
	}
}