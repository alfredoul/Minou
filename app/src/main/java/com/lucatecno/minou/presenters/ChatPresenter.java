package com.lucatecno.minou.presenters;

import com.google.firebase.database.DataSnapshot;
import com.lucatecno.minou.interfaces.FirebaseCallBacks;
import com.lucatecno.minou.interfaces.ModelCallBacks;
import com.lucatecno.minou.models.ChatPojo;
import com.lucatecno.minou.models.MessageModel;
import com.lucatecno.minou.utils.FirebaseManager;
import com.lucatecno.minou.views.IChatView;

import java.util.ArrayList;


/**
 * Created by saksham on 26/6/17.
 */
public class ChatPresenter implements FirebaseCallBacks,ModelCallBacks {

    private IChatView mIChatView;
    private MessageModel mModel;

    public ChatPresenter(IChatView iChatView){
        this.mIChatView=iChatView;
        this.mModel=new MessageModel();
    }

    public void sendMessageToFirebase(String roomName, String message) {
        if (!message.trim().equals("")){
            FirebaseManager.getInstance(roomName,this).sendMessageToFirebase(message);
        }
        mIChatView.clearEditText();
    }

    public void setListener(String roomName) {
        FirebaseManager.getInstance(roomName,this).addMessageListeners();
    }

    public void onDestory(String roomName) {
        FirebaseManager.getInstance(roomName,this).removeListeners();
        FirebaseManager.getInstance(roomName,this).destroy();
        mIChatView=null;
    }

    @Override
    public void onNewMessage(DataSnapshot dataSnapshot) {
        mModel.addMessages(dataSnapshot,this);
    }

    @Override
    public void onModelUpdated(ArrayList<ChatPojo> messages) {
        if (messages.size()>0) {
            mIChatView.updateList(messages);
        }
    }
}
