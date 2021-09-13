package me.san.rotinafacil.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import me.san.rotinafacil.databinding.FragmentContatoBinding
import me.san.rotinafacil.databinding.FragmentConversaBinding
import me.san.rotinafacil.listener.RecyclerViewListener
import me.san.rotinafacil.model.ConversaModel
import me.san.rotinafacil.model.UsuarioModel
import me.san.rotinafacil.view.activity.ChatActivity
import me.san.rotinafacil.view.adapter.ContatosAdapter
import me.san.rotinafacil.view.adapter.ConversasAdapter
import me.san.rotinafacil.viewmodel.fragment.ContatoViewModel
import me.san.rotinafacil.viewmodel.fragment.ConversaViewModel

class ConversaFragment : Fragment() {

    private lateinit var binding: FragmentConversaBinding
    private val mAdapter = ConversasAdapter()
    private lateinit var mFormViewModel: ConversaViewModel
    private lateinit var mListener: RecyclerViewListener<ConversaModel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConversaBinding.inflate(layoutInflater)
        mFormViewModel = ViewModelProvider(this).get(ConversaViewModel::class.java)

        mListener = object : RecyclerViewListener<ConversaModel> {

            override fun onDeleteClick(model: ConversaModel) {
            }

            override fun onItemClick(model: ConversaModel) {
                val intent = Intent(requireActivity(), ChatActivity::class.java)
                intent.putExtra("chatContato", model.usuarioExibicao)
                startActivity(intent)
            }

            override fun onLongItemClick(model: ConversaModel) {
            }

        }

        listeners()
        observe()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        mAdapter.attachListener(mListener)
        mFormViewModel.getList()
    }

    override fun onPause() {
        super.onPause()
        mFormViewModel.removeEvent()
    }

    override fun onStop() {
        super.onStop()
        mFormViewModel.removeEvent()
    }

    private fun listeners() {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvConversas.layoutManager = layoutManager
        binding.rvConversas.setHasFixedSize(true)
        binding.rvConversas.adapter = mAdapter
    }

    private fun observe() {
        mFormViewModel.list.observe(viewLifecycleOwner, {
            mAdapter.updateList(it)
        })
    }
}