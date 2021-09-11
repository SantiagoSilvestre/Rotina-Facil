package me.san.rotinafacil.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.*
import me.san.rotinafacil.config.ConfiguracaoFirebase
import me.san.rotinafacil.listener.ValidationListener
import me.san.rotinafacil.model.UsuarioModel

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application
    private var autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao()

    private val mListener = MutableLiveData<ValidationListener>()
    var listener: LiveData<ValidationListener> = mListener

    fun logar(email: String, senha: String) {
        autenticacao.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    mListener.value = ValidationListener()
                } else {
                    var msg = ""
                    try {
                        throw it.exception!!
                    } catch (e: FirebaseAuthInvalidUserException) {
                        msg = "Usuário não está cadastrado!"
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        msg = "E-mail e senha não correspondem a um usuário cadastrado"
                    } catch (e: Exception) {
                        msg = "Erro ao logar usuário ${e.message}"
                        e.printStackTrace()
                    }
                    mListener.value = ValidationListener(msg)
                }
            }
    }

    fun verificarUsuarioLogado() {
        var usuarioAtual = autenticacao.currentUser
        if (usuarioAtual != null) mListener.value = ValidationListener()
        else mListener.value = ValidationListener(" ")
    }


}

