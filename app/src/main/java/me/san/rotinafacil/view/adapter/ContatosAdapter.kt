package me.san.rotinafacil.view.adapter

import android.net.Uri
import android.util.Log
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
import me.san.rotinafacil.model.UsuarioModel

class ContatosAdapter: RecyclerView.Adapter<ContatosAdapter.MyViewHolder>() {

    private var mList: List<UsuarioModel> = arrayListOf()
    private lateinit var mListener: RecyclerViewListener<UsuarioModel>

    inner class MyViewHolder(itemView: View, val listener: RecyclerViewListener<UsuarioModel>) : RecyclerView.ViewHolder(itemView) {
        val foto = itemView.findViewById<CircleImageView>(R.id.circle_img)
        val textName = itemView.findViewById<TextView>(R.id.text_title)
        val textEmail = itemView.findViewById<TextView>(R.id.text_subtitulo)
        val layoutItem = itemView.findViewById<LinearLayout>(R.id.layout_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLista = LayoutInflater.from(parent.context).inflate(R.layout.adapter_contatos, parent, false)
        return MyViewHolder(itemLista, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val usuario = mList[position]
        holder.textName.text = usuario.nome
        holder.textEmail.text = usuario.email
        if (usuario.foto != "") {
            val uri = Uri.parse(usuario.foto)
            Glide.with(holder.itemView.context).load(uri).into(holder.foto)
        } else {
            holder.foto.setImageResource(R.drawable.padrao)
        }
        holder.layoutItem.setOnClickListener {
           holder.listener.onItemClick(usuario)
        }
    }

    override fun getItemCount(): Int {
        Log.d("lista", mList.size.toString())
        return mList.size
    }

    fun attachListener(listener: RecyclerViewListener<UsuarioModel>) {
        mListener = listener
    }

    fun updateList (list: List<UsuarioModel>){
        notifyDataSetChanged()
        mList = list
    }
}