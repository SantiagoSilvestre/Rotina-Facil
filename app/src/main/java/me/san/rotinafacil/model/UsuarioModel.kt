package me.san.rotinafacil.model

import com.google.firebase.database.Exclude
import me.san.rotinafacil.config.ConfiguracaoFirebase
import me.san.rotinafacil.config.UsuarioFirebase
import java.io.Serializable
import kotlin.collections.HashMap

class UsuarioModel: Serializable {

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

    var foto = ""

    fun salvar() {
        val firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase()
        val usuario = firebaseRef.child("usuarios").child(this.uid)
        usuario.setValue(this)
    }

    fun atualizar() {
        val identificadorUser = UsuarioFirebase.getIdentificadorUsuario()
        val database = ConfiguracaoFirebase.getFirebaseDatabase()

        val usuariosRef = database.child("usuarios")
            .child(identificadorUser)

        val valores = converterParaMap()

        usuariosRef.updateChildren(valores)
    }

    @Exclude
    fun converterParaMap(): Map<String, Any> {
        val usuarioMap: HashMap<String, Any> = HashMap()
        usuarioMap.put("email", this.email)
        usuarioMap.put("nome", this.nome)
        usuarioMap.put("foto", this.foto)

        return usuarioMap
    }
}