package me.san.rotinafacil.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import me.san.rotinafacil.databinding.FragmentConversaBinding

class ConversaFragment : Fragment() {

    private lateinit var binding: FragmentConversaBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConversaBinding.inflate(layoutInflater)
        return binding.root
    }
}