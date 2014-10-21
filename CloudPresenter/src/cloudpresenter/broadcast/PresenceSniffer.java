/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudpresenter.broadcast;

import cloudpresenter.controller.DeviceManager;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author Nawanjana Bandara <Co-founder at Cluztech>
 */
public class PresenceSniffer extends Thread {

    private InetAddress localdevice;
    private int port = 0;
    private InetAddress multicastgroup;
    private MulticastSocket ms;
    private byte[] buffer = new byte[65509];

    public PresenceSniffer(int port) throws UnknownHostException {
        localdevice = InetAddress.getLocalHost();
        this.port = port;
        multicastgroup = InetAddress.getByName("224.0.0.1");
        try {
            ms = new MulticastSocket(port);
            ms.joinGroup(multicastgroup);
        } catch (SocketException ex) {
            System.err.println(ex);
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                buffer = new byte[65509];
                System.gc();
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                ms.receive(dp);
                String s = new String(dp.getData());
                //System.out.println(s);
                DeviceManager.detectPresence(s.trim(), dp.getAddress().getHostAddress());
            }
        } catch (SocketException ex) {
            System.err.println(ex);
        } catch (IOException ex) {
            System.err.println(ex);
        }
//        
    }
}
