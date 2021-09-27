package me.san.rotinafacil.view.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import me.san.rotinafacil.R
import me.san.rotinafacil.listener.RecyclerViewListener
import me.san.rotinafacil.model.ConversaModel
import me.san.rotinafacil.model.UsuarioModel

class ConversasAdapter: RecyclerView.Adapter<ConversasAdapter.MyViewHolder>() {

    private var mList: List<ConversaModel> = arrayListOf()
    private lateinit var mListener: RecyclerViewListener<ConversaModel>

    inner class MyViewHolder(itemView: View, val listener: RecyclerViewListener<ConversaModel>) : RecyclerView.ViewHolder(itemView) {
        val foto = itemView.findViewById<CircleImageView>(R.id.circle_img)
        val textName = itemView.findViewById<TextView>(R.id.text_title)
        val textUltimaMensagem = itemView.findViewById<TextView>(R.id.text_subtitulo)
        val layoutItem = itemView.findViewById<LinearLayout>(R.id.layout_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLista = LayoutInflater.from(parent.context).inflate(R.layout.adapter_contatos, parent, false)
        return MyViewHolder(itemLista, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val conversa = mList[position]
        holder.textName.text = conversa.usuarioExibicao?.nome
        holder.textUltimaMensagem.text = conversa.ultimaMensagem
        if (conversa.usuarioExibicao?.foto != "") {
            val uri = Uri.parse(conversa.usuarioExibicao?.foto)
            Glide.with(holder.itemView.context).load(uri).into(holder.foto)
        } else {
            holder.foto.setImageResource(R.drawable.padrao)
        }
        holder.layoutItem.setOnClickListener {
           holder.listener.onItemClick(conversa)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun attachListener(listener: RecyclerViewListener<ConversaModel>) {
        mListener = listener
    }

    fun updateList (list: List<ConversaModel>){
        mList = list
        notifyDataSetChanged()
    }
}