package com.mb.neox.com.models

data class Item(
    val itemCode: String,
    val itemName: String,
    val uUbicacion: String
)

data class ItemResponse(
    val value: List<Item>
)
