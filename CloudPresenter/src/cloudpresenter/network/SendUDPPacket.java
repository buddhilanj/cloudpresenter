/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudpresenter.network;

import cloudpresenter.utils.ByteUtils;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static cloudpresenter.network.UDPHeader.*;

/**
 *
 * @author Nawanjana
 */
public class SendUDPPacket extends Thread {

    private byte[] data;
    private int complete = 0;
    private InetAddress reciever;
    private static final int port = 6060;
    UDPHeader header;

    public SendUDPPacket(byte[] data, long timestamp, byte type, String reciever) throws UnknownHostException {
        this.data = data;
        this.reciever = InetAddress.getByName(reciever);
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
                DatagramSocket ds = new DatagramSocket();
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length, reciever, port);
                ds.send(dp);
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
