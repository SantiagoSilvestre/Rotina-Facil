package me.san.rotinafacil.viewmodel.activity

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import me.san.rotinafacil.config.ConfiguracaoFirebase
import me.san.rotinafacil.listener.ValidationListener
import me.san.rotinafacil.model.UsuarioModel
import me.san.rotinafacil.helper.Base64Custom
import me.san.rotinafacil.config.UsuarioFirebase
import me.san.rotinafacil.model.TaskModel

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val identificadorUser = UsuarioFirebase.getIdentificadorUsuario()
    val database = ConfiguracaoFirebase.getFirebaseDatabase()
    val usuariosRef = database.child("usuarios")
        .child(identificadorUser)

    private val mListener = MutableLiveData<ValidationListener>()
    var listener: LiveData<ValidationListener> = mListener

    private val mUsuarioModel = MutableLiveData<UsuarioModel>()
    var usuarioModel: LiveData<UsuarioModel> = mUsuarioModel

    val postListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            mUsuarioModel.value = dataSnapshot.getValue(UsuarioModel::class.java)!!
        }

        override fun onCancelled(databaseError: DatabaseError) {
        }
    }

    fun save(task: TaskModel) {
        task.pontuacao = 0
        task.uidUsuario = identificadorUser
        task.salvar()
        mListener.value = ValidationListener()
    }

    fun update(task: TaskModel) {
        task.atualizar()
        if (task.completa) {
            usuariosRef.addListenerForSingleValueEvent(postListener)
        }
        mListener.value = ValidationListener()
    }

}

