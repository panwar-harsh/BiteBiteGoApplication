package com.example.food_ordering_app

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

       Handler().postDelayed({
               val intent=Intent(this,StartActivity::class.java)
               startActivity(intent)
               finish()
           },3000)
    }
}