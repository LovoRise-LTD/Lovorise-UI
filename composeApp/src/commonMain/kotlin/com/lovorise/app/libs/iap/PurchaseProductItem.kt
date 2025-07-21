package com.lovorise.app.libs.iap

data class PurchaseProductItem(
    val productId:String,
    val type:ProductType
){
    enum class ProductType{
        IN_APP,SUBSCRIPTION
    }
}
