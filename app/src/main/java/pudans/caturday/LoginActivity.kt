package pudans.caturday

import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

	private val mFirebaseAuth = Firebase.auth

	private val mActivityResultListener = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
		try {
			GoogleSignIn
				.getSignedInAccountFromIntent(result.data)
				.getResult(ApiException::class.java)
				.idToken?.let { firebaseAuthWithGoogle(it) }
		} catch (e: Throwable) {
			printText("Error while login")
		}
	}

	override fun onStart() {
		super.onStart()

		printText("Sign in")
		signIn()
	}

	private fun signIn() {
		val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestIdToken(getString(R.string.default_web_client_id))
			.requestEmail()
			.build()
		mActivityResultListener.launch(GoogleSignIn.getClient(this, options).signInIntent)
	}

	private fun openFeedScreen() {
		finish()
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
				}
			}
	}

	private fun printText(text: String) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
	}
}