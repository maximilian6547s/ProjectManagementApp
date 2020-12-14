package com.maximcuker.projectmanagementapp.activities

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.maximcuker.projectmanagementapp.R
import com.maximcuker.projectmanagementapp.adapters.TaskListItemsAdapter
import com.maximcuker.projectmanagementapp.firebase.FirestoreClass
import com.maximcuker.projectmanagementapp.models.Board
import com.maximcuker.projectmanagementapp.models.Task
import com.maximcuker.projectmanagementapp.utils.Constants
import kotlinx.android.synthetic.main.activity_task_list.*

class TaskListActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        var boardDocumentId = ""
        if (intent.hasExtra(Constants.DOCUMENT_ID)) {
            boardDocumentId = intent.getStringExtra(Constants.DOCUMENT_ID)
        }

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getBoardDetails(this, boardDocumentId)
    }
    private fun setupActionBar(title:String) {
        setSupportActionBar(toolbar_task_list_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = title
        }
        toolbar_task_list_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun boardDetails(board: Board?) {
        hideProgressDialog()
        if (board != null) {
            setupActionBar(board.name)

            val addTaskList = Task(resources.getString(R.string.add_list))
            board.taskList.add(addTaskList)

            rv_task_list.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

            rv_task_list.setHasFixedSize(true)

            val adapter = TaskListItemsAdapter(this, board.taskList)
            rv_task_list.adapter = adapter
        }
    }

}