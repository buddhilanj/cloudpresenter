/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudpresenter.controller;

import cloudpresenter.gui.MainScreen2;
import cloudpresenter.model.Device;
import cloudpresenter.model.Group;
import java.net.MulticastSocket;
import java.util.Date;
import java.util.LinkedList;
import static cloudpresenter.gui.MainScreen2.dlmDetectedGroups;
import static cloudpresenter.gui.MainScreen2.dlmJoinedGroups;

/**
 *
 * @author Nawanjana
 */
public class GroupManager {

    private static LinkedList<Group> groups = new LinkedList<Group>();
    private static LinkedList<Group> joined = new LinkedList<Group>();

    public static Group getGroup(String id) {
        for (Group group : groups) {
            if (id.equals(group.getGroupID())) {
                return group;
            }
        }
        return null;
    }

    public static LinkedList<Group> getList(){
        return groups;
    }

    public static void addGroup(Group group) {
        if (!isExistingIP(group.getGroupID())) {
            groups.add(group);
            dlmDetectedGroups.addElement(group.getGroupID());
        }
    }

    public static void joinGroup(Group group) {
        if (!isJoinedIP(group.getGroupID())) {
            MainScreen2.rUDPms.joinGroup(group);
            joined.add(group);
            dlmJoinedGroups.addElement(group.getGroupID());
        }
    }

    public static Group getGroupByIP(String ip) {
        for (Group group : groups) {
            if (ip.equals(group.getMcastIP())) {
                return group;
            }
        }
        return null;
    }

     public static Group getJoinedGroupByIP(String ip) {
        for (Group group : joined) {
            if (ip.equals(group.getMcastIP())) {
                return group;
            }
        }
        return null;
    }

    public synchronized static boolean removeFromList(Group group) {
        if (groups.remove(group)) {
            return dlmDetectedGroups.removeElement(group);
        }
        return false;
    }

    public synchronized static boolean leaveFromGroup(Group group) {
        if (joined.remove(group)) {
            MainScreen2.rUDPms.leaveGroup(group);
            return dlmJoinedGroups.removeElement(group);
        }
        return false;
    }

     public static boolean isJoinedIP(String ip) {
        for (Group group : joined) {
            if (ip.equals(group.getMcastIP())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isExistingIP(String ip) {
        for (Group group : groups) {
            if (ip.equals(group.getMcastIP())) {
                return true;
            }
        }
        return false;
    }

    public synchronized static void detectPresence(String data, String ipaddress) throws Exception {
        String[] array = data.split(":");
        if (array.length > 2 && (array[0].equals("group"))) {
            boolean present = false;
            Group detected = null;
            for (Group group : groups) {
                if (group.getMcastIP().equals(array[2])) {
                    present = true;
                    detected = group;
                }
            }
            if (!present) {
                Device admin = DeviceManager.getDeviceByIP(array[3]);
                if (admin == null) {
                    throw new Exception("Admin not in Device Manager. Not implemented yet");
                }
                Group g = new Group(array[1], array[2], admin);
                groups.add(g);
                dlmDetectedGroups.addElement(g.getGroupID());
            } else {
                detected.setCreated(new Date().getTime());
            }
        }
    }
}
