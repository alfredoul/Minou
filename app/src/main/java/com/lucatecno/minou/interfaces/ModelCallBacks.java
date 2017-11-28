package com.lucatecno.minou.interfaces;

import com.lucatecno.minou.models.ChatPojo;

import java.util.ArrayList;


/**
 * Created by saksham on 26/6/17.
 */

public interface ModelCallBacks {
    void onModelUpdated(ArrayList<ChatPojo> messages);
}
