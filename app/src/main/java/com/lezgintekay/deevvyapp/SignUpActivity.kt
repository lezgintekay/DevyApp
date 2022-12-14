package com.lezgintekay.deevvyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

    }

    fun signUp(view: View) {

        val email = emailTextSignUp.text.toString()
        val pass = passwordTextSignUp.text.toString()

        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val intent = Intent(this, NewsActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception->
            Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()

        }


    }
    fun backToSignIn(view:View) {
        val intent = Intent(this, UserActivity::class.java)
        startActivity(intent)
        finish()
    }
}