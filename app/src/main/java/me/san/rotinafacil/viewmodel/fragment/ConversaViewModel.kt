package me.san.rotinafacil.viewmodel.fragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import me.san.rotinafacil.config.ConfiguracaoFirebase
import me.san.rotinafacil.config.UsuarioFirebase
import me.san.rotinafacil.listener.ValidationListener
import me.san.rotinafacil.model.ConversaModel

class ConversaViewModel(application: Application) : AndroidViewModel(application) {


    val userCurrent = UsuarioFirebase.getUsuarioAtual()
    val identificador = UsuarioFirebase.getIdentificadorUsuario()
    val conversaRef = ConfiguracaoFirebase.getFirebaseDatabase()
        .child("conversas")
        .child(identificador)

    private val mListener = MutableLiveData<ValidationListener>()
    var listener: LiveData<ValidationListener> = mListener
    var listaConversas = arrayListOf<ConversaModel>()

    private val mList = MutableLiveData<List<ConversaModel>>()
    var list: LiveData<List<ConversaModel>> = mList

    val postListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            for (dados in dataSnapshot.children) {
                val conv = dados.getValue(ConversaModel::class.java)
                if (conv != null) {
                    if (!userCurrent.email.equals(conv.usuarioExibicao?.email)) {
                        listaConversas.add(conv)
                    }
                }
            }
            mList.value = listaConversas
        }

        override fun onCancelled(databaseError: DatabaseError) {

        }
    }

    fun getList() {
        conversaRef.removeEventListener(postListener)
        listaConversas = arrayListOf()
        conversaRef.addValueEventListener( postListener)
    }

    fun removeEvent() {
        conversaRef.removeEventListener(postListener)
        mList.value = listaConversas
    }

}

