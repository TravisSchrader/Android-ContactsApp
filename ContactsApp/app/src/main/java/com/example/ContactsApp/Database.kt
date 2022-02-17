package com.example.ContactsApp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// for all DB stuff this class needs to be made
// create a class which is a sub class of SQLiteHelper, the SQLite helper takes in the context, the name of the db, factory, and which version
class DatabaseManager(context: Context): SQLiteOpenHelper(context, "MyDB", null, 1){

    // create the database, in here create the table which I will be using
    override fun onCreate(db: SQLiteDatabase?) {
        // creating a table with a name and number as text, currently has no primary key.
        db?.execSQL("CREATE TABLE IF NOT EXISTS CONTACTS(name TEXT, number TEXT)")
    }

    // what to do when you want to upgrade the db
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // most likely will never have to update the database
    }


    // we want to insert a string into the database
    fun insert(name: String, number: String){
        writableDatabase.execSQL("INSERT INTO CONTACTS (NAME, NUMBER) VALUES($name, $number)")
    }

    // delete a given contact from the db, currently the db has not primary key so I am using the combination of name and number
    fun deleteContact(_name: String, _number: String){
        writableDatabase.execSQL("DELETE FROM CONTACTS WHERE NAME=$_name AND NUMBER=$_number")
    }

    // return list of all rows from the DB
    fun readAllRows(): List<String>{
        var result = mutableListOf<String>()

        // get a table back from the query
        var cursor = writableDatabase.rawQuery("SELECT * FROM DATES", null)

        // go through the table and add the elements in each row at column 0 to the result list
        while (cursor.moveToNext()){
            result.add(cursor.getString(0))
        }

        return result

    }
}