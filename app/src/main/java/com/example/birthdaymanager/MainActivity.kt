package com.example.birthdaymanager

import android.content.Context
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.content.Intent
import android.graphics.Color
import android.widget.ListView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_birthday.*
import java.io.File
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    var fileContents: String? = null
    val arrayList: ArrayList<String>? = null
    val filename = "birthdayManager.txt"
    private var customAdapter: CustomAdapter? = null
    private var contactArrayList: ArrayList<Contact>? = null
    private var birthdayListView: ListView? = null

    // This method is used to select which fragment will
    // be opened based on the navigation menu item that is
    // selected
    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.person_add -> {
                    val personAddFragment = PersonAddFragment.newInstance()

                    openFragment(personAddFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.import_contacts -> {
                    val importFragment = ImportFragment.newInstance()
                    openFragment(importFragment)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.birthday_list -> {
                    val birthdayFragment = BirthdayFragment.newInstance()
                    openFragment(birthdayFragment)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }


    // This method is called if the activity was previously
    // not in focus, but is currently.
    // If data is received from the PersonAdd Fragment successfully,
    // A short message is toasted to the screen
    override fun onResume() {

        try {

            super.onResume()

            //DETERMINE WHO STARTED THIS ACTIVITY
            val sender = intent.extras!!.getString("SENDER_KEY")

            //IF ITS THE FRAGMENT THEN RECEIVE DATA
            if (sender != null) {

                if(sender.contains("Delete")){
                    this.receiveData()
                    Toast.makeText(this, "File Deleted Successfully", Toast.LENGTH_LONG).show()
                }
                else {
                    this.receiveData()
                    Toast.makeText(this, "Added Successfully", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {

        }
    }

    // This method receives all the user's input from the
    // PersonAdd Fragment and saves it to a string mutable variable
    // The contents of the string variable are then appended to the
    // Birthday List file
    // This method also receives the modified file content from the
    // BirthdayFragement. This data is sent when the user chooses to
    // delete an entry.
    private fun receiveData() {
        //RECEIVE DATA VIA INTENT
        val i = intent
        val sender = intent.extras!!.getString("SENDER_KEY")


        if(sender!!.contains("Delete")) {

            fileContents = i.getStringExtra("DELETE_FILE_CONTENTS")

            openFileOutput(filename, Context.MODE_PRIVATE).use {
                it.write(fileContents!!.toByteArray())
            }
        }
        else {
            fileContents = i.getStringExtra("FILE_CONTENTS")

            openFileOutput(filename, Context.MODE_APPEND).use {
                it.write(fileContents!!.toByteArray())
            }
        }

    }

    // This methods gets the currently selected navigation item
    // and opens the fragment associated with it, by replacing the
    // Main activity view, with the layout of the selected fragment
    // The text views in the main activity layout are set to be hidden
    // from the view
    // This method also checks to see if the Birthday Fragment is selected
    // If it is, it opens the birthday list text file and bundles its content
    // onto a string variable
    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)

        if (fragment.toString().contains("Birthday")) {
            val bundle = Bundle()
            var menu = openFileInput(filename).reader()
            var content = menu.readText()

            bundle.putString("fileContents", content)
            fragment.arguments = bundle
        }
        transaction.addToBackStack(null)
        transaction.commit()
        homeTextSecond.visibility = View.INVISIBLE
        homeTextFirst.visibility = View.INVISIBLE
    }

}