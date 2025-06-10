package com.example.food_ordering_app

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.food_ordering_app.databinding.ActivityLocationBinding

class LocationActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLocationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val locationList : Array<String> = arrayOf("Jaipur","Haryana","Goa","Gurugram","New Delhi")

        val adapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,locationList)
        val autoCompleteTextView=binding.ListOfLocation
        autoCompleteTextView.setAdapter(adapter)
    }
}