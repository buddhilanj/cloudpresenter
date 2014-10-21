/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudpresenter.model;

import cloudpresenter.controller.GroupManager;
import java.io.IOException;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nawanjana Bandara <Co-founder at Cluztech>
 */
public class Group {

    private String groupID;
    private String mcastIP;
    private String password;
    private Device[] devices;
    private Device admin;
    private InetAddress localdevice;
    private int port = 4000;
    private InetAddress multicastgroup;
    private MulticastSocket ms;
    private Long created;

    public Group(String tgroupID, Device admin, String password) throws UnknownHostException, ConnectException, IOException, Exception {
        Group exisiting = GroupManager.getGroup(tgroupID);
        if (exisiting != null) {
            throw new Exception("There is a group existing by this id");
        }
        created = new Date().getTime();
        this.groupID = tgroupID;
        this.password = password;
        this.admin = admin;
        localdevice = InetAddress.getLocalHost();
        int newgroup = 1;

        while (newgroup < 256 && GroupManager.isExistingIP(String.format("224.0.1.%d", newgroup))) {
            newgroup++;
        }
        if (newgroup > 255) {
            throw new ConnectException("No Multicast IP available");
        }
        mcastIP = String.format("224.0.1.%d", newgroup);
        multicastgroup = InetAddress.getByName("224.0.0.1");
        ms = new MulticastSocket();
        ms.setTimeToLive(16);
        ms.joinGroup(multicastgroup);
        new Thread(new Runnable() {

            public void run() {
                while (true) {
                    try {
                        String message = "group:" + groupID + ":" + mcastIP + ":"+localdevice.getHostAddress()+":";
                        byte[] data = message.getBytes();
                        DatagramPacket dp = new DatagramPacket(data, data.length, multicastgroup, port);
                        ms.send(dp);
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Group.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(Group.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }).start();

    }

    public Group(String groupID, String mcastIP, Device admin) {
        this.groupID = groupID;
        this.mcastIP = mcastIP;
        this.admin = admin;
        created = new Date().getTime();
    }

    /**
     * @return the groupID
     */
    public String getGroupID() {
        return groupID;
    }

    /**
     * @param groupID the groupID to set
     */
    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    /**
     * @return the mcastIP
     */
    public String getMcastIP() {
        return mcastIP;
    }

    /**
     * @param mcastIP the mcastIP to set
     */
    public void setMcastIP(String mcastIP) {
        this.mcastIP = mcastIP;
    }

    /**
     * @return the password
     */
    /**
     * @return the devices
     */
    public Device[] getDevices() {
        return devices;
    }

    /**
     * @return the created
     */
    public Long getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(Long created) {
        this.created = created;
    }
}
