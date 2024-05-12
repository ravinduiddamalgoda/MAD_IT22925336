package com.example.taskmangamentapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taskmangamentapp.databinding.ActivityAddNoteBinding
import com.example.taskmangamentapp.databinding.ActivityMainBinding

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var db:NoteDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddNoteBinding.inflate(layoutInflater)

        setContentView(binding.root)
        db= NoteDatabase(this)
        binding.saveButton.setOnClickListener{
          val title=binding.titleEditText.text.toString() // get user inputs title
            val content =binding.contentEditText.text.toString()// get user input content
            val note =Note (0,title,content)// Arguments pass in data class
            db.insertNote(note)// calling insert note function
            finish() // finish use to close the activity and riderct to main
            Toast.makeText(this,"Note saved successfully",Toast.LENGTH_LONG).show()
        }


    }
}