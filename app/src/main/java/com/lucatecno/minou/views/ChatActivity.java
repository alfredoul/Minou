package com.lucatecno.minou.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.lucatecno.minou.R;
import com.lucatecno.minou.models.ChatPojo;
import com.lucatecno.minou.presenters.ChatPresenter;
import com.lucatecno.minou.utils.MyUtils;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener,IChatView {

    private EditText mEdittextChat;
    private ChatPresenter mChatPresenter;
    private RecyclerView mRecyclerView;
    private String mRoomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mRoomName=getIntent().getStringExtra(MyUtils.EXTRA_ROOM_NAME);
        mChatPresenter =new ChatPresenter(this);
        mChatPresenter.setListener(mRoomName);

        mEdittextChat=(EditText) findViewById(R.id.edittext_chat_message);
        mRecyclerView=(RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        findViewById(R.id.button_send_message).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_send_message:
                mChatPresenter.sendMessageToFirebase(mRoomName,mEdittextChat.getText().toString());
                break;
        }
    }

    @Override
    public void updateList(ArrayList<ChatPojo> list) {
        ChatAdapter chatAdapter=new ChatAdapter(this,list);
        mRecyclerView.setAdapter(chatAdapter);
        mRecyclerView.scrollToPosition(list.size()-1);
    }

    @Override
    public void clearEditText() {
        mEdittextChat.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mChatPresenter.onDestory(mRoomName);
    }
}
