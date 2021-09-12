package me.san.rotinafacil.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import me.san.rotinafacil.model.UsuarioModel
import me.san.rotinafacil.databinding.ActivityCadastrarUserBinding
import me.san.rotinafacil.ui.ToastHelper
import me.san.rotinafacil.ui.ValidateFilds
import me.san.rotinafacil.viewmodel.activity.CadastroUsuarioViewModel

class CadastrarUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastrarUserBinding
    private lateinit var mFormViewModel: CadastroUsuarioViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastrarUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mFormViewModel = ViewModelProvider(this).get(CadastroUsuarioViewModel::class.java)
        observe()
        listeners()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun observe() {
        mFormViewModel.usuarioListener.observe(this, {
            if (it.success()) {
                ToastHelper.exibirToast(this, "Usuário cadastrado com sucesso!")
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                val message = it.failure()
                ToastHelper.exibirToast(this, message)
            }
        })
    }

    private fun listeners() {
        binding.txtLogar.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        binding.btnCadastrar.setOnClickListener {
            cadastrar()
        }
    }

    private fun cadastrar() {
        if (validate()) {
            val usuario = UsuarioModel().apply {
                this.nome = binding.editNome.text.toString()
                this.email = binding.editEmail.text.toString()
                this.senha = binding.editSenha.text.toString()
            }
            mFormViewModel.cadastrar(usuario)
        }
    }

    private fun validate(): Boolean {
        var listInput = arrayListOf<TextInputEditText>()
        listInput.add(binding.editEmail)
        listInput.add(binding.editNome)
        listInput.add(binding.editSenha)
        return ValidateFilds.validar(listInput, this)
    }

}