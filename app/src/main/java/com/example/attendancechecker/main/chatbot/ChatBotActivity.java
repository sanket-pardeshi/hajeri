package com.example.attendancechecker.main.chatbot;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import com.example.attendancechecker.R;
import com.example.attendancechecker.main.AppBase;
import com.example.attendancechecker.main.database.DatabaseHandler;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Locale;

public class ChatBotActivity extends AppCompatActivity {

    private EditText userMsgEdt;
    private final String USER_KEY = "user";
    private final String BOT_KEY = "bot";
    DatabaseHandler handler = AppBase.handler;
    private final String BOT_KEY_CLICK = "bot_click";
    //creating a variable for our volley request queue.
    private RequestQueue mRequestQueue;
    //creating a variable for array list and adapter class.
    private ArrayList<MessageModal> messageModalArrayList;
    private MessageRVAdapter messageRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);
        //on below line we are initializing all our views.
        //creating variables for our widgets in xml file.
        RecyclerView chatsRV = findViewById(R.id.idRVChats);
        ImageButton sendMsgIB = findViewById(R.id.idIBSend);
        userMsgEdt = findViewById(R.id.idEdtMessage);
        //below line is to initialize our request queue.
        mRequestQueue = Volley.newRequestQueue(ChatBotActivity.this);
        mRequestQueue.getCache().clear();
        //creating a new array list
        messageModalArrayList = new ArrayList<>();
        //adding on click listener for send message button.
        sendMsgIB.setOnClickListener(v -> {
            //checking if the message entered by user is empty or not.
            if (userMsgEdt.getText().toString().isEmpty()) {
                //if the edit text is empty display a toast message.
                Toast.makeText(ChatBotActivity.this, "Please enter your message..", Toast.LENGTH_SHORT).show();
                return;
            }
            //calling a method to send message to our bot to get response.
            String msg = userMsgEdt.getText().toString().toLowerCase(Locale.ROOT);
            sendMessage(msg);
            //below line we are setting text in our edit text as empty
            userMsgEdt.setText("");

        });

        //on below line we are initialiing our adapter class and passing our array lit to it.
        messageRVAdapter = new MessageRVAdapter(messageModalArrayList, this);
        //below line we are creating a variable for our linear layout manager.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatBotActivity.this, RecyclerView.VERTICAL, false);
        //below line is to set layout manager to our recycler view.
        chatsRV.setLayoutManager(linearLayoutManager);
        //below line we are setting adapter to our recycler view.
        chatsRV.setAdapter(messageRVAdapter);
    }

    private void chatBot(String userMsg) {
        if ((userMsg.contains("edit profile") && !userMsg.equals("how to edit profile"))) {
            messageModalArrayList.add(new MessageModal("Did you mean : How to edit profile, if yes, type 'how to edit profile'", BOT_KEY_CLICK));
        } else if ((userMsg.contains("search profile") && !userMsg.equals("how to search profile"))) {
            messageModalArrayList.add(new MessageModal("Did you mean : How to search profile, if yes, type 'how to search profile'", BOT_KEY_CLICK));
        } else if ((userMsg.contains("create profile") && !userMsg.equals("how to create profile"))) {
            messageModalArrayList.add(new MessageModal("Did you mean : How to create profile, if yes, type 'how to create profile'", BOT_KEY_CLICK));
        } else if ((userMsg.contains("notes") && !userMsg.equals("how to create notes"))) {
            messageModalArrayList.add(new MessageModal("Did you mean : How to create notes, if yes, type 'how to create notes'", BOT_KEY_CLICK));
        } else if ((userMsg.contains("schedule") && !userMsg.equals("how to schedule lecture"))) {
            messageModalArrayList.add(new MessageModal("Did you mean : How to schedule lecture, if yes, type 'how to schedule lecture'", BOT_KEY_CLICK));
        } else if ((userMsg.contains("attendance") && !userMsg.equals("how to take attendance"))) {
            messageModalArrayList.add(new MessageModal("Did you mean : How to take attendance, if yes, type 'how to take attendance'", BOT_KEY_CLICK));
        } else if ((userMsg.contains("clear") && !userMsg.equals("how to clear data"))) {
            messageModalArrayList.add(new MessageModal("Did you mean : How to clear data, if yes, type 'how to clear data'", BOT_KEY_CLICK));
        }
        else if(userMsg.contains("an:")){
            String qu = "SELECT * FROM STUDENT WHERE regno = '" + userMsg.substring(3).toUpperCase() + "'";
            Cursor cursor = handler.execQuery(qu);
            cursor.moveToFirst();
            String buffer = "";
            buffer += " " + cursor.getString(0) + "\n";
            buffer += " " + cursor.getString(1) + "\n";
            buffer += " " + cursor.getString(2) + "\n";
            buffer += " " + cursor.getString(3) + "\n";
            buffer += " " + cursor.getInt(4) + "\n";
            messageModalArrayList.add(new MessageModal(buffer, BOT_KEY));
            messageRVAdapter.notifyDataSetChanged();
        }
        else {
            messageModalArrayList.add(new MessageModal("I'm sorry , I'm not getting what you're trying to say, please ask me once again.", BOT_KEY_CLICK));
        }
//        if ((userMsg.contains("edit profile") && !userMsg.equals("how to edit profile"))) {
//            messageModalArrayList.add(new MessageModal("Did you mean : How to edit profile, if yes, type 'how to edit profile'", BOT_KEY));
//        } else if ((userMsg.contains("search profile") && !userMsg.equals("how to search profile"))) {
//            messageModalArrayList.add(new MessageModal("Did you mean : How to search profile, if yes, type 'an:Admission_Number'", BOT_KEY));
//        } else if ((userMsg.contains("create profile") && !userMsg.equals("how to create profile"))) {
//            messageModalArrayList.add(new MessageModal("Did you mean : How to create profile, if yes, type 'how to create profile'", BOT_KEY));
//        } else if ((userMsg.contains("notes") && !userMsg.equals("how to create notes"))) {
//            messageModalArrayList.add(new MessageModal("Did you mean : How to create notes, if yes, type 'how to create notes'", BOT_KEY));
//        } else if ((userMsg.contains("schedule") && !userMsg.equals("how to schedule lecture"))) {
//            messageModalArrayList.add(new MessageModal("Did you mean : How to schedule lecture, if yes, type 'how to schedule lecture'", BOT_KEY));
//        } else if ((userMsg.contains("attendance") && !userMsg.equals("how to take attendance"))) {
//            messageModalArrayList.add(new MessageModal("Did you mean : How to take attendance, if yes, type 'how to take attendance'", BOT_KEY));
//        } else if ((userMsg.contains("clear") && !userMsg.equals("how to clear data"))) {
//            messageModalArrayList.add(new MessageModal("Did you mean : How to clear data, if yes, type 'how to clear data'", BOT_KEY));
//        } else if(userMsg.contains("an:")){
//            String qu = "SELECT * FROM STUDENT WHERE regno = '" + userMsg.substring(3).toUpperCase() + "'";
//            Cursor cursor = handler.execQuery(qu);
//            cursor.moveToFirst();
//            String buffer = "";
//            buffer += " " + cursor.getString(0) + "\n";
//            buffer += " " + cursor.getString(1) + "\n";
//            buffer += " " + cursor.getString(2) + "\n";
//            buffer += " " + cursor.getString(3) + "\n";
//            buffer += " " + cursor.getInt(4) + "\n";
//            messageModalArrayList.add(new MessageModal(buffer, BOT_KEY));
//            messageRVAdapter.notifyDataSetChanged();
//        }else {
//            messageModalArrayList.add(new MessageModal("I'm sorry , I'm not getting what you're trying to say, please ask me once again.", BOT_KEY));
//        }
    }

    void sendMessage(String userMsg) {
        int flag=0;
        //below line is to pass message to our array list which is entered by the user.
        messageModalArrayList.add(new MessageModal(userMsg, USER_KEY));
        messageRVAdapter.notifyDataSetChanged();
        //url for our brain
        //make sure to add mshape for uid.
//        String url = "http://api.brainshop.ai/get?bid=170912&key=gbtb2CnOL7nHgwOB&uid=[uid]&msg=" + userMsg;
//        String url = "https://run.mocky.io/v3/560c65d4-17ea-4f23-8a9c-551ba24c734c";
        String url = "https://run.mocky.io/v3/b788b321-45bc-4295-b50b-dc8b0180b725";
//        System.out.println(url);
        //creating a variable for our request queue.
        RequestQueue queue = Volley.newRequestQueue(ChatBotActivity.this);
        //on below line we are making a json object request for a get request and passing our url .
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                //in on response method we are extracting data from json response and adding this response to our array list.
//                String botResponse = response.getString("cnt");
                String botResponse = response.getString(userMsg);
                System.out.println(botResponse);
                messageModalArrayList.add(new MessageModal(botResponse, BOT_KEY));
                //notifying our adapter as data changed.
                messageRVAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
                chatBot(userMsg);
                messageRVAdapter.notifyDataSetChanged();
                //handling error response from bot.
//                messageModalArrayList.add(new MessageModal("No response", BOT_KEY));
//                messageRVAdapter.notifyDataSetChanged();
            }

        },
                error -> {
                    //error handling.
                    messageModalArrayList.add(new MessageModal("Sorry no response found", BOT_KEY));
                    Toast.makeText(ChatBotActivity.this, "No response from the bot..", Toast.LENGTH_SHORT).show();
                }
        );

        //at last adding json object request to our queue.
        queue.add(jsonObjectRequest);
    }

}