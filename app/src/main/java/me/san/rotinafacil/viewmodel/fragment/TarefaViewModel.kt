package me.san.rotinafacil.viewmodel.fragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import me.san.rotinafacil.config.ConfiguracaoFirebase
import me.san.rotinafacil.config.UsuarioFirebase
import me.san.rotinafacil.helper.TratarDatas
import me.san.rotinafacil.listener.ValidationListener
import me.san.rotinafacil.model.TaskModel
import me.san.rotinafacil.model.UsuarioModel

class TarefaViewModel(application: Application) : AndroidViewModel(application) {

    private val mListener = MutableLiveData<ValidationListener>()
    var listener: LiveData<ValidationListener> = mListener
    var listaTasks = arrayListOf<TaskModel>()

    private val identificadorUser = UsuarioFirebase.getIdentificadorUsuario()
    val database = ConfiguracaoFirebase.getFirebaseDatabase()
    val usuariosRef = database.child("usuarios")
        .child(identificadorUser)

    private val mUsuarioModel = MutableLiveData<UsuarioModel>()
    var usuarioModel: LiveData<UsuarioModel> = mUsuarioModel

    private val mList = MutableLiveData<List<TaskModel>>()
    var list: LiveData<List<TaskModel>> = mList

    private val mRemoveTask = MutableLiveData<Boolean>()
    var removeTask: LiveData<Boolean> = mRemoveTask

    val postListenerUsuario = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            mUsuarioModel.value = dataSnapshot.getValue(UsuarioModel::class.java)!!
        }

        override fun onCancelled(databaseError: DatabaseError) {
        }
    }

    val postListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            for (dados in dataSnapshot.children) {
                val task = dados.getValue(TaskModel::class.java)
                if (task != null) {
                    listaTasks.add(task)
                }
            }
            mList.value = listaTasks
        }

        override fun onCancelled(databaseError: DatabaseError) {

        }
    }

    fun getList(taskRef: DatabaseReference) {
        taskRef.removeEventListener(postListener)
        listaTasks = arrayListOf()
        taskRef.addValueEventListener(postListener)
    }

    fun removeEvent(taskRef: DatabaseReference) {
        taskRef.removeEventListener(postListener)
        mList.value = listaTasks
    }

    fun removeTask(task: TaskModel) {
        task.remove()
        mRemoveTask.value = true
    }

    fun getUser() {
        usuariosRef.addListenerForSingleValueEvent(postListenerUsuario)
    }

}

