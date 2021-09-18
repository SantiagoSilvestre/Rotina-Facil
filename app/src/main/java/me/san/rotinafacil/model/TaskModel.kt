package me.san.rotinafacil.model

import com.google.firebase.database.Exclude
import me.san.rotinafacil.config.ConfiguracaoFirebase
import me.san.rotinafacil.config.UsuarioFirebase
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
            .child(this.timestamp.toString())
            .setValue(this)
    }

    fun atualizar() {
        val identificadorUser = UsuarioFirebase.getIdentificadorUsuario()
        val database = ConfiguracaoFirebase.getFirebaseDatabase()

        val tarefasRef = database.child("tasks")
            .child(identificadorUser)
            .child(TratarDatas.dataParaFirebase(this.date))
            .child(this.timestamp.toString())

        val valores = converterParaMap()

        tarefasRef.updateChildren(valores)
    }

    @Exclude
    fun converterParaMap(): Map<String, Any> {
        val taskMap: HashMap<String, Any> = HashMap()
        taskMap.put("completa", this.completa)
        taskMap.put("date", this.date)
        taskMap.put("descricao", this.descricao)
        taskMap.put("hour", this.hour)
        taskMap.put("pontuacao", this.pontuacao)
        taskMap.put("title", this.title)
        taskMap.put("uidUsuario", this.uidUsuario)

        return taskMap
    }


}