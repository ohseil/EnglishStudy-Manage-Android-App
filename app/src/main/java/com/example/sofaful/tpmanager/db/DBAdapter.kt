package com.example.sofaful.tpmanager.db

import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import java.io.IOException

/**
 * Created by Seil on 2020-11-09.
 */
class DBAdapter {

    companion object {
        var mContext: Context? = null
        var mDb: SQLiteDatabase? = null
        var mDbHelper: DBHelper? = null
        var mCursor: Cursor? = null
    }

    constructor(context: Context?) {
        mContext = context
        mDbHelper = DBHelper(mContext)
    }

    fun createDB() {

        try {

            mDbHelper?.createDB()

        } catch (e: IOException) {
            throw Error("UnableToCreateDB")
        }
    }

    fun open(): DBAdapter {

        try {

            mDbHelper?.openDataBase()
            mDb = mDbHelper?.writableDatabase
        }
        catch (mSQLException: SQLException) {
            throw mSQLException
        }
        return this
    }

    fun close() {
        mDbHelper?.close()
    }

    fun getData(chapter: String): Cursor? {

        try {

            this.open()
            val sql = "select * from " + chapter
            mCursor = mDb?.rawQuery(sql, null)

            if (mCursor != null)
                mCursor?.moveToNext()

            this.close()
            return mCursor
        }
        catch (mSQLException: SQLException) {
            throw mSQLException
        }
    }

}