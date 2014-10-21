/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudpresenter.servers;

import cloudpresenter.Statistics;
import cloudpresenter.controller.ImageStreamManager;
import java.awt.AWTException;
import java.io.IOException;
import java.net.UnknownHostException;
import cloudpresenter.imagestream.ImageStream;
import cloudpresenter.imagestream.ScreenFrame;
import cloudpresenter.network.UDPHeader;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nawanjana Bandara <Co-founder at Cluztech>
 */
public class ImageReciever extends Thread {

    private byte[] buffer;
    private ImageStream stream;
    private ScreenFrame frame;
    private UDPHeader header;
    static byte[] data = null;
    static boolean[] part = null;
    private InetAddress reciever;
    private long finalstamp;

    public ImageReciever(byte[] buffer, InetAddress reciever) {
        this.buffer = buffer;
        this.reciever = reciever;
        header = new UDPHeader();
//        Statistics.threadsRecieving++;
//        Statistics.currentThreads++;
    }

    private synchronized static ImageStream getImageStream(InetAddress reciever) throws UnknownHostException {
        ImageStream stream = ImageStreamManager.getStreamByIP(reciever);
        if (stream == null) {
            stream = ImageStreamManager.addNewImageStream(reciever.getHostName());
        }
        return stream;
    }

    @Override
    public void run() {
        try {
            stream = ImageReciever.getImageStream(reciever);
            header.extractMetaDataFromPacket(buffer);
            int fullsize = header.getFullsize();
            int count = header.getCount();
            int size = header.getSize();
            long timestapm = header.getTimestamp();
            int tempcount = 0;
            if (finalstamp == 0) {
                finalstamp = timestapm;
            }
            while (!stream.addPartData(buffer, timestapm, count, size, fullsize) && tempcount++ < 10);
            /*if(finalstamp==timestapm){
            if (data == null) {
                data = new byte[fullsize];
                if (fullsize % bufferSize == 0) {
                    part = new boolean[fullsize / bufferSize];
                } else {
                    part = new boolean[(fullsize / bufferSize) + 1];
                }
            }
            part[count] = true;
            System.arraycopy(buffer, metaDataSize, data, count * bufferSize, size);
            boolean flag = true;
            for (boolean b : part) {
                if (!b) {
                    flag = false;
                }
            }
            if (flag) {
                ByteArrayInputStream bias = new ByteArrayInputStream(data);
                BufferedImage image = ImageIO.read(bias);
                if(image==null){
                    System.err.println("NUll IMage");
                }
                String name = reciever.getHostName() + "@" + new SimpleDateFormat("yyyy-MM-dd HHmm ss SSS").format(header.getIdtag());
                File imagefile = new File(name + ".jpg");
                ImageIO.write(image, "jpg", imagefile);
                this.data = null;
                //                        screencap.ViewImage screen = new screencap.ViewImage();
                //                        screen.setImage(image);
                //                        screen.repaint();
                //                        screen.setVisible(true);
            }
            }*/
        } catch (AWTException ex) {
            Logger.getLogger(ImageReciever.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ImageReciever.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(ImageReciever.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void finalize() throws Throwable {
//        Statistics.threadsdestryed++;
//        Statistics.currentThreads--;
        //System.out.println(""+Statistics.threadsRecieving+":"+Statistics.threadsdestryed+":"+Statistics.currentThreads);
        super.finalize();
    }

}
