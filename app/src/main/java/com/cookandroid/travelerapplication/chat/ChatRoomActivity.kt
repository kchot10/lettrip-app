package com.cookandroid.travelerapplication.chat;

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.cookandroid.travelerapplication.R
import com.cookandroid.travelerapplication.chat.model.MessageType
import com.cookandroid.travelerapplication.databinding.ActivityChatroomBinding
import com.cookandroid.travelerapplication.helper.FileHelper
import com.cookandroid.travelerapplication.helper.S3Uploader
import com.cookandroid.travelerapplication.task.InsertData_Chat
import com.cookandroid.travelerapplication.task.InsertData_ChatRoom
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter


class ChatRoomActivity : AppCompatActivity(), View.OnClickListener, S3Uploader.OnUploadListener {

    private var IS_IMAGE = false;


    private val REQUEST_CODE_PERMISSION = 100
    private val REQUEST_CODE_IMAGE = 200

    private var mImageUrl = ""

    val TAG = ChatRoomActivity::class.java.simpleName

    lateinit var binding: ActivityChatroomBinding

    lateinit var mSocket: Socket
    lateinit var userName: String
    lateinit var roomName: String
    lateinit var IP_ADDRESS: String
    lateinit var user_id: String
    lateinit var room_id: String

    val gson: Gson = Gson()

    //For setting the recyclerView.
    val chatList: ArrayList<Message> = arrayListOf()
    lateinit var chatRoomAdapter: ChatRoomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatroomBinding.inflate(layoutInflater)
        val view: View = binding.getRoot()
        setContentView(view)
        val fileHelper = FileHelper(this)
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS")
//        user_id = fileHelper.readFromFile("user_id") Todo: 만남글 업데이트 이후 이걸로 바꾸기
        user_id = "25"

        IP_ADDRESS = "3.34.98.95"

        // Todo: 만남글 중에 만남글 작성자의 id와 나의 id가 일치한 채팅방을 찾아보고 없으면 새로 만들기 (일단 if else가 있고 새로운 채팅을 할때라 생각. 아래는 채팅방 만들기)
        val meet_up_post_id = "1" // Todo: 만남글 업데이트 후 고치기 (만남글 만들면 자동 생성될듯)
        val meet_up_id = "1" // Todo: 만남글 업데이트 후 고치기 (만남글을 보고 수락을 누르면 생성될듯)
        val write_user_id = "1" // Todo: 만남글 업데이트 후 고치기 (만남글 만들 때 생성될듯)
        val request_user_id = user_id;
        val last_message = "" // 채팅을 처음하는 거니까 당연 없음
        val meet_up_status = "PENDING" // Todo: 만남글 업데이트 후 고치기 (만남글 만들면 자동 생성될듯)

        val insertdata_chat_room = InsertData_ChatRoom()
        insertdata_chat_room.execute(
            "http://" + IP_ADDRESS + "/0930/create_chat_room.php",
            meet_up_post_id,
            meet_up_id,
            write_user_id,
            request_user_id,
            last_message,
            meet_up_status
        )
        Handler().postDelayed({
            val withdraw_result: String = insertdata_chat_room.return_string
            roomName = withdraw_result
        }, 500) // 0.5초 지연 시간
        // Todo: (여기까지 if else로 가두기)

        binding.send.setOnClickListener(this)
        binding.leave.setOnClickListener(this)
        binding.image.setOnClickListener(this)

        //Todo: 만남글에서 user_Name(닉네임) 가지고 오기.
        try {
            userName = intent.getStringExtra("userName")!!
        } catch (e: Exception) {
            e.printStackTrace()
        }


        //Set Chatroom adapter

        chatRoomAdapter = ChatRoomAdapter(this, chatList)
        binding.recyclerView.adapter = chatRoomAdapter

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        //Let's connect to our Chat room! :D
        try {
            mSocket = IO.socket("http://" + IP_ADDRESS + ":3001")
            Log.d("success", mSocket.id())

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("fail", "Failed to connect")
        }

        mSocket.connect()
        mSocket.on(Socket.EVENT_CONNECT, onConnect)
        mSocket.on("newUserToChatRoom", onNewUser)
        mSocket.on("updateChat", onUpdateChat)
        mSocket.on("userLeftChatRoom", onUserLeft)


        requestPermissions()
    }


    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_PERMISSION
            )
        }
    }

    private fun requestImageUpload() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                val imageUri = data.data
                imageUri?.let { uploadImage(it) }
            }
        }
    }

    private fun uploadImage(imageUri: Uri) {
        val s3Uploader = S3Uploader(this)
        s3Uploader.uploadImage(imageUri, this)
    }

    var onUserLeft = Emitter.Listener {
        val leftUserName = it[0] as String
        val chat: Message = Message(leftUserName, "", "", MessageType.USER_LEAVE.index, IS_IMAGE)
        addItemToRecyclerView(chat)
    }

    var onUpdateChat = Emitter.Listener {
        val chat: Message = gson.fromJson(it[0].toString(), Message::class.java)
        Log.d("youn12345", chat.isImage.toString())
        IS_IMAGE = chat.isImage;
        if(IS_IMAGE == true){
            chat.viewType = MessageType.IMAGE_PARTNER.index
        }else{
            chat.viewType = MessageType.CHAT_PARTNER.index
        }
        addItemToRecyclerView(chat)
    }

    var onConnect = Emitter.Listener {
        val data = initialData(userName, roomName)
        val jsonData = gson.toJson(data)
        mSocket.emit("subscribe", jsonData)

    }

    var onNewUser = Emitter.Listener {
        val name = it[0] as String //This pass the userName!
        val chat = Message(name, "", roomName, MessageType.USER_JOIN.index, IS_IMAGE);
        addItemToRecyclerView(chat)
        Log.d(TAG, "on New User triggered.")
    }


    private fun sendMessage() {
        val content = binding.editText.text.toString()
        val sendData = SendMessage(userName, content, roomName, if (IS_IMAGE) true else false)
        val jsonData = gson.toJson(sendData)
        mSocket.emit("newMessage", jsonData)
        if(IS_IMAGE == true){
            val message = Message(userName, content, roomName, MessageType.IMAGE_MINE.index, IS_IMAGE);
            addItemToRecyclerView(message)
        }else{
            val message = Message(userName, content, roomName, MessageType.CHAT_MINE.index, IS_IMAGE);
            addItemToRecyclerView(message)
        }

        val room_id = roomName // Todo: 만남글 업데이트 후 고치기 (만남글을 보고 수락을 누르면 생성될듯)
        val message = content
        val send_user_id = user_id
        val receive_user_id = "1" // Todo: 만남글 업데이트 후 고치기 (만남글을 보면 상대방이 누군지 알아서 적을 수 있을듯)
        val is_image = IS_IMAGE

        val insertdata_chat = InsertData_Chat()
        insertdata_chat.execute(
            "http://" + IP_ADDRESS + "/0930/save_chat.php",
            room_id,
            message,
            send_user_id,
            receive_user_id,
            is_image.toString()
        )
    }

    private fun addItemToRecyclerView(message: Message) {

        //Since this function is inside of the listener,
        // You need to do it on UIThread!
        runOnUiThread {
            chatList.add(message)
            chatRoomAdapter.notifyItemInserted(chatList.size)
            binding.editText.setText("")
            binding.recyclerView.scrollToPosition(chatList.size - 1) //move focus on last message
        }
    }


    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.send -> sendMessage()
            R.id.leave -> onDestroy()
            R.id.image -> requestImageUpload()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::userName.isInitialized && ::roomName.isInitialized) {
            val data = initialData(userName, roomName)
            val jsonData = gson.toJson(data)
            mSocket.emit("unsubscribe", jsonData)
            mSocket.disconnect()
        }
    }

    override fun onProgress(progress: Int) {}

    override fun onSuccess(
        imageUrl: String,
        fileSize: String?,
        originalFileName: String?,
        storedFileName: String?
    ) {
        mImageUrl = imageUrl
        Toast.makeText(this, "이미지 업로드 성공: $imageUrl", Toast.LENGTH_SHORT).show()
        binding.editText.setText(mImageUrl);
        IS_IMAGE = true;
        sendMessage();
        IS_IMAGE = false;

        // Refresh()
    }

    override fun onFailure() {}

}
