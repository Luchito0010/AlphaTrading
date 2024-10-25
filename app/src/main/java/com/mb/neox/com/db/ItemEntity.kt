package com.mb.neox.com.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey val itemCode: String,
    val itemName: String,
    val uUbicacion: String
)
