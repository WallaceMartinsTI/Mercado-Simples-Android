package com.wcsm.mercadosimples.database

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.wcsm.mercadosimples.database.DatabaseHelper.Companion.LOG_INFO_DB
import com.wcsm.mercadosimples.model.Product

class ProductDAO(context: Context) : IProductDAO {

    private val writing = DatabaseHelper(context).writableDatabase
    private val reading = DatabaseHelper(context).readableDatabase

    override fun save(product: Product): Boolean {
        val content = ContentValues()
        content.put(DatabaseHelper.COL_PRODUCT_ID, product.id)
        content.put(DatabaseHelper.COL_PRODUCT_NAME, product.name)
        content.put(DatabaseHelper.COL_PRODUCT_QUANTITY, product.quantity)
        content.put(DatabaseHelper.COL_PRODUCT_PRICE, product.price)

        try {
            writing.insert(DatabaseHelper.TABLE_NAME, null, content)
            Log.i(LOG_INFO_DB, "PRODUCT saved successfully")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i(LOG_INFO_DB, "Error SAVING product")
            return false
        }

        return true
    }

    override fun delete(productId: String): Boolean {
        val args = arrayOf(productId)
        try {
            writing.delete(
                DatabaseHelper.TABLE_NAME,
                "${DatabaseHelper.COL_PRODUCT_ID} = ?",
                args
            )
            Log.i(LOG_INFO_DB, "PRODUCT deleted successfully")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i(LOG_INFO_DB, "Error DELETING product")
            return false
        }

        return true
    }

    override fun deleteALlProducts() : Boolean {
        try {
            writing.delete(DatabaseHelper.TABLE_NAME, null, null)
            Log.i(LOG_INFO_DB, "ALL PRODUCT deleted successfully")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i(LOG_INFO_DB, "Error DELETING ALL product")
            return false
        }

        return true
    }

    override fun listProducts(): MutableList<Product> {
        val productsList = mutableListOf<Product>()

        val sql = "SELECT" +
                " ${DatabaseHelper.COL_PRODUCT_ID}, ${DatabaseHelper.COL_PRODUCT_NAME}," +
                " ${DatabaseHelper.COL_PRODUCT_QUANTITY}, ${DatabaseHelper.COL_PRODUCT_PRICE}" +
                " FROM ${DatabaseHelper.TABLE_NAME};"

        val cursor = reading.rawQuery(sql, null)

        val productIdIndex = cursor.getColumnIndex(DatabaseHelper.COL_PRODUCT_ID)
        val productNameIndex = cursor.getColumnIndex(DatabaseHelper.COL_PRODUCT_NAME)
        val productQuantityIndex = cursor.getColumnIndex(DatabaseHelper.COL_PRODUCT_QUANTITY)
        val productPriceIndex = cursor.getColumnIndex(DatabaseHelper.COL_PRODUCT_PRICE)

        while(cursor.moveToNext()) {
            val id = cursor.getString(productIdIndex)
            val name = cursor.getString(productNameIndex)
            val quantity = cursor.getInt(productQuantityIndex)
            val price = cursor.getDouble(productPriceIndex)

            productsList.add(Product(id, name, quantity, price))
        }

        return productsList
    }
}