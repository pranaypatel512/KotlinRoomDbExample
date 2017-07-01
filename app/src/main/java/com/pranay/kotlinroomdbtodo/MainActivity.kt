package com.pranay.kotlinroomdbtodo

import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import com.pranay.kotlinroomdbtodo.adapter.ToDoRecyclerAdapter
import com.pranay.kotlinroomdbtodo.dao.ToDoTaskDao
import com.pranay.kotlinroomdbtodo.database.ToDoDatabase
import com.pranay.kotlinroomdbtodo.entity.ToDoTask
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 *  Note: We need to add room database operation in a
 */
class MainActivity : AppCompatActivity() {

    lateinit var toDB: RoomDatabase // Room Database instance
    lateinit var editTextTodo: EditText
    lateinit var add_btn: Button
    lateinit var allTodos: ArrayList<ToDoTask>
    lateinit var toRecyclerAdapter: ToDoRecyclerAdapter
    lateinit var todo_recycler_view: RecyclerView
    lateinit var todoTaskDao: ToDoTaskDao // Dao Object for Perform database operation
    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewAndDatabase()
    }

    /**
     *  init View and room data base
     */
    private fun initViewAndDatabase() {
        initDataBase()
        allTodos = ArrayList()
        initViews()
        todo_recycler_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        todo_recycler_view.adapter = toRecyclerAdapter

        getAllTodos()
    }

    private fun initDataBase() {
        // Init Room data base
        toDB = Room.databaseBuilder(this,
                ToDoDatabase::class.java, "todo_database").build()
        todoTaskDao = (toDB as ToDoDatabase).taskToDo() // Get Database Dao Object
        toRecyclerAdapter = ToDoRecyclerAdapter() // init Adapter
        toRecyclerAdapter.setDeleteListener(object : ToDoRecyclerAdapter.MyAdapterListener {
            override fun onDeleteViewClick(position: Int) {
                deleteFromDb(allTodos[position], position) // Get To-Do Id for delete
            }
        })
    }

    private fun initViews() {
        editTextTodo = findViewById(R.id.editTextTodo) as EditText
        add_btn = findViewById(R.id.add_btn) as Button
        todo_recycler_view = findViewById(R.id.todo_recycler_view) as RecyclerView
        add_btn.setOnClickListener({
            if (!TextUtils.isEmpty(editTextTodo.text)) { // Check if edit text have to description
                addNewTask(editTextTodo.text.toString())
            }
        })
    }

    /**
     * Call For Delete To-Do
     */
    private fun deleteFromDb(taskToDelete: ToDoTask, position: Int) {
        compositeDisposable.add(Observable.fromCallable { todoTaskDao.deleteToDoTask(taskToDelete) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    allTodos.removeAt(position)
                    toRecyclerAdapter.notifyItemRemoved(position)
                }))
    }


    /**
     * Add New TO DO
     */
    fun addNewTask(toDoDetails: String) {
        val newTask = ToDoTask(toDoDetails) // Create New To do entity to add new to-do
        compositeDisposable.add(Observable.fromCallable { todoTaskDao.addToDoTask(newTask) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    allTodos.add(newTask) // Add new task in list
                    toRecyclerAdapter.notifyItemChanged(allTodos.size) // notify data change for newly added to do
                    (allTodos.size - 1).
                            takeIf { it >= 0 } // only greater then 0
                            ?.let {
                                // Then do smoothscroll to position
                                todo_recycler_view.smoothScrollToPosition(it)
                            }

                }))
    }

    /**
     * Update New TO-DO
     */
    private fun updateList(todoId: Long) {
        // Get Last added To-do and in recycler list
        compositeDisposable.add(todoTaskDao.getTaskUsingId(todoId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    toRecyclerAdapter.addTodo(it)
                })
        )

    }

    /**
     * Get All To-Do
     */
    private fun getAllTodos() {
        // TODO: Can't try to access room database on main thread
        //StackOverFlow Question : https://stackoverflow.com/q/44167111/2949612

        /*allTodos = (toDB as ToDoDatabase).taskToDo().allToDoTask() as ArrayList<ToDoTask>
        if (allTodos.size > 0) {
            toRecyclerAdapter.setData(allTodos)
            toRecyclerAdapter.notifyDataSetChanged()
        }*/

        // Get Last added All To-do and in recycler list
        compositeDisposable.add((toDB as ToDoDatabase).taskToDo().allToDoTask()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()) // Observe on Android Main thread
                .subscribe({
                    allTodos.clear() // Clear app task
                    allTodos.addAll(it) // Add All tasks
                    toRecyclerAdapter.setData(allTodos) // Add All task in adapter

                })

        )
    }

    override fun onDestroy() {
        compositeDisposable.dispose() // Dispose
        super.onDestroy()
    }
}
