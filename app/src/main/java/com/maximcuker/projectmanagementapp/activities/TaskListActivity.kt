package com.maximcuker.projectmanagementapp.activities

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.maximcuker.projectmanagementapp.R
import com.maximcuker.projectmanagementapp.adapters.TaskListItemsAdapter
import com.maximcuker.projectmanagementapp.firebase.FirestoreClass
import com.maximcuker.projectmanagementapp.models.Board
import com.maximcuker.projectmanagementapp.models.Card
import com.maximcuker.projectmanagementapp.models.Task
import com.maximcuker.projectmanagementapp.utils.Constants
import kotlinx.android.synthetic.main.activity_task_list.*

class TaskListActivity : BaseActivity() {

    private lateinit var mBoardDetails:Board

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

    private fun setupActionBar() {
        setSupportActionBar(toolbar_task_list_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = mBoardDetails.name
        }
        toolbar_task_list_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun boardDetails(board: Board?) {
        hideProgressDialog()
        if (board != null) {
            mBoardDetails = board
            setupActionBar()

            val addTaskList = Task(resources.getString(R.string.add_list))
            board.taskList.add(addTaskList)

            rv_task_list.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

            rv_task_list.setHasFixedSize(true)

            val adapter = TaskListItemsAdapter(this, board.taskList)
            rv_task_list.adapter = adapter
        }
    }

    fun addUpdateTaskListSuccess() {
        hideProgressDialog()
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getBoardDetails(this, mBoardDetails.documentId)
    }

    fun createTaskList(taskListName: String) {
        val task = Task(taskListName, FirestoreClass().getCurrentUserId().toString())
        mBoardDetails.taskList.add(0, task)
        removeLastElementOFTaskList()

        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().addUpdateTaskList(this,mBoardDetails)
    }

    fun updateTaskList(position:Int,listName:String, model:Task){
        val task = Task(listName, model.createdBy)
        mBoardDetails.taskList[position] = task
        removeLastElementOFTaskList()
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().addUpdateTaskList(this,mBoardDetails)
    }

    fun deleteTaskList(position: Int) {
        mBoardDetails.taskList.removeAt(position)
        removeLastElementOFTaskList()
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().addUpdateTaskList(this,mBoardDetails)
    }

    fun addCardToTaskList(position: Int, cardName:String) {
        removeLastElementOFTaskList()
        val cardAssignedUsersList: ArrayList<String> = ArrayList()
        val currentUserID = FirestoreClass().getCurrentUserId().toString()
        val currentTask = mBoardDetails.taskList[position]
        cardAssignedUsersList.add(currentUserID)
        val card = Card(cardName,currentUserID,cardAssignedUsersList)

        val cardsList = currentTask.cards
        cardsList.add(card)

        val task = Task(currentTask.title, currentTask.createdBy, cardsList)

        mBoardDetails.taskList[position] = task
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().addUpdateTaskList(this,mBoardDetails)

    }

    private fun removeLastElementOFTaskList() {
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size-1)
    }

}