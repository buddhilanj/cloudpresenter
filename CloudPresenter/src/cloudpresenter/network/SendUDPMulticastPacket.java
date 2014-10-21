/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudpresenter.network;

import java.net.MulticastSocket;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static cloudpresenter.network.UDPHeader.*;

/**
 *
 * @author Nawanjana
 */
public class SendUDPMulticastPacket extends Thread {

    private byte[] data;
    private int complete = 0;
    private InetAddress reciever;
    private MulticastSocket ms;
    private static final int port = 6066;
    UDPHeader header;

    public SendUDPMulticastPacket(byte[] data, long timestamp, byte type, String reciever) throws UnknownHostException, IOException {
        this.data = data;
        this.reciever = InetAddress.getByName(reciever);
        ms = new MulticastSocket();
        ms.setTimeToLive(16);
        header = new UDPHeader(timestamp, type, data.length);
        //System.out.println("Size:"+data.length+" @ "+timestamp);
    }

    @Override
    public void run() {
        while (complete < data.length) {
            try {
                byte[] buffer = new byte[bufferSize + metaDataSize];
                int left = data.length - complete;
                if (left > bufferSize) {
                    left = bufferSize;
                }
                header.setSize(left);
                byte[] metadata = header.generateNextMetaFile();
                System.arraycopy(metadata, 0, buffer, 0, metaDataSize);
                System.arraycopy(data, complete, buffer, metaDataSize, left);
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length, reciever, port);
                ms.send(dp);
                complete += left;
                header.incrementCount();
            } catch (ParseException ex) {
                Logger.getLogger(SendUDPPacket.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SocketException ex) {
                Logger.getLogger(SendUDPPacket.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SendUDPPacket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
