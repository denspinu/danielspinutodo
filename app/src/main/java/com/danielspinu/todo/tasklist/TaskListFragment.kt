package com.danielspinu.todo.tasklist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danielspinu.todo.R
import com.danielspinu.todo.form.FormActivity
import com.danielspinu.todo.network.Api
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import java.util.*

class TaskListFragment: Fragment() {
    private val adapter = TaskListAdapter()
    private val viewModel: TasksListViewModel by viewModels()

    val createTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as? Task

        if (task != null) {
            viewModel.create(task)
        }
    }

    val updateTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as? Task

        if (task != null) {
            viewModel.update(task)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        val addButton = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)

        adapter.onClickDelete = { task ->
            viewModel.delete(task)
        }
        adapter.onClickEdit = { task ->
            val intent = Intent(context, FormActivity::class.java)
            intent.putExtra("task", task)
            updateTask.launch(intent)
        }

        addButton.setOnClickListener {
            val intent = Intent(context, FormActivity::class.java)
            createTask.launch(intent)
        }

        lifecycleScope.launch {
            viewModel.tasksStateFlow.collect { newList ->
                adapter.currentList = newList
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            val userInfo = Api.userWebService.getInfo().body()!!

            view?.findViewById<TextView>(R.id.userInfo)?.text = "${userInfo.firstName} ${userInfo.lastName}"
        }
        viewModel.refresh()
    }
}