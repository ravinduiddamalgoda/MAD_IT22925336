package com.example.taskmangamentapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NoteDatabase (context: Context) :SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_version){

    companion object{
        private const val DATABASE_NAME="notesapp.db"
        private const val DATABASE_version=1
        private const val TABLE_NAME="allnotes"
        private const val COLOUMN_ID="id"
        private const val COLOUMN_TITLE="title"
        private const val COLOUMN_CONTENT="content"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery="CREATE TABLE $TABLE_NAME($COLOUMN_ID INTEGER PRIMARY KEY, $COLOUMN_TITLE TEXT,$COLOUMN_CONTENT TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery="DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertNote(note: Note){
        val db=writableDatabase
        val values =ContentValues().apply {
            put(COLOUMN_TITLE,note.title)
            put(COLOUMN_CONTENT,note.content)

        }
        db.insert(TABLE_NAME,null,values)
        db.close()
    }

    fun getAllNotes():List<Note>{
        val notesList = mutableListOf<Note>()
        val db= readableDatabase
        val query="SELECT * FROM $TABLE_NAME"
        val cursor =db.rawQuery(query,null) // use to execute the query

        while (cursor.moveToNext()){

            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLOUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLOUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLOUMN_CONTENT))

            val note=Note(id,title,content)
            notesList.add(note)
        }
        cursor.close()
        db.close()
        return notesList
    }
    fun updateNote(note:Note){
        val db=writableDatabase
        val values = ContentValues().apply {
            put(COLOUMN_TITLE,note.title)
            put(COLOUMN_CONTENT,note.content)
        }
        val whereClause="$COLOUMN_ID=?"
        val whereArgs= arrayOf(note.id.toString())
        db.update(TABLE_NAME,values,whereClause,whereArgs)
        db.close()
    }

    fun getNoteByID(noteId:Int):Note{
        val db= readableDatabase
        val query="SELECT * FROM $TABLE_NAME WHERE $COLOUMN_ID=$noteId"
        val cursor=db.rawQuery(query,null)
        cursor.moveToFirst()

        val id=cursor.getInt(cursor.getColumnIndexOrThrow(COLOUMN_ID))
        val title=cursor.getString(cursor.getColumnIndexOrThrow(COLOUMN_TITLE))
        val content=cursor.getString(cursor.getColumnIndexOrThrow(COLOUMN_CONTENT))

        cursor.close()
        db.close()
        return Note(id,title,content)
    }

    fun deleteNote(noteId: Int){
        val db = writableDatabase
        val whereClause="$COLOUMN_ID=?"
        val whereArgs= arrayOf(noteId.toString())
        db.delete(TABLE_NAME,whereClause,whereArgs)
        db.close()
    }

}