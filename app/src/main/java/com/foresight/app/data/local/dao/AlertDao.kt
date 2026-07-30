package com.foresight.app.data.local.dao

import androidx.room.*
import com.foresight.app.data.local.entity.Alert
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {

    @Query("SELECT * FROM alerts WHERE itemId = :itemId")
    fun getAlertsForItem(itemId: Long): Flow<List<Alert>>

    @Query("SELECT * FROM alerts WHERE isSent = 0 ORDER BY alertDays DESC")
    suspend fun getPendingAlerts(): List<Alert>

    @Query("SELECT * FROM alerts WHERE isSent = 1 ORDER BY sentAt DESC")
    fun getSentAlerts(): Flow<List<Alert>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alert: Alert): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(alerts: List<Alert>)

    @Update
    suspend fun update(alert: Alert)

    @Delete
    suspend fun delete(alert: Alert)

    @Query("DELETE FROM alerts WHERE itemId = :itemId")
    suspend fun deleteAlertsForItem(itemId: Long)
}
