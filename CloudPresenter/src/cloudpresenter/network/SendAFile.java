/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudpresenter.network;

import cloudpresenter.gui.FileProgress;
import cloudpresenter.model.Device;
import cloudpresenter.servers.FileRecieving;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Nawanjana
 */
public class SendAFile extends Thread {

    private File sending;
    private Device reciever;
    private FileProgress viewer;

    public SendAFile(File sending, Device reciever, FileProgress viewer) {
        this.sending = sending;
        this.reciever = reciever;
        this.viewer = viewer;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(reciever.getIpAddress(), FileRecieving.FILESHARINGPORT);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(sending.getName());
            oos.writeLong(sending.length());
            FileInputStream fis = new FileInputStream(sending);
            byte[] buffer = new byte[FileRecieving.BUFFER_SIZE];
            Integer bytesRead = 0;
            while ((bytesRead = fis.read(buffer)) > 0) {
                oos.writeObject(bytesRead);
                oos.writeObject(Arrays.copyOf(buffer, buffer.length));
                viewer.increaseSize(bytesRead);
            }
            oos.close();
            ois.close();
            JOptionPane.showMessageDialog(null, "File:" + sending.getName() + " Sent!", "File Sent", JOptionPane.INFORMATION_MESSAGE);
            viewer.setComplete();
        } catch (UnknownHostException ex) {
            Logger.getLogger(SendAFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SendAFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
