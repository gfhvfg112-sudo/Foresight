package com.foresight.app.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.foresight.app.data.local.entity.Category
import com.foresight.app.data.local.entity.Item

data class ItemWithCategory(
    @Embedded val item: Item,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: Category
)
