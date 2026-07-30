package com.foresight.app.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.foresight.app.data.local.entity.Alert
import com.foresight.app.data.local.entity.Item

data class ItemWithAlerts(
    @Embedded val item: Item,
    @Relation(
        parentColumn = "id",
        entityColumn = "itemId"
    )
    val alerts: List<Alert>
)
