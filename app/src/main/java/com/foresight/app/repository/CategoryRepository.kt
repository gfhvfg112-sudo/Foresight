package com.foresight.app.repository

import com.foresight.app.data.local.dao.CategoryDao
import com.foresight.app.data.local.dao.CategoryFieldDao
import com.foresight.app.data.local.entity.Category
import com.foresight.app.data.local.entity.CategoryField
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao,
    private val fieldDao: CategoryFieldDao
) {
    fun getAllCategories(): Flow<List<Category>> =
        categoryDao.getAllCategories()

    suspend fun getAllCategoriesOnce(): List<Category> =
        categoryDao.getAllCategoriesOnce()

    fun getCategoryById(id: Long): Flow<Category?> =
        categoryDao.getCategoryById(id)

    suspend fun getCategoryByIdOnce(id: Long): Category? =
        categoryDao.getCategoryByIdOnce(id)

    suspend fun insert(category: Category): Long =
        categoryDao.insert(category)

    suspend fun update(category: Category) =
        categoryDao.update(category)

    // Category fields
    fun getFieldsForCategory(categoryId: Long): Flow<List<CategoryField>> =
        fieldDao.getFieldsForCategory(categoryId)

    suspend fun insertField(field: CategoryField): Long =
        fieldDao.insert(field)
}
