package com.example.app1.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app1.databinding.TaskCardBinding

class TodoAdapter(private val list:MutableList<TaskData>):
RecyclerView.Adapter<TodoAdapter.TodoViewHolder>(){

    private var listener : TodoAdapterClickInterface? = null
    fun setListener(listener: TodoAdapterClickInterface){
        this.listener = listener
    }

    inner class TodoViewHolder(val binding: TaskCardBinding): RecyclerView.ViewHolder(binding.root)

    // ViewHolder class
//    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val taskName: TextView = itemView.findViewById(R.id.taskName)
//        val editTask: ImageView = itemView.findViewById(R.id.editTask)
//        val deleteTask: ImageView = itemView.findViewById(R.id.deleteTask)
//
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = TaskCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        with(holder){
            with(list[position]){
                binding.taskName.text = this.task

                binding.deleteTask.setOnClickListener {
                    listener?.onDeleteTask(this)
                }

               binding.editTask.setOnClickListener {
                    listener?.onEditTask(this)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface TodoAdapterClickInterface{
        fun onDeleteTask(todoData:TaskData)
        fun onEditTask(todoData:TaskData)

    }
}