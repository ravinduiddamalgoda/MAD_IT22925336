package me.sunera.taskSet

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TasksAdapter (private var tasks:List<Task>, context: Context):RecyclerView.Adapter<TasksAdapter.NoteViewHolder>() {
 private val db: TaskDatabase = TaskDatabase.getDatabase(context)
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    class NoteViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val titleTextView: TextView =itemView.findViewById(R.id.titleTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        val updateButton: ImageView = itemView.findViewById(R.id.updateButton)
        val deleteButton:ImageView=itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.task_item,parent,false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val task = tasks[position]
        holder.titleTextView.text=task.title
        holder.contentTextView.text=task.description

        holder.updateButton.setOnClickListener{
            val intent =Intent(holder.itemView.context, UpdateTaskActivity::class.java).apply {
                putExtra("task_id",task.id)
            }
            holder.itemView.context.startActivity(intent)
        }
        holder.deleteButton.setOnClickListener{
            val taskDao = db.taskDao()
            scope.launch {
                withContext(Dispatchers.IO){ taskDao.deleteTask(task)}
                val newTasks = withContext(Dispatchers.IO){
                    taskDao.getAllTasks()
                }
                refreshData(newTasks)
            }
            Toast.makeText(holder.itemView.context,"Todo successfully deleted ",Toast.LENGTH_SHORT).show()
        }

    }

    fun refreshData(newTasks : List<Task>){
       tasks= newTasks
       notifyDataSetChanged()
    }

}
