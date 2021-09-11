package me.san.rotinafacil.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.san.rotinafacil.databinding.FragmentContatoBinding

class ContatoFragment : Fragment() {

    private lateinit var binding: FragmentContatoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContatoBinding.inflate(layoutInflater)
        binding.textContato.text = "Contatooooooo Fragmenteeee"
        return binding.root
    }

}