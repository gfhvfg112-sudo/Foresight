package com.foresight.app.data.model

enum class ItemStatus(val code: Int) {
    ACTIVE(0),
    EXPIRED(1),
    DISCARDED(2),
    REPLACED(3);

    companion object {
        fun fromCode(code: Int): ItemStatus =
            entries.firstOrNull { it.code == code } ?: ACTIVE
    }
}
