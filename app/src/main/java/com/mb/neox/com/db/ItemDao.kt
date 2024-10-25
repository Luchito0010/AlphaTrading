package com.mb.neox.com.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ItemDao {

    @Insert
    fun insertItems(items: List<ItemEntity>)

    @Query("SELECT * FROM items WHERE ItemCode = :itemCode")
    fun getItemByCode(itemCode: String): ItemEntity?
}
