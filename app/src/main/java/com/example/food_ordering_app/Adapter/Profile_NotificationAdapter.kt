package com.example.food_ordering_app.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.food_ordering_app.NotificationBottomSheetFragment
import com.example.food_ordering_app.R
import com.example.food_ordering_app.ModelClass.NotificationItems

class Profile_NotificationAdapter(var context : NotificationBottomSheetFragment,var NotificationArrayList : ArrayList<NotificationItems>)
    : RecyclerView.Adapter<Profile_NotificationAdapter.NotificationViewHolder>(){

    class NotificationViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        var notification_image = itemView.findViewById<ImageView>(R.id.notificationImageView)
        var notification_text = itemView.findViewById<TextView>(R.id.notificationTextView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Profile_NotificationAdapter.NotificationViewHolder {
      val inflater = LayoutInflater.from(parent.context)
      val itemView=inflater.inflate(R.layout.notification_item,parent,false)
      return NotificationViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: Profile_NotificationAdapter.NotificationViewHolder,
        position: Int
    ) {
       val currentItem = NotificationArrayList[position]

        holder.notification_text.text = currentItem.notificationTitle
        holder.notification_image.setImageResource(currentItem.notificationImage)
    }

    override fun getItemCount(): Int {
       return NotificationArrayList.size
    }
}
