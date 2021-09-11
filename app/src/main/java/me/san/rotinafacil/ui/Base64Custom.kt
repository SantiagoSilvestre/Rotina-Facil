package me.san.rotinafacil.ui

import android.util.Base64

class Base64Custom {
    companion object {
        fun codificarBase64(texto: String) :String {
            return Base64.encodeToString(texto.toByteArray(), Base64.DEFAULT)
                .replace("[\\n\\r]".toRegex(), "")
        }

        fun decodificarBase64(textoCodificdo: String) :String {
            return String(Base64.decode(textoCodificdo, Base64.DEFAULT))
        }
    }
}