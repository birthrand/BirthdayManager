package com.example.birthdaymanager

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import kotlinx.android.synthetic.main.fragment_birthday.*
import kotlinx.android.synthetic.main.fragment_birthday.view.*
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [BirthdayFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [BirthdayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BirthdayFragment : Fragment() {


    var fileContents:String?=""
    var customAdapter: CustomAdapter? = null
    var newValue: String? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var  rootView = inflater.inflate(R.layout.fragment_birthday, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // The code below receives contents of the birthday text file
        // from the internal storage as a single line, splits the line into
        // two arrays based on the delimiters provided and assigns each entry
        // to the instantiation of the contact class.
        // This is then added to the contact array list which acts as the source
        // of the list view adapter. This ensures that all the contents in the
        // file are displayed
        // When the delete selected button is pressed, the onClickListener is
        // triggered and the content of the selected listView item is retrieved.
        // The sendData() function is then called to pass the obtained data to the
        // MainActivity
        fileContents = arguments?.getString("fileContents")
        var myValue = this.arguments!!.getString("fileContents")
        var firstArray = arrayOf<String>()

        var contactArrayList= ArrayList<Contact>()

            if (myValue!!.contains("|")) {
                firstArray = myValue.split("|").toTypedArray()
            }

            for (line in firstArray) {
                if (line.contains(",")) {
                    val secondArray = line.split(",").toTypedArray()
                    val contact = Contact()
                    contact.name = secondArray[0]
                    contact.phoneNumber = secondArray[1]
                    contact.birthDate = secondArray[2]

                    contactArrayList.add(contact)
                }
            }

       customAdapter = CustomAdapter(requireContext(), contactArrayList!!)

        view.birthdayListView.adapter = customAdapter

        var item = Contact()
        var itemContent:String=""

        birthdayListView.onItemClickListener=
            AdapterView.OnItemClickListener{ parent, view, position, id ->
            view.setBackgroundColor(Color.LTGRAY)
            item = birthdayListView.getItemAtPosition(position) as Contact
                itemContent = "|"+ item.name + "," + item.phoneNumber + "," + item.birthDate
            }

        deleteButton.setOnClickListener {
            if(myValue.contains(itemContent!!) && itemContent!="")
            {
                newValue = myValue.replace(itemContent!!,"")

            }

            sendData()

        }
    }

    companion object {
        // TODO: Rename and change types and number of parameters

        fun newInstance(): BirthdayFragment = BirthdayFragment()
    }

    // This function gets the updated file contents and
    // sends it to the Main activity, which then modifies
    // birthday list file to remove the deleted entry.
    // Then the main activity is started and the view is updated
    // to reflect the layout change
    private fun sendData() {
        //INTENT OBJ
        val i = Intent(
            activity!!.baseContext,
            MainActivity::class.java
        )

        //PACK DATA
        i.putExtra("SENDER_KEY", "DeleteFile")
        i.putExtra("DELETE_FILE_CONTENTS", newValue)

        //START ACTIVITY
        activity!!.startActivity(i)
    }
}
