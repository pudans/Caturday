package pudans.caturday

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import pudans.caturday.ui.FeedScreen
import pudans.caturday.ui.ProfileScreen
import pudans.caturday.ui.theme.CaturdayTheme

@FlowPreview
@AndroidEntryPoint
@ExperimentalPagerApi
@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
@ExperimentalAnimationApi
class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		if (FirebaseAuth.getInstance().currentUser == null) {
			startActivity(Intent(this, LoginActivity::class.java))
		}

		setContent {
			val navController = rememberNavController()
			CaturdayTheme {
				Scaffold(
					bottomBar = {
						BottomNavigation(
							backgroundColor = Color.Black
						) {
							val navBackStackEntry by navController.currentBackStackEntryAsState()
							val currentRoute = navBackStackEntry?.destination?.route

							Screen.ITEMS.forEach { screen ->
								val isSelected = currentRoute == screen.route
								BottomNavigationItem(
									selectedContentColor = Color.Black,
									icon = { NavigationIcon(isSelected, screen) },
									label = { NavigationLAbel(isSelected, screen) },
									alwaysShowLabel = false,
									selected = isSelected,
									onClick = {
										if (screen != Screen.UploadVideo) {
											navController.navigate(screen.route) {
												popUpTo(navController.graph.startDestinationRoute!!) {
													saveState = true
												}
												launchSingleTop = true
												restoreState = true
											}
										} else {
											startActivity(Intent(this@MainActivity, UploadVideoActivity::class.java), intent.extras)
										}
									}
								)
							}
						}
					}
				) { innerPadding ->
					Box(modifier = Modifier.padding(innerPadding)) {
						NavHost(navController = navController, startDestination = Screen.Feed.route) {
							composable(Screen.Feed.route) { FeedTransitionAnimation { FeedScreen() } }
							composable(Screen.Profile.route) { ProfileTransitionAnimation { ProfileScreen() } }
						}
					}
				}
			}
		}
	}
}

@Composable
private fun NavigationIcon(isSelected: Boolean, screen: Screen) {
	Icon(
		imageVector = if (isSelected) screen.selectedIcon else screen.defaultIcon,
		contentDescription = "",
		tint = Color.White
	)
}

@Composable
private fun NavigationLAbel(isSelected: Boolean, screen: Screen) {
	Text(
		text = stringResource(screen.resourceId),
		color = Color.White
	)
}

@ExperimentalAnimationApi
@Composable
fun ProfileTransitionAnimation(content: @Composable AnimatedVisibilityScope.() -> Unit) {
	AnimatedVisibility(
		visibleState = remember { MutableTransitionState(false).apply { targetState = true } },
		enter = slideInHorizontally(initialOffsetX = { 400 }) + fadeIn(),
		exit = slideOutVertically() + fadeOut(),
		content = content,
	)
}

@ExperimentalAnimationApi
@Composable
fun FeedTransitionAnimation(content: @Composable AnimatedVisibilityScope.() -> Unit) {
	AnimatedVisibility(
		visibleState = remember { MutableTransitionState(false).apply { targetState = true } },
		enter = slideInHorizontally(initialOffsetX = { -400 }) + fadeIn(),
		exit = slideOutVertically() + fadeOut(),
		content = content,
	)
}



