/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudpresenter.controller;

import cloudpresenter.gui.MainScreen2;
import cloudpresenter.gui.ManageFirends;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import cloudpresenter.model.Device;
import java.util.Date;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import static cloudpresenter.gui.MainScreen2.dlmServer;

/**
 *
 * @author Nawanjana Bandara <Co-founder at Cluztech>
 */
public class ServerDevices {

    public static LinkedList<Device> friends = new LinkedList<Device>();

    public static Device getDevice(String name) {
        for (Device device : friends) {
            if (name.equals(device.getDeviceName())) {
                return device;
            }
        }
        return null;
    }

    public static Device getDeviceByIP(String ip) {
        for (Device device : friends) {
            if (ip.equals(device.getIpAddress())) {
                return device;
            }
        }
        return null;
    }

    public synchronized static boolean removeFromList(Device device) {
        dlmServer.removeElement(device.getDeviceName());
        return friends.remove(device);
    }

    public static void emptyList() {
        friends.clear();
        dlmServer.clear();
    }

    public synchronized static void detectPresence(final String deviceName, String ipaddress, String status) {
        if (status.equals("rejected")) {
            new Thread(new Runnable() {
                public String friend = deviceName;
                public void run() {
                    try {

                        CloseableHttpClient httpclient = HttpClients.createDefault();
                        HttpPost httppost = new HttpPost("http://Amaterasu-PC/cloudpresenterserver/friendreq.php");
                        // Request parameters and other properties.
                        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
                        params.add(new BasicNameValuePair("user", MainScreen2.getLoggedInUser()));
                        params.add(new BasicNameValuePair("friend", friend));
                        params.add(new BasicNameValuePair("status", "delete"));
                        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                        //Execute and get the response.
                        HttpResponse response = httpclient.execute(httppost);
                        HttpEntity entity = response.getEntity();
                        String result = EntityUtils.toString(entity);
                        JSONObject json = new JSONObject(result);
                        String status = json.getString("friendreq");
                        if (status.equals("successful")) {
                            JOptionPane.showMessageDialog(null, friend+" has turned down your request!", "Rejection", JOptionPane.INFORMATION_MESSAGE);
                        } else if (status.equals("error")) {
                            String error = json.getString("error");
                            if (error.startsWith("Duplicate entry")) {
                                error = "Request already sent";
                            }
                            JOptionPane.showMessageDialog(null, error, "Request Approval Failed", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (JSONException ex) {
                        Logger.getLogger(ManageFirends.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(ManageFirends.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(ManageFirends.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }).start();
        } else {
            boolean present = false;
            Device detected = null;
            for (Device device : friends) {
                if (device.getIpAddress().trim().equals(ipaddress.trim())) {
                    present = true;
                    detected = device;
                }
            }
            if (!present) {
                Device d = new Device(ipaddress, deviceName, "device");
                friends.add(d);
                dlmServer.addElement(d.getDeviceName());
            } else {
                detected.setCreated(new Date().getTime());
            }
        }
    }
}
