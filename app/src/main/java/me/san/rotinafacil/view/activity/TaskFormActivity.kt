package me.san.rotinafacil.view.activity

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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


class TaskFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskFormBinding
    private lateinit var mViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        observe()
        listeners()
        loadFromActivity()
    }

    private fun loadFromActivity() {
        //if (intent.hasExtra(TASK_ID)) {
            //val intExtra = intent.getIntExtra(TASK_ID, 0)
           // if (intExtra != 0) {
           //     mIdTask = intExtra
            //    mViewModel.load(mIdTask)
         //   }
       // }
    }

    private fun listeners() {
        val toolbar = binding.toolbarConfig.toolbarPrincipal
        toolbar.title = getString(R.string.tarefa)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.tilDate.editText?.setOnClickListener {
            val newCalendar = Calendar.getInstance()
            var datePicker: DatePickerDialog?
            datePicker = DatePickerDialog(this,
                {
                  view, year, monthOfYear, dayOfMonth ->
                  val newDate = Calendar.getInstance()
                  newDate[year, monthOfYear] = dayOfMonth
                  binding.editData.setText(DATE_FORMATE.format(newDate.time))
                },
                newCalendar[Calendar.YEAR],
                newCalendar[Calendar.MONTH],
                newCalendar[Calendar.DAY_OF_MONTH],
                )

            datePicker.show()

            //val datePicker = MaterialDatePicker.Builder.datePicker().build()




            /*
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.tilDate.text = Date(it + offset).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")

             */
        }
        binding.tilHour.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker
                .Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()
            timePicker.addOnPositiveButtonClickListener {
                val minute = if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute

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
        /*
        mViewModel.task.observe(this, {
            binding.tilTitle.text = it.title
            binding.tilDate.text = it.date
            binding.tilHour.text = it.hour
            binding.tilDescricao.text = it.descricao
        })

         */
    }


    fun handleSave() {
        val task = TaskModel().apply {
            this.date = binding.tilDate.text
            this.title = binding.tilTitle.text
            this.descricao = binding.tilDescricao.text
            this.hour = binding.tilHour.text
            this.timestamp = System.currentTimeMillis()
        }
        mViewModel.save(task)
    }

}