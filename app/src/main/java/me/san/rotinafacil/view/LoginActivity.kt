package me.san.rotinafacil.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import me.san.rotinafacil.databinding.ActivityLoginBinding
import me.san.rotinafacil.ui.ToastHelper
import me.san.rotinafacil.ui.ValidateFilds
import me.san.rotinafacil.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mLoginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        mLoginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        setContentView(binding.root)
        observe()
        listeners()
    }

    override fun onStart() {
        super.onStart()
        mLoginViewModel.verificarUsuarioLogado()
    }

    private fun observe() {
        mLoginViewModel.listener.observe(this, {
            if (it.success()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                if (it.failure() != " ") {
                    ToastHelper.exibirToast(this, it.failure())
                }
            }

        })
    }

    private fun listeners() {
        binding.btnLogar.setOnClickListener {
            logar()
        }
        binding.txtCadastrar.setOnClickListener {
            startActivity(Intent(this, CadastrarUserActivity::class.java))
            finish()
        }
    }

    private fun logar() {
        if (validate()) {
            mLoginViewModel.logar(binding.editEmail.text.toString(), binding.editSenha.text.toString())
        }
    }

    private fun validate(): Boolean {
        var listInput = arrayListOf<TextInputEditText>()
        listInput.add(binding.editEmail)
        listInput.add(binding.editSenha)
        return ValidateFilds.validar(listInput, this)
    }
}

