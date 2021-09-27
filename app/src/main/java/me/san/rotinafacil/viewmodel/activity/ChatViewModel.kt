package me.san.rotinafacil.viewmodel.activity

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import me.san.rotinafacil.R
import me.san.rotinafacil.config.ConfiguracaoFirebase
import me.san.rotinafacil.listener.ValidationListener
import me.san.rotinafacil.model.UsuarioModel
import me.san.rotinafacil.helper.Base64Custom
import me.san.rotinafacil.config.UsuarioFirebase
import me.san.rotinafacil.model.ConversaModel
import me.san.rotinafacil.model.MensagemModel
import java.io.ByteArrayOutputStream
import java.util.*

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application
    private val uidUsuarioRemetente = UsuarioFirebase.getIdentificadorUsuario()
    private val database = ConfiguracaoFirebase.getFirebaseDatabase()
    private lateinit var usuarioDestinatario: String
    private lateinit var mensagensRef: DatabaseReference
    var listaMensagens = arrayListOf<MensagemModel>()
    private val storage = ConfiguracaoFirebase.getFirebaseStorage()


    private val mUsuarioListener = MutableLiveData<ValidationListener>()
    var usuarioListener: LiveData<ValidationListener> = mUsuarioListener

    private val mListaMensagem = MutableLiveData<List<MensagemModel>>()
    var listaMensagem: LiveData<List<MensagemModel>> = mListaMensagem

    private val mListener = MutableLiveData<ValidationListener>()
    var listener: LiveData<ValidationListener> = mListener

    val eventListenerMensagem = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val mensagem = snapshot.getValue(MensagemModel::class.java)
            if (mensagem != null) {
                listaMensagens.add(mensagem)
            }
            mListaMensagem.value = listaMensagens
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            TODO("Not yet implemented")
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            TODO("Not yet implemented")
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            TODO("Not yet implemented")
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

    }

    fun enviarMensagem(usuario: UsuarioModel, msg: String) {
        if (msg.isNotEmpty()) {
            usuarioDestinatario = Base64Custom.codificarBase64(usuario.email)
            val mensagem = MensagemModel().apply {
                this.mensagem = msg
                this.uidUsuario = uidUsuarioRemetente
            }
            salvarMensagem(uidUsuarioRemetente, usuarioDestinatario, mensagem)
            salvarConversa(mensagem, usuarioDestinatario, usuario)
        } else {
            mUsuarioListener.value =
                ValidationListener(context.getString(R.string.digite_uma_mensagem))
        }
    }

    fun salvarMensagem(remetente: String, destinatario: String, mensagem: MensagemModel) {
        val database = ConfiguracaoFirebase.getFirebaseDatabase()
        val mensagemRef = database.child("mensagens")
        mensagemRef.child(remetente)
            .child(destinatario)
            .push()
            .setValue(mensagem)

        mensagemRef.child(destinatario)
            .child(remetente)
            .push()
            .setValue(mensagem)
        mUsuarioListener.value = ValidationListener()
    }

    fun salvarConversa(mensagem: MensagemModel, destinatario: String, usuario: UsuarioModel) {
        val conversaRemetente = ConversaModel().apply {
            this.uidRemetente = uidUsuarioRemetente
            this.uidDestinatario = destinatario
            this.ultimaMensagem = mensagem.mensagem
            this.usuarioExibicao = usuario
        }
        conversaRemetente.salvar()
    }

    fun getList(usuario: UsuarioModel) {
        usuarioDestinatario = Base64Custom.codificarBase64(usuario.email)
        mensagensRef = database.child("mensagens")
            .child(uidUsuarioRemetente)
            .child(usuarioDestinatario)
        mensagensRef.removeEventListener(eventListenerMensagem)
        listaMensagens = arrayListOf()
        mensagensRef.addChildEventListener(eventListenerMensagem)
    }

    fun salvarImagem(imagem: Bitmap, usuario: UsuarioModel) {

        usuarioDestinatario = Base64Custom.codificarBase64(usuario.email)

        // Recuperar os dados da imagem para o firebase
        val baos = ByteArrayOutputStream()
        imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos)
        val dadosImagem = baos.toByteArray()
        val nomeImagem = UUID.randomUUID().toString()

        val imagemRef = storage
            .child("imagens")
            .child("fotos")
            .child(uidUsuarioRemetente)
            .child("$nomeImagem.jpeg")

        val uploadTask = imagemRef.putBytes(dadosImagem)
        uploadTask.addOnFailureListener {
           mListener.value = ValidationListener("Erro ao enviar imagem")
        }.addOnSuccessListener {
            imagemRef.downloadUrl.addOnCompleteListener { itR ->
                val url = itR.result.toString()
                val mensagemModel = MensagemModel().apply {
                    this.uidUsuario = uidUsuarioRemetente
                    this.mensagem = "imagem.jpeg"
                    this.imagem = url
                }
             this.salvarMensagem(uidUsuarioRemetente, usuarioDestinatario, mensagemModel)
             this.salvarMensagem(usuarioDestinatario, uidUsuarioRemetente, mensagemModel)
            }
            mListener.value = ValidationListener()
        }
    }


}

