package com.example.receiving_test

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SmsDatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "smsDatabase"
        private const val TABLE_SMS = "sms"
        private const val KEY_ID = "id"
        private const val KEY_SENDER = "sender"
        private const val KEY_RECIPIENT = "recipient"
        private const val KEY_MESSAGE = "message"
        private const val KEY_TIMESTAMP = "timestamp"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createSmsTable = ("CREATE TABLE " + TABLE_SMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_SENDER + " TEXT,"
                + KEY_RECIPIENT + " TEXT,"
                + KEY_MESSAGE + " TEXT,"
                + KEY_TIMESTAMP + " TEXT" + ")")
        db?.execSQL(createSmsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_SMS")
        onCreate(db)
    }

    fun addSms(sms: SmsModel) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_SENDER, sms.sender)
        values.put(KEY_RECIPIENT, sms.recipient)
        values.put(KEY_MESSAGE, sms.message)
        values.put(KEY_TIMESTAMP, sms.timestamp)
        db.insert(TABLE_SMS, null, values)
        db.close()
    }

    fun getConversation(sender: String, recipient: String): List<SmsModel> {
        val smsList = ArrayList<SmsModel>()
        val db = this.writableDatabase

        // Query to get the messages exchanged between sender and recipient
        val selectQuery = "SELECT * FROM $TABLE_SMS WHERE ($KEY_SENDER = ? AND $KEY_RECIPIENT = ?) OR ($KEY_SENDER = ? AND $KEY_RECIPIENT = ?)"
        
        val cursor = db.rawQuery(selectQuery, arrayOf(sender, recipient, recipient, sender))

        if (cursor.moveToFirst()) {
            do {
                val sms = SmsModel()
                sms.id = Integer.parseInt(cursor.getString(0))
                sms.sender = cursor.getString(1)
                sms.recipient = cursor.getString(2)
                sms.message = cursor.getString(3)
                sms.timestamp = cursor.getString(4)
                smsList.add(sms)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return smsList
    }


    fun getAllSms(): List<SmsModel> {
        val smsList = ArrayList<SmsModel>()
        val selectQuery = "SELECT  * FROM $TABLE_SMS"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val sms = SmsModel()
                sms.id = Integer.parseInt(cursor.getString(0))
                sms.sender = cursor.getString(1)
                sms.recipient = cursor.getString(2)
                sms.message = cursor.getString(3)
                sms.timestamp = cursor.getString(4)
                smsList.add(sms)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return smsList
    }
}
