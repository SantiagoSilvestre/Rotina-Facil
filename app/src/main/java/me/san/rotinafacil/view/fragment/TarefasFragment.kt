package me.san.rotinafacil.view.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import me.san.rotinafacil.config.ConfiguracaoFirebase
import me.san.rotinafacil.config.UsuarioFirebase
import me.san.rotinafacil.databinding.FragmentTarefasBinding
import me.san.rotinafacil.helper.Constants
import me.san.rotinafacil.helper.ToastHelper
import me.san.rotinafacil.helper.TratarDatas
import me.san.rotinafacil.listener.RecyclerViewListener
import me.san.rotinafacil.model.TaskModel
import me.san.rotinafacil.view.activity.MainActivity
import me.san.rotinafacil.view.activity.TaskFormActivity
import me.san.rotinafacil.view.adapter.TarefasAdapter
import me.san.rotinafacil.viewmodel.activity.MainViewModel
import java.util.*


class TarefasFragment : Fragment() {

    private lateinit var binding: FragmentTarefasBinding
    private val mAdapter = TarefasAdapter()
    private lateinit var mViewModel: MainViewModel
    private lateinit var mListener: RecyclerViewListener<TaskModel>
    val identificador = UsuarioFirebase.getIdentificadorUsuario()
    private lateinit var mTask: TaskModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTarefasBinding.inflate(layoutInflater)
        mViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mListener = object : RecyclerViewListener<TaskModel> {

            override fun onItemClick(model: TaskModel) {
                val intent = Intent(requireActivity(), TaskFormActivity::class.java)
                intent.putExtra("task", model)
                startActivity(intent)
            }

            override fun onLongItemClick(model: TaskModel) {
                val alertDialog = AlertDialog.Builder(requireActivity())
                alertDialog.setTitle("Remover Tarefa")
                alertDialog.setMessage("Deseja remover a tarefa?")
                alertDialog.setPositiveButton("Sim") { _, _ ->
                    mViewModel.getUser()
                    mTask = model
                }
                alertDialog.setNeutralButton("Não") { _, _ ->
                }
                alertDialog.create()
                alertDialog.show()

            }

        }

        listeners()
        observe()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        mAdapter.attachListener(mListener)
        val taskRef = ConfiguracaoFirebase.getFirebaseDatabase()
            .child("tasks")
            .child(identificador)
            .child(TratarDatas.dataParaFirebase(binding.textData.text.toString()))
        mViewModel.getList(taskRef)
        mViewModel.getListContatos()
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getListContatos()
    }

    override fun onPause() {
        super.onPause()
        val taskRef = ConfiguracaoFirebase.getFirebaseDatabase()
            .child("tasks")
            .child(identificador)
            .child(TratarDatas.dataParaFirebase(binding.textData.text.toString()))
        mViewModel.removeEvent(taskRef)
    }

    override fun onStop() {
        super.onStop()
        val taskRef = ConfiguracaoFirebase.getFirebaseDatabase()
            .child("tasks")
            .child(identificador)
            .child(TratarDatas.dataParaFirebase(binding.textData.text.toString()))
        mViewModel.removeEvent(taskRef)
    }

    private fun listeners() {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvTarefas.layoutManager = layoutManager
        binding.rvTarefas.setHasFixedSize(true)
        binding.rvTarefas.adapter = mAdapter

        binding.btnNewTask.setOnClickListener {
            val intent = Intent(requireActivity(), TaskFormActivity::class.java)
            startActivity(intent)
        }

        val newDate = Calendar.getInstance()
        binding.textData.text = Constants.LOCALE.DATE_FORMATE.format(newDate.time)

        binding.btnCandario.setOnClickListener {
            val newCalendar = Calendar.getInstance()
            val datePicker: DatePickerDialog?
            datePicker = DatePickerDialog(
                requireActivity(),
                { _, year, monthOfYear, dayOfMonth ->
                    val newDate = Calendar.getInstance()
                    newDate[year, monthOfYear] = dayOfMonth
                    binding.textData.text = Constants.LOCALE.DATE_FORMATE.format(newDate.time)
                    val taskRef = ConfiguracaoFirebase.getFirebaseDatabase()
                        .child("tasks")
                        .child(identificador)
                        .child(TratarDatas.dataParaFirebase(binding.textData.text.toString()))
                    mViewModel.getList(taskRef)
                },
                newCalendar[Calendar.YEAR],
                newCalendar[Calendar.MONTH],
                newCalendar[Calendar.DAY_OF_MONTH],
            )

            datePicker.show()
        }

    }

    private fun observe() {
        mViewModel.list.observe(viewLifecycleOwner, {
            mAdapter.updateList(it)
        })

        mViewModel.usuarioModel.observe(viewLifecycleOwner, {
            it.pontuacaoTotal -= mTask.pontuacao
            it.atualizar()
            mViewModel.removeTask(mTask)
        })

        mViewModel.removeTask.observe(viewLifecycleOwner, {
            if (it) {
                mAdapter.attachListener(mListener)
                val taskRef = ConfiguracaoFirebase.getFirebaseDatabase()
                    .child("tasks")
                    .child(identificador)
                    .child(TratarDatas.dataParaFirebase(binding.textData.text.toString()))
                mViewModel.getList(taskRef)
                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()

                ToastHelper.exibirToast(requireActivity(), "Removida com sucesso!")
            }
        })
    }

}