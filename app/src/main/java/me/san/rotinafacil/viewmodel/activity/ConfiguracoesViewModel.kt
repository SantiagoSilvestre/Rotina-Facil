package me.san.rotinafacil.viewmodel.activity

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.*
import me.san.rotinafacil.config.ConfiguracaoFirebase
import me.san.rotinafacil.listener.ValidationListener
import me.san.rotinafacil.config.UsuarioFirebase
import java.io.ByteArrayOutputStream

class ConfiguracoesViewModel(application: Application) : AndroidViewModel(application) {

    private val storage = ConfiguracaoFirebase.getFirebaseStorage()
    private val identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario()
    private val usuario = UsuarioFirebase.getUsuarioAtual()
    val usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado()

    private val mListener = MutableLiveData<ValidationListener>()
    var listener: LiveData<ValidationListener> = mListener

    private val mFirebaseUser = MutableLiveData<FirebaseUser>()
    var firebaseUser: LiveData<FirebaseUser> = mFirebaseUser

    private val mNomeAtualizado = MutableLiveData<Boolean>()
    var nomeAtualizado: LiveData<Boolean> = mNomeAtualizado

    private val mFotoAtualizada = MutableLiveData<Boolean>()
    var fotoAtualizada: LiveData<Boolean> = mFotoAtualizada


     fun salvarImagem(imagem: Bitmap) {

        // Recuperar os dados da imagem para o firebase
        val baos = ByteArrayOutputStream()
        imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos)
        val dadosImagem = baos.toByteArray()

        val imagemRef = storage
            .child("imagens")
            .child("perfil")
            .child("$identificadorUsuario.jpeg")

        val uploadTask = imagemRef.putBytes(dadosImagem)
        uploadTask.addOnFailureListener {
            mListener.value = ValidationListener("Erro ao fazer upload da imagem")
        }.addOnSuccessListener {
            imagemRef.downloadUrl.addOnCompleteListener { itR ->
                val url = itR.result
                atualizarFotoUsuario(url)
            }
            mListener.value = ValidationListener()
        }
    }

    fun atualizarFotoUsuario(url: Uri?) {
         if (UsuarioFirebase.atualizarFotoUsuario(url)) {
             usuarioLogado.foto = url.toString()
             usuarioLogado.atualizar()
             mFotoAtualizada.value = true
         }
    }

    fun vericaUsuario() {
        mFirebaseUser.value = usuario
    }

    fun atualizarNome(nome: String) {
        if (UsuarioFirebase.atualizarNomeUsuario(nome)) {
            usuarioLogado.nome = nome
            usuarioLogado.atualizar()
            mNomeAtualizado.value = true
        }
    }

}

