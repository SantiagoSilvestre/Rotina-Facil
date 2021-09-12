package me.san.rotinafacil.view.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import me.san.rotinafacil.R
import me.san.rotinafacil.model.UsuarioModel

class ContatosAdapter: RecyclerView.Adapter<ContatosAdapter.MyViewHolder>() {

    private var mList: List<UsuarioModel> = arrayListOf()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foto = itemView.findViewById<CircleImageView>(R.id.circle_img_contato)
        val textName = itemView.findViewById<TextView>(R.id.text_nome_contato)
        val textEmail = itemView.findViewById<TextView>(R.id.text_email_contato)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLista = LayoutInflater.from(parent.context).inflate(R.layout.adapter_contatos, parent, false)
        return MyViewHolder(itemLista)
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
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun updateList (list: List<UsuarioModel>){
        mList = list
        notifyDataSetChanged()
    }
}