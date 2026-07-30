package com.foresight.app.data.local.dao

import androidx.room.*
import com.foresight.app.data.local.entity.ItemCustomField
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemCustomFieldDao {

    @Query("SELECT * FROM item_custom_fields WHERE itemId = :itemId")
    fun getCustomFieldsForItem(itemId: Long): Flow<List<ItemCustomField>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(field: ItemCustomField): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(fields: List<ItemCustomField>)

    @Query("DELETE FROM item_custom_fields WHERE itemId = :itemId")
    suspend fun deleteCustomFieldsForItem(itemId: Long)
}
