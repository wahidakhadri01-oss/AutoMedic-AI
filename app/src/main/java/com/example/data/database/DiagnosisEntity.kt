package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diagnosis_reports")
data class DiagnosisEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val carBrand: String,
    val carModel: String,
    val carYear: String,
    val fuelType: String,
    val transmission: String,
    val symptoms: String, // Comma-separated list of symptom strings
    val date: Long,
    val faultName: String,
    val probability: Int, // e.g., 85 for 85%
    val explanation: String,
    val inspectionMethod: String,
    val repairMethod: String,
    val estimatedTime: String,
    val cost: String,
    val severity: String // e.g. "high", "medium", "low"
)
