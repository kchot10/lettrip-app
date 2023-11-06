/**
 * @author Joyce Hong
 * @email soja0524@gmail.com
 * @created 2019-09-03
 * @desc
 */

package com.cookandroid.travelerapplication.chat;

import android.content.Context
import com.cookandroid.travelerapplication.chat.model.MessageType
import com.cookandroid.travelerapplication.helper.FileHelper

data class Message (val userName : String, val messageContent : String, val roomName: String,var viewType : Int, val isImage: Boolean)
data class initialData (val userName : String, val roomName : String)
data class SendMessage(val userName : String, val messageContent: String, val roomName: String, val isImage: Boolean)