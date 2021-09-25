package me.san.rotinafacil.helper

import java.text.SimpleDateFormat
import java.util.*

class Constants {
    object CODES {
        const val PERMISSION_CODE = 1000
        const val IMAGE_CAPTURE_CODE = 1001
        const val IMAGE_CAPTURE_GALERY = 1002
        const val TIPO_REMETENTE = 0
        const val  TIPO_DESTINATARIO = 1
    }
    object LOCALE {
        val BRASIL = Locale("pt", "BR")
        val DATE_FORMATE = SimpleDateFormat(
            "dd/MM/yyyy", BRASIL
        )
    }
}