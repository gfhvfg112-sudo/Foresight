package com.foresight.app.data.local.dao

import androidx.room.*
import com.foresight.app.data.local.entity.Item
import com.foresight.app.data.local.relations.ItemWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Transaction
    @Query("SELECT * FROM items WHERE status = 0 ORDER BY expiryDate ASC")
    fun getActiveItemsWithCategory(): Flow<List<ItemWithCategory>>

    @Query("SELECT * FROM items WHERE status = 0 AND expiryDate > :now ORDER BY expiryDate ASC")
    fun getExpiringSoonItems(now: Long): Flow<List<ItemWithCategory>>

    @Query("SELECT * FROM items WHERE status = 0 AND expiryDate <= :now ORDER BY expiryDate ASC")
    fun getExpiredItems(now: Long): Flow<List<ItemWithCategory>>

    @Query("SELECT * FROM items WHERE id = :id")
    fun getItemById(id: Long): Flow<Item?>

    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun getItemByIdOnce(id: Long): Item?

    @Transaction
    @Query("SELECT * FROM items WHERE id = :id")
    fun getItemWithCategory(id: Long): Flow<ItemWithCategory?>

    @Query("""
        SELECT * FROM items 
        WHERE status = 0 
        AND (name LIKE '%' || :query || '%' OR notes LIKE '%' || :query || '%')
        ORDER BY expiryDate ASC
    """)
    fun searchItems(query: String): Flow<List<ItemWithCategory>>

    @Query("SELECT * FROM items WHERE barcode = :barcode LIMIT 1")
    suspend fun getItemByBarcode(barcode: String): Item?

    @Query("SELECT * FROM items WHERE categoryId = :categoryId AND status = 0 ORDER BY expiryDate ASC")
    fun getItemsByCategory(categoryId: Long): Flow<List<ItemWithCategory>>

    @Query("SELECT * FROM items WHERE status = 0 AND expiryDate BETWEEN :start AND :end ORDER BY expiryDate ASC")
    fun getItemsExpiringBetween(start: Long, end: Long): Flow<List<ItemWithCategory>>

    @Query("SELECT COUNT(*) FROM items WHERE status = 0")
    fun getActiveItemCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Item): Long

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("DELETE FROM items WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE items SET status = :status, updatedAt = :now WHERE id = :id")
    suspend fun updateStatus(id: Long, status: Int, now: Long = System.currentTimeMillis())

    @Query("SELECT * FROM items WHERE status = 0 AND expiryDate <= :threshold")
    suspend fun getItemsExpiringBefore(threshold: Long): List<Item>
}
