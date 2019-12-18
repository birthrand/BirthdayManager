package com.example.birthdaymanager

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import kotlinx.android.synthetic.main.fragment_person_add.*
import kotlinx.android.synthetic.main.fragment_person_add.view.*
import android.content.Intent

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PersonAddFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PersonAddFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PersonAddFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var birthdayListView: ListView? = null
    private var customAdapter: CustomAdapter? = null
    private var contactArrayList: ArrayList<Contact>? = null
    val filename = "birthdayList.txt"
    var fileContents: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var  rootView = inflater.inflate(R.layout.fragment_person_add, container, false)
        return rootView
    }

    // The method implement the addbutton onClick listener
    // When the add button is pressed, the data in the input field
    // is stored in a contact holder class and concatenated to a
    // single string. Then the sendData function is called to send the
    // inputted data to the Main activity
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        addButton.setOnClickListener {

            val newContact = Contact()
            newContact.name = firstName.text.toString() + " " + lastName.text.toString()
            newContact.phoneNumber = phoneNumber.text.toString()
            newContact.birthDate = birthDate.text.toString()

            fileContents = "|"+ newContact.name + "," + newContact.phoneNumber + "," + newContact.birthDate


            sendData()
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PersonAddFragment.
         */
        // TODO: Rename and change types and number of parameters

        /*fun newInstance(param1: String, param2: String) =
            PersonAddFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }*/
        fun newInstance(): PersonAddFragment = PersonAddFragment()
    }


    // This method sends the data obtained from the user to
    // the Main Activity in form of a string.
    // A key is also passed along, this key will be used to
    // confirm that data was actually sent from this fragment
    // This method also clears the input fields

    private fun sendData() {
        //INTENT OBJ
        val i = Intent(
            activity!!.baseContext,
            MainActivity::class.java
        )

        //PACK DATA
        i.putExtra("SENDER_KEY", "MyFragment")
        i.putExtra("FILE_CONTENTS", fileContents)

        //RESET WIDGETS
        firstName.setText("")
        lastName.setText("")
        birthDate.setText("")
        phoneNumber.setText("")

        //START ACTIVITY
        activity!!.startActivity(i)
    }
}
