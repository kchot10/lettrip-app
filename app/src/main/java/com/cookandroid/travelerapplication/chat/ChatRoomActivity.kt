package com.cookandroid.travelerapplication.chat;

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.cookandroid.travelerapplication.R
import com.cookandroid.travelerapplication.chat.model.MessageType
import com.cookandroid.travelerapplication.databinding.ActivityChatroomBinding
import com.cookandroid.travelerapplication.helper.FileHelper
import com.cookandroid.travelerapplication.helper.GMailSender
import com.cookandroid.travelerapplication.helper.S3Uploader
import com.cookandroid.travelerapplication.meetup.MeetupPostDetailActivity
import com.cookandroid.travelerapplication.task.InsertData_Chat
import com.cookandroid.travelerapplication.task.InsertData_ChatRoom
import com.cookandroid.travelerapplication.task.InsertData_Meetup
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter


class ChatRoomActivity : AppCompatActivity(), View.OnClickListener, S3Uploader.OnUploadListener,
    InsertData_Meetup.AsyncTaskCompleteListener, InsertData_ChatRoom.AsyncTaskCompleteListener {

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
    lateinit var meet_up_date: String
    lateinit var code: String

    val gson: Gson = Gson()

    //For setting the recyclerView.
    val chatList: ArrayList<Message> = arrayListOf()
    lateinit var chatRoomAdapter: ChatRoomAdapter

    lateinit var meet_up_post_id: String
    lateinit var meet_up_id: String
    lateinit var  write_user_id: String
    lateinit var  request_user_id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatroomBinding.inflate(layoutInflater)
        val view: View = binding.getRoot()
        setContentView(view)
        val fileHelper = FileHelper(this)
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS")
//        IP_ADDRESS = "13.125.232.136"
        user_id = fileHelper.readFromFile("user_id");
//        user_id = "25";
        meet_up_id = "-1"
        meet_up_date = "";

        userName = fileHelper.readFromFile("nickname");
        if(intent.getBooleanExtra("first_chat", false)){
            meet_up_post_id = intent.getStringExtra("meet_up_post_id")!!
            write_user_id = user_id;
            request_user_id = intent.getStringExtra("request_user_id")!! // 수락 누르고 들어오면 요청한 사람
        }else{
            meet_up_post_id = intent.getStringExtra("meet_up_post_id")!!
            write_user_id = intent.getStringExtra("write_user_id")!!;
            request_user_id = user_id; // 채팅 누르고 들어오면 자기 자신 Todo: 이전 Activty에서 상대방 넘겨주기
            //Todo: 채팅 불러오기
        }
        binding.send.setOnClickListener(this)
        binding.leave.setOnClickListener(this)
        binding.image.setOnClickListener(this)
        binding.promise.setOnClickListener(this)

        Log.d("erros", "write_user_id:"+write_user_id+" request_user_id:"+request_user_id);

        val insertdata_chat_room = InsertData_ChatRoom(this)
        insertdata_chat_room.execute(
            "http://" + IP_ADDRESS + "/0930/create_chat_room.php",
            meet_up_post_id,
            meet_up_id,
            write_user_id,
            request_user_id
        )

        //Todo: select를 통해 MeetUp에 만남이 있는지 확인 후 meet_up_date 수정 (현재는 미구현으로 if로 대체)



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

    private fun requestMeetUpUpload() {
        if(meet_up_date.equals("")){
            showDialog()
        }else {
            showMeetUp()
        }
    }

    private fun showMeetUp() {
        val builder = AlertDialog.Builder(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.fragment_authentication, null)
        builder.setView(dialogView)

        val layout1 = dialogView.findViewById<LinearLayout>(R.id.layout1)
        val textView = dialogView.findViewById<TextView>(R.id.textView)
        val authSend = dialogView.findViewById<Button>(R.id.authSend)
        val editText = dialogView.findViewById<EditText>(R.id.editText)
        val auth = dialogView.findViewById<Button>(R.id.auth)

        code = ""
        auth.visibility = View.INVISIBLE
        editText.visibility = View.INVISIBLE

        authSend.setOnClickListener{
            code = GMailSender.createEmailCode()
            textView.setText(code)
            authSend.visibility = View.INVISIBLE
            auth.visibility = View.VISIBLE
        }

        auth.setOnClickListener{
            //Todo: update를 통해 resultOK라면 인증 성공
        }

        if(true){ // Todo: 몽고디비에 새로운 커넥션 만들어서 거기에서 만남 id를 통해 인증할게 있는지 확인
            // Todo: 있으면 번호를 입력하면됨
        }else{
            // Todo: 없으면 번호 받고 대기
        }

        val dialog = builder.create()
        dialog.show()
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
        val receive_user_id = write_user_id // Todo: 만남글 업데이트 후 고치기 (만남글을 보면 상대방이 누군지 알아서 적을 수 있을듯)
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
            R.id.promise -> requestMeetUpUpload();
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
    override fun onTaskComplete(result_string: String) {
        runOnUiThread {
            meet_up_id = result_string;

            val promise_text = userName+ " 님이 약속을 등록했습니다.\n"+
                    MeetupPostDetailActivity.reformatDate(meet_up_date,"yyyy-MM-dd HH:mm:ss","'날짜:' yyyy-MM-dd (E)\n'시간:' hh'시' mm'분'")
            binding.editText.setText(promise_text)
            IS_IMAGE = false;
            sendMessage()

            promiseRefresh()
        }
    }

    private fun promiseRefresh() {
        if(!meet_up_date.equals("")){
            binding.promise
        }
    }

    override fun onTaskComplete_InsertData_ChatRoom(result_string: String) {
        runOnUiThread {
            roomName = result_string

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

        }


    }
    fun showDialog() {
        val builder = AlertDialog.Builder(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.time_picker_layout, null)
        builder.setView(dialogView)


        val datePicker = dialogView.findViewById<DatePicker>(R.id.datePicker)
        val timePicker = dialogView.findViewById<TimePicker>(R.id.timePicker)

        builder.setPositiveButton("확인") { dialog, which ->
            val meet_up_post_id = intent.getStringExtra("meet_up_post_id")!!
            val request_user_id = if (intent.getStringExtra("request_user_id") == null) {
                user_id
            } else {
                intent.getStringExtra("request_user_id")
            }
            val write_user_id = if (intent.getStringExtra("write_user_id") == null) {
                user_id
            } else {
                intent.getStringExtra("write_user_id")
            }
            val selectedDate = "${datePicker.year}-${datePicker.month + 1}-${datePicker.dayOfMonth}"
            val selectedTime = "${String.format("%02d", timePicker.currentHour)}:${String.format("%02d", timePicker.currentMinute)}:00"
            meet_up_date = "$selectedDate $selectedTime"

            if(meet_up_post_id.isNullOrEmpty() || request_user_id.isNullOrEmpty() || write_user_id.isNullOrEmpty() || meet_up_date.isNullOrEmpty()){
                Log.e("errors","showDialog 에러 발생")
            }else{
                val insertdataMeetup = InsertData_Meetup(this)
                insertdataMeetup.execute("http://" + IP_ADDRESS + "/1028/InsertData_Meetup.php",meet_up_post_id,write_user_id,request_user_id, meet_up_date)
            }

            Toast.makeText(this, meet_up_date, Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        builder.setNegativeButton("취소") { dialog, which ->
            // 다이얼로그 취소 버튼을 클릭한 경우 수행할 작업을 여기에 추가하세요.
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }
}
