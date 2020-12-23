package com.maximcuker.projectmanagementapp.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.maximcuker.projectmanagementapp.R
import com.maximcuker.projectmanagementapp.activities.*
import com.maximcuker.projectmanagementapp.models.Board
import com.maximcuker.projectmanagementapp.models.Card
import com.maximcuker.projectmanagementapp.models.User
import com.maximcuker.projectmanagementapp.utils.Constants


class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo: User) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId()!!)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisteredSuccess()
            }.addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error writing document")
            }
    }

    fun getBoardDetails(activity: TaskListActivity, documentId:String) {
        mFireStore.collection(Constants.BOARDS)
            .document(documentId)
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())
                val board = document.toObject(Board::class.java)
                board?.documentId = document.id
                activity.boardDetails(board)
            }.addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, e.message)
            }
    }

    fun createBoard(activity: CreateBoardActivity, boardInfo:Board) {
        mFireStore.collection(Constants.BOARDS)
            .document()
            .set(boardInfo, SetOptions.merge())
            .addOnSuccessListener {
                Log.i(activity.javaClass.simpleName, "Board created successfully")
                Toast.makeText(activity, "Board created successfully", Toast.LENGTH_LONG).show()
                activity.boardCreatedSuccessfully()
            }.addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, e.message)
            }
    }

    fun getBoardsList(activity: MainActivity) {
        getCurrentUserId()?.let {
            mFireStore.collection(Constants.BOARDS)
                .whereArrayContains(Constants.ASSIGNED_TO, it)
                .get()
                .addOnSuccessListener {
                    document ->
                    Log.i(activity.javaClass.simpleName, document.documents.toString())
                    val boardList: ArrayList<Board> = ArrayList()
                    for (i in document.documents) {
                        val board = i.toObject(Board::class.java)
                        if (board != null) {
                            board.documentId = i.id
                            boardList.add(board)
                        }
                    }
                    activity.fillBoardsListToUI(boardList)
                }.addOnFailureListener {
                    e->
                    activity.hideProgressDialog()
                    Log.e(activity.javaClass.simpleName, e.message)
                }
        }
    }

    fun addUpdateTaskList(activity: Activity, board: Board) {
        val taskListHashMap = HashMap<String, Any>()
        taskListHashMap[Constants.TASK_LIST] = board.taskList
        mFireStore.collection(Constants.BOARDS).document(board.documentId).update(taskListHashMap).addOnSuccessListener {
            Log.i(activity.javaClass.simpleName, "TaskList updated successfully")
            if (activity is TaskListActivity) {
                activity.addUpdateTaskListSuccess()
            } else if (activity is CardDetailsActivity) {
                activity.addUpdateTaskListSuccess()
            }
        }.addOnFailureListener {
            exception->
            if (activity is TaskListActivity) {
                activity.hideProgressDialog()
            } else if (activity is CardDetailsActivity) {
                activity.hideProgressDialog()
            }
            Log.e(activity.javaClass.simpleName, "Error while creating a board", exception)
        }
    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String,Any>) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId()!!)
            .update(userHashMap)
            .addOnSuccessListener {
                Log.i(activity.javaClass.simpleName, "Profile data updated successfully")
                Toast.makeText(activity, "Profile data updated successfully", Toast.LENGTH_LONG).show()
                when(activity) {
                    is MainActivity -> {
                        activity.tokenUpdateSuccess()
                    }
                    is MyProfileActivity -> {
                        activity.profileUpdateSuccess()
                    }
                }
            }.addOnFailureListener {
                e->
                when(activity) {
                    is MainActivity -> {
                        activity.hideProgressDialog()
                    }
                    is MyProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName, "Error while creating a board",e)
                Toast.makeText(activity, "Profile data update error", Toast.LENGTH_LONG).show()

            }
    }

    fun loadUserData(activity: Activity, readBoardsList: Boolean = false) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId()!!)
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User::class.java)
                if (loggedInUser != null) {
                    when (activity) {
                        is SignInActivity -> activity.signInSuccess(loggedInUser)
                        is MainActivity -> {
                            activity.updateNavigationUserDetails(loggedInUser, readBoardsList)
                        }
                        is MyProfileActivity -> {
                            activity.setUserDataInUI(loggedInUser)
                        }
                    }
                }
            }.addOnFailureListener {e->
                when(activity) {
                    is SignInActivity -> activity.hideProgressDialog()
                    is MainActivity -> {
                        activity.hideProgressDialog()
                    }}
                 Log.e(activity.javaClass.simpleName, "Error writing document")
            }

    }

    fun getCurrentUserId(): String? {
        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun getAssignedMembersListDetails(activity: Activity, assignedTo: ArrayList<String>) {
        mFireStore.collection(Constants.USERS).whereIn(Constants.ID, assignedTo).get().addOnSuccessListener {
            document->
            Log.i(activity.javaClass.simpleName, document.documents.toString())

            val users: ArrayList<User> = ArrayList()

            for (i in document.documents) {
                val user = i.toObject(User::class.java)
                if (user != null) {
                    users.add(user)
                }
            }
            if (activity is MembersActivity) {
                activity.setupMembersList(users)
            } else if (activity is TaskListActivity) {
                activity.boardMembersDetailsList(users)
            }
        }.addOnFailureListener { e->
            if (activity is MembersActivity) {
                activity.hideProgressDialog()
            } else if (activity is TaskListActivity) {
                activity.hideProgressDialog()
            }
            Log.e(activity.javaClass.simpleName, "Error while creating a board",e)
        }
    }

    fun getMemberDetails(activity: MembersActivity, email:String) {
        mFireStore.collection(Constants.USERS)
            .whereEqualTo(Constants.EMAIL,email).get()
            .addOnSuccessListener {
            document ->
            if (document.documents.size > 0) {
                val user = document.documents[0].toObject(User::class.java)
                if (user != null) {
                    activity.memberDetails(user)
                } else {
                    activity.hideProgressDialog()
                    activity.showErrorSnackBar(activity.getString(R.string.no_member_found))
                }
            }
        }.addOnFailureListener { e->
            activity.hideProgressDialog()
            Log.e(activity.javaClass.simpleName, "Error while getting user details",e)
        }
    }

    fun assignMemberToBoard(activity:MembersActivity, board:Board, user:User) {
        val assignedToHashMap = HashMap<String,Any>()
        assignedToHashMap[Constants.ASSIGNED_TO] = board.assignedTo

        mFireStore.collection(Constants.BOARDS)
            .document(board.documentId)
            .update(assignedToHashMap)
            .addOnSuccessListener {
                activity.memberAssignSuccess(user)
            }.addOnFailureListener { e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while creating a board",e)
            }
    }

}