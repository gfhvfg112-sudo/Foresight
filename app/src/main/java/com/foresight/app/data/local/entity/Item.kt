package com.foresight.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "items",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("categoryId"), Index("expiryDate"), Index("status")]
)
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val categoryId: Long,
    val expiryDate: Long, // epoch millis
    val notes: String = "",
    val photoUri: String? = null,
    val barcode: String? = null,
    val isRecurring: Boolean = false,
    val recurrenceDays: Int? = null,
    val status: Int = 0, // 0=Active, 1=Expired, 2=Discarded, 3=Replaced
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
