/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudpresenter.network;

import cloudpresenter.imagestream.ImageStream;
import cloudpresenter.imagestream.ScreenFrame;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Nawanjana Bandara <Co-founder at Cluztech>
 */
public class ScreenSharingServer extends Thread {

    ImageStream is;
    boolean stop = false;
    private final String reciever;
    private boolean multicast = false;

    public ScreenSharingServer(String reciever, boolean ismuticast) {
        is = new ImageStream(100, 3000);
        is.start();
        this.reciever = reciever;
        multicast = ismuticast;
    }

    @Override
    public void run() {
        try {
            startStream();
        } catch (UnknownHostException ex) {
            Logger.getLogger(ScreenSharingServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ScreenSharingServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void startStream() throws UnknownHostException, IOException {
        while (!stop) {
            ScreenFrame scfr = null;
            boolean tryagain = false;
            do {
                try {
                    scfr = is.getNewest();
                    tryagain = false;
                } catch (NoSuchFieldException ex) {
                    tryagain = true;
                } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                    tryagain = true;
                }
            } while (tryagain);
            BufferedImage image = scfr.image;
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", buffer);
            if (!multicast) {
                new SendUDPPacket(buffer.toByteArray(), scfr.timestamp, (byte) 0, getReciever()).start();
            } else {
                new SendUDPMulticastPacket(buffer.toByteArray(), scfr.timestamp, (byte) 0, getReciever()).start();
            }
            is.imagestream.remove(scfr);
        }
    }

    /**
     * @return the reciever
     */
    public String getReciever() {
        return reciever;
    }

    public void stopStream(){
        stop=true;
    }
//    public static void main(String[] args) {
//        try {
//            RecieveUDPPacket rUDP = new RecieveUDPPacket();
//            rUDP.start();
//            ScreenSharingServer server = new ScreenSharingServer("localhost",false);
//            Thread.sleep(3000);
//            server.startStream();
//        } catch (InterruptedException ex) {
//            Logger.getLogger(ScreenSharingServer.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (UnknownHostException ex) {
//            Logger.getLogger(ScreenSharingServer.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(ScreenSharingServer.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//    }
}
