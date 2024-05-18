package me.sunera.taskSet

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.sunera.taskSet.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: TaskDatabase
    private lateinit var tasksAdapter: TasksAdapter
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tasksAdapter = TasksAdapter(mutableListOf(),this)

        db = TaskDatabase.getDatabase(this)
        binding.notesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.notesRecyclerView.adapter = tasksAdapter

        scope.launch {
            val tasks = withContext(Dispatchers.IO) { db.taskDao().getAllTasks() }
            tasksAdapter.refreshData(tasks)
        }

        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val taskDao = db.taskDao()
        scope.launch {
            val tasks = withContext(Dispatchers.IO) { db.taskDao().getAllTasks() }
            tasksAdapter.refreshData(tasks)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}