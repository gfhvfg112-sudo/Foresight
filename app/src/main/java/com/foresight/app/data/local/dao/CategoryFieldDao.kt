package com.foresight.app.data.local.dao

import androidx.room.*
import com.foresight.app.data.local.entity.CategoryField
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryFieldDao {

    @Query("SELECT * FROM category_fields WHERE categoryId = :categoryId")
    fun getFieldsForCategory(categoryId: Long): Flow<List<CategoryField>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(field: CategoryField): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(fields: List<CategoryField>)

    @Delete
    suspend fun delete(field: CategoryField)
}
