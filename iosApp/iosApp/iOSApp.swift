import SwiftUI
import ComposeApp
import BackgroundTasks

@main
struct iOSApp: App {
    
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate

    init() {
        AppModuleKt.initializeKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            
            ContentView()
        }
    }
}


class AppDelegate: NSObject, UIApplicationDelegate {
    
    

    func application(_ application: UIApplication,didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil) -> Bool {
       // registerBackgroundTask()
        
        return true
    }

    private func registerBackgroundTask() {
        BGTaskScheduler.shared.register(forTaskWithIdentifier: "com.lovorise.refreshToken", using: nil) { task in
            self.handleBackgroundTask(task as! BGAppRefreshTask)
        }
    }
     
    
    func application(_ application: UIApplication,
                     continue userActivity: NSUserActivity,
                     restorationHandler: @escaping ([UIUserActivityRestoring]?) -> Void) -> Bool {
        if userActivity.activityType == NSUserActivityTypeBrowsingWeb,
           let url = userActivity.webpageURL {
            ExtractAndStoreReferralCodeKt.extractAndStoreReferralCodeFromUrl(url:url.absoluteString,context:nil)
        }
        return true
    }
    
    func applicationDidEnterBackground(_ application: UIApplication) {
       // scheduleBackgroundTask() // ✅ Reschedule when app goes to background
    }

    private func handleBackgroundTask(_ task: BGAppRefreshTask) {
        scheduleBackgroundTask()
        
        task.expirationHandler = {
            print("❌ Background task expired.")
            task.setTaskCompleted(success: false)
        }
     
        Task {
            do {
                let success = try await TokenUtilKt.refreshToken() as? Bool ?? false
                print(success ? "✅ Token refreshed successfully" : "❌ Token refresh failed")
                task.setTaskCompleted(success: success)
            } catch {
                print("❌ Error refreshing token: \(error.localizedDescription)")
                task.setTaskCompleted(success: false)
            }
        }

    }

    func scheduleBackgroundTask() {
        let request = BGAppRefreshTaskRequest(identifier: "com.lovorise.refreshToken")
        request.earliestBeginDate = Date(timeIntervalSinceNow: 1 * 60 * 60)
        do {
            try BGTaskScheduler.shared.submit(request)
            print("✅ Background task scheduled successfully for 2 hours later.")
        } catch {
            print("❌ Failed to schedule background task: \(error.localizedDescription)")
        }
    }
}




/// An enumeration representing various types of in-app purchase (IAP) handler alerts.
enum IAPHandlerAlertType {
    /// Indicates that product IDs are not set.
    case setProductIds
    /// Indicates that purchases are disabled on the device.
    case disabled
    /// Indicates that a purchase has been successfully restored.
    case restored
    /// Indicates that a purchase has been successfully made.
    case purchased
    
    /// A computed property that returns the message associated with each alert type.
    var message: String{
        switch self {
        case .setProductIds: return "Product ids not set, call setProductIds method!"
        case .disabled: return "Purchases are disabled in your device!"
        case .restored: return "You've successfully restored your purchase!"
        case .purchased: return "You've successfully bought this purchase!"
        }
    }
}

