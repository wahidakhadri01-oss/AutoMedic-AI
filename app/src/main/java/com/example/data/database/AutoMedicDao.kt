package com.example.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AutoMedicDao {
    @Query("SELECT * FROM maintenance_records ORDER BY date DESC")
    fun getAllMaintenanceRecords(): Flow<List<MaintenanceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaintenanceRecord(record: MaintenanceEntity)

    @Query("DELETE FROM maintenance_records WHERE id = :id")
    suspend fun deleteMaintenanceRecord(id: Int)

    @Query("SELECT * FROM diagnosis_reports ORDER BY date DESC")
    fun getAllDiagnosisReports(): Flow<List<DiagnosisEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiagnosisReport(report: DiagnosisEntity)

    @Query("DELETE FROM diagnosis_reports WHERE id = :id")
    suspend fun deleteDiagnosisReport(id: Int)
}
