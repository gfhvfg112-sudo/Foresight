package com.foresight.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.foresight.app.data.local.converter.Converters
import com.foresight.app.data.local.dao.*
import com.foresight.app.data.local.entity.*
import com.foresight.app.data.local.seed.SeedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Category::class,
        Item::class,
        Alert::class,
        CategoryField::class,
        ItemCustomField::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ForesightDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun itemDao(): ItemDao
    abstract fun alertDao(): AlertDao
    abstract fun categoryFieldDao(): CategoryFieldDao
    abstract fun itemCustomFieldDao(): ItemCustomFieldDao

    companion object {
        const val DATABASE_NAME = "foresight.db"

        fun create(context: Context): ForesightDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ForesightDatabase::class.java,
                DATABASE_NAME
            )
                .addCallback(DatabaseCallback())
                .build()
        }
    }

    private class DatabaseCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Use raw SQL to seed — avoids circular Room dependency
            CoroutineScope(Dispatchers.IO).launch {
                val categories = SeedData.defaultCategories
                val statement = db.compileStatement(
                    "INSERT OR REPLACE INTO categories (name, iconName, colorHex, isDefault, sortOrder) VALUES (?, ?, ?, ?, ?)"
                )

                categories.forEachIndexed { index, category ->
                    statement.clearBindings()
                    statement.bindString(1, category.name)
                    statement.bindString(2, category.iconName)
                    statement.bindString(3, category.colorHex)
                    statement.bindLong(4, if (category.isDefault) 1L else 0L)
                    statement.bindLong(5, category.sortOrder.toLong())
                    val id = statement.executeInsert()

                    // Insert category fields
                    val fields = SeedData.defaultFields(id, category.name)
                    val fieldStatement = db.compileStatement(
                        "INSERT OR REPLACE INTO category_fields (categoryId, fieldName, fieldType, isRequired, optionsJson) VALUES (?, ?, ?, ?, ?)"
                    )
                    fields.forEach { field ->
                        fieldStatement.clearBindings()
                        fieldStatement.bindLong(1, id)
                        fieldStatement.bindString(2, field.fieldName)
                        fieldStatement.bindString(3, field.fieldType)
                        fieldStatement.bindLong(4, if (field.isRequired) 1L else 0L)
                        fieldStatement.bindString(5, field.optionsJson ?: "")
                        fieldStatement.executeInsert()
                    }
                    fieldStatement.close()
                }
                statement.close()
            }
        }
    }
}
