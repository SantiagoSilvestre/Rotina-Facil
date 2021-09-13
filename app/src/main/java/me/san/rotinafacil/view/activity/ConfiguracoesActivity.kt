package me.san.rotinafacil.view.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import me.san.rotinafacil.R
import me.san.rotinafacil.databinding.ActivityConfiguracoesBinding
import me.san.rotinafacil.helper.Constants
import me.san.rotinafacil.helper.Constants.CODES.PERMISSION_CODE
import me.san.rotinafacil.helper.Permissao
import me.san.rotinafacil.helper.ToastHelper
import me.san.rotinafacil.viewmodel.activity.ConfiguracoesViewModel

class ConfiguracoesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfiguracoesBinding
    private lateinit var mConfiguracoesViewModel: ConfiguracoesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfiguracoesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mConfiguracoesViewModel = ViewModelProvider(this).get(ConfiguracoesViewModel::class.java)
        Permissao.validarPermissoes(this)

        listeners ()
        observe()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            var imagem: Bitmap? = null
            try {
                when(requestCode) {
                    Constants.CODES.IMAGE_CAPTURE_CODE -> {
                        imagem = data?.extras?.get("data") as Bitmap
                    }
                    Constants.CODES.IMAGE_CAPTURE_GALERY -> {
                        val localImagem = data?.data
                        imagem = MediaStore.Images.Media.getBitmap(contentResolver, localImagem)
                    }
                }
                if (imagem != null ) {
                    binding.circleImgPerfil.setImageBitmap(imagem)
                    mConfiguracoesViewModel.salvarImagem(imagem)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //called when user presses ALLOW or DENY from Permission Request Popup
        when (requestCode) {
            PERMISSION_CODE -> {

                if (grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
        mConfiguracoesViewModel.vericaUsuario()
        val toolbar = binding.toolbarConfig.toolbarPrincipal
        toolbar.title = "Configurações"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.imgCamera.setOnClickListener {
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (i.resolveActivity(packageManager) != null) {
                startActivityForResult(i, Constants.CODES.IMAGE_CAPTURE_CODE)
            }
        }
        binding.imgPhoto.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            if (i.resolveActivity(packageManager) != null) {
                startActivityForResult(i, Constants.CODES.IMAGE_CAPTURE_GALERY  )
            }
        }

        binding.imgAtualizarNome.setOnClickListener {
            mConfiguracoesViewModel.atualizarNome(binding.editNome.text.toString())
        }

    }

    private fun observe() {
        mConfiguracoesViewModel.listener.observe(this, {
            if (it.success()) {
                ToastHelper.exibirToast(this, "Sucesso ao fazer upload")
            } else {
                ToastHelper.exibirToast(this, it.failure())
            }
        })
        mConfiguracoesViewModel.firebaseUser.observe(this, {
            if (it.displayName != "" && it.displayName != null ) {
                binding.editNome.setText(it.displayName)
            }
            if (it.photoUrl != null) {
                Glide.with(this)
                    .load(it.photoUrl)
                    .into(binding.circleImgPerfil)
            } else {
                binding.circleImgPerfil.setImageResource(R.drawable.padrao)
            }
        })
        mConfiguracoesViewModel.nomeAtualizado.observe(this, {
            if (it) {
                ToastHelper.exibirToast(this, "Nome Atualizado")
            }
        })
        mConfiguracoesViewModel.fotoAtualizada.observe(this, {
            if (it) {
                ToastHelper.exibirToast(this, "Foto atualizada")
            }
        })
    }
}