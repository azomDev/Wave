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

        private const val TABLE_CONVERSATIONS = "conversations"
        private const val KEY_CONVERSATION_ID = "conversation_id"
        private const val KEY_CREATED_AT = "created_at"

        private const val TABLE_PARTICIPANTS = "participants"
        private const val KEY_PARTICIPANT_ID = "participant_id"
        private const val KEY_PARTICIPANT_NAME = "participant_name"

        private const val TABLE_CONVERSATION_PARTICIPANTS = "conversation_participants"
        private const val KEY_JOINED_AT = "joined_at"

        private const val TABLE_MESSAGES = "messages"
        private const val KEY_MESSAGE_ID = "message_id"
        private const val KEY_SENDER_ID = "sender_id"
        private const val KEY_CONTENT = "content"
        private const val KEY_TIMESTAMP = "timestamp"
    }

    //! Check if onCreate is good
    override fun onCreate(db: SQLiteDatabase?) {
        val createConversationsTable = ("CREATE TABLE " + TABLE_CONVERSATIONS + "("
                + KEY_CONVERSATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_CREATED_AT + " TEXT)")

        val createParticipantsTable = ("CREATE TABLE " + TABLE_PARTICIPANTS + "("
                + KEY_PARTICIPANT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_PARTICIPANT_NAME + " TEXT)")

        val createMessagesTable = ("CREATE TABLE " + TABLE_MESSAGES + "("
                + KEY_MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_CONVERSATION_ID + " INTEGER,"
                + KEY_SENDER_ID + " INTEGER,"
                + KEY_CONTENT + " TEXT,"
                + KEY_TIMESTAMP + " TEXT,"
                + "FOREIGN KEY(" + KEY_CONVERSATION_ID + ") REFERENCES " + TABLE_CONVERSATIONS + "(" + KEY_CONVERSATION_ID + "),"
                + "FOREIGN KEY(" + KEY_SENDER_ID + ") REFERENCES " + TABLE_PARTICIPANTS + "(" + KEY_PARTICIPANT_ID + "))")

        db?.execSQL(createConversationsTable)
        db?.execSQL(createParticipantsTable)
        db?.execSQL(createMessagesTable)
    }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Drop old tables if they exist
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_MESSAGES")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PARTICIPANTS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CONVERSATIONS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CONVERSATION_PARTICIPANTS")

        // Create tables again
        onCreate(db)
    }

    //! Check if addMessage is good
    fun addMessage(message: MessageModel) {
        val db = this.writableDatabase

        // You'll need to create or fetch participant IDs for the sender
        val senderId = message.senderId

        // Check if sender is already in the participants table, and add them if not
        val selectParticipantQuery = "SELECT * FROM $TABLE_PARTICIPANTS WHERE $KEY_PARTICIPANT_ID = ?"
        var cursor = db.rawQuery(selectParticipantQuery, arrayOf(senderId.toString()))
        if (!cursor.moveToFirst()) {
            val values = ContentValues()
            values.put(KEY_PARTICIPANT_ID, senderId)
            // Replace with actual name if available
            db.insert(TABLE_PARTICIPANTS, null, values)
        }
        
        // Check if a conversation already exists for the sender
        val selectConversationQuery = "SELECT * FROM $TABLE_CONVERSATIONS WHERE $KEY_CONVERSATION_ID = ?"
        cursor = db.rawQuery(selectConversationQuery, arrayOf(message.conversationId.toString()))

        var conversationId: Int

        if (cursor.moveToFirst()) {
            // If a conversation exists, get its ID
            conversationId = Integer.parseInt(cursor.getString(0))
        } else {
            // If no conversation exists, create a new one and get its ID
            val conversationValues = ContentValues()
            conversationValues.put(KEY_CONVERSATION_ID, message.conversationId)
            conversationValues.put(KEY_CREATED_AT, message.timestamp) // Or current timestamp
            conversationId = db.insert(TABLE_CONVERSATIONS, null, conversationValues).toInt()

            // Add the sender to the conversation
            val conversationParticipantValues = ContentValues()
            conversationParticipantValues.put(KEY_CONVERSATION_ID, conversationId)
            conversationParticipantValues.put(KEY_PARTICIPANT_ID, senderId)
            conversationParticipantValues.put(KEY_JOINED_AT, message.timestamp) // Or current timestamp
            db.insert(TABLE_CONVERSATION_PARTICIPANTS, null, conversationParticipantValues)
        }

        // Add the message to the messages table with the appropriate conversation ID and sender ID
        val messageValues = ContentValues()
        messageValues.put(KEY_MESSAGE_ID, message.messageId)
        messageValues.put(KEY_CONVERSATION_ID, conversationId)
        messageValues.put(KEY_SENDER_ID, senderId)
        messageValues.put(KEY_CONTENT, message.message)
        messageValues.put(KEY_TIMESTAMP, message.timestamp)
        db.insert(TABLE_MESSAGES, null, messageValues)

        cursor.close()
        db.close()
    }

    //! Check if getConversation is good
    fun getConversation(conversationId: Int): List<MessageModel> {
        val messageList = ArrayList<MessageModel>()
        val db = this.readableDatabase

        // Query to get the messages for this conversation ID
        val selectQuery = "SELECT * FROM $TABLE_MESSAGES WHERE $KEY_CONVERSATION_ID = ? ORDER BY $KEY_TIMESTAMP DESC"
        val messageCursor = db.rawQuery(selectQuery, arrayOf(conversationId.toString()))

        if (messageCursor.moveToFirst()) {
            do {
                val message = MessageModel()
                message.messageId = Integer.parseInt(messageCursor.getString(messageCursor.getColumnIndex(KEY_MESSAGE_ID)))
                message.conversationId = Integer.parseInt(messageCursor.getString(messageCursor.getColumnIndex(KEY_CONVERSATION_ID)))
                message.senderId = Integer.parseInt(messageCursor.getString(messageCursor.getColumnIndex(KEY_SENDER_ID)))
                message.message = messageCursor.getString(messageCursor.getColumnIndex(KEY_CONTENT))
                message.timestamp = messageCursor.getString(messageCursor.getColumnIndex(KEY_TIMESTAMP))
                messageList.add(message)
            } while (messageCursor.moveToNext())
        }
        messageCursor.close()

        return messageList
    }

    //! Check if getAllConversation is good
    fun getAllConversations(): List<ConversationModel> {
        val db = this.readableDatabase
        val allConversations = "SELECT * FROM $TABLE_CONVERSATIONS"
        val conversationList = mutableListOf<ConversationModel>()
        val cursor = db.rawQuery(allConversations, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex(KEY_CONVERSATION_ID))
            val createdAt = cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT))

            val conversation = ConversationModel(id)
            conversationList.add(conversation)
        }

        cursor.close()
        return conversationList
    }

    //! Check if getAllParticipantsInConversation is good
    fun getAllParticipantsInConversation(conversationId: Int) : List<ParticipantModel> {
        val db = this.readableDatabase
        val selectParticipantsQuery = ("SELECT * FROM $TABLE_PARTICIPANTS WHERE $KEY_PARTICIPANT_ID IN " +
                "(SELECT $KEY_PARTICIPANT_ID FROM $TABLE_CONVERSATION_PARTICIPANTS WHERE $KEY_CONVERSATION_ID = ?)")
        val cursor = db.rawQuery(selectParticipantsQuery, arrayOf(conversationId.toString()))
        val participantList = mutableListOf<ParticipantModel>()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex(KEY_PARTICIPANT_ID))
            val name = cursor.getString(cursor.getColumnIndex(KEY_PARTICIPANT_NAME))

            val participant = ParticipantModel(id, name)
            participantList.add(participant)
        }

        cursor.close()
        return participantList
    }

    //! Check if getConversationId is good and if it is needed
    fun getConversationId(sender: String, recipient: String): String? {
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_CONVERSATIONS WHERE ($COLUMN_PARTICIPANT_ONE = ? AND $COLUMN_PARTICIPANT_TWO = ?) OR ($COLUMN_PARTICIPANT_ONE = ? AND $COLUMN_PARTICIPANT_TWO = ?)"
        val cursor = db.rawQuery(selectQuery, arrayOf(sender, recipient, recipient, sender))

        if (cursor.moveToFirst()) {
            val conversationId = cursor.getString(cursor.getColumnIndex(COLUMN_ID))
            cursor.close()
            return conversationId
        }

        cursor.close()
        return null
    }

}
