package me.sunera.taskSet

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.sunera.taskSet.databinding.ActivityUpdateTaskBinding

class UpdateTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateTaskBinding
    private lateinit var db: TaskDatabase
    private var taskId:Int=-1
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = TaskDatabase.getDatabase(this)

        val taskId = intent.getIntExtra("task_id",-1)
        if (taskId== -1){
            finish()
            return
        }

        scope.launch {
            withContext(Dispatchers.IO){
                val task=db.taskDao().getTaskById(taskId)
                binding.updateTitleEditText.setText(task.title)
                binding.updateContentEditText.setText(task.description)
            }
        }

        binding.updateSaveButton.setOnClickListener{
            val newTitle=binding.updateTitleEditText.text.toString()
            val newContent=binding.updateContentEditText.text.toString()
            val updatedTask= Task(taskId,newTitle,newContent)
            scope.launch {
                withContext(Dispatchers.IO){
                    db.taskDao().updateTask(updatedTask)
                }
            }

            finish()
            Toast.makeText(this,"Todo updated Successfully",Toast.LENGTH_SHORT).show()

        }

    }
}