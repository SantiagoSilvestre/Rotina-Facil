package me.san.rotinafacil.view.fragment

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
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
import me.san.rotinafacil.viewmodel.fragment.ContatoViewModel

class ContatoFragment : Fragment() {

    private lateinit var binding: FragmentContatoBinding
    private val mAdapter = ContatosAdapter()
    private lateinit var mFormViewModel: ContatoViewModel
    private lateinit var mListener: RecyclerViewListener<UsuarioModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContatoBinding.inflate(layoutInflater)
        mFormViewModel = ViewModelProvider(this).get(ContatoViewModel::class.java)

        mListener = object : RecyclerViewListener<UsuarioModel> {

            override fun onDeleteClick(model: UsuarioModel) {
            }

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
        binding.rvContatos.layoutManager = layoutManager
        binding.rvContatos.setHasFixedSize(true)
        binding.rvContatos.adapter = mAdapter
    }

    private fun observe() {
        mFormViewModel.list.observe(viewLifecycleOwner, {
            mAdapter.updateList(it)
        })
    }

}