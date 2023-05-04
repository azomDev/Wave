package com.example.receiving_test

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.database.Cursor

class SmsDatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "smsDatabase"

        private const val TABLE_CONVERSATIONS = "conversations"
        private const val KEY_CONVERSATION_ID = "conversation_id"

        private const val TABLE_USERS = "users"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_PHONE_NUMBER = "user_phone_number"

        private const val TABLE_PARTICIPANTS = "participants"

        private const val TABLE_MESSAGES = "messages"
        private const val KEY_MESSAGE_ID = "message_id"
        private const val KEY_SENDER_ID = "sender_id"
        private const val KEY_CONTENT = "content"
        private const val KEY_TIMESTAMP = "timestamp"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createConversationsTable = """CREATE TABLE $TABLE_CONVERSATIONS(
                $KEY_CONVERSATION_ID INTEGER PRIMARY KEY AUTOINCREMENT
                )"""

        val createUsersTable = """CREATE TABLE $TABLE_USERS(
                $KEY_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_USER_PHONE_NUMBER TEXT
                )"""

        val createParticipantsTable = """CREATE TABLE $TABLE_PARTICIPANTS(
                $KEY_USER_ID INTEGER,
                $KEY_CONVERSATION_ID INTEGER,
                FOREIGN KEY($KEY_USER_ID) REFERENCES $TABLE_USERS($KEY_USER_ID),
                FOREIGN KEY($KEY_CONVERSATION_ID) REFERENCES $TABLE_CONVERSATIONS($KEY_CONVERSATION_ID)
                )"""

        val createMessagesTable = """CREATE TABLE $TABLE_MESSAGES(
                $KEY_MESSAGE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_CONVERSATION_ID  INTEGER,
                $KEY_SENDER_ID INTEGER,
                $KEY_CONTENT TEXT,
                $KEY_TIMESTAMP TEXT,
                FOREIGN KEY($KEY_CONVERSATION_ID) REFERENCES $TABLE_CONVERSATIONS($KEY_CONVERSATION_ID),
                FOREIGN KEY($KEY_SENDER_ID) REFERENCES $TABLE_USERS($KEY_USER_ID)
                )"""

        db?.execSQL(createConversationsTable)
        db?.execSQL(createUsersTable)
        db?.execSQL(createParticipantsTable)
        db?.execSQL(createMessagesTable)
    }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Drop old tables if they exist
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_MESSAGES")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CONVERSATIONS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PARTICIPANTS")

        // Create tables again
        onCreate(db)
    }

    fun addMessage(message: MessageModel) {
        val db = this.writableDatabase

        // Add the message to the messages table with the appropriate conversation ID and sender ID
        val messageValues = ContentValues().apply {
            put(KEY_CONVERSATION_ID, message.conversationId)
            put(KEY_SENDER_ID, message.senderId)
            put(KEY_CONTENT, message.message)
            put(KEY_TIMESTAMP, message.timestamp)
        }
        
        db.insert(TABLE_MESSAGES, null, messageValues)
        Log.d("MyApp", "The message $message.message was added to the database")
        db.close()
    }


    fun getMessagesFromConversation(conversationId: Int): List<MessageModel> {
        val messageList = ArrayList<MessageModel>()
        val db = this.readableDatabase

        // Query to get the messages for this conversation ID
        val selectQuery = "SELECT * FROM $TABLE_MESSAGES WHERE $KEY_CONVERSATION_ID = ? ORDER BY $KEY_TIMESTAMP DESC"
        val messageCursor = db.rawQuery(selectQuery, arrayOf(conversationId.toString()))

        while (messageCursor.moveToNext()) {
            val message = MessageModel()
            message.messageId = Integer.parseInt(messageCursor.getString(messageCursor.getColumnIndex(KEY_MESSAGE_ID)))
            message.conversationId = Integer.parseInt(messageCursor.getString(messageCursor.getColumnIndex(KEY_CONVERSATION_ID)))
            message.senderId = Integer.parseInt(messageCursor.getString(messageCursor.getColumnIndex(KEY_SENDER_ID)))
            message.message = messageCursor.getString(messageCursor.getColumnIndex(KEY_CONTENT))
            message.timestamp = messageCursor.getString(messageCursor.getColumnIndex(KEY_TIMESTAMP))
            messageList.add(message)
        }
        messageCursor.close()
        db.close()

        return messageList
    }

    fun getAllConversations(): List<Int> {
        val conversationList = ArrayList<Int>()

        try {
            val db = this.readableDatabase
            val allConversations = "SELECT * FROM $TABLE_CONVERSATIONS"

            val cursor = db.rawQuery(allConversations, null)

            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex(KEY_CONVERSATION_ID))
                conversationList.add(id)
            }

            cursor.close()
            db.close()

            Log.d("Database", "Successfully fetched all conversations")

        } catch (e: Exception) {
            Log.e("Database", "Failed to fetch all conversations", e)
        }

        return conversationList
    }


    fun getAllUsersInConversation(conversationId: Int) : List<UserModel> {
        val participantList = mutableListOf<UserModel>()
        val db = this.readableDatabase

        try {
            val selectParticipantsQuery = """SELECT * FROM $TABLE_USERS
                    WHERE $KEY_USER_ID IN
                    (SELECT $KEY_USER_ID FROM $TABLE_PARTICIPANTS 
                    WHERE $KEY_CONVERSATION_ID = ?)"""
            val cursor = db.rawQuery(selectParticipantsQuery, arrayOf(conversationId.toString()))
        
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex(KEY_USER_ID))
                val phoneNumber = cursor.getString(cursor.getColumnIndex(KEY_USER_PHONE_NUMBER))
                participantList.add(UserModel(id, phoneNumber))
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("Database", "Failed to fetch users in conversation", e)
        } finally {
            db.close()
        }

        return participantList
    }

    fun getConversationId(senderId: Int): Int? {
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            val myId = createOrReturnUser("me", db)

            val selectQuery = """
                SELECT $KEY_CONVERSATION_ID FROM $TABLE_PARTICIPANTS 
                WHERE $KEY_USER_ID IN (?, ?)
                GROUP BY $KEY_CONVERSATION_ID
                HAVING COUNT(DISTINCT $KEY_USER_ID) = 2
            """
            cursor = db.rawQuery(selectQuery, arrayOf(senderId.toString(), myId.toString()))

            if (cursor.moveToFirst()) {
                val conversationId = cursor.getInt(cursor.getColumnIndex(KEY_CONVERSATION_ID))
                Log.d("Database", "Conversation id: $conversationId was fetched")
                return conversationId
            }
        } catch (e: Exception) {
            Log.e("Database", "Failed to fetch conversation id", e)
        } finally {
            cursor?.close()
            db.close()
        }
        return null
    }

    fun createNewConversation(): Int {
        var conversationId = -1

        try {
            val db = this.writableDatabase

            // Create a new conversation
            db.execSQL("INSERT INTO $TABLE_CONVERSATIONS DEFAULT VALUES")

            // Get the ID of the newly inserted conversation
            val cursor = db.rawQuery("SELECT last_insert_rowid()", null)
            if (cursor.moveToFirst()) {
                conversationId = cursor.getInt(0)
            }
            cursor.close()

            val userId = createOrReturnUser("me")
            createNewParticipant(userId, conversationId)

            db.close()

            Log.d("Database", "Successfully created new conversation with ID: $conversationId")

        } catch (e: Exception) {
            Log.e("Database", "Failed to create new conversation", e)
        }

        return conversationId
    }

    fun createOrReturnUser(phoneNumber: String, dbParam: SQLiteDatabase? = null): Int {
        var userId = -1
        var cursor: Cursor? = null
        val db: SQLiteDatabase
        val shouldCloseDb: Boolean

        if (dbParam == null) {
            db = this.readableDatabase
            shouldCloseDb = true
        } else {
            db = dbParam
            shouldCloseDb = false
        }

        try {
            // Query to check if user already exists
            val selectQuery = """SELECT $KEY_USER_ID FROM $TABLE_USERS 
            WHERE $KEY_USER_PHONE_NUMBER = ?"""
            cursor = db.rawQuery(selectQuery, arrayOf(phoneNumber))

            // If user exists, return their ID
            if (cursor.moveToFirst()) {
                userId = cursor.getInt(cursor.getColumnIndex(KEY_USER_ID))
                Log.d("Database", "User exists with ID: $userId and phone number: $phoneNumber")
                return userId
            }
            cursor.close()

            // User does not exist, so create a new one
            val userValues = ContentValues().apply {
                put(KEY_USER_PHONE_NUMBER, phoneNumber)
            }

            // Insert the new user into the table and get its ID
            userId = db.insert(TABLE_USERS, null, userValues).toInt()

            // If insert failed, db.insert() returns -1
            if (userId == -1) {
                Log.e("Database", "Failed to create new user")
                return userId
            }

            Log.d("Database", "Successfully created new user with ID: $userId")

        } catch (e: Exception) {
            Log.e("Database", "Failed to create or return user", e)
        } finally {
            cursor?.close()
            if (shouldCloseDb) {
                db.close()
            }
        }

        return userId
    }

    fun createNewParticipant(userId: Int, conversationId: Int): Boolean {
        try {
            val db = this.writableDatabase

            // Create a participant user
            val participantValues = ContentValues().apply {
                put(KEY_USER_ID, userId)
                put(KEY_CONVERSATION_ID, conversationId)
            }

            // Insert the new participant into the table
            val newRowId = db.insert(TABLE_PARTICIPANTS, null, participantValues)

            db.close()

            if (newRowId == -1L) {
                Log.e("Database", "Failed to create new participant")
                return false
            }

            Log.d("Database", "Successfully created new participant with ID: $userId and conversationID: $conversationId")
            return true

        } catch (e: Exception) {
            Log.e("Database", "Failed to create new participant", e)
            return false
        }
    }

    fun getUser(userId: Int): UserModel? {
        try {
            val db = this.readableDatabase

            val selectQuery = "SELECT * FROM $TABLE_USERS WHERE $KEY_USER_ID = ?"
            val cursor = db.rawQuery(selectQuery, arrayOf(userId.toString()))

            var user: UserModel? = null

            if (cursor.moveToFirst()) {
                val id = cursor.getInt(cursor.getColumnIndex(KEY_USER_ID))
                val phoneNumber = cursor.getString(cursor.getColumnIndex(KEY_USER_PHONE_NUMBER))
                user = UserModel(id, phoneNumber)
            }

            cursor.close()
            db.close()

            if (user == null) {
                Log.e("Database", "Failed to get user with ID: $userId")
                return null
            }

            Log.d("Database", "Successfully fetched user with ID: $userId")
            return user

        } catch (e: Exception) {
            Log.e("Database", "Failed to get user with ID: $userId", e)
            return null
        }
    }

}
