/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudpresenter.controller;

import cloudpresenter.model.Device;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static cloudpresenter.gui.MainScreen2.dlmDevices;

/**
 *
 * @author Nawanjana Bandara <Co-founder at Cluztech>
 */
public class DeviceManager {

    private static LinkedList<Device> devices = new LinkedList<Device>();

    public static Device getDevice(String name) {
        for (Device device : devices) {
            if (name.equals(device.getDeviceName())) {
                return device;
            }
        }
        return null;
    }

    public static LinkedList<Device> getList(){
        return devices;
    }

    public static Device getDeviceByIP(String ip) {
        for (Device device : devices) {
            if (ip.equals(device.getIpAddress())) {
                return device;
            }
        }
        return null;
    }

    public synchronized static boolean removeFromList(Device device) {
        if (devices.remove(device)) {
            return dlmDevices.removeElement(device.getDeviceName());
        } else {
            return false;
        }
    }

    public synchronized static void detectPresence(String data, String ipaddress) {
        String[] array = data.split(":");
        if (array.length > 2 && (array[0].equals("device") || array[0].equals("remote"))) {
            boolean present = false;
            Device detected = null;
            for (Device device : devices) {
                if (device.getIpAddress().trim().equals(array[2].trim())) {
                    present = true;
                    detected = device;
                }
            }
            if (!present) {
                Device d = new Device(array[2], array[1], array[0]);
                devices.add(d);
                dlmDevices.addElement(d.getDeviceName());
            } else {
                detected.setCreated(new Date().getTime());
            }
        } else if (array[0].equals("group")) {
            try {
                GroupManager.detectPresence(data, ipaddress);
            } catch (Exception ex) {
                Logger.getLogger(DeviceManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
