package com.example.food_ordering_app.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.food_ordering_app.Adapter.MenuAdapter
import com.example.food_ordering_app.MenuBottomSheetFragment
import com.example.food_ordering_app.ModelClass.MenuItemModel
import com.example.food_ordering_app.R
import com.example.food_ordering_app.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var myRecyclerView: RecyclerView
    private var FoodArrayList: ArrayList<MenuItemModel> = arrayListOf()
    private lateinit var database : FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        myRecyclerView = binding.recyclerViewId
        binding.ViewMenuButton.setOnClickListener {
            val bottomSheetFragment = MenuBottomSheetFragment()
            bottomSheetFragment.show(parentFragmentManager, "test")
        }
        RetriveAndDisplayPopularItems()
        return binding.root
    }

    private fun RetriveAndDisplayPopularItems() {

        database = FirebaseDatabase.getInstance()
        val foodReference : DatabaseReference = database.reference.child("menu")

        //Fetch data from Firebase
        foodReference.addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                if (!isAdded || context == null) return
                FoodArrayList.clear()
             for(foodSnapshot in snapshot.children){
                 val foodItem = foodSnapshot.getValue(MenuItemModel::class.java)
                 if (foodItem != null) {
                     FoodArrayList.add(foodItem)
                 }
             }
                RandomPopularItems()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load menu: ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun RandomPopularItems() {
         val index = FoodArrayList.indices.toList().shuffled()
        val ItemsToShow = 6
        val subsetMenuItems = index.take(ItemsToShow).map { FoodArrayList[it] }

        setPopularItemAdapter(subsetMenuItems)
    }

    private fun setPopularItemAdapter(subsetMenuItems: List<MenuItemModel>) {
        if (!isAdded || context == null) return
        myRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        myRecyclerView.adapter = MenuAdapter(requireContext(),subsetMenuItems)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.pic1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.pic2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.pic3, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.pic4, ScaleTypes.FIT))

        val ImageSlider = binding.imageSlider
        ImageSlider.setImageList(imageList)
        ImageSlider.setImageList(imageList, ScaleTypes.FIT)

        ImageSlider.setItemClickListener(object : ItemClickListener {
            override fun doubleClick(position: Int) {
                // No action needed or add future implementation
            }

            override fun onItemSelected(position: Int) {

                val itemPosition = imageList[position]
                val itemMessage = "Selected Image $position"
                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}

