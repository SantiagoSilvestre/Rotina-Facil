package me.san.rotinafacil.view.activity

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import me.san.rotinafacil.R
import me.san.rotinafacil.databinding.ActivityConfiguracoesBinding
import me.san.rotinafacil.ui.Constants.CODES.PERMISSION_CODE
import me.san.rotinafacil.ui.Permissao
import me.san.rotinafacil.ui.ToastHelper

class ConfiguracoesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfiguracoesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfiguracoesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Permissao.validarPermissoes(this)

        listeners ()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //called when user presses ALLOW or DENY from Permission Request Popup
        when (requestCode) {
            PERMISSION_CODE -> {

                if (!grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    alertaUsuario()
                }
            }
        }
    }

    private fun alertaUsuario() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.title_permissao_nega))
            .setMessage(getString(R.string.permisson_denied))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.confirmar)) { dialog, which -> finish() }
            .create()
            .show()
    }

    private fun listeners () {
        val toolbar = binding.toolbarConfig.toolbarPrincipal
        toolbar.title = "Configurações"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}