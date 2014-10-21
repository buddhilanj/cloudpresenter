/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudpresenter.model;

import cloudpresenter.controller.DeviceManager;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nawanjana Bandara <Co-founder at Cluztech>
 */
public class Device {

    private String ipAddress;
    private String deviceName;
    private Device remote;
    private Group group;
    private Device control;
    private Long created;
    private String type;

    public Device(String ipAddress, String deviceName, String type) {
        this.ipAddress = ipAddress;
        this.deviceName = deviceName;
        this.type = type;
        created = new Date().getTime();
        checkExpire();
    }

    /**
     * @return the ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * @param ipAddress the ipAddress to set
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * @return the deviceName
     */
    public String getDeviceName() {
        return deviceName;
    }

    /**
     * @param deviceName the deviceName to set
     */
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    /**
     * @return the remote
     */
    public Device getRemote() {
        return remote;
    }

    /**
     * @param remote the remote to set
     */
    public void setRemote(Device remote) {
        this.remote = remote;
    }

    /**
     * @return the group
     */
    public Group getGroup() {
        return group;
    }

    /**
     * @param group the group to set
     */
    public void setGroup(Group group) {
        this.group = group;
    }

    /**
     * @return the control
     */
    public Device getControl() {
        return control;
    }

    /**
     * @param control the control to set
     */
    public void setControl(Device control) {
        this.control = control;
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

    public void expire() {
        DeviceManager.removeFromList(this);
    }

    public void checkExpire() {
        new Thread(new Runnable() {

            public void run() {
                while (true) {
                    try {
                        if (new Date().getTime() - created > 10000) {
                            expire();
                        }
                        Thread.sleep(2500);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }).start();
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }
}
