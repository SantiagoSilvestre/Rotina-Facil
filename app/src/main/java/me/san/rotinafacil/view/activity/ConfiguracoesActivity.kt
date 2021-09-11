package me.san.rotinafacil.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.san.rotinafacil.databinding.ActivityConfiguracoesBinding

class ConfiguracoesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfiguracoesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfiguracoesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listeners ()
    }

    private fun listeners () {
        val toolbar = binding.toolbarConfig.toolbarPrincipal
        toolbar.title = "Configurações"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}