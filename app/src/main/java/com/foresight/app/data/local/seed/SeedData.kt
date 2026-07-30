package com.foresight.app.data.local.seed

import com.foresight.app.data.local.entity.Category
import com.foresight.app.data.local.entity.CategoryField

object SeedData {

    val defaultCategories = listOf(
        Category(name = "Food", iconName = "restaurant", colorHex = "#E8710A", isDefault = true, sortOrder = 0),
        Category(name = "Medicine", iconName = "medication", colorHex = "#BA1A1A", isDefault = true, sortOrder = 1),
        Category(name = "Documents", iconName = "description", colorHex = "#1A6B3C", isDefault = true, sortOrder = 2),
        Category(name = "Subscriptions", iconName = "subscriptions", colorHex = "#6750A4", isDefault = true, sortOrder = 3),
        Category(name = "Insurance", iconName = "health_and_safety", colorHex = "#0B57D0", isDefault = true, sortOrder = 4),
        Category(name = "Warranties", iconName = "verified", colorHex = "#7C4DFF", isDefault = true, sortOrder = 5),
        Category(name = "Household", iconName = "home", colorHex = "#0D652D", isDefault = true, sortOrder = 6),
        Category(name = "Other", iconName = "more_horiz", colorHex = "#5F6368", isDefault = true, sortOrder = 7),
    )

    fun defaultFields(categoryId: Long, categoryName: String): List<CategoryField> {
        return when (categoryName) {
            "Food" -> listOf(
                CategoryField(categoryId = categoryId, fieldName = "Storage Location", fieldType = "select", optionsJson = """["Fridge","Freezer","Pantry","Counter"]"""),
                CategoryField(categoryId = categoryId, fieldName = "Is Opened", fieldType = "boolean"),
                CategoryField(categoryId = categoryId, fieldName = "Quantity", fieldType = "text"),
            )
            "Medicine" -> listOf(
                CategoryField(categoryId = categoryId, fieldName = "Lot Number", fieldType = "text"),
                CategoryField(categoryId = categoryId, fieldName = "Is Opened", fieldType = "boolean"),
                CategoryField(categoryId = categoryId, fieldName = "Dosage", fieldType = "text"),
            )
            "Documents" -> listOf(
                CategoryField(categoryId = categoryId, fieldName = "Document Number", fieldType = "text"),
                CategoryField(categoryId = categoryId, fieldName = "Issued By", fieldType = "text"),
                CategoryField(categoryId = categoryId, fieldName = "Renewal Required", fieldType = "boolean"),
            )
            "Subscriptions" -> listOf(
                CategoryField(categoryId = categoryId, fieldName = "Monthly Cost", fieldType = "text"),
                CategoryField(categoryId = categoryId, fieldName = "Auto Renew", fieldType = "boolean"),
                CategoryField(categoryId = categoryId, fieldName = "URL", fieldType = "text"),
            )
            "Insurance" -> listOf(
                CategoryField(categoryId = categoryId, fieldName = "Provider", fieldType = "text"),
                CategoryField(categoryId = categoryId, fieldName = "Policy Number", fieldType = "text"),
                CategoryField(categoryId = categoryId, fieldName = "Premium", fieldType = "text"),
            )
            "Warranties" -> listOf(
                CategoryField(categoryId = categoryId, fieldName = "Retailer", fieldType = "text"),
                CategoryField(categoryId = categoryId, fieldName = "Order Number", fieldType = "text"),
                CategoryField(categoryId = categoryId, fieldName = "Purchase Price", fieldType = "text"),
            )
            "Household" -> listOf(
                CategoryField(categoryId = categoryId, fieldName = "Location", fieldType = "text"),
                CategoryField(categoryId = categoryId, fieldName = "Brand", fieldType = "text"),
            )
            else -> emptyList()
        }
    }
}
