/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudpresenter.broadcast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nawanjana Bandara <Co-founder at Cluztech>
 */
public class PresenseBroadcaster extends Thread {

    private InetAddress localdevice;
    private int port = 0;
    private InetAddress multicastgroup;
    private MulticastSocket ms;
    private boolean alive;

    public PresenseBroadcaster(int port) throws UnknownHostException {
        localdevice = InetAddress.getLocalHost();
        this.port = port;
        multicastgroup = InetAddress.getByName("224.0.0.1");
    }

    public void joinGroup(int timeToLive) {
        try {
            ms = new MulticastSocket();
            ms.setTimeToLive(timeToLive);
            ms.joinGroup(multicastgroup);
            alive = true;
        } catch (SocketException ex) {
            System.err.println(ex);
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        alive = false;
        Thread.sleep(1000);
        ms.leaveGroup(multicastgroup);
        ms.close();
        super.finalize();
    }

    @Override
    public void run() {
        try {
            while (!alive) {
                Thread.sleep(1000);
            }
            while (alive) {
                String message = "device:"+localdevice.getHostName() + ":" + localdevice.getHostAddress()+":";
                byte[] data = message.getBytes();
                DatagramPacket dp = new DatagramPacket(data, data.length, multicastgroup, port);
                ms.send(dp);
                Thread.sleep(5000);
            }
//            MulticastSocket ms = new MulticastSocket();
//            ms.setTimeToLive(1);
//            ms.joinGroup(multicastgroup);
//            for (int i = 1; i < 10; i++) {
//                ms.send(dp);
//            }
            //ms.leaveGroup(multicastgroup);
            //ms.close();

        } catch (InterruptedException ex) {
            Logger.getLogger(PresenseBroadcaster.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SocketException ex) {
            System.err.println(ex);
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}
