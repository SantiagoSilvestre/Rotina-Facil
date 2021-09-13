package me.san.rotinafacil.viewmodel.fragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import me.san.rotinafacil.config.ConfiguracaoFirebase
import me.san.rotinafacil.config.UsuarioFirebase
import me.san.rotinafacil.listener.ValidationListener
import me.san.rotinafacil.model.TaskModel

class TarefaViewModel(application: Application) : AndroidViewModel(application) {

    val identificador = UsuarioFirebase.getIdentificadorUsuario()
    val taskRef = ConfiguracaoFirebase.getFirebaseDatabase()
        .child("tasks")
        .child(identificador)

    private val mListener = MutableLiveData<ValidationListener>()
    var listener: LiveData<ValidationListener> = mListener
    var listaTasks = arrayListOf<TaskModel>()

    private val mList = MutableLiveData<List<TaskModel>>()
    var list: LiveData<List<TaskModel>> = mList

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

    fun getList() {
        taskRef.removeEventListener(postListener)
        listaTasks = arrayListOf()
        taskRef.addValueEventListener(postListener)
    }

    fun removeEvent() {
        taskRef.removeEventListener(postListener)
        mList.value = listaTasks
    }

}

