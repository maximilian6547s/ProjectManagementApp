package com.maximcuker.projectmanagementapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.maximcuker.projectmanagementapp.R
import kotlinx.android.synthetic.main.activity_task_list.*
import kotlinx.android.synthetic.main.app_bar_main.*

class TaskListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)
        setupActionBar()

    }
    private fun setupActionBar() {
        setSupportActionBar(toolbar_task_list_activity)
        toolbar_task_list_activity.setNavigationIcon(R.drawable.ic_white_color_back_24dp)
        toolbar_task_list_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}