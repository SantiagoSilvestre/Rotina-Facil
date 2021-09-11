package me.san.rotinafacil.config

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

abstract class ConfiguracaoFirebase {
    companion object {
        private var database: DatabaseReference? = null
        private var auth: FirebaseAuth? = null

        fun getFirebaseDatabase(): DatabaseReference {
            if (database == null) {
                database = FirebaseDatabase.getInstance().reference
            }
            return database!!
        }

        fun getFirebaseAutenticacao(): FirebaseAuth {
            if (auth == null) {
                auth = FirebaseAuth.getInstance()
            }
            return auth!!
        }
    }

}