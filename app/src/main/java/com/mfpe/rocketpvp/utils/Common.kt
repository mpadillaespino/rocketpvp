package com.mfpe.rocketpvp.utils

import android.content.ClipboardManager
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mfpe.rocketpvp.R
import com.mfpe.rocketpvp.models.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.math.floor
import kotlin.math.sqrt

object Common {

    fun getPokedex(context: Context): Pokedex {
        val jsonPokedex = context.resources.openRawResource(R.raw.pokedex)
        val stringPokedex = jsonPokedex.bufferedReader().use { it.readText() }
        return Gson().fromJson(stringPokedex, object : TypeToken<Pokedex>() {}.type)
    }

    fun getLeagues(): List<League> {
        return listOf(
            League("Liga Super", 1500),
            League("Liga Ultra", 2500),
            League("Liga Master", 999999)
        )
    }

    fun getIvRanges(): List<IvRange> {
        return listOf(
            IvRange(0, "Captura salvaje"),
            IvRange(4, "Potenciado por clima"),
            IvRange(1, "Intercambio: buenos amigos"),
            IvRange(2, "Intercambio: grandes amigos"),
            IvRange(3, "Intercambio: ultra amigos"),
            IvRange(5, "Intercambio: mejores amigos"),
            IvRange(12, "Intercambio: amistad con suerte"),
            IvRange(10, "Incursión / huevo / misión")
        )
    }

    fun getIvs(): Array<String> {
        return arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15")
    }

    fun getCooldown(distance: Float): String {
        if (distance > 0 && distance <= 2) {
            return "1 minuto"
        } else if (distance > 2 && distance <= 5) {
            return "2 minutos"
        } else if (distance > 5 && distance <= 7) {
            return "5 minutos"
        } else if (distance > 7 && distance <= 10) {
            return "7 minutos"
        } else if (distance > 10 && distance <= 12) {
            return "8 minutos"
        } else if (distance > 12 && distance <= 18) {
            return "10 minutos"
        } else if (distance > 18 && distance <= 26) {
            return "15 minutos"
        } else if (distance > 26 && distance <= 42) {
            return "19 minutos"
        } else if (distance > 42 && distance <= 65) {
            return "22 minutos"
        } else if (distance > 65 && distance <= 80) {
            return "28 minutos"
        } else if (distance > 80 && distance <= 100) {
            return "35 minutos"
        } else if (distance > 100 && distance <= 220) {
            return "40 minutos"
        } else if (distance > 220 && distance <= 350) {
            return "51 minutos"
        } else if (distance > 350 && distance <= 460) {
            return "60 minutos"
        } else if (distance > 460 && distance <= 500) {
            return "1 hora y 5 minutos"
        } else if (distance > 500 && distance <= 565) {
            return "1 hora y 9 minutos"
        } else if (distance > 565 && distance <= 700) {
            return "1 hora y 18 minutos"
        } else if (distance > 700 && distance <= 830) {
            return "1 hora y 30 minutos"
        } else if (distance > 830 && distance <= 1000) {
            return "1 hora y 39 minutos"
        } else if (distance > 1000) {
            return "2 horas y 5 minutos"
        }

        return ""
    }

    fun getCp(at: Int, df: Int, st: Int , multiple2: Double): Int {
        return floor((at * sqrt(df * st.toDouble()) * multiple2) / 10).toInt()
    }

    fun getRealLevel(level: Int): Double {
        return level / 2.0 + 1
    }

    fun formatDecimals(num: Double): String {
        val locale = Locale(Constants.LOCALE_EN, Constants.LOCALE_US)
        val decimalFormat = NumberFormat.getNumberInstance(locale) as DecimalFormat
        decimalFormat.applyPattern(Constants.AMOUNT_PATTERN)
        return decimalFormat.format(num)
    }

    fun getCoordinatesFromClipboard(context: Context): String {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        if(clipboard.hasPrimaryClip()){
            val text = clipboard.primaryClip!!.getItemAt(0).text.toString()
            val matchResult = Constants.LATLONG_REGEX.toRegex().find(text)
            return matchResult?.value ?: ""
        }
        return ""
    }

}