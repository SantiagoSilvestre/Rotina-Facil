package me.san.rotinafacil.view.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.san.rotinafacil.R
import me.san.rotinafacil.config.UsuarioFirebase
import me.san.rotinafacil.listener.RecyclerViewListener
import me.san.rotinafacil.model.MensagemModel
import me.san.rotinafacil.helper.Constants

class MensagemAdapter: RecyclerView.Adapter<MensagemAdapter.MyViewHolder>() {

    private var mList: List<MensagemModel> = arrayListOf()
    private lateinit var mListener: RecyclerViewListener<MensagemModel>


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textMensagem = itemView.findViewById<TextView>(R.id.txt_msg)
        val img = itemView.findViewById<ImageView>(R.id.img_msg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return if (viewType == Constants.CODES.TIPO_REMETENTE) {
            val itemLista = LayoutInflater.from(parent.context).inflate(R.layout.adapter_mensagem_remetente, parent, false)
            MyViewHolder(itemLista)
        } else {
            val itemLista = LayoutInflater.from(parent.context).inflate(R.layout.adapter_mensagem_destinatario, parent, false)
            MyViewHolder(itemLista)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val mensagem = mList[position]
        val uidUsuario = UsuarioFirebase.getIdentificadorUsuario()
        if (uidUsuario == mensagem.uidUsuario) return Constants.CODES.TIPO_REMETENTE

        return Constants.CODES.TIPO_DESTINATARIO
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mensagem = mList[position]
        val img = mensagem.imagem
        if (img != "") {
            val uri = Uri.parse(img)
            Glide.with(holder.itemView.context).load(uri).into(holder.img)
            holder.textMensagem.visibility = View.GONE
        } else {
            holder.textMensagem.text = mensagem.mensagem
            holder.img.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun attachListener(listener: RecyclerViewListener<MensagemModel>) {
        mListener = listener
    }


    fun updateList (list: List<MensagemModel>){
        mList = list
        notifyDataSetChanged()
    }
}