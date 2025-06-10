package com.example.food_ordering_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.food_ordering_app.databinding.ActivityMainBinding
import com.example.food_ordering_app.fragment.CartFragment
import com.example.food_ordering_app.fragment.HomeFragment
import com.example.food_ordering_app.fragment.ListFragment
import com.example.food_ordering_app.fragment.ProfileFragment
import com.example.food_ordering_app.fragment.SearchFragment

class MainActivity : AppCompatActivity() { // Change this line

    // View Binding instance
    private lateinit var binding: ActivityMainBinding
    private var isTransactionPending = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize with HomeFragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, HomeFragment())
                .commit()
        }

        binding.notificationButton.setOnClickListener{
            val bottomSheetFragment = NotificationBottomSheetFragment()
            bottomSheetFragment.show(supportFragmentManager, "test")
        }

        binding.bottomNavigation.setOnItemSelectedListener { item->

            if (isTransactionPending) {
                return@setOnItemSelectedListener true
            }

            when(item.itemId){
                R.id.homeFragment -> replaceWithFragment(HomeFragment())
                R.id.cartFragment2 -> replaceWithFragment(CartFragment())
                R.id.searchFragment -> replaceWithFragment(SearchFragment())
                R.id.listFragment -> replaceWithFragment(ListFragment())
                R.id.profileFragment -> replaceWithFragment(ProfileFragment())
            }
            true
        }

    }

    private fun replaceWithFragment(fragment : Fragment) {
        isTransactionPending = true
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
        // Reset flag after transaction completes
        binding.root.post { isTransactionPending = false }
    }
}

