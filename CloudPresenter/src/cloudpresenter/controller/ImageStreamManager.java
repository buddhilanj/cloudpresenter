/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudpresenter.controller;

import cloudpresenter.model.Device;
import cloudpresenter.imagestream.ImageStream;
import cloudpresenter.network.ScreenSharingServer;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import static cloudpresenter.gui.MainScreen2.dlmIncoming;
import static cloudpresenter.gui.MainScreen2.dlmOutgoing;


/**
 *
 * @author Nawanjana
 */
public class ImageStreamManager {

    private static LinkedList<ImageStream> inboundstreams = new LinkedList<ImageStream>();
    private static LinkedList<ScreenSharingServer> outboundstreams = new LinkedList<ScreenSharingServer>();

    public static ImageStream getStreamByIP(InetAddress ip) {
        ImageStream relevant = null;
        for (ImageStream stream : inboundstreams) {
            if (stream.getHostIP().equals(ip.getHostAddress())) {
                relevant = stream;
            }
        }
        return relevant;

    }

     public static ScreenSharingServer getOutgoingStreamByIP(InetAddress ip) {
        ScreenSharingServer relevant = null;
        for (ScreenSharingServer stream : outboundstreams) {
            if (stream.getReciever().equals(ip.getHostAddress())) {
                relevant = stream;
            }
        }
        return relevant;

    }

     public static void addNewImageStream(ScreenSharingServer created,String hostname){
         outboundstreams.add(created);
         dlmOutgoing.addElement(hostname);
     }

    public static LinkedList<ImageStream> getList(){
        return inboundstreams;
    }

    public static ImageStream addNewImageStream(String hostname) throws UnknownHostException {
        ImageStream created = new ImageStream(100, 300, hostname);
        inboundstreams.add(created);
        boolean tryagain = false;
        Device device = DeviceManager.getDeviceByIP(created.getHostIP());
        String name =hostname;
        if(device!=null){
            hostname = device.getDeviceName();
        }
        dlmIncoming.addElement(hostname);
        return created;
    }

    public static boolean removeStream(ImageStream stream){
        if(inboundstreams.remove(stream)){
            return dlmIncoming.removeElement(DeviceManager.getDeviceByIP(stream.getHostIP()).getDeviceName());
        }
        return false;
    }

    public static boolean removeOutboundStream(ScreenSharingServer stream){
        if(outboundstreams.remove(stream)){
            return dlmOutgoing.removeElement(DeviceManager.getDeviceByIP(stream.getReciever()).getDeviceName());
        }
        return false;
    }
}
