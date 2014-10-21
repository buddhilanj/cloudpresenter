/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cloudpresenter.controller;

import cloudpresenter.gui.ChatScreen;
import cloudpresenter.model.Device;
import java.net.InetAddress;
import java.util.LinkedList;

/**
 *
 * @author Nawanjana
 */
public class ChatManager {
     public static LinkedList<ChatScreen> chats = new LinkedList<ChatScreen>();

     public static ChatScreen getStreamByIP(InetAddress ip) {
        ChatScreen relevant = null;
        for (ChatScreen chat : chats) {
            if (chat.getOtherEndIP().equals(ip.getHostAddress())) {
                relevant = chat;
            }
        }
        return relevant;

    }

    public static ChatScreen addNewChatStream(ChatScreen host){
        ChatScreen created = host;
        chats.add(created);
        return created;
    }
}
