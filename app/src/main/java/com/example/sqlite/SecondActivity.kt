package com.example.sqlite

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SecondActivity : AppCompatActivity() {

    private val role = mutableListOf(
        "Должность",
        "Фармацевт",
        "Санитарка",
        "Консультант",
        "Заведующая"
    )

    private val db = DBHelper(this, null)

    private lateinit var toolbarSecond: Toolbar
    private lateinit var roleSpinner: Spinner
    private lateinit var enterNameET: EditText
    private lateinit var enterPhoneET: EditText
    private lateinit var addNameBTN: Button
    private lateinit var printNameBTN: Button
    private lateinit var clearTableBTN: Button
    private lateinit var nameTV: TextView
    private lateinit var phoneTV: TextView
    private lateinit var roleTV: TextView

    @SuppressLint("MissingInflatedId", "Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_second)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()

        setSupportActionBar(toolbarSecond)
        title = "База данных"
        toolbarSecond.setLogo(R.drawable.baseline_archive_24)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            role
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        roleSpinner.adapter = adapter

        addNameBTN.setOnClickListener {
            if (enterNameET.text.isEmpty() || enterPhoneET.text.isEmpty() || roleSpinner.selectedItem == "Должность") {
                Toast.makeText(
                    applicationContext,
                    "Заполнены не все поля",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            val name = enterNameET.text.toString()
            val phone = enterPhoneET.text.toString()
            val role = roleSpinner.selectedItem.toString()

            db.addName(name, phone, role)
            Toast.makeText(
                this,
                "$name, $phone и $role добавлены в базу данных",
                Toast.LENGTH_LONG
            ).show()
            enterNameET.text.clear()
            enterPhoneET.text.clear()
            roleSpinner.setSelection(0)
        }

        printNameBTN.setOnClickListener {
            val cursor = db.getInfo()
            if (cursor != null && cursor.moveToFirst()) {
                cursor.moveToFirst()
                nameTV.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME)) + "\n")
                phoneTV.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_PHONE)) + "\n")
                roleTV.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ROLE)) + "\n")
            }
            while (cursor!!.moveToNext()) {
                nameTV.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME)) + "\n")
                phoneTV.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_PHONE)) + "\n")
                roleTV.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ROLE)) + "\n")
            }
            cursor.close()
        }

        clearTableBTN.setOnClickListener {
            db.removeAll()
            nameTV.text = ""
            phoneTV.text = ""
            roleTV.text = ""
        }
    }

    private fun init() {
        toolbarSecond = findViewById(R.id.toolbarSecond)
        roleSpinner = findViewById(R.id.roleSpinner)
        enterNameET = findViewById(R.id.enterNameET)
        enterPhoneET = findViewById(R.id.enterPhoneET)
        addNameBTN = findViewById(R.id.addNameBTN)
        printNameBTN = findViewById(R.id.printNameBTN)
        clearTableBTN = findViewById(R.id.clearTableBTN)
        nameTV = findViewById(R.id.nameTV)
        phoneTV = findViewById(R.id.phoneTV)
        roleTV = findViewById(R.id.roleTV)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_second, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.exitMenuSecond -> {
                finishAffinity()
                Toast.makeText(
                    applicationContext,
                    "Программа завершена",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}