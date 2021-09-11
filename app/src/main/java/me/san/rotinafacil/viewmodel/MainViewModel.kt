package me.san.rotinafacil.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.*
import me.san.rotinafacil.config.ConfiguracaoFirebase
import me.san.rotinafacil.listener.ValidationListener
import me.san.rotinafacil.model.UsuarioModel

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application
    private var autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao()

    private val mListener = MutableLiveData<ValidationListener>()
    var listener: LiveData<ValidationListener> = mListener

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

