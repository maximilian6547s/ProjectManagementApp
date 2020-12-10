package com.maximcuker.projectmanagementapp.firebase

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.maximcuker.projectmanagementapp.activities.MainActivity
import com.maximcuker.projectmanagementapp.activities.SignInActivity
import com.maximcuker.projectmanagementapp.activities.SignUpActivity
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
                Log.e(activity.javaClass.simpleName, "Error writing document")
            }
    }

    fun signInUser(activity: Activity) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId()!!)
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User::class.java)
                if (loggedInUser != null) {
                    when (activity) {
                        is SignInActivity -> activity.signInSuccess(loggedInUser)
                        is MainActivity -> {
                            activity.updateNavigationUserDetails(loggedInUser)
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
}