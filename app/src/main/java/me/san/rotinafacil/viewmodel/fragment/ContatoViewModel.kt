package me.san.rotinafacil.viewmodel.fragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import me.san.rotinafacil.config.ConfiguracaoFirebase
import me.san.rotinafacil.listener.ValidationListener
import me.san.rotinafacil.model.UsuarioModel

class ContatoViewModel(application: Application) : AndroidViewModel(application) {

    val usuarioRef = ConfiguracaoFirebase.getFirebaseDatabase().child("usuarios")

    private val mListener = MutableLiveData<ValidationListener>()
    var listener: LiveData<ValidationListener> = mListener
    var listaContatos = arrayListOf<UsuarioModel>()

    private val mList = MutableLiveData<List<UsuarioModel>>()
    var list: LiveData<List<UsuarioModel>> = mList

    val postListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            for (dados in dataSnapshot.children) {
                val usuario = dados.getValue(UsuarioModel::class.java)
                if (usuario != null) {
                    listaContatos.add(usuario)
                }
            }
            mList.value = listaContatos
        }

        override fun onCancelled(databaseError: DatabaseError) {

        }
    }

    fun getList() {
        usuarioRef.removeEventListener(postListener)
        listaContatos = arrayListOf()
        usuarioRef.addValueEventListener( postListener)
    }

    fun removeEvent() {
        usuarioRef.removeEventListener(postListener)
        mList.value = listaContatos
    }

}

