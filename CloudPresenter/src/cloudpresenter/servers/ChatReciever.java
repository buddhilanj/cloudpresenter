/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cloudpresenter.servers;

import cloudpresenter.controller.ChatManager;
import cloudpresenter.controller.DeviceManager;
import cloudpresenter.gui.ChatScreen;
import cloudpresenter.notification.NotifyUI;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Nawanjana
 */
public class ChatReciever extends Thread {
    public final static int chattingport = 6062;

    @Override
    public void run() {
        ServerSocket theServer;
        Socket theConnection = null;
        try {
            theServer = new ServerSocket(chattingport);
            try {
                while (true) {
                    theConnection = theServer.accept();
                    DataInputStream dIn = new DataInputStream(theConnection.getInputStream());
                    String incoming = dIn.readUTF();
                    ChatScreen relevant = ChatManager.getStreamByIP(theConnection.getInetAddress());
                    if (relevant == null){
                        relevant = new ChatScreen(DeviceManager.getDevice(theConnection.getInetAddress().getHostName()));
                        ChatManager.addNewChatStream(relevant);
                    }
                    if(!relevant.isVisible()){
                        new NotifyUI(incoming, incoming, relevant, incoming);
                        //relevant.setVisible(true);
                        //System.out.println("Set Visibled");
                    }
                    relevant.inputChat(incoming);
                    theConnection.close();
                }
            } catch (IOException ex) {
                theConnection.close();
                System.err.println(ex);
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }


}
