package pudans.caturday

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.firebase.auth.FirebaseAuth

@ExperimentalFoundationApi
@ExperimentalPagerApi
class StartActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		if (FirebaseAuth.getInstance().currentUser == null) {
			startActivity(Intent(baseContext, LoginActivity::class.java))
		} else {
			startActivity(Intent(baseContext, MainActivity::class.java))
		}

		finish()
	}
}