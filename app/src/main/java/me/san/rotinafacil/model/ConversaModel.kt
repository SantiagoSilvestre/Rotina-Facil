package me.san.rotinafacil.model

import me.san.rotinafacil.config.ConfiguracaoFirebase
import java.io.Serializable

class ConversaModel: Serializable {
    var uidRemetente = ""
    var uidDestinatario = ""
    var ultimaMensagem  = ""
    var usuarioExibicao: UsuarioModel? = null

    fun salvar() {
        val database = ConfiguracaoFirebase.getFirebaseDatabase()
        val conversaRef = database.child("conversas")
        conversaRef.child(this.uidRemetente)
            .child(uidDestinatario)
            .setValue(this)
    }
}