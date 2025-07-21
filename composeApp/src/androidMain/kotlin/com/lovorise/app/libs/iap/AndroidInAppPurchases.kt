package com.lovorise.app.libs.iap

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.AcknowledgePurchaseResponseListener
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.ProductType
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.queryPurchasesAsync
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class InAppPurchaseAndSubscription(
    val context: Context,
    val activity: Activity,
    private val androidCBHook : (JSONObject)->Unit
):InAppPurchasesAndSubscriptions
{
    /**
     * Indicates whether the product is consumable.
     */
    private var isProductConsumable:Boolean? = null
    /**
     * The billing client instance for handling in-app purchases.
     */
    private var billingClient: BillingClient

    private var purchaseCB : (GoogleReceiptData)->Unit = {}

    /**
     * Listener for purchases updated events.
     */
    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (purchase in purchases) {
                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
                        if (isProductConsumable != null) {
                            acknowledgePurchase(
                                isProductConsumable = isProductConsumable!!,
                                purchaseToken = purchase.purchaseToken
                            )
                            callbackFunCall(purchase)
                        }
                    }
                }
            }
        }

    /**
     * Initializes the billing client instance.
     */
    init {

        billingClient = BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()

    }

    /**
     * Listener for acknowledge purchase response.
     */

    private val acknowledgePurchaseResponseListener = AcknowledgePurchaseResponseListener {
        if (it.responseCode == BillingClient.BillingResponseCode.OK) {
            print(it)
            //here purchase is successfully acknowledged and we cannot make purchase to this product
            //WebToNativeInterface.loadJsCode("javascript:console.log(\"acknowledged\" $it);")
        }
    }

    /**
     * Retrieves the purchase history for both in-app purchases and subscriptions.
     */
    fun getPurchaseHistory() {
        startConnection(false,"","",false)
    }

    /**
     * Returns the purchase history to the callback function.
     */
    fun returnPurchaseHistory(){
        try {
            CoroutineScope(Dispatchers.IO).launch {

                val respArray = JSONArray()
                val response = JSONObject()
                response.put("type", "purchaseList")

                val subsParams = QueryPurchasesParams.newBuilder()
                    .setProductType(ProductType.SUBS)

                val inAppParams = QueryPurchasesParams.newBuilder()
                    .setProductType(ProductType.INAPP)


                val subsPurchaseResult = billingClient.queryPurchasesAsync(subsParams.build())
                val inAppPurchaseResult = billingClient.queryPurchasesAsync(inAppParams.build())


                subsPurchaseResult.purchasesList.forEach { purchase ->
                    val purchaseObj = JSONObject(purchase.originalJson)
                    respArray.put(purchaseObj)
                }

                inAppPurchaseResult.purchasesList.forEach { purchase ->
                    val purchaseObj = JSONObject(purchase.originalJson)
                    respArray.put(purchaseObj)
                }
                response.put("purchaseData",respArray)
                androidCBHook.invoke(response)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    /**
     * Acknowledges a purchase and handles consumable and non-consumable products.
     */
    private fun acknowledgePurchase(isProductConsumable: Boolean, purchaseToken: String) {
        if (isProductConsumable) {
            val consumeParams =
                ConsumeParams.newBuilder()
                    .setPurchaseToken(purchaseToken)
                    .build()

            billingClient.consumeAsync(consumeParams) { billingResult, purchaseToken ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    print(purchaseToken)
                }
            }
        } else {
            val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchaseToken)
            billingClient.acknowledgePurchase(acknowledgePurchaseParams.build(),
                acknowledgePurchaseResponseListener)
        }
    }

    /**
     * Invokes the callback function with the purchase details.
     */
    private fun callbackFunCall(purchase: Purchase) {
        val purchaseObj = JSONObject(purchase.originalJson)
        //val purchaseToken = if (purchaseObj.has("purchaseToken")) purchaseObj.getString("purchaseToken") else ""
        val productId = if (purchaseObj.has("productId")) purchaseObj.getString("productId") else ""
        //val transactionId = if (purchaseObj.has("orderId")) purchaseObj.getString("orderId") else ""
        purchaseCB.invoke(GoogleReceiptData(purchaseToken = purchase.purchaseToken, productId = productId, orderId = purchase.orderId ?: "", purchaseTime = purchase.purchaseTime,
            purchaseState = purchase.purchaseState, acknowledged = purchase.isAcknowledged, packageName = purchase.packageName, quantity = purchase.quantity))
    }

    /**
     * Starts the purchase transaction by connecting to the billing client.
     */
    override fun startTransaction(item: PurchaseProductItem, onPurchased: (Any) -> Unit) {
        this.purchaseCB = onPurchased
        startConnection(true, productType = if (item.type == PurchaseProductItem.ProductType.SUBSCRIPTION) ProductType.SUBS else ProductType.INAPP, productId = item.productId,true)
    }

    //    fun startTransaction(productType: String, productId: String, isConsumable: Boolean){
//    }

    /**
     * Establishes a connection to the billing client and starts the purchase flow.
     */
    private fun startConnection(callTransaction: Boolean, productType: String, productId: String, isConsumable: Boolean) {
        isProductConsumable = isConsumable
        if(productType == ProductType.SUBS){
            isProductConsumable = false
        }
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    if(callTransaction){
                        getProductDetails(productId, productType)
                    }
                    else{
                        returnPurchaseHistory()
                    }
                }
            }

            override fun onBillingServiceDisconnected() {

            }
        })
    }

    /**
     * Retrieves the details of the specified product.
     */
    private fun getProductDetails(productId: String, productType: String) {
        val actualProductType =
            if (productType == "inapp") ProductType.INAPP
            else ProductType.SUBS
        val productList =
            listOf(
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(productId)
                    .setProductType(actualProductType)
                    .build()
            )
        val params = QueryProductDetailsParams.newBuilder().setProductList(productList)

        billingClient.queryProductDetailsAsync(params.build()) {
                billingResult,
                productDetailsList, ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && productDetailsList.isNotEmpty()) {

                startPurchaseFlow(productDetailsList[0])
            }
        }
    }

    /**
     * Initiates the purchase flow for the specified product.
     */
    private fun startPurchaseFlow(productDetails: ProductDetails) {
        try {
            val offerToken = productDetails.subscriptionOfferDetails?.get(0)?.offerToken

            val productDetailsParamsList = if (offerToken != null)
                listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(productDetails)
                        .setOfferToken(offerToken)
                        .build()
                ) else listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetails)
                    .build()
            )

            val billingFlowParams =
                BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(productDetailsParamsList)
                    .build()

            billingClient.launchBillingFlow(
                activity,
                billingFlowParams
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}