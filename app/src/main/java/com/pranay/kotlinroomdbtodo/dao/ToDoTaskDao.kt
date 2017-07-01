package com.pranay.kotlinroomdbtodo.dao

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.pranay.kotlinroomdbtodo.entity.ToDoTask
import io.reactivex.Flowable

/**
 * Created by Pranay on 7/1/2017.
 */

@Dao   interface ToDoTaskDao {
    // Get All- to do Tasks
    @Query("select * from todo_table")
    //fun allToDoTask(): List<ToDoTask> // Simple Kotlin
    fun allToDoTask(): Flowable<List<ToDoTask>>  // RxJava

    // Task using task ID
    @Query("select * from todo_table where id = :arg0")
    fun getTaskUsingId( todo_id: Long):Flowable<ToDoTask>

    // Insert Task
    @Insert(onConflict = REPLACE)
    fun addToDoTask(todoTask:ToDoTask):Long

    // Update Task
    @Update(onConflict = REPLACE)
    fun updateToDoTask(todoTask:ToDoTask)

    // Delete task
    @Delete()
    fun deleteToDoTask(todoTask:ToDoTask)
}