package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "maintenance_records")
data class MaintenanceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String, // e.g. "تغيير الزيت", "فلتر الهواء"
    val date: Long, // timestamp
    val currentMileage: Int,
    val nextMileage: Int,
    val notes: String
)
