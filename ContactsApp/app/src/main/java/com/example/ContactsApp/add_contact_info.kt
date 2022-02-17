package com.example.finalstudycontactdetails

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

/**
 * A simple [Fragment] subclass.
 * Use the [add_contact_info.newInstance] factory method to
 * create an instance of this fragment.
 */
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

//        dialog?.setTitle("Enter New Contact: ")
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment add_contact_info.
         */
        // TODO: Rename and change types and number of parameters
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