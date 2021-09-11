package me.san.rotinafacil.viewmodel

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

class CadastroUsuarioViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application
    private var autenticacao: FirebaseAuth? = null

    private val mUsuarioListener = MutableLiveData<ValidationListener>()
    var usuarioListener: LiveData<ValidationListener> = mUsuarioListener


    fun cadastrar(usuario: UsuarioModel) {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao()
        autenticacao!!.createUserWithEmailAndPassword(
            usuario.email, usuario.senha
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                mUsuarioListener.value = ValidationListener()
            } else {
                var msg = ""
                try {
                    throw it.exception!!
                } catch (e: FirebaseAuthWeakPasswordException) {
                    msg = "Digite uma senha mais forte!"
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    msg = "Digite um e-mail válido!"
                } catch (e: FirebaseAuthUserCollisionException) {
                    msg = "Esta conta já foi cadastrada!"
                } catch (e: Exception) {
                    msg = "Erro ao cadastrar usuário: ${e.message}"
                    e.printStackTrace()
                }
                mUsuarioListener.value = ValidationListener(msg)
            }
        }
    }


}

