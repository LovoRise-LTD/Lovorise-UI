import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController(mediaPlayerWorker: VideoPlayerWorker(), cacheUtil: IosCacheUtil(), iap:IOSInAppPurchasesAndSubscriptions())
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
                .ignoresSafeArea(.all) // Compose has own keyboard handler
    }
}

//class MyLocationService: NSObject, ObservableObject, CLLocationManagerDelegate {
//    var locationManager = CLLocationManager()
//    private let geocoder = CLGeocoder() // Geocoder for reverse lookup
//
//    @Published var location: CLLocation?
//    @Published var error : String?
//
//    override init() {
//        super.init()
//        locationManager.delegate = self
//        locationManager.desiredAccuracy = kCLLocationAccuracyThreeKilometers
//        locationManager.requestWhenInUseAuthorization() // This will trigger `locationManagerDidChangeAuthorizati
//    }
//    
//    func locationManagerDidChangeAuthorization(_ manager: CLLocationManager) {
//        switch manager.authorizationStatus {
//        case .authorizedWhenInUse, .authorizedAlways:
//            print("authorized to access")
//            //locationManager.startUpdatingLocation()
//        case .denied, .restricted:
//            print("Location access denied or restricted.")
//        case .notDetermined:
//            locationManager.requestWhenInUseAuthorization()
//        @unknown default:
//            break
//        }
//    }
//
//    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
//        if let location = locations.last {
//            DispatchQueue.main.async {
//                self.location = location
//                self.getCityAndCountry(from: location)
//                print("Updated location: \(location.coordinate.latitude), \(location.coordinate.longitude)")
//            }
//        }
//    }
//    
//    func getCityAndCountry(from location: CLLocation) {
//        geocoder.reverseGeocodeLocation(location) { placemarks, error in
//            if let error = error {
//                print("Failed to get city and country: \(error.localizedDescription)")
//                return
//            }
//            
//            if let placemark = placemarks?.first {
//                let city = placemark.locality ?? "Unknown City"
//                let country = placemark.country ?? "Unknown Country"
//                
//                print("City: \(city), Country: \(country)")
//            }
//        }
//    }
//    
//    func getCityAndCountry(from location: CLLocation, completion: @escaping (String) -> Void) {
//         geocoder.reverseGeocodeLocation(location) { placemarks, error in
//             if let error = error {
//                 print("Failed to get city and country: \(error.localizedDescription)")
//                 completion("Error: \(error.localizedDescription)")
//                 return
//             }
//             
//             if let placemark = placemarks?.first {
//                 let city = placemark.locality ?? "Unknown City"
//                 let country = placemark.country ?? "Unknown Country"
//                 
//                 let cityCountry = "\(city), \(country)"
//                 print("City: \(city), Country: \(country)")
//                 
//                 completion(cityCountry)  // Return the result via the completion handler
//             } else {
//                 completion("Unknown City, Unknown Country")
//             }
//         }
//     }
//      
//
//    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
//        self.error = error.localizedDescription
//        print("Failed to find user's location: \(error.localizedDescription)")
//    }
//}
//
