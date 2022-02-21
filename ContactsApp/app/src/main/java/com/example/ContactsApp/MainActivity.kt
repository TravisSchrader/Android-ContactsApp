package com.example.ContactsApp

/*
Author: Travis Schrader
This program creates a simple contacts application.
 */


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalstudycontactdetails.R
import com.example.finalstudycontactdetails.add_contact_info
import com.example.finalstudycontactdetails.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), add_contact_info.contactInfoListener,
    MyListAdapter.listElementListener {

    companion object{
        private var binding: ActivityMainBinding? = null
        lateinit var myDB: DatabaseManager
        lateinit var addContact: Button
        lateinit var myRecyclerList: RecyclerView
        lateinit var myListAdapter: MyListAdapter
        var myContacts = mutableListOf<List<String>>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initialize the binding, this ensures it will not be null
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        // setting the title
        this.title = "Contacts"

        // grab DB
        myDB = DatabaseManager(this)

        // test persistence of contacts
        var contactCursor = myDB.writableDatabase.rawQuery("SELECT * FROM CONTACTS", null)

        // set up addContact Button
        addContact = binding!!.addContact


        // set up list
        myRecyclerList = binding!!.ContactList
        myListAdapter = MyListAdapter() // initialize adapter for list
        myListAdapter.myListener = this
        myRecyclerList.layoutManager = LinearLayoutManager(this)

        myRecyclerList.adapter = myListAdapter

        // add to list the first on create
        if(savedInstanceState == null){

            // when opening contacts, load all the contacts in the DB to the list which will then be set at the recycler view list
            // Only want to do this the first time I create the app
            while(contactCursor.moveToNext()){
                myContacts.add(listOf(contactCursor.getString(0), contactCursor.getString(1)))
            }

            // Initialize the list with all upper and lower case letters
            // these contacts are not added to the DB they are just to test the application
            for (num in 65..122){
                if(num !in 91..96){
                    myContacts.add(listOf(num.toChar().toString(), num.toString()))
                }
            }
        }

        // sort the list
        myContacts.sortWith(
            compareBy(String.CASE_INSENSITIVE_ORDER, {it[0]})
        )

        // set my listAdpters list to my contacts list from the DB
        myListAdapter.setList(myContacts)

        // setting up a contact listener
        binding!!.addContact.setOnClickListener {
            var myFrag = add_contact_info()
            myFrag.contactListener = this

            myFrag.show(supportFragmentManager, "Enter Contact Info")
        }
    }

    // Add to the on save instance bundle if this default contact is in the list
    // That way the list isnt updated each time the app rotates
    override fun onSaveInstanceState(outState: Bundle) {
        if(myContacts.contains(listOf("A", "65"))){
            outState.putString("list", "Initialized")
        }
        super.onSaveInstanceState(outState)
    }

    // null out the binding on destroy
    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }


    // implementing listener method for the add_contact_info fragment
    override fun submitContact(name: String, number: String) {
        addToRecyclerList(name, number)

        myDB.insert("\'$name\'", "\'$number\'")
    }

    // Debug toast method
    override fun DisplayToast(name: String, number: String) {
        val toast = Toast.makeText(this, " ${name} ${number}", Toast.LENGTH_SHORT)
        toast.show()
    }
    // error toast message for when the contact entered is empty in the add_contact_info fragment
    override fun emptyContactToast() {
        val toast = Toast.makeText(this, " Please Enter Contact Info ", Toast.LENGTH_SHORT)
        toast.show()
    }
    // Implementing a deleted Contact Method for my list adapter delete button
    override fun deleteContact(name: String, number: String) {
        myDB.deleteContact("\'$name\'","\'$number\'")
    }

    //  editing a contact, delete the previous contact then add the new one
    override fun editContact(
        originalName: String,
        originalNumber: String,
        name: String,
        number: String,
        position: Int
    ) {
        myDB.deleteContact("\'$originalName\'","\'$originalNumber\'")
        myContacts.removeAt(position)
        myDB.insert("\'$name\'", "\'$number\'")
        addToRecyclerList(name,number)
    }

    // Implementing list adapter function to open the contact edit dialog
    // needs to make a bundle and then open the add_contact_info fragment with that bundle
    override fun openEdit(name: String, number: String, position: Int) {
        var editFrag = add_contact_info()

        // passing in existing data of the contact to the contact fragment
        var args : Bundle = Bundle()
        args.putString("param1", name)
        args.putString("param2", number)
        args.putInt("param3", position)

        editFrag.arguments = args
        editFrag.contactListener = this

        editFrag.show(supportFragmentManager, "Edit Contact Info")

    }

    // Adds to my contact list and sorts the list
    // this method handles adding to my list so I can sort it in one spot
    fun addToRecyclerList(name: String, number: String){
        myContacts.add(listOf(name, number))
        myContacts.sortWith(
            compareBy(String.CASE_INSENSITIVE_ORDER, {it[0]})
        )

        myListAdapter.notifyDataSetChanged()
    }
}