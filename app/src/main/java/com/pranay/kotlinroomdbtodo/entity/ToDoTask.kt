package com.pranay.kotlinroomdbtodo.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Pranay on 7/1/2017.
 */
@Entity(tableName = "todo_table")
class ToDoTask(@ColumnInfo(name = "todo_task")
               var todoTask: String) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}