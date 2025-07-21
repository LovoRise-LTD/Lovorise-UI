package com.lovorise.app.chat.link_og_tag

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class LinkPreviewDataService {


    suspend fun getLinkPreviewData(url: String): LinkPreviewData? {

        val client = HttpClient()

        val html = try {
            client.get(url).body<String?>()
        } catch (e:Exception){
            null
        } finally {
            client.close()
        } ?: return null


        val ogTitle = extractMetaTagContent(html, "og:title")
        val ogDescription = extractMetaTagContent(html, "og:description")
        val ogImage = extractMetaTagContent(html, "og:image")
        var ogSiteName = extractMetaTagContent(html, "og:site_name")


        if (ogSiteName.isNullOrBlank()){
             if (url.lowercase().contains("youtube")) {
                 ogSiteName =  "YouTube"
            }
            else if (url.lowercase().contains("facebook")) {
                 ogSiteName = "Facebook"
            }
            else {
                 ogSiteName =  null
            }
        }

        val data = LinkPreviewData(title = ogTitle, description = ogDescription, imageUrl = ogImage, siteName = ogSiteName, favIcon = extractFavicon(html), url = url)

        println("the data is $data")
        println("the data is $data")
        println("the data is $data")
        println("the data is $data")

        return data

    }

    private fun extractMetaTagContent(html: String, property: String): String? {
        val regex = """<meta property="$property" content="(.*?)"\s*/?>""".toRegex()
        return regex.find(html)?.groups?.get(1)?.value
    }

    private fun extractFavicon(html: String): String? {
        val regex = """<link rel="(?:shortcut )?icon" href="(.*?)"\s*/?>""".toRegex()
        return regex.find(html)?.groups?.get(1)?.value
    }


    companion object{
        fun extractUrls(text: String): List<String> {
            val urlRegex = "(https?://[\\w-]+(\\.[\\w-]+)+(/[\\w-./?%&=]*)?)|(www\\.[\\w-]+(\\.[\\w-]+)+(/[\\w-./?%&=]*)?)|(\\b[\\w-]+(\\.[\\w-]+)+\\b)".toRegex()
            return urlRegex.findAll(text).map {
                val url = it.value
                if (!url.startsWith("http") && !url.startsWith("www")) "https://$url" else url
            }.toList()
        }
    }
}