/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudpresenter.network;

import cloudpresenter.model.Group;
import cloudpresenter.servers.ImageReciever;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nawanjana
 */
public class RecieveUDPMulticastPacket extends Thread {

    public final static int openedPort = 6066;
    static byte[] buffer = new byte[65535];
    private static MulticastSocket ms;

    public RecieveUDPMulticastPacket() {
        try {
            ms = new MulticastSocket(openedPort);
        } catch (IOException ex) {
            Logger.getLogger(RecieveUDPMulticastPacket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void joinGroup(Group group) {
        try {
            InetAddress multicastgroup = InetAddress.getByName(group.getMcastIP());
            ms.joinGroup(multicastgroup);
        } catch (SocketException ex) {
            System.err.println(ex);
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public void leaveGroup(Group group) {
        try {
            InetAddress multicastgroup = InetAddress.getByName(group.getMcastIP());
            ms.leaveGroup(multicastgroup);
        } catch (SocketException ex) {
            System.err.println(ex);
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    @Override
    public void run() {

        while (true) {
            try {
                buffer = new byte[65535];
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                ms.receive(dp);
                ImageReciever ir = new ImageReciever(dp.getData(), dp.getAddress());
                ir.start();
            } catch (IOException ex) {
                System.err.println(ex);
                Logger.getLogger(ImageReciever.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
//    public static void main(String[] args) {
//        new RecieveUDPPacket().start();
//    }
}
