//
//  Untitled.swift
//  Lovorise
//
//  Created by Akash kamati on 03/03/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import UIKit
import StoreKit
import ComposeApp


public class IOSInAppPurchasesAndSubscriptions: NSObject, SKProductsRequestDelegate, SKPaymentTransactionObserver, InAppPurchasesAndSubscriptions {
    
    //MARK:- Properties
    //MARK:- Private
    /// An array of product IDs set for in-app purchases.
    fileprivate var productIds = [String]()
    /// The ID of the product being purchased.
    fileprivate var productID = ""
    /// The products request object used to fetch available products.
    fileprivate var productsRequest = SKProductsRequest()
    /// A closure to be executed after fetching available products.
    fileprivate var fetchProductComplition: (([SKProduct])->Void)?
    /// The product being purchased.
    fileprivate var productToPurchase: SKProduct?
    /// A closure to be executed after purchasing a product.
    fileprivate var purchaseProductComplition: ((IAPHandlerAlertType, SKProduct?, SKPaymentTransaction?)->Void)?
    
    fileprivate var onPurchased: (AppleReceiptData) -> Void = { _ in }
    
    //MARK:- Public
    /// Indicates whether logging is enabled for IAP operations.
    var isLogEnabled: Bool = true
    
    // MARK: - Public Methods
     
     /**
      Sets the product IDs for in-app purchases.
      
      - Parameter ids: An array of strings representing the product IDs to be set.
      */
    func setProductIds1(ids: [String]) {
        self.productIds = ids
    }

    /**
      Checks whether the device is capable of making purchases.
      
      - Returns: A Boolean value indicating whether purchases can be made on the device.
      */
    func canMakePurchases() -> Bool {  return SKPaymentQueue.canMakePayments()  }
    
    /**
     Initiates the purchase of a product with the specified product ID.
     
     - Parameters:
        - productId: The ID of the product to be purchased.
        - complition: A closure to be executed after the purchase attempt is completed.
     */
    func purchaseByProductId(productId: String, complition: @escaping ((IAPHandlerAlertType, SKProduct?, SKPaymentTransaction?)->Void)){
        func fetchProductComplition(products:[SKProduct]) {
            if(products.isEmpty){

            }else{
                self.purchase(product: products[0], complition: complition)
            }
        }
        
        self.fetchProductComplition = fetchProductComplition
        
        productsRequest = SKProductsRequest(productIdentifiers: Set([productId]))
        productsRequest.delegate = self
        productsRequest.start()
    }
    
    
    
    public func startTransaction(item: PurchaseProductItem, onPurchased: @escaping (Any) -> Void) {
        self.onPurchased = onPurchased
        handleInAppPurchase(productId: item.productId)
    }
    
    
    
    /**
       Initiates the purchase of a specified product.
       
       - Parameters:
          - product: The product to be purchased.
          - complition: A closure to be executed after the purchase attempt is completed.
       */
    func purchase(product: SKProduct, complition: @escaping ((IAPHandlerAlertType, SKProduct?, SKPaymentTransaction?)->Void)) {
        
        self.purchaseProductComplition = complition
        self.productToPurchase = product

        if self.canMakePurchases() {
            let payment = SKPayment(product: product)
            SKPaymentQueue.default().add(self)
            SKPaymentQueue.default().add(payment)
            
            log("PRODUCT TO PURCHASE: \(product.productIdentifier)")
            productID = product.productIdentifier
        }
        else {
            complition(IAPHandlerAlertType.disabled, nil, nil)
        }
    }
    
    // RESTORE PURCHASE
    /**
      Restores previously completed purchases.
      */
    func restorePurchase(){
        SKPaymentQueue.default().add(self)
        SKPaymentQueue.default().restoreCompletedTransactions()
    }
    
    
    // FETCH AVAILABLE IAP PRODUCTS
    /**
        Fetches available in-app purchase (IAP) products.
        
        - Parameter complition: A closure to be executed after fetching available products.
        */
    func fetchAvailableProducts(complition: @escaping (([SKProduct])->Void)){
        
        self.fetchProductComplition = complition
        // Put here your IAP Products ID's
        if self.productIds.isEmpty {
            log(IAPHandlerAlertType.setProductIds.message)
            fatalError(IAPHandlerAlertType.setProductIds.message)
        }
        else {
            productsRequest = SKProductsRequest(productIdentifiers: Set(self.productIds))
            productsRequest.delegate = self
            productsRequest.start()
        }
    }
    
    //MARK:- Private
    /**
     Logs a message if logging is enabled.

     - Parameter object: The object to be logged.
     */
    fileprivate func log <T> (_ object: T) {
        if isLogEnabled {
            NSLog("\(object)")
        }
    }
    
    /**
     Notifies the delegate that the request failed.

     - Parameters:
        - request: The request object that failed.
        - error: The error that occurred during the request.
     */
    public func request(_ request: SKRequest, didFailWithError error: Error) {
        print("Request failed: \(error)")
    }
    
    // REQUEST IAP PRODUCTS
    /**
     Notifies the delegate that the products request received a response.

     - Parameters:
        - request: The request object that received the response.
        - response: The response to the products request.
     */
    public func productsRequest (_ request:SKProductsRequest, didReceive response:SKProductsResponse) {
        if (response.products.count > 0) {
            if let complition = self.fetchProductComplition {
                complition(response.products)
            }
        }
    }
    
    /**
     Notifies the delegate that the payment queue finished restoring completed transactions.

     - Parameter queue: The payment queue that finished restoring completed transactions.
     */
    public func paymentQueueRestoreCompletedTransactionsFinished(_ queue: SKPaymentQueue) {
        if let complition = self.purchaseProductComplition {
            complition(IAPHandlerAlertType.restored, nil, nil)
        }
    }
    
    // IAP PAYMENT QUEUE
    /**
     Tells the observer that one or more transactions have been updated.

     - Parameters:
        - queue: The payment queue containing the transactions that were updated.
        - transactions: An array of updated transactions.
     */
//    public func paymentQueue(_ queue: SKPaymentQueue, updatedTransactions transactions: [SKPaymentTransaction]) {
//        for transaction:AnyObject in transactions {
//            if let trans = transaction as? SKPaymentTransaction {
//                switch trans.transactionState {
//                case .purchased:
//                    log("Product purchase done")
//                    SKPaymentQueue.default().finishTransaction(transaction as! SKPaymentTransaction)
//                    if let complition = self.purchaseProductComplition {
//                        complition(IAPHandlerAlertType.purchased, self.productToPurchase, trans)
//                    }
//                    break
//                    
//                case .failed:
//                    log("Product purchase failed")
//                    SKPaymentQueue.default().finishTransaction(transaction as! SKPaymentTransaction)
//                    break
//                case .restored:
//                    log("Product restored")
//                    SKPaymentQueue.default().finishTransaction(transaction as! SKPaymentTransaction)
//                    break
//                    
//                default: break
//                }}}
//    }
    
    
    public func paymentQueue(_ queue: SKPaymentQueue, updatedTransactions transactions: [SKPaymentTransaction]) {
        for trans in transactions {
           // guard let trans = transaction as? SKPaymentTransaction else { continue }
            
            switch trans.transactionState {
            case .purchased, .restored:
                log("Transaction successful or restored")

                let transactionId = trans.transactionIdentifier ?? ""
                let productId = trans.payment.productIdentifier
                let purchaseToken = getAppStoreEncodedReceipt() ?? ""
                
                
                let receiptData = AppleReceiptData(
                    productId: productId,
                    transactionId: transactionId,
                    purchaseToken: purchaseToken,
                    quantity: Int32(trans.payment.quantity)
                )

                if !transactionId.isEmpty {
                    self.onPurchased(receiptData)
                }

                SKPaymentQueue.default().finishTransaction(trans)

            case .failed:
                log("Transaction failed")
                SKPaymentQueue.default().finishTransaction(trans)

            default:
                break
            }
        }
    }

    
    

    
    
    /**
     Handles in-app purchase requests.

     - Parameter productId: The ID of the product to be purchased.
     */
    
    func handleInAppPurchase(productId: String) {
        purchaseByProductId(productId: productId) { iapHandlerAlertType, skProduct, skPaymentTransaction in
            
//            if iapHandlerAlertType == .purchased, let transaction = skPaymentTransaction {
//                let transactionId = transaction.transactionIdentifier ?? ""
//                let purchasedProductId = transaction.payment.productIdentifier
//                
//                if !transactionId.isEmpty {
//                    let transactionInfo: [String: String] = [
//                        "transactionId": transactionId,
//                        "productId": purchasedProductId
//                    ]
//
//                    // Convert dictionary to JSON string
//                    if let jsonData = try? JSONSerialization.data(withJSONObject: transactionInfo, options: []),
//                       let jsonString = String(data: jsonData, encoding: .utf8) {
//                        self.onPurchased(jsonString) // Send properly formatted JSON
//                    }
//                } else {
//                    print("Transaction ID is missing!")
//                }
//            }
        }
    }
    
    func getAppStoreEncodedReceipt() -> String? {
        if let appStoreReceiptURL = Bundle.main.appStoreReceiptURL,
           FileManager.default.fileExists(atPath: appStoreReceiptURL.path) {
            
            do {
                let receiptData = try Data(contentsOf: appStoreReceiptURL, options: .alwaysMapped)
                return receiptData.base64EncodedString(options: [])
            } catch {
                print("Error reading receipt data: \(error.localizedDescription)")
            }
        }
        return nil
    }
    
    
    func handleInAppPurchase1(productId:String){
        purchaseByProductId(productId:productId, complition: { iapHandlerAlterType, skProduct,skPaymentTransaction  in
            if(iapHandlerAlterType == IAPHandlerAlertType.purchased){
                if let appStoreReceiptURL = Bundle.main.appStoreReceiptURL,
                   FileManager.default.fileExists(atPath: appStoreReceiptURL.path) {
                    
                    do {
                        let receiptData = try Data(contentsOf: appStoreReceiptURL, options: .alwaysMapped)
                        let receiptString:String = receiptData.base64EncodedString(options: [])
//                        let responseObject: [String: Any] = [
//                            "isSuccess":true,
//                            "receiptData":receiptString,
//                            "type":"inAppPurchase"
//                        ]
                        
                      //  self.onPurchased(receiptString)
                        //todo::
                    }
                    catch { print("Couldn't read receipt data with error: " + error.localizedDescription) }
                }
            }
            
        })
    }
    
}
