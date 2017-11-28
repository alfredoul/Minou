package com.lucatecno.minou.views;

import com.lucatecno.minou.models.ChatPojo;

import java.util.ArrayList;


/**
 * Created by saksham on 26/6/17.
 */

public interface IChatView {
    void updateList(ArrayList<ChatPojo> list);
    void clearEditText();
}
