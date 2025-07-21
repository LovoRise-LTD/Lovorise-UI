package com.lovorise.app.libs.copy_share

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import coil3.PlatformContext

actual fun copyToClipboard(text: String,context:PlatformContext) {
    try {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("copied_text", text)
        clipboard.setPrimaryClip(clip)
        Log.d("Clipboard", "Text copied to clipboard: $text")
    } catch (e: Exception) {
        Log.e("Clipboard", "Failed to copy text to clipboard", e)
    }
}

actual fun shareText(text: String,context:PlatformContext) {
    try {
        val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(android.content.Intent.EXTRA_TEXT, text)
        }
        val chooser = android.content.Intent.createChooser(intent, "Share via")
        context.startActivity(chooser)
    } catch (e: Exception) {
        Log.e("ShareText", "Failed to share text", e)
    }
}