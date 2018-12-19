package com.ever.four.deptomaniger.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ever.four.deptomaniger.R
import com.ever.four.deptomaniger.util.BundleIdentifier
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    val RC_SIGN_IN = 1
    private lateinit var gso: GoogleSignInOptions
    private lateinit var googleApiClient: GoogleApiClient
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private var onConnectionFailedListener = OnConnectionFailedListener {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        firebaseAuth = FirebaseAuth.getInstance()

        authStateListener = FirebaseAuth.AuthStateListener{
            it.currentUser?.let {
                Intent(this, ShoppingListActivity::class.java).also {
                    startActivity(it)
                }
            }
        }

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
            requestIdToken(getString(R.string.default_web_client_id)).
            requestEmail().
            build()

        googleApiClient = GoogleApiClient.Builder(this).
            enableAutoManage(this, onConnectionFailedListener).
            addApi(Auth.GOOGLE_SIGN_IN_API, gso).
            build()

        googleSignInButton.setOnClickListener { googleSignIn() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode.equals(RC_SIGN_IN)) {
            var result: GoogleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                firebaseAuthWithGoogle(result.signInAccount)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    private fun firebaseAuthWithGoogle(signInAccount: GoogleSignInAccount?) {
        var credentials = GoogleAuthProvider.getCredential((signInAccount?.idToken), null)
        firebaseAuth.signInWithCredential(credentials).addOnCompleteListener {
            if (it.isSuccessful) {
                firebaseAuth.currentUser?.let {
                    var firebaseUser = it
                }
            } else {
                //authentication failed
            }
        }
    }

    private fun googleSignIn() {
        var intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        startActivityForResult(intent, RC_SIGN_IN)
    }
}
