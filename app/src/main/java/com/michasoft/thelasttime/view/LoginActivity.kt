package com.michasoft.thelasttime.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.michasoft.thelasttime.LastTimeApplication
import com.michasoft.thelasttime.R
import com.michasoft.thelasttime.databinding.ActivityLoginBinding
import com.michasoft.thelasttime.model.User
import com.michasoft.thelasttime.model.repo.UserRepository
import dagger.android.AndroidInjection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {
    @Inject lateinit var userRepository: UserRepository

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as LastTimeApplication).applicationComponent.inject(this)
        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.signInButton.setOnClickListener {
            signIn()
        }

        if (userRepository.currentUser != null) {
            routeFurther()
        }
    }

    private fun signIn() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            if(Firebase.auth.currentUser != null) {
                onSignIn()
            }
        } else {
            Timber.e("onSignInResult: error: " + result.resultCode)
        }
    }

    private fun onSignIn() {
        val authUser = Firebase.auth.currentUser!!
        CoroutineScope(Dispatchers.IO).launch {
            var user = userRepository.getUserByRemoteId(authUser.uid)
            if(user == null) {
                user = User(User.generateId(), authUser.uid, authUser.displayName ?: authUser.email!!)
                userRepository.insertUser(user)
            }
            val succeed = userRepository.signIn(user)
            withContext(Dispatchers.Main) {
                if (succeed) {
                    routeFurther()
                } else {
                    Toast.makeText(this@LoginActivity, "Sign in failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun routeFurther() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    companion object {
        fun start(activity: AppCompatActivity) {
            val intent = Intent(activity, LoginActivity::class.java)
            activity.startActivity(intent)
        }
    }
}