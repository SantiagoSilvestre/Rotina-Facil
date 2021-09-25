package me.san.rotinafacil.viewmodel.activity

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import me.san.rotinafacil.config.ConfiguracaoFirebase
import me.san.rotinafacil.config.UsuarioFirebase
import me.san.rotinafacil.listener.ValidationListener
import me.san.rotinafacil.model.TaskModel
import me.san.rotinafacil.model.UsuarioModel

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao()

    private val mListener = MutableLiveData<ValidationListener>()
    var listener: LiveData<ValidationListener> = mListener

    private val mListUsuario = MutableLiveData<List<UsuarioModel>>()
    var listUsuario: LiveData<List<UsuarioModel>> = mListUsuario

    var listaTasks = arrayListOf<TaskModel>()
    var listaContatos = arrayListOf<UsuarioModel>()

    private val identificadorUser = UsuarioFirebase.getIdentificadorUsuario()
    val database = ConfiguracaoFirebase.getFirebaseDatabase()
    val usuariosRef = database.child("usuarios")
        .child(identificadorUser)

    private val usuarioRefContato = ConfiguracaoFirebase.getFirebaseDatabase()
        .child("usuarios")
        .orderByChild("pontuacaoTotal")

    private val mUsuarioModel = MutableLiveData<UsuarioModel>()
    var usuarioModel: LiveData<UsuarioModel> = mUsuarioModel

    private val mList = MutableLiveData<List<TaskModel>>()
    var list: LiveData<List<TaskModel>> = mList

    private val mRemoveTask = MutableLiveData<Boolean>()
    var removeTask: LiveData<Boolean> = mRemoveTask

    private val postListenerUsuario = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            mUsuarioModel.value = dataSnapshot.getValue(UsuarioModel::class.java)!!
        }

        override fun onCancelled(databaseError: DatabaseError) {
        }
    }

    private val postListenerContatos = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            for (dados in dataSnapshot.children) {
                val usuario = dados.getValue(UsuarioModel::class.java)
                if (usuario != null) {
                    listaContatos.add(usuario)
                }
            }
            Log.d("lista1", listaContatos.size.toString())
            mListUsuario.value = listaContatos.reversed()
        }

        override fun onCancelled(databaseError: DatabaseError) {
        }
    }

    private val postListener = object : ValueEventListener {
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

    fun getListContatos() {
        listaContatos = arrayListOf()
        usuarioRefContato.addValueEventListener( postListenerContatos)
    }

    fun getList(taskRef: DatabaseReference) {
        taskRef.removeEventListener(postListener)
        listaTasks = arrayListOf()
        taskRef.addValueEventListener(postListener)
    }

    fun removeEventContato() {
        usuarioRefContato.removeEventListener(postListenerContatos)
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

    fun deslogar() {
        try {
            autenticacao.signOut()
            mListener.value = ValidationListener()
        } catch (e: Exception) {
            mListener.value = ValidationListener("Ocorreu um erro no processo!")
            e.printStackTrace()
        }
    }

}

