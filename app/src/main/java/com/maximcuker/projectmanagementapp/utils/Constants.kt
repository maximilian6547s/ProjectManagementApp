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
    const val BOARD_MEMBERS_LIST = "board_members_list"
    const val SELECT = "Select"
    const val UN_SELECT = "UnSelect"
    const val APP_PREFERENCES = "AppPreferences"

    const val FCM_TOKEN_UPDATED = "fcmTokenUpdated"
    const val FCM_TOKEN = "fcmToken"

    const val FCM_BASE_URL:String = "https://fcm.googleapis.com/fcm/send"
    const val FCM_AUTHORIZATION:String = "authorization"
    const val FCM_KEY:String = "key"
    const val FCM_SERVER_KEY:String = "AAAAbtxHpKM:APA91bHhGjmnQp9YnqxPY3b6XjgbgkHS8rdMepc5yDbLK8p6fmq9yolxpfHoKp_AVBUYLEl76H3Y7jnexIPm5B4CuyDW1mt5nBdX3XHG6O828mLCCgOftiZ0zK9I928giAwVWKmn0Caa"
    const val FCM_KEY_TITLE:String = "title"
    const val FCM_KEY_MESSAGE:String = "message"
    const val FCM_KEY_DATA:String = "data"
    const val FCM_KEY_TO:String = "to"

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