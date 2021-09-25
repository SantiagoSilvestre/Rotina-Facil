package me.san.rotinafacil.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import me.san.rotinafacil.databinding.FragmentContatoBinding
import me.san.rotinafacil.listener.RecyclerViewListener
import me.san.rotinafacil.model.UsuarioModel
import me.san.rotinafacil.view.activity.ChatActivity
import me.san.rotinafacil.view.adapter.ContatosAdapter
import me.san.rotinafacil.viewmodel.activity.MainViewModel

class ContatoFragment : Fragment() {

    private lateinit var binding: FragmentContatoBinding
    private val mAdapter = ContatosAdapter()
    private lateinit var mFormViewModel: MainViewModel
    private lateinit var mListener: RecyclerViewListener<UsuarioModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContatoBinding.inflate(layoutInflater)
        mFormViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mListener = object : RecyclerViewListener<UsuarioModel> {
            override fun onItemClick(model: UsuarioModel) {
                val intent = Intent(requireActivity(), ChatActivity::class.java)
                intent.putExtra("chatContato", model)
                startActivity(intent)
            }

            override fun onLongItemClick(model: UsuarioModel) {
            }

        }

        listeners()
        observe()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        mAdapter.attachListener(mListener)
        mFormViewModel.getListContatos()
    }

    override fun onPause() {
        super.onPause()
        mFormViewModel.removeEventContato()
    }

    override fun onStop() {
        super.onStop()
        mFormViewModel.removeEventContato()
    }

    private fun listeners() {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvContatos.layoutManager = layoutManager
        binding.rvContatos.setHasFixedSize(true)
        binding.rvContatos.adapter = mAdapter
    }

    private fun observe() {
        mFormViewModel.listUsuario.observe(viewLifecycleOwner, {
            mAdapter.updateList(it)
            Log.d("lista", "passou por aqui?")
            mFormViewModel.removeEventContato()
        })
    }

}