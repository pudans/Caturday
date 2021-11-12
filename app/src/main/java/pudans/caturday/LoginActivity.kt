package pudans.caturday

import android.content.Intent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@ExperimentalFoundationApi
@ExperimentalPagerApi
class LoginActivity : AppCompatActivity() {

	private val mFirebaseAuth = Firebase.auth

	private val mActivityResultListener = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
		try {
			GoogleSignIn
				.getSignedInAccountFromIntent(result.data)
				.getResult(ApiException::class.java)
				.idToken?.let { firebaseAuthWithGoogle(it) }
		} catch (e: Throwable) {
			onFailLogin()
		}
	}

	override fun onStart() {
		super.onStart()
		printText("Signing in")
		signIn()
	}

	private fun signIn() {
		val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestIdToken("664909726145-fqt5h52d3tn2oo5utpue0oh8set3uied.apps.googleusercontent.com")
			.requestEmail()
			.build()
		val client = GoogleSignIn.getClient(this@LoginActivity, options).signInIntent
		mActivityResultListener.launch(client)
	}

	private fun firebaseAuthWithGoogle(idToken: String) {
		val credential = GoogleAuthProvider.getCredential(idToken, null)
		mFirebaseAuth
			.signInWithCredential(credential)
			.addOnCompleteListener(this@LoginActivity) { task ->
				when (task.isSuccessful) {
					true -> onSuccessLogin()
					false -> onFailLogin()
				}
			}
	}

	private fun onSuccessLogin() {
		printText("Signed as ${mFirebaseAuth.currentUser?.displayName}")
		startActivity(Intent(baseContext, MainActivity::class.java))
		finish()
	}

	private fun onFailLogin() {
		printText("Error while login")
		finish()
	}

	private fun printText(text: String) {
		Toast.makeText(this.baseContext, text, Toast.LENGTH_SHORT).show()
	}
}