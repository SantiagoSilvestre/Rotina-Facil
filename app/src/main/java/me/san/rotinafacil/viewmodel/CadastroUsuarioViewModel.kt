package me.san.rotinafacil.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import me.san.rotinafacil.config.ConfiguracaoFirebase
import me.san.rotinafacil.model.UsuarioModel

class CadastroUsuarioViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application
    private var autenticacao: FirebaseAuth? = null

    private val mAutenticado = MutableLiveData<Boolean>()
    var autenticado: LiveData<Boolean> = mAutenticado


    fun cadastrar(usuario: UsuarioModel) {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao()
        autenticacao!!.createUserWithEmailAndPassword(
        usuario.email, usuario.senha
        ).addOnCompleteListener {
            if (it.isSuccessful) {

            } else {
                var msg = ""
                try {
                    throw it.exception!!
                } catch (e: FirebaseAuthWeakPasswordException) {

                }
            }
        }
    }


}

