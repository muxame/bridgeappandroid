package net.bridgeint.app.interfaces;

import net.bridgeint.app.models.ChatModel.Message;

/**
 * Created by DeviceBee on 8/23/2017.
 */

public interface SocketMessageListerner {
    void onGetMessageAck(Message message, int type);
}
