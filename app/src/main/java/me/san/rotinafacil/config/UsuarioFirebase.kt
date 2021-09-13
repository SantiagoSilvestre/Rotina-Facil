package me.san.rotinafacil.config

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import me.san.rotinafacil.model.UsuarioModel
import me.san.rotinafacil.helper.Base64Custom

class UsuarioFirebase {
    companion object {
        fun getIdentificadorUsuario(): String {
            val user = ConfiguracaoFirebase.getFirebaseAutenticacao()
            val email = user.currentUser?.email
            return Base64Custom.codificarBase64(email!!)
        }

        fun getUsuarioAtual(): FirebaseUser {
            val user = ConfiguracaoFirebase.getFirebaseAutenticacao()
            return user.currentUser!!
        }

        fun atualizarFotoUsuario(url: Uri?) : Boolean {
           try {
               val user = getUsuarioAtual()
               val profile = UserProfileChangeRequest
                   .Builder()
                   .setPhotoUri(url)
                   .build()
               user.updateProfile(profile).addOnCompleteListener {
                   if (!it.isSuccessful) {
                       Log.d("Perfil", "Erro ao atualizar perfil.")
                   }
               }
               return true
           } catch (e: Exception) {
               e.printStackTrace()
               return false
           }
        }

        fun atualizarNomeUsuario(nome: String) : Boolean {
            try {
                val user = getUsuarioAtual()
                val profile = UserProfileChangeRequest
                    .Builder()
                    .setDisplayName(nome)
                    .build()
                user.updateProfile(profile).addOnCompleteListener {
                    if (!it.isSuccessful) {
                        Log.d("Perfil", "Erro ao atualizar nome de  perfil.")
                    }
                }
                return true
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
        }

        fun getDadosUsuarioLogado() : UsuarioModel {
            val firebaseUser = getUsuarioAtual()
            val usuario = UsuarioModel()
            usuario.nome = firebaseUser.displayName!!
            usuario.email = firebaseUser.email!!

            if (firebaseUser.photoUrl == null ) {
                usuario.foto = ""
            } else {
                usuario.foto = firebaseUser.photoUrl.toString()
            }

            return usuario
        }
    }
}