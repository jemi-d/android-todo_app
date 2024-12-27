package com.example.app1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.app1.databinding.AddTodoFragmentBinding
import com.example.app1.utils.TaskData
import com.google.android.material.textfield.TextInputEditText

class AddTodoFragment : DialogFragment() {

    private lateinit var binding: AddTodoFragmentBinding
    private lateinit var listener: DialogNextBtnClickListener
    private var todoData: TaskData? = null

    fun setListener(listener: DialogNextBtnClickListener){
        this.listener = listener
    }

    companion object{
        const val TAG = "AddTodoFragment"

        @JvmStatic
        fun newInstance(taskId:String, task:String) = AddTodoFragment().apply {
            arguments = Bundle().apply {
                putString("taskId",taskId)
                putString("task", task)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = AddTodoFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(arguments !=null){
            todoData = TaskData(arguments?.getString("taskId").toString(),arguments?.getString("task").toString())

            binding.todoField.setText(todoData?.task.toString())
        }
        addTaskEvent()
    }

    private fun addTaskEvent(){

        binding.addTaskBtn.setOnClickListener {
            val todoTask  = binding.todoField.text.toString()

            if(todoTask.isNotEmpty()){
                if(todoData == null){
                    listener.onSaveTask(todoTask,binding.todoField)
                }else{
                    todoData?.task = todoTask
                    listener.onUpdateTask(todoData!!,binding.todoField)
                }

            }else{
                Toast.makeText(requireContext(),"Please type some task",Toast.LENGTH_SHORT).show()
            }
        }

        binding.closeBtn.setOnClickListener {
            dismiss()
        }
    }

    interface DialogNextBtnClickListener{
        fun onSaveTask(todo:String, taskEditText: TextInputEditText)
        fun onUpdateTask(todoData:TaskData, taskEditText: TextInputEditText)
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)
    }

}