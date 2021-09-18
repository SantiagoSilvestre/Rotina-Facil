package me.san.rotinafacil.view.fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.Task
import me.san.rotinafacil.R
import me.san.rotinafacil.config.ConfiguracaoFirebase
import me.san.rotinafacil.config.UsuarioFirebase
import me.san.rotinafacil.databinding.FragmentConversaBinding
import me.san.rotinafacil.databinding.FragmentTarefasBinding
import me.san.rotinafacil.helper.Constants
import me.san.rotinafacil.helper.TratarDatas
import me.san.rotinafacil.listener.RecyclerViewListener
import me.san.rotinafacil.model.ConversaModel
import me.san.rotinafacil.model.TaskModel
import me.san.rotinafacil.view.activity.ChatActivity
import me.san.rotinafacil.view.activity.TaskFormActivity
import me.san.rotinafacil.view.adapter.ConversasAdapter
import me.san.rotinafacil.view.adapter.TarefasAdapter
import me.san.rotinafacil.viewmodel.fragment.ConversaViewModel
import me.san.rotinafacil.viewmodel.fragment.TarefaViewModel
import java.util.*


class TarefasFragment : Fragment() {

    private lateinit var binding: FragmentTarefasBinding
    private val mAdapter = TarefasAdapter()
    private lateinit var mFormViewModel: TarefaViewModel
    private lateinit var mListener: RecyclerViewListener<TaskModel>
    val identificador = UsuarioFirebase.getIdentificadorUsuario()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTarefasBinding.inflate(layoutInflater)
        mFormViewModel = ViewModelProvider(this).get(TarefaViewModel::class.java)

        mListener = object : RecyclerViewListener<TaskModel> {

            override fun onDeleteClick(model: TaskModel) {
            }

            override fun onItemClick(model: TaskModel) {
                val intent = Intent(requireActivity(), ChatActivity::class.java)
                intent.putExtra("task", model)
                startActivity(intent)
            }

            override fun onLongItemClick(model: TaskModel) {
            }

        }

        listeners()
        observe()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        mAdapter.attachListener(mListener)
        var taskRef = ConfiguracaoFirebase.getFirebaseDatabase()
            .child("tasks")
            .child(identificador)
            .child(TratarDatas.dataParaFirebase(binding.textData.text.toString()))
        mFormViewModel.getList(taskRef)
    }

    override fun onPause() {
        super.onPause()
        var taskRef = ConfiguracaoFirebase.getFirebaseDatabase()
            .child("tasks")
            .child(identificador)
            .child(TratarDatas.dataParaFirebase(binding.textData.text.toString()))
        mFormViewModel.removeEvent(taskRef)
    }

    override fun onStop() {
        super.onStop()
        var taskRef = ConfiguracaoFirebase.getFirebaseDatabase()
            .child("tasks")
            .child(identificador)
            .child(TratarDatas.dataParaFirebase(binding.textData.text.toString()))
        mFormViewModel.removeEvent(taskRef)
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
            var datePicker: DatePickerDialog?
            datePicker = DatePickerDialog(
                requireActivity(),
                { view, year, monthOfYear, dayOfMonth ->
                    val newDate = Calendar.getInstance()
                    newDate[year, monthOfYear] = dayOfMonth
                    binding.textData.text = Constants.LOCALE.DATE_FORMATE.format(newDate.time)
                    var taskRef = ConfiguracaoFirebase.getFirebaseDatabase()
                        .child("tasks")
                        .child(identificador)
                        .child(TratarDatas.dataParaFirebase(binding.textData.text.toString()))
                    mFormViewModel.getList(taskRef)
                },
                newCalendar[Calendar.YEAR],
                newCalendar[Calendar.MONTH],
                newCalendar[Calendar.DAY_OF_MONTH],
            )

            datePicker.show()
        }

    }

    private fun observe() {
        mFormViewModel.list.observe(viewLifecycleOwner, {
            mAdapter.updateList(it)
        })
    }

}