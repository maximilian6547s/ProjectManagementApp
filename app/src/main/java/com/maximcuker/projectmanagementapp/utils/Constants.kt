package com.maximcuker.projectmanagementapp.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import com.maximcuker.projectmanagementapp.activities.MyProfileActivity

object Constants {

    const val USERS: String = "users"
    const val BOARDS: String = "boards"

    const val IMAGE: String = "image"
    const val NAME: String = "name"
    const val MOBILE: String = "mobile"
    const val ASSIGNED_TO: String = "assignedTo"
    const val PICK_IMAGE_REQUEST_CODE = 2
    const val READ_STORAGE_PERMISSION_CODE = 1
    const val DOCUMENT_ID = "documentId"
    const val TASK_LIST = "taskList"
    const val BOARD_DETAIL = "board_detail"
    const val ID = "id"
    const val EMAIL = "email"
    const val TASK_LIST_ITEM_POSITION = "task_list_item_position"
    const val CARD_LIST_ITEM_POSITION = "card_list_item_position"

    //TODO refactor
    fun colorsList():ArrayList<String> {
        val colorsList = ArrayList<String>()
        colorsList.add("#43C86F")
        colorsList.add("#0C90F1")
        colorsList.add("#F72400")
        colorsList.add("#7A8089")
        colorsList.add("#D57C1D")
        colorsList.add("#770000")
        colorsList.add("#0022F8")
        return colorsList
    }

    fun showImageChooser(activity: Activity) {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }

}