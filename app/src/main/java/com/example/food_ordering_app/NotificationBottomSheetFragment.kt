package com.example.food_ordering_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.food_ordering_app.Adapter.Profile_NotificationAdapter
import com.example.food_ordering_app.databinding.FragmentNotificationBottomSheetBinding
import com.example.food_ordering_app.ModelClass.NotificationItems
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NotificationBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding : FragmentNotificationBottomSheetBinding
    private lateinit var NotificationArrayList : ArrayList<NotificationItems>
    private lateinit var myRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotificationArrayList = arrayListOf()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notificationTitle= arrayOf(
            "Your order has been Canceled Successfully",
            "Order has been taken by the driver",
            "Congrats Your Order Placed"
        )
        val notificationImage = arrayOf(
            R.drawable.notification_image1,
            R.drawable.notification_image2,
            R.drawable.notification_image3
        )

        for (index in notificationTitle.indices)
        {
            val item = NotificationItems(
                notificationTitle[index],
                notificationImage[index]
            )
            NotificationArrayList.add(item)
        }
        //RecyclerView Setup

        myRecyclerView = binding.notificationRecylerView

        myRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        myRecyclerView.adapter = Profile_NotificationAdapter(this , NotificationArrayList)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBottomSheetBinding.inflate(layoutInflater,container,false)

        return binding.root
    }
}
