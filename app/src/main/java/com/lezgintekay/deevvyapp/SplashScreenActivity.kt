package com.lezgintekay.deevvyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_screen_splash.*

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var logoAnimation : Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_splash)

        logoAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_intro_logo)
        imageView.animation = logoAnimation

        val timer = object : CountDownTimer(3000,1000) {
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                val intent = Intent(this@SplashScreenActivity, UserActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
        timer.start()

    }


}