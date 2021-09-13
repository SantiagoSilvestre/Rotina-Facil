package me.san.rotinafacil.viewmodel.activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import me.san.rotinafacil.config.ConfiguracaoFirebase
import me.san.rotinafacil.listener.ValidationListener
import me.san.rotinafacil.model.UsuarioModel
import me.san.rotinafacil.helper.Base64Custom
import me.san.rotinafacil.config.UsuarioFirebase
import me.san.rotinafacil.model.TaskModel

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application
    private val identificadorUser = UsuarioFirebase.getIdentificadorUsuario()

    private val mListener = MutableLiveData<ValidationListener>()
    var listener: LiveData<ValidationListener> = mListener


    fun save(task: TaskModel) {
        task.pontuacao = 0
        task.uidUsuario = identificadorUser
        task.salvar()
        mListener.value = ValidationListener()
    }

}

