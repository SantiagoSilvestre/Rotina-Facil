package me.san.rotinafacil.viewmodel.activity

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import me.san.rotinafacil.config.ConfiguracaoFirebase
import me.san.rotinafacil.listener.ValidationListener
import me.san.rotinafacil.config.UsuarioFirebase
import me.san.rotinafacil.model.UsuarioModel
import java.io.ByteArrayOutputStream

class ConfiguracoesViewModel(application: Application) : AndroidViewModel(application) {

    private val storage = ConfiguracaoFirebase.getFirebaseStorage()
    val database = ConfiguracaoFirebase.getFirebaseDatabase()
    private val identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario()
    private val usuario = UsuarioFirebase.getUsuarioAtual()

    private val mListener = MutableLiveData<ValidationListener>()
    var listener: LiveData<ValidationListener> = mListener

    private val mFirebaseUser = MutableLiveData<FirebaseUser>()
    var firebaseUser: LiveData<FirebaseUser> = mFirebaseUser

    private val mNomeAtualizado = MutableLiveData<Boolean>()
    var nomeAtualizado: LiveData<Boolean> = mNomeAtualizado

    private val mFotoAtualizada = MutableLiveData<Boolean>()
    var fotoAtualizada: LiveData<Boolean> = mFotoAtualizada

    private val mUsuarioModel = MutableLiveData<UsuarioModel>()
    var usuarioModel: LiveData<UsuarioModel> = mUsuarioModel

    val usuariosRef = database.child("usuarios")
        .child(identificadorUsuario)

    private val postListenerUsuario = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            mUsuarioModel.value = dataSnapshot.getValue(UsuarioModel::class.java)!!
        }

        override fun onCancelled(databaseError: DatabaseError) {
        }
    }


     fun salvarImagem(imagem: Bitmap, usuario: UsuarioModel) {

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
                atualizarFotoUsuario(url, usuario)
            }
            mListener.value = ValidationListener()
        }
    }

    fun atualizarFotoUsuario(url: Uri?, usuario: UsuarioModel) {
         if (UsuarioFirebase.atualizarFotoUsuario(url)) {
             usuario.foto = url.toString()
             usuario.atualizar()
             mFotoAtualizada.value = true
         }
    }

    fun vericaUsuario() {
        mFirebaseUser.value = usuario
    }

    fun atualizarNome(nome: String, usuario: UsuarioModel) {
        if (UsuarioFirebase.atualizarNomeUsuario(nome)) {
            usuario.nome = nome
            usuario.atualizar()
            mNomeAtualizado.value = true
        }
    }

    fun getUser() {
        usuariosRef.addListenerForSingleValueEvent(postListenerUsuario)
    }

}

