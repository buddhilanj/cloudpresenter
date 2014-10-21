/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudpresenter.servers;

import cloudpresenter.controller.DeviceManager;
import cloudpresenter.controller.MouseController;
import cloudpresenter.gui.MainScreen2;
import cloudpresenter.model.Device;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Nawanjana
 */
public class RemoteMouseReciever extends Thread {

    public final static int remotePort = 6065;

    @Override
    public void run() {
        ServerSocket theServer;
        Socket theConnection = null;
        try {
            theServer = new ServerSocket(remotePort);
            try {

                while (true) {
                    theConnection = theServer.accept();
                    Device device = DeviceManager.getDeviceByIP(theConnection.getInetAddress().getHostAddress());
                    if (MainScreen2.remote != null && MainScreen2.remote.equals(device)) {
                        DataInputStream dIn = new DataInputStream(theConnection.getInputStream());
                        String incoming = dIn.readUTF();
                        String[] data = incoming.split(":");
                        MouseController.getInstance().newUpdate(data);
                    }
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
