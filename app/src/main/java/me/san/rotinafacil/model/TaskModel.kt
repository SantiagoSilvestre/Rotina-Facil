package me.san.rotinafacil.model

import me.san.rotinafacil.config.ConfiguracaoFirebase
import java.io.Serializable

class TaskModel: Serializable {

    var title = ""

    var descricao = ""

    var hour = ""

    var date = ""

    var uidUsuario = ""

    var pontuacao = 0

    var completa = false

    var timestamp: Long = 0

    fun salvar() {
        val database = ConfiguracaoFirebase.getFirebaseDatabase()
        val tarefasRef = database.child("tasks")
        tarefasRef.child(this.uidUsuario)
            .push()
            .setValue(this)
    }


}