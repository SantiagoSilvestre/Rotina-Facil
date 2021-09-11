package me.san.rotinafacil.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.san.rotinafacil.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observe()
        listeners()
    }

    private fun observe() {

    }

    private fun listeners() {
        binding.btnLogar.setOnClickListener{
            //Logar
        }
        binding.txtCadastrar.setOnClickListener {
            startActivity(Intent(this,CadastrarUserActivity::class.java))
            finish()
        }
    }
}

