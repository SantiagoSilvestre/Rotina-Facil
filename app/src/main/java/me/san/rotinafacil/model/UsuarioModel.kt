package me.san.rotinafacil.model

import com.google.firebase.database.Exclude
import me.san.rotinafacil.config.ConfiguracaoFirebase

class UsuarioModel {

    var uid = ""

        @Exclude
        get() = field
        set(value) {
            field = value
        }

    var nome = ""

    var email = ""

    var senha = ""
        @Exclude
        get() = field
        set(value) {
            field = value
        }

    fun salvar() {
        val firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase()
        val usuario = firebaseRef.child("usuarios").child(this.uid)
        usuario.setValue(this)
    }
}