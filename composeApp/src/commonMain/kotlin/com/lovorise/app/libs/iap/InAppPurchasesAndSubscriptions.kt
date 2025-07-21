package com.lovorise.app.libs.iap

interface InAppPurchasesAndSubscriptions {

     fun startTransaction(item: PurchaseProductItem,onPurchased:(Any)->Unit)

}

