package com.foresight.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val iconName: String = "category",
    val colorHex: String = "#6750A4",
    val isDefault: Boolean = false,
    val sortOrder: Int = 0
)
