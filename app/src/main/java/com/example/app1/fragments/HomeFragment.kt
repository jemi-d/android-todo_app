package com.example.app1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app1.R
import com.example.app1.databinding.HomeFragmentBinding
import com.example.app1.utils.TaskData
import com.example.app1.utils.TodoAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment(), AddTodoFragment.DialogNextBtnClickListener,
    TodoAdapter.TodoAdapterClickInterface {

    private lateinit var binding: HomeFragmentBinding
    private lateinit var navController: NavController
    private lateinit var dataBaseRef: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private var popupFragment : AddTodoFragment? = null
    private lateinit var adapter : TodoAdapter
    private lateinit var taskList: MutableList<TaskData>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding  = HomeFragmentBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        binding.progressBarList.visibility = View.VISIBLE
        getDataFromFirebase()
        binding.progressBarList.visibility = View.GONE
        addFunction()
    }

    private fun init(view: View){
        navController = Navigation.findNavController(view)
        firebaseAuth = FirebaseAuth.getInstance()
        dataBaseRef = FirebaseDatabase.getInstance().reference.child("Tasks").child(firebaseAuth.currentUser?.uid.toString())

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        taskList = mutableListOf()
        adapter = TodoAdapter(taskList)
        adapter.setListener(this)
        binding.recyclerView.adapter = adapter
    }

    private fun getDataFromFirebase(){
        dataBaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                taskList.clear()
                for (taskSnapshot in snapshot.children){
                    val todoTask = taskSnapshot.key?.let {
                        TaskData(it, taskSnapshot.value.toString())
                    }
                    if(todoTask != null){
                        taskList.add(todoTask)
                    }else{
                        Toast.makeText(requireContext(),"Todo List is null",Toast.LENGTH_SHORT).show()
                    }
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),error.toString(),Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun addFunction(){
        binding.addBtn.setOnClickListener {
            if(popupFragment != null)
                childFragmentManager.beginTransaction().remove(popupFragment!!).commit()
            popupFragment = AddTodoFragment()
            popupFragment!!.setListener(this)
            popupFragment!!.show(
                childFragmentManager,AddTodoFragment.TAG
            )
        }
    }

    override fun onSaveTask(todo: String, taskEditText: TextInputEditText) {
        dataBaseRef.push().setValue(todo).addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(requireContext(),"Todo saved successfully",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(),it.exception?.toString(),Toast.LENGTH_SHORT).show()
            }
            taskEditText.text = null
            popupFragment!!.dismiss()
        }
    }

    override fun onUpdateTask(todoData: TaskData, taskEditText: TextInputEditText) {
        val map = HashMap<String, Any>()
        map[todoData.taskId] = todoData.task
        dataBaseRef.updateChildren(map).addOnCompleteListener{
            if (it.isSuccessful){
                Toast.makeText(requireContext(),"Task updated successfully",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(),it.exception?.toString(),Toast.LENGTH_SHORT).show()
            }
            taskEditText.text = null
            popupFragment!!.dismiss()
        }
    }

    override fun onDeleteTask(todoData: TaskData) {
        dataBaseRef.child(todoData.taskId).removeValue().addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(requireContext(),"Task deleted successfully",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(),it.exception?.toString(),Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onEditTask(todoData: TaskData) {
        if(popupFragment != null)
            childFragmentManager.beginTransaction().remove(popupFragment!!).commit()

        popupFragment = AddTodoFragment.newInstance(todoData.taskId,todoData.task)
        popupFragment!!.setListener(this)
        popupFragment!!.show(
            childFragmentManager,AddTodoFragment.TAG
        )
    }

}