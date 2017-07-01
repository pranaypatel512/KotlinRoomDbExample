package com.pranay.kotlinroomdbtodo.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.pranay.kotlinroomdbtodo.dao.ToDoTaskDao
import com.pranay.kotlinroomdbtodo.entity.ToDoTask

/**
 * Created by Admin on 7/1/2017.
 */

@Database(entities = arrayOf(ToDoTask::class), version = 1, exportSchema = false)
abstract class ToDoDatabase: RoomDatabase() {
    abstract fun taskToDo(): ToDoTaskDao
}