/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudpresenter.servers;

import cloudpresenter.controller.DeviceManager;
import cloudpresenter.gui.MainScreen2;
import cloudpresenter.model.Device;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 *
 * @author Nawanjana
 */
public class RemoteKeyReciever extends Thread {

    public final static int remotePort = 6064;
    public static boolean shift = false;
    public static boolean ctrl = false;
    public static boolean alt = false;

    @Override
    public void run() {
        ServerSocket theServer;
        Socket theConnection = null;
        try {
            theServer = new ServerSocket(remotePort);
            try {
                while (true) {
                    theConnection = theServer.accept();

                    DataInputStream dIn = new DataInputStream(theConnection.getInputStream());
                    String incoming = dIn.readUTF();
                    System.out.println(incoming);
                    boolean accepted = false;
                    Robot r = new Robot();
                    KeyStroke ks = null;
                    Device device = DeviceManager.getDeviceByIP(theConnection.getInetAddress().getHostAddress());

                    if (incoming.equals("Request Control")) {
                        if (!device.equals(MainScreen2.remote)) {
                            int result = JOptionPane.showConfirmDialog(null, device.getDeviceName() + " is Requesting for Remote access. Aceept?", "Remote Request", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                            DataOutputStream dOut = new DataOutputStream(theConnection.getOutputStream());
                            if (result == JOptionPane.YES_OPTION) {
                                MainScreen2.remote = device;
                                dOut.writeUTF("Accepted");
                            }else{
                                dOut.writeUTF("Rejected");
                            }
                        }
                    }
                    if (MainScreen2.remote != null && MainScreen2.remote.equals(device)) {
                        if (incoming.contains("\r")) {
                            r.keyPress(KeyEvent.VK_ENTER);
                            r.keyRelease(KeyEvent.VK_ENTER);
                        } else if(incoming.equals("Request Control")){
                            
                        }
                        else if(incoming.contains("\b")) {
                            r.keyPress(KeyEvent.VK_BACK_SPACE);
                        } else if (incoming.contains("Mouse:Left")) {
                            r.mousePress(InputEvent.BUTTON1_MASK);
                            r.mouseRelease(InputEvent.BUTTON1_MASK);
                        } else if (incoming.contains("Mouse:Right")) {
                            r.mousePress(InputEvent.BUTTON3_MASK);
                            r.mouseRelease(InputEvent.BUTTON3_MASK);
                        } else if (incoming.contains("UP")) {
                            r.keyPress(KeyEvent.VK_UP);
                        } else if (incoming.contains("Left")) {
                            r.keyPress(KeyEvent.VK_LEFT);
                        } else if (incoming.contains("Down")) {
                            r.keyPress(KeyEvent.VK_DOWN);
                        } else if (incoming.contains("Right")) {
                            r.keyPress(KeyEvent.VK_RIGHT);
                        } else if (incoming.contains("Esc")) {
                            r.keyPress(KeyEvent.VK_ESCAPE);
                        } else if (incoming.contains("Del")) {
                            r.keyPress(KeyEvent.VK_DELETE);
                        } else if (incoming.contains("Ctrl")) {
                            if (!ctrl) {
                                r.keyPress(KeyEvent.VK_CONTROL);
                                ctrl = true;
                            } else {
                                r.keyRelease(KeyEvent.VK_CONTROL);
                                ctrl = false;
                            }
                        } else if (incoming.contains("Shift")) {
                            if (!shift) {
                                r.keyPress(KeyEvent.VK_SHIFT);
                                shift = true;
                            } else {
                                r.keyRelease(KeyEvent.VK_SHIFT);
                                shift = false;
                            }
                        } else if (incoming.contains("Alt")) {
                            if (!alt) {
                                r.keyPress(KeyEvent.VK_ALT);
                                alt = true;
                            } else {
                                r.keyRelease(KeyEvent.VK_ALT);
                                alt = false;
                            }
                        } else if (incoming.contains("Tab")) {
                            r.keyPress(KeyEvent.VK_TAB);
                        } else {
                            ks = KeyStroke.getKeyStroke(incoming.charAt(0), 0);
                            r.keyPress(ks.getKeyCode());
                        }
                    }
                    theConnection.close();
                }

            } catch (AWTException ex) {
                Logger.getLogger(RemoteKeyReciever.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                theConnection.close();
                System.err.println(ex);
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}
