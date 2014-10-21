/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudpresenter.network;

import cloudpresenter.servers.ImageReciever;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nawanjana
 */
public class RecieveUDPPacket extends Thread {

    public final static int openedPort = 6060;
    static byte[] buffer;

    public synchronized static void openLitsningServer() {
        try {
            DatagramSocket ds = new DatagramSocket(openedPort);

            while (true) {
                try {
                    buffer = new byte[65535];
                    DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                    ds.receive(dp);
                    ImageReciever ir = new ImageReciever(dp.getData(), dp.getAddress());
                    ir.start();
                    System.gc();
                } catch (IOException ex) {
                    System.err.println(ex);
                    Logger.getLogger(ImageReciever.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SocketException ex) {
            System.err.println(ex);
            Logger.getLogger(ImageReciever.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {
        openLitsningServer();
    }
//    public static void main(String[] args) {
//        new RecieveUDPPacket().start();
//    }
}
