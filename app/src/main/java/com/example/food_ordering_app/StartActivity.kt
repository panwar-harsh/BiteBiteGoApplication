package com.example.food_ordering_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.food_ordering_app.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    private lateinit var binding : ActivityStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.NextButton.setOnClickListener()
        {
            val intent= Intent(this,LoginActivity :: class.java)
            startActivity(intent)
            finish()
        }
    }
}