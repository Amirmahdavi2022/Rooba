package app.rooba.ui.components

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

const val TELEGRAM_CHANNEL = "parsv2r"

/** Opens the Telegram channel in the Telegram app, or in the browser if Telegram isn't installed. */
fun openTelegramChannel(context: Context) {
    val app = Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=$TELEGRAM_CHANNEL"))
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    try {
        context.startActivity(app)
    } catch (_: ActivityNotFoundException) {
        openUrl(context, "https://t.me/$TELEGRAM_CHANNEL")
    }
}

fun openUrl(context: Context, url: String) {
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    } catch (_: ActivityNotFoundException) {
    }
}

/** "DE" -> 🇩🇪 */
fun flagEmoji(countryCode: String): String {
    val cc = countryCode.trim().uppercase()
    if (cc.length != 2 || !cc.all { it in 'A'..'Z' }) return "🌐"
    return String(Character.toChars(0x1F1E6 + (cc[0] - 'A'))) + String(Character.toChars(0x1F1E6 + (cc[1] - 'A')))
}
