package com.example.food_ordering_app.ModelClass

data class CartItems(var foodName : String?=null,
                     var foodPrice : String?=null,
                     var foodImage : String?=null,
                     var shortDescription : String?=null,
                     var ingredients : String?=null,
                     var foodQuantity : Int?=null)
