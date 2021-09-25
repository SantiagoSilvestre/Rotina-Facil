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
import me.san.rotinafacil.model.TaskModel
import me.san.rotinafacil.model.UsuarioModel

class TarefasAdapter: RecyclerView.Adapter<TarefasAdapter.MyViewHolder>() {

    private var mList: List<TaskModel> = arrayListOf()
    private lateinit var mListener: RecyclerViewListener<TaskModel>

    inner class MyViewHolder(itemView: View, val listener: RecyclerViewListener<TaskModel>) : RecyclerView.ViewHolder(itemView) {
        val textTitle = itemView.findViewById<TextView>(R.id.text_title)
        val textHora = itemView.findViewById<TextView>(R.id.text_subtitulo)
        val textCompleta = itemView.findViewById<TextView>(R.id.text_legenda)
        val layoutItem = itemView.findViewById<LinearLayout>(R.id.layout_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLista = LayoutInflater.from(parent.context).inflate(R.layout.adapter_tarefas, parent, false)
        return MyViewHolder(itemLista, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val task = mList[position]
        holder.textTitle.text = task.title
        holder.textHora.text = task.hour
        holder.textCompleta.text = if (task.completa) "Finalizado" else "Não finalizado"
        holder.layoutItem.setOnClickListener {
           holder.listener.onItemClick(task)
        }
        holder.layoutItem.setOnLongClickListener {
            holder.listener.onLongItemClick(task)
            true
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun attachListener(listener: RecyclerViewListener<TaskModel>) {
        mListener = listener
    }

    fun updateList (list: List<TaskModel>){
        mList = list
        notifyDataSetChanged()
    }
}