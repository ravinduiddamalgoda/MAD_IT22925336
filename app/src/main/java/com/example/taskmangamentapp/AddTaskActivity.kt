package me.sunera.taskSet

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.sunera.taskSet.databinding.ActivityAddTaskBinding

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var db: TaskDatabase

    private val scope = CoroutineScope(Dispatchers.Main + Job())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddTaskBinding.inflate(layoutInflater)

        setContentView(binding.root)
        db= TaskDatabase.getDatabase(this)
        binding.saveButton.setOnClickListener{
          val title=binding.titleEditText.text.toString() // get user inputs title
            val content =binding.contentEditText.text.toString()// get user input content
            if (title.isEmpty() || content.isEmpty()){
                Toast.makeText(this,"Details are Empty. Please Enter All Details.",Toast.LENGTH_LONG).show()
//                finish()
            }else{
                val task = Task (0,title,content)// Arguments pass in data class
                scope.launch{
                    withContext(Dispatchers.IO) {db.taskDao().insertTask(task)}
                }
                // calling insert note function
                finish() // finish use to close the activity and riderct to main
                Toast.makeText(this,"Todo saved successfully",Toast.LENGTH_LONG).show()
            }

        }

    }
}