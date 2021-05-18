package pudans.caturday

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@ExperimentalPagerApi
class LoginActivity : AppCompatActivity() {

	private val mFirebaseAuth = Firebase.auth

	override fun onStart() {
		super.onStart()

		val currentUser = mFirebaseAuth.currentUser
		if (currentUser == null) {
			printText("Sign in")
			signIn()

		} else {
			printText("Login as ${currentUser.email}")
			openFeedScreen()
			finish()
		}
	}

	private fun signIn() {
		val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestIdToken(getString(R.string.default_web_client_id))
			.requestEmail()
			.build()
		val googleSignInClient = GoogleSignIn.getClient(this, options)
		startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)
	}

	private fun openFeedScreen() {
		startActivity(Intent(this, FeedActivity::class.java))
//		startActivity(Intent(this, UploadVideoActivity::class.java))

	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		if (requestCode == RC_SIGN_IN) {
			try {
				GoogleSignIn
					.getSignedInAccountFromIntent(data)
					.getResult(ApiException::class.java)
					.idToken?.let { firebaseAuthWithGoogle(it) }
			} catch (e: Throwable) {
				printText("Error while login")
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data)
		}
	}

	private fun firebaseAuthWithGoogle(idToken: String) {
		val credential = GoogleAuthProvider.getCredential(idToken, null)
		mFirebaseAuth
			.signInWithCredential(credential)
			.addOnCompleteListener(this) { task ->
				if (task.isSuccessful) {
					openFeedScreen()
				} else {
					printText("Error while login")
					finish()
				}
			}
	}

	private fun printText(text: String) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
	}

	companion object {
		private const val RC_SIGN_IN = 9001
	}
}