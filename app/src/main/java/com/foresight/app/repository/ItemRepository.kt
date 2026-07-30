package com.foresight.app.repository

import com.foresight.app.data.local.dao.AlertDao
import com.foresight.app.data.local.dao.ItemCustomFieldDao
import com.foresight.app.data.local.dao.ItemDao
import com.foresight.app.data.local.entity.Alert
import com.foresight.app.data.local.entity.Item
import com.foresight.app.data.local.entity.ItemCustomField
import com.foresight.app.data.local.relations.ItemWithCategory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemRepository @Inject constructor(
    private val itemDao: ItemDao,
    private val alertDao: AlertDao,
    private val customFieldDao: ItemCustomFieldDao
) {
    fun getActiveItems(): Flow<List<ItemWithCategory>> =
        itemDao.getActiveItemsWithCategory()

    fun getExpiringSoonItems(now: Long): Flow<List<ItemWithCategory>> =
        itemDao.getExpiringSoonItems(now)

    fun getExpiredItems(now: Long): Flow<List<ItemWithCategory>> =
        itemDao.getExpiredItems(now)

    fun getItemById(id: Long): Flow<Item?> =
        itemDao.getItemById(id)

    suspend fun getItemByIdOnce(id: Long): Item? =
        itemDao.getItemByIdOnce(id)

    fun getItemWithCategory(id: Long): Flow<ItemWithCategory?> =
        itemDao.getItemWithCategory(id)

    fun searchItems(query: String): Flow<List<ItemWithCategory>> =
        itemDao.searchItems(query)

    fun getItemsByCategory(categoryId: Long): Flow<List<ItemWithCategory>> =
        itemDao.getItemsByCategory(categoryId)

    fun getItemsExpiringBetween(start: Long, end: Long): Flow<List<ItemWithCategory>> =
        itemDao.getItemsExpiringBetween(start, end)

    fun getActiveItemCount(): Flow<Int> =
        itemDao.getActiveItemCount()

    suspend fun insert(item: Item): Long =
        itemDao.insert(item)

    suspend fun update(item: Item) =
        itemDao.update(item)

    suspend fun delete(item: Item) =
        itemDao.delete(item)

    suspend fun deleteById(id: Long) =
        itemDao.deleteById(id)

    suspend fun updateStatus(id: Long, status: Int) =
        itemDao.updateStatus(id, status)

    suspend fun getItemByBarcode(barcode: String): Item? =
        itemDao.getItemByBarcode(barcode)

    suspend fun getItemsExpiringBefore(threshold: Long): List<Item> =
        itemDao.getItemsExpiringBefore(threshold)

    // Alert operations
    fun getAlertsForItem(itemId: Long): Flow<List<Alert>> =
        alertDao.getAlertsForItem(itemId)

    suspend fun getPendingAlerts(): List<Alert> =
        alertDao.getPendingAlerts()

    fun getSentAlerts(): Flow<List<Alert>> =
        alertDao.getSentAlerts()

    suspend fun insertAlert(alert: Alert): Long =
        alertDao.insert(alert)

    suspend fun insertAlerts(alerts: List<Alert>) =
        alertDao.insertAll(alerts)

    suspend fun updateAlert(alert: Alert) =
        alertDao.update(alert)

    // Custom field operations
    fun getCustomFieldsForItem(itemId: Long): Flow<List<ItemCustomField>> =
        customFieldDao.getCustomFieldsForItem(itemId)

    suspend fun insertCustomField(field: ItemCustomField): Long =
        customFieldDao.insert(field)

    suspend fun insertCustomFields(fields: List<ItemCustomField>) =
        customFieldDao.insertAll(fields)
}
