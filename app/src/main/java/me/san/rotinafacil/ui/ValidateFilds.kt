package me.san.rotinafacil.ui

import android.content.Context
import com.google.android.material.textfield.TextInputEditText
import me.san.rotinafacil.R

class ValidateFilds {
    companion object {
        fun validar(arrayFilds: List<TextInputEditText>, context: Context): Boolean {
            var camposValidos = true
            arrayFilds.forEach {
                if (it.text.toString() == "") {
                    it.error = context.getString(R.string.campo_invalido)
                    camposValidos = false
                }
            }
            return camposValidos
        }
    }
}