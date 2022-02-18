package com.example.finalstudycontactdetails

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment

// arguments passed to this fragment which let me know if this fragment is being used to edit a contact or not
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

class add_contact_info : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var name: String? = null
    private var number: String? = null
    private var position: Int? = null

    interface contactInfoListener {
        fun submitContact(name: String, number: String)
        fun emptyContactToast()
        fun editContact(originalName: String, originalNumber: String, name: String, number: String, position: Int)
    }

    // set up listener
    var contactListener: contactInfoListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            Log.d("EDIT", "In here? ")
            name = it.getString(ARG_PARAM1)
            number = it.getString(ARG_PARAM2)
            position = it.getInt(ARG_PARAM3)
            Log.d("EDIT", name.toString())
            Log.d("EDIT", number.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setLayout(100,100)

        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_add_contact_info, container, false)


        var nameEditText = view.findViewById<EditText>(R.id.contactName)
        var numberEditText = view.findViewById<EditText>(R.id.contactNumber)
        var submitButton = view.findViewById<Button>(R.id.submitContactButton)
        var cancelButton = view.findViewById<Button>(R.id.cancelContactButton)

        // set the text values to be the bundle values if we are editing an existing contact
        nameEditText.setText(if(name != null) name else nameEditText.text)
        numberEditText.setText(if(number != null) number else numberEditText.text)

        Log.d("EDIT", numberEditText.text.toString())
        Log.d("EDIT", number.toString())


        submitButton.setOnClickListener {

            // check if there is actually a valid contact
            if(nameEditText.text.toString() == "" || numberEditText.text.toString() == ""){
                contactListener?.emptyContactToast()
            }

            // valid contact
            else{

                // creating a new contact
                if(name == null){
                    contactListener?.submitContact(nameEditText.text.toString(), numberEditText.text.toString())
                    println("Submitting contact.... name: ${nameEditText.text.toString()} number: ${numberEditText.text.toString()}")
                    dismiss() // closes the fragment
                }

                // editing an existing contact
                else{
                    contactListener?.editContact(name as String, number as String, nameEditText.text.toString(), numberEditText.text.toString(), position as Int)
                    dismiss() // closes the fragment
                }



            }
        }

        // decided not to edit/create contact
        cancelButton.setOnClickListener {
            dismiss()
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            add_contact_info().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}