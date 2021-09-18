package me.san.rotinafacil.model

import android.text.TextUtils.replace
import me.san.rotinafacil.config.ConfiguracaoFirebase
import me.san.rotinafacil.helper.TratarDatas
import java.io.Serializable
import java.util.Collections.replaceAll

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
            .child(TratarDatas.dataParaFirebase(this.date))
            .push()
            .setValue(this)
    }


}