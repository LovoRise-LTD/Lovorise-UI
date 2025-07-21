//
//  VideoPlayerWorker.swift
//  Lovorise
//
//  Created by Akash kamati on 25/10/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import AVKit
import Cache
import ComposeApp


class IosCacheUtil : CacheUtil {
    
    let nsCache = NSCache<NSString, AnyObject>()
    
    func cacheImage(image:Any,key:String) {
        if let img = image as? UIImage{
            nsCache.setObject(img, forKey: key as NSString)
        }
    }
    
    func getCachedImage(key:String) -> Any? {
        return nsCache.object(forKey: key as NSString)
    }
}

class VideoPlayerWorker : MediaPlayerWorker {
    
   
    
    
  
    

    private var player: AVPlayer!
    let diskConfig = DiskConfig(name: "DiskCache")
    let memoryConfig = MemoryConfig(expiry: .never, countLimit: 10, totalCostLimit: 10)

    lazy var storage: Cache.Storage<String, Data>? = {
        return try? Cache.Storage(diskConfig: diskConfig, memoryConfig: memoryConfig, fileManager: FileManager(), transformer: TransformerFactory.forData())
    }()

//    /// Plays a video either from the network if it's not cached or from the cache.
//    func play(with url: URL) -> AVPlayer {
//        let playerItem: CachingPlayerItem
//        do {
//            let result = try storage!.entry(forKey: url.absoluteString)
//            // The video is cached.
//            playerItem = CachingPlayerItem(data: result.object, mimeType: "video/mp4", fileExtension: "mp4")
//        } catch {
//            // The video is not cached.
//            playerItem = CachingPlayerItem(url: url)
//        }
//
//        playerItem.delegate = self
//        self.player = AVPlayer(playerItem: playerItem)
//        self.player.automaticallyWaitsToMinimizeStalling = false
//        self.player.play()
//        return self.player
//    }
    
    
    func getMediaItem(url: String) -> Any? {
        let playerItem: CachingPlayerItem
        do {
            let result = try storage!.entry(forKey: url)
            // The video is cached.
            playerItem = CachingPlayerItem(data: result.object, mimeType: "video/mp4", fileExtension: "mp4")
            print("✅ Cached video found for URL: \(url)")
        } catch {
            // The video is not cached.
            print("❌ Video not found in cache: \(url)")
            playerItem = CachingPlayerItem(url: URL(string: url)!)
        }

        playerItem.delegate = self
        
        return playerItem
       
    }

}

// MARK: - CachingPlayerItemDelegate
extension VideoPlayerWorker: CachingPlayerItemDelegate {
    func playerItem(_ playerItem: CachingPlayerItem, didFinishDownloadingData data: Data) {
        // Video is downloaded. Saving it to the cache asynchronously.
        storage?.async.setObject(data, forKey: playerItem.url.absoluteString, completion: { _ in })
    }
}
