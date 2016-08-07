package com.ebceu4.offlinechatsample.infrastructure.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.ebceu4.offlinechatsample.infrastructure.dto.ChatMessage;

import java.sql.Timestamp;
import java.util.ArrayList;

public final class DbTables {
    public DbTables() {}

    public static abstract class ChatMessageTable implements BaseColumns {
        public static final String TABLE_NAME = "message";

        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_ISSENTBYUSER = "isSentByUser";

        public static final String[] STAR_PROJECTION = {_ID, COLUMN_TEXT, COLUMN_TIMESTAMP, COLUMN_ISSENTBYUSER};


        public static final String SQL_CREATE =
                "CREATE TABLE " + TABLE_NAME + " ("
                                + _ID + " INTEGER PRIMARY KEY,"
                                + COLUMN_TEXT + " TEXT, "
                                + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                                + COLUMN_ISSENTBYUSER + " INTEGER DEFAULT 0)";

        public static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static ChatMessage insert(Db db, ChatMessage chatMessage) {

            ContentValues values = new ContentValues();
            values.put(COLUMN_TEXT, chatMessage.getText());
            values.put(COLUMN_ISSENTBYUSER, chatMessage.getIsSentByUser() ? 1 : 0);

            long id = db.getWritableDatabase().insert(TABLE_NAME, null, values);
            ChatMessage result = selectById(db, id);

            db.close();

            return result;
        }

        public static ChatMessage selectById(Db db, long id) {
            Cursor cursor = db.getReadableDatabase().query(TABLE_NAME, STAR_PROJECTION, _ID + " == ?", new String[]{Long.toString(id)}, null, null, null);

            if(cursor.moveToFirst())
            {
                return new ChatMessage(
                        cursor.getLong(0),
                        cursor.getString(1),
                        java.sql.Timestamp.valueOf(cursor.getString(2)),
                        cursor.getInt(3) == 1);
            }

            return null;
        }

        public static ArrayList<ChatMessage> select(Db db, int limit, Timestamp timestamp) {
            String sortOrder = COLUMN_TIMESTAMP + " DESC";
            String selection = null;
            String[] selectionArgs = null;

            if(timestamp != null) {
                selection = COLUMN_TIMESTAMP + " < DATETIME(?)";
                selectionArgs = new String[]{ timestamp.toString() };
            }

            Cursor cursor = db.getReadableDatabase().query(TABLE_NAME, STAR_PROJECTION, selection, selectionArgs, null, null, sortOrder, Integer.toString(limit));

            ArrayList<ChatMessage> chatMessages = new ArrayList<>(limit);

            if(!cursor.moveToLast())
                return chatMessages;

            do {
                chatMessages.add(new ChatMessage(
                        cursor.getLong(0),
                        cursor.getString(1),
                        java.sql.Timestamp.valueOf(cursor.getString(2)),
                        cursor.getInt(3) == 1));
            } while (cursor.moveToPrevious());

            cursor.close();
            db.close();

            return chatMessages;
        }
    }
}
