package com.wcsm.mercadosimples.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE, null, VERSION) {

    companion object {
        const val DATABASE = "Products.db"
        const val VERSION = 1
        const val TABLE_NAME = "products"
        const val COL_PRODUCT_ID = "product_id"
        const val COL_PRODUCT_NAME = "name"
        const val COL_PRODUCT_QUANTITY = "quantity"
        const val COL_PRODUCT_PRICE = "price"

        const val LOG_INFO_DB = "info_db"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                " $COL_PRODUCT_ID TEXT NOT NULL PRIMARY KEY," +
                " $COL_PRODUCT_NAME VARCHAR(50) NOT NULL," +
                " $COL_PRODUCT_QUANTITY INTEGER NOT NULL," +
                " $COL_PRODUCT_PRICE NUMERIC NOT NULL" +
                ");"

        try {
            db?.execSQL(sql)
            Log.i(LOG_INFO_DB, "Created PRODUCTS table successful")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i(LOG_INFO_DB, "Error creating PRODUCTS table")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}