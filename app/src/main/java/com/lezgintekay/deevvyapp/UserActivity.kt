package com.lezgintekay.deevvyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        auth = FirebaseAuth.getInstance()

        val registeredUser = auth.currentUser
        if (registeredUser != null) {
            val intent = Intent(this,NewsActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    fun signIn (view:View) {
        auth.signInWithEmailAndPassword(emailText.text.toString(),passwordText.text.toString()).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val currentUser = auth.currentUser?.email.toString()
                Toast.makeText(this,"Welcome : ${currentUser}",Toast.LENGTH_LONG).show()

                val intent = Intent(this, NewsActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener {exception ->
            Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_LONG).show()
        }

    }
    fun signUpPage(view: View) {

        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        finish()
    }

}