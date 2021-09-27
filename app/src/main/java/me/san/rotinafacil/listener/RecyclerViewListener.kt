package me.san.rotinafacil.listener

interface RecyclerViewListener<T>  {


    fun onItemClick(model: T)

    fun onLongItemClick(model: T)

}