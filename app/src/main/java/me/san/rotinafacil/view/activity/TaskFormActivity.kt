package me.san.rotinafacil.view.activity

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import me.san.rotinafacil.R
import me.san.rotinafacil.databinding.ActivityTaskFormBinding
import me.san.rotinafacil.helper.ToastHelper
import me.san.rotinafacil.helper.text
import me.san.rotinafacil.model.TaskModel
import me.san.rotinafacil.viewmodel.activity.TaskViewModel
import java.util.*
import me.san.rotinafacil.helper.Constants.LOCALE.DATE_FORMATE
import me.san.rotinafacil.model.UsuarioModel


class TaskFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskFormBinding
    private lateinit var mViewModel: TaskViewModel
    private lateinit var mTask: TaskModel
    private lateinit var mUsuarioModel: UsuarioModel
    private var pontos = 0
    private var pontosAretirar = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFromActivity()
        mViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        observe()
        listeners()
    }

    private fun loadFromActivity() {
        val bundle = intent.extras
        if (bundle != null) {
            mTask = bundle.getSerializable("task") as TaskModel
        }
    }

    private fun listeners() {
        val toolbar = binding.toolbarConfig.toolbarPrincipal
        toolbar.title = getString(R.string.tarefa)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Colando os dados das tarefas
        if (this::mTask.isInitialized) {
            binding.tilTitle.text = mTask.title
            binding.tilDescricao.text = mTask.descricao
            binding.tilDate.text = mTask.date
            binding.tilHour.text = mTask.hour
            binding.btnNewTask.text = getString(R.string.save_task)
            binding.tilDate.isEnabled = false
            binding.checkTask.visibility = View.VISIBLE
            if (mTask.completa) {
                binding.checkTask.isChecked = mTask.completa
                binding.ratingBar.visibility = View.VISIBLE
                binding.ratingBar.rating = mTask.pontuacao.toFloat()
            }

            pontosAretirar = mTask.pontuacao
        }
        //fim
        binding.checkTask.setOnClickListener {
            if (binding.checkTask.isChecked)
                binding.ratingBar.visibility = View.VISIBLE
            else
                binding.ratingBar.visibility = View.GONE
        }

        binding.ratingBar.setOnRatingBarChangeListener { ratingBar, fl, b ->
            pontos = fl.toInt()
        }

        binding.tilDate.editText?.setOnClickListener {
            val newCalendar = Calendar.getInstance()
            var datePicker: DatePickerDialog?
            datePicker = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    val newDate = Calendar.getInstance()
                    newDate[year, monthOfYear] = dayOfMonth
                    binding.editData.setText(DATE_FORMATE.format(newDate.time))
                },
                newCalendar[Calendar.YEAR],
                newCalendar[Calendar.MONTH],
                newCalendar[Calendar.DAY_OF_MONTH],
            )

            datePicker.show()
        }
        binding.tilHour.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker
                .Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()
            timePicker.addOnPositiveButtonClickListener {
                val minute =
                    if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute

                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour

                binding.tilHour.text = "$hour:$minute"
            }
            timePicker.show(supportFragmentManager, null)
        }

        binding.btnNewTask.setOnClickListener {
            handleSave()
            finish()
        }

        binding.btnCancel.setOnClickListener { finish() }
    }

    fun observe() {
        mViewModel.listener.observe(this, {
            if (it.success()) {
                ToastHelper.exibirToast(this, "Salvo com sucesso!")
                finish()
            } else {
                ToastHelper.exibirToast(this, it.failure())
            }
        })

        mViewModel.usuarioModel.observe(this, {
            mUsuarioModel = it
            mUsuarioModel.pontuacaoTotal -= pontosAretirar
            mUsuarioModel.pontuacaoTotal += pontos
            mUsuarioModel.atualizar()
        })
    }


    fun handleSave() {

        if (!this::mTask.isInitialized) {
            val task = TaskModel().apply {
                this.date = binding.tilDate.text
                this.title = binding.tilTitle.text
                this.descricao = binding.tilDescricao.text
                this.hour = binding.tilHour.text
                this.timestamp = System.currentTimeMillis()
            }
            mViewModel.save(task)
        } else {
            mTask.title = binding.tilTitle.text
            mTask.date = binding.tilDate.text
            mTask.descricao = binding.tilDescricao.text
            mTask.hour = binding.tilHour.text
            mTask.completa = binding.checkTask.isChecked
            mTask.pontuacao = pontos
            mViewModel.update(mTask)
        }
    }

}