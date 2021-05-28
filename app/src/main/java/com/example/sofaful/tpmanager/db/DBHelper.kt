package com.example.sofaful.tpmanager.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by Seil on 2020-11-09.
 */
class DBHelper: SQLiteOpenHelper {

    companion object {
        val DB_NAME: String = "DB_of_TP_Manager.db"
        var DB_PATH: String = ""
        var mDB: SQLiteDatabase? = null
        var mContext: Context? = null
    }

    constructor(context: Context?): super(context, DB_NAME, null, 1) {

        if (android.os.Build.VERSION.SDK_INT >= 17) {
            DB_PATH = context?.applicationInfo?.dataDir + "/databases/"
        }
        else {
            DB_PATH = "/data/data/" + context?.packageName + "/databases/"
        }
        mContext = context
    }

    fun createDB() {

        // DB가 없으면 asset 폴더에서 복사해온다.
        val isDbExist = checkDB()
        if (!isDbExist) {
            this.readableDatabase
            this.close()
            try {

                copyDB()

            } catch(e: IOException) {
                throw Error("ErrorCopyingDB")
            }
        }
    }

    // /data/data/your package/databases/Da Name <-이 경로에서 데이터베이스가 존재하는지 확인한다
    fun checkDB(): Boolean {
        val file = File(DB_PATH + DB_NAME)
        return file.exists()
    }

    // assets 폴더에서 DB를 복하한다.
    fun copyDB() {
        val iStream = mContext!!.assets.open(DB_NAME)
        val outFileName = DB_PATH + DB_NAME
        val oStream = FileOutputStream(outFileName)
        var mBuffer = ByteArray(1024, {0})
        var mLength: Int = iStream.read(mBuffer)

        while (mLength > 0 ) {
            oStream.write(mBuffer, 0, mLength)
            mLength = iStream.read(mBuffer)
        }

        oStream.flush()
        oStream.close()
        iStream.close()
    }

    fun openDataBase(): Boolean {

        val mPath = DB_PATH + DB_NAME
        mDB = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY)
        return mDB != null
    }

    override fun close() {
        if (mDB != null)
            mDB?.close()
        super.close()
    }

    override fun onCreate(p0: SQLiteDatabase?) {
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }

}