package me.san.rotinafacil.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import me.san.rotinafacil.R
import me.san.rotinafacil.databinding.ActivityMainBinding
import me.san.rotinafacil.ui.ToastHelper
import me.san.rotinafacil.view.fragment.ContatoFragment
import me.san.rotinafacil.view.fragment.ConversaFragment
import me.san.rotinafacil.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        mViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setContentView(binding.root)
        listeners()
        observe()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflate = menuInflater
        inflate.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_sair -> {
                mViewModel.deslogar()
            }
            R.id.menu_configuracoes -> {

            }
            R.id.menu_pesquisa -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun listeners() {
        val toolbar = binding.toolbar.toolbarPrincipal
        toolbar.title = "Whatssap"
        setSupportActionBar(toolbar)

        val adapter = FragmentPagerItemAdapter(
            supportFragmentManager,
            FragmentPagerItems.with(this)
                .add("Conversas", ConversaFragment::class.java)
                .add("Contatos", ContatoFragment::class.java)
                .create()
        )
        val viewPager = binding.viewPager
        viewPager.adapter = adapter
        binding.viewPagerTab.setViewPager(viewPager)

    }

    private fun observe() {
        mViewModel.listener.observe(this, {
            if (it.success()) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                ToastHelper.exibirToast(this, it.failure())
            }
        })
    }

}