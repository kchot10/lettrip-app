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
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.cookandroid.travelerapplication.R
import com.cookandroid.travelerapplication.chat.model.MessageType
import com.cookandroid.travelerapplication.databinding.ActivityChatroomBinding
import com.cookandroid.travelerapplication.helper.FileHelper
import com.cookandroid.travelerapplication.helper.GMailSender
import com.cookandroid.travelerapplication.helper.S3Uploader
import com.cookandroid.travelerapplication.meetup.MeetUp
import com.cookandroid.travelerapplication.meetup.MeetupPost
import com.cookandroid.travelerapplication.meetup.MeetupPostDetailActivity
import com.cookandroid.travelerapplication.search.SearchReviewActivity
import com.cookandroid.travelerapplication.task.*
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter


class ChatRoomActivity : AppCompatActivity(), View.OnClickListener, S3Uploader.OnUploadListener, SelectData_MeetUpOne.AsyncTaskCompleteListener, SelectData_MeetUpPostOne.AsyncTaskCompleteListener,
    InsertData_Meetup.AsyncTaskCompleteListener, InsertData_ChatRoom.AsyncTaskCompleteListener,InsertData_Auth.AsyncTaskCompleteListener, SelectData_MeetUp.AsyncTaskCompleteListener, SelectData_Auth.AsyncTaskCompleteListener, SelectData_Chat.AsyncTaskCompleteListener {

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
    lateinit var editText: EditText
    lateinit var layout1 :LinearLayout
    lateinit var auth :Button
    lateinit var dialog :AlertDialog
    lateinit var meet_up: MeetUp
    lateinit var performer_id: String
    lateinit var your_nickname: String
    lateinit var real_meet_up: MeetUp

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
        user_id = fileHelper.readFromFile("user_id")
        userName = fileHelper.readFromFile("my_nickname")


        meet_up_id = "-1"
        meet_up_date = "";
        performer_id = "-1"

        binding.imageView.setClipToOutline(true)
        Glide.with(applicationContext)
            .load(intent.getStringExtra("image_url")!!)
            .into(binding.imageView)
        your_nickname = intent.getStringExtra("nickname")!!;
        meet_up_id = intent.getStringExtra("meet_up_id")!!
        binding.partnerName.text = your_nickname;
        if(intent.getBooleanExtra("first_chat", false)){
            meet_up_post_id = intent.getStringExtra("meet_up_post_id")!!
            write_user_id = user_id;
            request_user_id = intent.getStringExtra("request_user_id")!! // 수락 누르고 들어오면 요청한 사람
        }else{
            meet_up_post_id = intent.getStringExtra("meet_up_post_id")!!
            write_user_id = intent.getStringExtra("write_user_id")!!;
            request_user_id = intent.getStringExtra("request_user_id")!!;
            room_id = intent.getStringExtra("room_id")!!
        }
        real_meet_up = MeetUp() // 오류처리 방지를 위한 선언
        meetUpRequest()
        meetUpPostRequest()




        binding.send.setOnClickListener(this)
        binding.leave.setOnClickListener(this)
        binding.image.setOnClickListener(this)
        binding.promise.setOnClickListener(this)
        // leave 이미지 버튼 클릭 시 이전 액티비티로 돌아가기
        binding.leave.setOnClickListener {
            onBackPressed()
        }

        val plusBtn = findViewById<ImageButton>(R.id.plusBtn)
        plusBtn.setOnClickListener {
            toggleExpandLayout()
        }

        val insertdata_chat_room = InsertData_ChatRoom(this)
        insertdata_chat_room.execute(
            "http://" + IP_ADDRESS + "/0930/create_chat_room.php",
            meet_up_post_id,
            meet_up_id,
            write_user_id,
            request_user_id
        )


        //인증버튼
        val certificationBtn = findViewById<ImageView>(R.id.review)
        certificationBtn.setOnClickListener {
            meetUpRequest()
            if(real_meet_up.meet_up_id.isNullOrEmpty() || real_meet_up.meet_up_status.equals("PENDING")){
                Toast.makeText(this, "만남을 성공하시거나 취소하신 후 리뷰를 작성해주세요.", Toast.LENGTH_SHORT).show()
            }else{
                // 성공했거나 실패했을 경우
                showCertificationPopup()
            }
        }



        requestPermissions()
    }

    private fun meetUpRequest() {
        val selectdata_meetup_one = SelectData_MeetUpOne(this)
        selectdata_meetup_one.execute(
            "http://" + IP_ADDRESS + "/1107/SelectData_MeetUpOne.php",
            meet_up_post_id
        )
    }


    private fun meetUpPostRequest() {
        val selectdata_meetup_post_one = SelectData_MeetUpPostOne(this)
        selectdata_meetup_post_one.execute(
            "http://" + IP_ADDRESS + "/1107/SelectData_MeetUpPostOne.php",
            meet_up_post_id
        )
    }


    //후기 작성 팝업 띄우기
    private fun showCertificationPopup() {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.popup_one_line_review, null)
        builder.setView(dialogView)

        val editText = dialogView.findViewById<EditText>(R.id.oneLineReviewMessage)
        val completeBtn = dialogView.findViewById<Button>(R.id.addOneLineReviewBtn)

        val dialog = builder.create()

        val layoutParams = WindowManager.LayoutParams()
        val displayMetrics = resources.displayMetrics
        val dialogWidth = (displayMetrics.widthPixels * 0.5).toInt() // 화면 너비의 90%

        // 배경을 투명하게 설정
        layoutParams.copyFrom(dialog.window!!.attributes)
        layoutParams.dimAmount = 0f // 0으로 설정하면 완전 투명, 1로 설정하면 완전 불투명
        dialog.window!!.attributes = layoutParams
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        completeBtn.setOnClickListener {
            val userInput = editText.text.toString() // 사용자가 입력한 내용을 가져옴
            if(userInput.length <= 5){
                Toast.makeText(this, "다섯 글자 이상 입력해주세요", Toast.LENGTH_SHORT).show()
            }else {
                val object_user_id = if (user_id.equals(write_user_id)) {
                    request_user_id
                } else {
                    write_user_id
                }
                val insertdata_meet_up_review = InsertData_Meet_Up_Review();
                insertdata_meet_up_review.execute(
                    "http://" + IP_ADDRESS + "/1107/InsertData_Meet_Up_Review.php",
                    userInput,
                    real_meet_up.meet_up_status,
                    real_meet_up.meet_up_id,
                    object_user_id,
                    user_id
                )
                dialog.dismiss() // 팝업 닫기
            }
        }

        // 팝업 외부를 클릭하여 닫을 수 있도록 설정
        builder.setCancelable(true)

        dialog.show()
    }

    private fun dpToPx(dp: Int): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }


    //확장레이아웃 가시성 변경
    private fun toggleExpandLayout() {
        val expandLayout = findViewById<LinearLayout>(R.id.expandLayout)
        if (expandLayout.visibility == View.VISIBLE) {
            expandLayout.visibility = View.GONE
        } else {
            expandLayout.visibility = View.VISIBLE
        }
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
        // 약속 잡기 버튼을 누르면
        // 1. 밋업이 있는지 확인해야함

        val selectdata_meetup = SelectData_MeetUp(this)
        selectdata_meetup.execute(
            "http://" + IP_ADDRESS + "/1028/SelectData_MeetUp.php",
            meet_up_post_id
        )
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
                binding.planTextView.text = "만남 인증"
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
    private fun showMeetUp() {
        val selectdata_auth = SelectData_Auth(this)
        selectdata_auth.execute(
            "http://" + IP_ADDRESS + "/0930/select_auth.php",
            meet_up_id
        )

        val builder = AlertDialog.Builder(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.fragment_authentication, null)
        builder.setView(dialogView)

        layout1 = dialogView.findViewById<LinearLayout>(R.id.layout1)
        val textView = dialogView.findViewById<TextView>(R.id.textView)
        val authSend = dialogView.findViewById<Button>(R.id.authSend)
        editText = dialogView.findViewById<EditText>(R.id.editText)
        auth = dialogView.findViewById<Button>(R.id.auth)

        val request_user_id = if (intent.getStringExtra("request_user_id") == null) { user_id } else { intent.getStringExtra("request_user_id") }
        val write_user_id = if (intent.getStringExtra("write_user_id") == null) { user_id } else { intent.getStringExtra("write_user_id") }
        val aibo_id = if (request_user_id.equals("user_id")) {write_user_id} else{request_user_id}
        auth.visibility = View.INVISIBLE
        editText.visibility = View.INVISIBLE

        authSend.setOnClickListener{
            code = GMailSender.createEmailCode()
            textView.setText(code)
            authSend.visibility = View.INVISIBLE
            auth.visibility = View.VISIBLE

            val insertdata_auth = InsertData_Auth(this)
            insertdata_auth.execute(
                "http://" + IP_ADDRESS + "/0930/insert_auth2.php",
                meet_up_id,
                user_id,
                aibo_id,
                code
            )
        }

        auth.setOnClickListener{

            if(!performer_id.equals(user_id)){
                    val selectdata_meetup = SelectData_MeetUp(this)
                    selectdata_meetup.execute(
                        "http://" + IP_ADDRESS + "/1028/SelectData_MeetUp.php",
                        meet_up_post_id
                    )

            }else{
                if (code.equals(editText.text.toString())) {
                    val updateData_meetUp = UpdateData_MeetUp();
                    updateData_meetUp.execute(
                        "http://" + IP_ADDRESS + "/1028/UpdateData_MeetUp.php",
                        meet_up_id, "COMPLETED"
                    );
                    val updatedata_meetup_mongo = UpdateData_MeetUp_Mongo()
                    updatedata_meetup_mongo.execute("http://" + IP_ADDRESS + "/1107/Update_MeetUp_Mongo.php",meet_up_post_id,meet_up_id,"COMPLETED")
                    Toast.makeText(this, "인증이 완료되었습니다. 상대의 인증하기도 눌러주세요", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "코드가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            // 만약 밋업의 status로 확인을 하고싶다면 COMPLETE인지 확인하기 위해 select meet_up이 필요함
            // 그러나 여기에서 수행해야하는 것은 requester와 perfomer의 인증이 모두 필요한 상황
            // requester의 경우 요청을 했을 때 select meet_up을 통해 COMPLETE인지 확인
            // performer의 경우 바로 자신의 editText의 값을 불러와서 인증코드와 비교한 후 일치하다면 COMPLTET로 변경
            // performer

//            if(code == "sender"){
//                code = "sender"
//            }else {
//                code = editText.text.toString();
//            }
//            val insertdata_auth = InsertData_Auth(this)
//            insertdata_auth.execute(
//                "http://" + IP_ADDRESS + "/0930/insert_auth2.php",
//                meet_up_id,
//                request_user_id,
//                write_user_id,
//                code
//            )
        }

        dialog = builder.create()
        dialog.show()

    }

    override fun onTaskComplete_SelectData_MeetUp(result: MeetUp) {


        runOnUiThread{
            if(result.meet_up_id.isNullOrEmpty()){
                if(user_id.equals(write_user_id)) {
                    Toast.makeText(this, "약속이 없습니다. 약속 만들어 만남을 가지세요!", Toast.LENGTH_SHORT).show()
                    showDialog();
                }else{
                    Toast.makeText(this, "약속은 포스트 작성자만 만들 수 있습니다.", Toast.LENGTH_SHORT).show()
                }
            }else{
                if(result.meet_up_status.equals("COMPLETED")){
                    binding.promise.isEnabled = false;
                    binding.planTextView.text = "만남 완료"
                }
                else {
                    Toast.makeText(this, "약속이 있습니다. 인증을 진행하세요", Toast.LENGTH_SHORT).show()
                    showMeetUp();
                }
            }
        }

        try{
            meet_up = result
            meet_up_id = meet_up.meet_up_id
        }catch (e:Exception){
            Log.e("errors", "밋업 불러오기 실패 = 밋업 없음");
        }
    }
    override fun onTaskComplete_SelectData_Auth(result_string: String) {

        runOnUiThread{
            if(result_string.equals("AUTH_EMPTY")){
                layout1.visibility = View.VISIBLE
                editText.visibility = View.INVISIBLE
                auth.visibility = View.INVISIBLE
            }else{
                val parts = result_string.split(" ", limit = 2)
                code = parts[0]
                performer_id = parts[1]
                layout1.visibility = View.INVISIBLE
                editText.visibility = View.VISIBLE
                auth.visibility = View.VISIBLE
            }
        }

//        if(!performer_id.equals(user_id)) {
//            //requester
//            val selectData_meetUp = SelectData_MeetUp(this);
//            selectData_meetUp.execute();
//            Handler().postDelayed({
//                if (meet_up.meet_up_status.equals("COMPLETE")) {
//                    Toast.makeText(this, "인증이 완료되었습니다.", Toast.LENGTH_SHORT).show()
//                    dialog.dismiss()
//                } else {
//                    Toast.makeText(this, "인증이 완료되지 않았습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            }, 1000) // 0.5초 지연 시간
//        }else{
//            if (code.equals(editText.text.toString())) {
//                val updateData_meetUp = UpdateData_MeetUp();
//                updateData_meetUp.execute(
//                    "http://" + IP_ADDRESS + "/1028/Update_MeetUp.php",
//                    meet_up_id, "PENDING"
//                );
//                Toast.makeText(this, "인증이 완료되었습니다. 상대의 인증하기도 눌러주세요", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this, "코드가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
//            }
//        }
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

        val room_id = roomName
        val message = content
        val send_user_id = user_id
        val receive_user_id = if(write_user_id.equals(user_id)) {request_user_id} else {write_user_id}
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
        val updatedata_meetup_mongo = UpdateData_MeetUp_Mongo()
        updatedata_meetup_mongo.execute("http://" + IP_ADDRESS + "/1107/Update_MeetUp_Mongo.php",real_meet_up.meet_up_post_id,real_meet_up.meet_up_id,"PENDING")
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

            try{
                val selectData_chat = SelectData_Chat(this)
                selectData_chat.execute(
                    "http://" + IP_ADDRESS + "/0930/select_chat.php",
                    room_id.toString()
                )
            }catch(e:Exception){
                Log.e("good","첫 채팅이라서 오류남")
            }

        }


    }

    override fun onTaskComplete_InsertData_Auth(result_string: String) {
    }

    override fun onTaskComplete_SelectData_Chat(result: java.util.ArrayList<Chat>) {
        runOnUiThread{
            result.forEach { chat ->
//                Log.e("errors", user_id+" ");
                val viewType = if (user_id.equals(chat.getSend_user_id().toString()) && chat.is_image == false) {
                    MessageType.CHAT_MINE.index
                } else if(user_id.equals(chat.getSend_user_id().toString()) && chat.is_image == true){
                    MessageType.IMAGE_MINE.index
                } else if(!user_id.equals(chat.getSend_user_id().toString()) && chat.is_image == false){
                    MessageType.CHAT_PARTNER.index
                } else{
                    MessageType.IMAGE_PARTNER.index
                }

                val nickname = if (user_id.equals(chat.getSend_user_id().toString())) {
                    userName
                } else{
                    your_nickname
                }


//                Log.e("errors", viewType.toString()+" "+ (chat.getSend_user_id().equals(user_id)).toString + (chat.is_image == false).toString());
                val newMessage = Message(nickname, chat.getMessage(), chat.getRoom_id(), viewType, chat.getIs_image())
                addItemToRecyclerView(newMessage)
            }

        }

    }

    override fun onTaskComplete_SelectData_MeetUpOne(result: MeetUp) {
        if(result.meet_up_id != null){
            real_meet_up = result;
        }
    }

    override fun onTaskComplete_SelectData_MeetUpPostOne(result: MeetupPost) {
//        Log.e("errors", result.meet_up_id+result.created_date+result.postTitle);
        runOnUiThread{
            binding.postTitle.text = result.postTitle;
            binding.city1.text = result.province;
            binding.city2.text = result.city;
            binding.postBtn.setOnClickListener{
                val intent: Intent
                intent = Intent(this, MeetupPostDetailActivity::class.java)
                intent.putExtra("meetup_post", result) // MeetupPost 클래스 형태로 보내는건 오류
                startActivity(intent)
            }
        }

    }
}


