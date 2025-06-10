package com.example.food_ordering_app.ModelClass

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class OrderDetails : Serializable, Parcelable {
    var userUid: String? = null
    var userName: String? = null
    var foodNames: MutableList<String>? = null
    var foodImages: MutableList<String>? = null
    var foodPrices: MutableList<String>? = null
    var foodQuantities: MutableList<Int>? = null
    var address: String? = null
    var totalPrice: String? = null
    var phoneNo: String? = null
    var orderAccepted: Boolean = false
    var paymentReceived: Boolean = false
    var itemPushKey: String? = null
    var currentTime: Long = 0

    // Primary constructor
    constructor()

    // Secondary constructor
    constructor(
        userId: String,
        name: String,
        foodNameList: ArrayList<String>,
        foodImageList: ArrayList<String>,
        foodPriceList: ArrayList<String>,
        cartItemQuantity: ArrayList<Int>,
        address: String,
        totalAmount: String,
        phone: String,
        time: Long,
        itemPushKey: String?,
        orderAccepted: Boolean,
        paymentReceived: Boolean
    ) : this() {
        this.userUid = userId
        this.userName = name
        this.foodNames = foodNameList
        this.foodPrices = foodPriceList
        this.foodImages = foodImageList
        this.foodQuantities = cartItemQuantity.toMutableList()
        this.address = address
        this.totalPrice = totalAmount
        this.phoneNo = phone
        this.currentTime = time
        this.itemPushKey = itemPushKey
        this.orderAccepted = orderAccepted
        this.paymentReceived = paymentReceived
    }

    // Parcelable constructor
    constructor(parcel: Parcel) : this() {
        userUid = parcel.readString()
        userName = parcel.readString()
        foodNames = parcel.createStringArrayList()
        foodImages = parcel.createStringArrayList()
        foodPrices = parcel.createStringArrayList()
        foodQuantities = parcel.createIntArray()?.toMutableList()
        address = parcel.readString()
        totalPrice = parcel.readString()
        phoneNo = parcel.readString()
        orderAccepted = parcel.readByte() != 0.toByte()
        paymentReceived = parcel.readByte() != 0.toByte()
        itemPushKey = parcel.readString()
        currentTime = parcel.readLong()
    }

    // Write object data to Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userUid)
        parcel.writeString(userName)
        parcel.writeStringList(foodNames)
        parcel.writeStringList(foodImages)
        parcel.writeStringList(foodPrices)
        parcel.writeIntArray(foodQuantities?.toIntArray())
        parcel.writeString(address)
        parcel.writeString(totalPrice)
        parcel.writeString(phoneNo)
        parcel.writeByte(if (orderAccepted) 1 else 0)
        parcel.writeByte(if (paymentReceived) 1 else 0)
        parcel.writeString(itemPushKey)
        parcel.writeLong(currentTime)
    }

    // Describe contents of Parcelable
    override fun describeContents(): Int {
        return 0
    }

    // Companion object for Parcelable
    companion object CREATOR : Parcelable.Creator<OrderDetails> {
        override fun createFromParcel(parcel: Parcel): OrderDetails {
            return OrderDetails(parcel)
        }

        override fun newArray(size: Int): Array<OrderDetails?> {
            return arrayOfNulls(size)
        }
    }
}
