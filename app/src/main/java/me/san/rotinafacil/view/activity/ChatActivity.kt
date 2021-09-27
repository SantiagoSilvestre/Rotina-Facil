package me.san.rotinafacil.view.activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import me.san.rotinafacil.R
import me.san.rotinafacil.databinding.ActivityChatBinding
import me.san.rotinafacil.listener.RecyclerViewListener
import me.san.rotinafacil.model.MensagemModel
import me.san.rotinafacil.model.UsuarioModel
import me.san.rotinafacil.helper.Constants
import me.san.rotinafacil.helper.ToastHelper
import me.san.rotinafacil.view.adapter.MensagemAdapter
import me.san.rotinafacil.viewmodel.activity.ChatViewModel

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var mUsuario: UsuarioModel
    private lateinit var mChatViewModel: ChatViewModel
    private val mAdapter = MensagemAdapter()
    private lateinit var mListener: RecyclerViewListener<MensagemModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        mChatViewModel = ViewModelProvider(this).get(ChatViewModel::class.java)
        setContentView(binding.root)
        mListener = object : RecyclerViewListener<MensagemModel> {

            override fun onItemClick(model: MensagemModel) {
                //val intent = Intent(applicationContext, ChatActivity::class.java)
                //intent.putExtra("chatContato", model)
                //startActivity(intent)
            }

            override fun onLongItemClick(model: MensagemModel) {
            }

        }

        loadFromList()
        listeners()
        observe()

    }

    override fun onResume() {
        super.onResume()
        mAdapter.attachListener(mListener)
        mChatViewModel.getList(mUsuario)
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
                }
                if (imagem != null ) {
                    mChatViewModel.salvarImagem(imagem, mUsuario)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun listeners() {
        binding.toolbar.title = ""
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.textNameToolbar.text = mUsuario.nome
        if (mUsuario.foto != "") {
            val uri = Uri.parse(mUsuario.foto)
            Glide.with(this)
                .load(uri)
                .into(binding.circleImgFoto)
        } else {
            binding.circleImgFoto.setImageResource(R.drawable.padrao)
        }

        binding.contentMain.btnSend.setOnClickListener {
            mChatViewModel.enviarMensagem(mUsuario, binding.contentMain.editMensagem.text.toString())
        }

        val layoutManager = LinearLayoutManager(applicationContext)
        binding.contentMain.rvMensagens.layoutManager = layoutManager
        binding.contentMain.rvMensagens.setHasFixedSize(true)
        binding.contentMain.rvMensagens.adapter = mAdapter

        binding.contentMain.imgCameraMessage.setOnClickListener {
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (i.resolveActivity(packageManager) != null) {
                startActivityForResult(i, Constants.CODES.IMAGE_CAPTURE_CODE)
            }
        }

    }

    private fun loadFromList() {
        val bundle = intent.extras
        if (bundle != null) {
            mUsuario = bundle.getSerializable("chatContato") as UsuarioModel
        }
    }

    private fun observe() {
        mChatViewModel.usuarioListener.observe(this, {
            if (it.success()) {
                binding.contentMain.editMensagem.setText("")
            } else {
                ToastHelper.exibirToast(this, it.failure())
            }
        })
        mChatViewModel.listaMensagem.observe(this, {
            mAdapter.updateList(it)
        })
        mChatViewModel.listener.observe(this, {
            if (it.success()) {
                ToastHelper.exibirToast(this, "Imagem enviada com sucesso!")
            } else {
                ToastHelper.exibirToast(this, it.failure())
            }
        })
    }
}