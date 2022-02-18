package com.example.ContactsApp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finalstudycontactdetails.R

// need these classes and methods everytime we use a recycler view
// instance of view for single list item, this must hold the elements which I want to change via my adapter
class MyListViewHolder(val view: View) : RecyclerView.ViewHolder(view){
    var name: TextView
    var number: TextView
    var editButton: ImageButton
    var deleteButton: ImageButton


    // here I find all the UI aspects which I hold in my list which I want to update
    init {
        name = view.findViewById(R.id.name)
        number = view.findViewById(R.id.number)
        editButton = view.findViewById(R.id.editContact)
        deleteButton = view.findViewById(R.id.deleteContact)
    }
}

// Model that adapts the list of items to the recycler view
class MyListAdapter : RecyclerView.Adapter<MyListViewHolder>() {
    var myContactsList = mutableListOf<List<String>>()

    /*
        interface for the main activity to implement, these methods allow communication between the recycler view
        and the activity which it is placed

     */
    interface listElementListener{
        fun DisplayToast(name: String, number: String)
        fun deleteContact(name: String, number: String)
        fun openEdit(name: String, number: String, position: Int)
    }

    var myListener: listElementListener? = null

    // tell it how many items in the list
    override fun getItemCount(): Int {
        return myContactsList.size
    }

    // what happens when the view holder is created, parent view group is the view containing this view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyListViewHolder {

        // inflate from the parent activity, creating a recycler list
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_layout, parent, false)

        return MyListViewHolder(view)
    }

    // takes in a view holder, and then a position for which we want to bind info
    override fun onBindViewHolder(holder: MyListViewHolder, position: Int) {
        holder.apply {
            val contactName = myContactsList[position][0]
            val contactNumber = myContactsList[position][1]

            name.text = "Name: ${contactName}"
            number.text = "Number: ${contactNumber}"

            name.setOnClickListener {
                myListener?.DisplayToast(name.text.toString(), number.text.toString())
            }

            deleteButton.setOnClickListener{
                myListener?.DisplayToast("Deleting: ${name.text.toString()}", "${number.text.toString()}")
                myListener?.deleteContact(contactName, contactNumber)
                myContactsList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, myContactsList.size)
            }

            editButton.setOnClickListener {
                myListener?.DisplayToast("Editing: ${name.text.toString()}", "${number.text.toString()}")
                myListener?.openEdit(contactName, contactNumber, position)
            }
        }
    }


    // function for adding a single meal
    fun addContact(newContact: List<String>){
        myContactsList.add(newContact)
        this.notifyDataSetChanged()

    }



    // function for setting a list and updating the screen
    fun setList(newList: MutableList<List<String>>){
        myContactsList = newList
        this.notifyDataSetChanged()
    }
}