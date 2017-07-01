package com.pranay.kotlinroomdbtodo.adapter

import android.support.v7.widget.RecyclerView
import android.widget.CheckBox
import android.widget.ImageView
import com.pranay.kotlinroomdbtodo.R
import com.pranay.kotlinroomdbtodo.entity.ToDoTask
import kotlinx.android.synthetic.main.list_item_todo.view.*

/**
 * Created by Pranay on 7/1/2017.
 */


class ToDoRecyclerAdapter : RecyclerView.Adapter<ToDoRecyclerAdapter.TaskViewHolder>() {

    lateinit var tasks: ArrayList<ToDoTask>
    lateinit var mDeleteListener: MyAdapterListener
    override fun onCreateViewHolder(parent: android.view.ViewGroup, type: Int): TaskViewHolder {
        return TaskViewHolder(parent)
    }

    fun setData(tasks: ArrayList<ToDoTask>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    fun addTodo(todoTask: ToDoTask) {
        this.tasks.add(todoTask)
        notifyItemChanged(tasks.size)
    }

    override fun onBindViewHolder(viewHolder: ToDoRecyclerAdapter.TaskViewHolder, position: Int) {
        viewHolder.bind(tasks[position], position)
    }

    override fun getItemCount(): Int = tasks.size

    inner class TaskViewHolder(parent: android.view.ViewGroup) :
            RecyclerView.ViewHolder(android.view.LayoutInflater.from(parent.context).
                    inflate(R.layout.list_item_todo, parent, false)) {

        fun bind(task: ToDoTask, position: Int) = with(itemView) {
            val taskCb = findViewById(R.id.checkBoxTodo) as CheckBox
            val deleteTodo = findViewById(R.id.btnDelete) as ImageView
            taskCb.checkBoxTodo.text = task.todoTask
            deleteTodo.setOnClickListener({
                mDeleteListener.onDeleteViewClick(position)
            })
        }
    }


    public fun setDeleteListener(deleteListener: MyAdapterListener): Unit {
        this.mDeleteListener = deleteListener
    }

    interface MyAdapterListener {
        fun onDeleteViewClick(position: Int)
    }
}