package com.danielspinu.todo.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.danielspinu.todo.R

class TaskListAdapter: RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    var currentList: List<Task> = emptyList()
    var onClickDelete: (Task) -> Unit = {}
    var onClickEdit: (Task) -> Unit = {}

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(task: Task) {
            val textView = itemView.findViewById<TextView>(R.id.task_title)
            textView.text = task.title
            val descriptionView = itemView.findViewById<TextView>(R.id.task_description)
            descriptionView.text = task.description
            val deleteButton = itemView.findViewById<ImageButton>(R.id.deleteButton)
            deleteButton.setOnClickListener{onClickDelete(task)}
            val editButton = itemView.findViewById<ImageButton>(R.id.editButton)
            editButton.setOnClickListener{onClickEdit(task)}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemCount(): Int {
        return currentList.size
    }
}