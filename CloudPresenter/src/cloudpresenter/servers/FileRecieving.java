/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudpresenter.servers;

import cloudpresenter.controller.DeviceManager;
import cloudpresenter.gui.FileProgress;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Nawanjana
 */
public class FileRecieving extends Thread {

    public static final int FILESHARINGPORT = 6063;
    public static final int BUFFER_SIZE = 1024;
    private FileProgress p;

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(FILESHARINGPORT);
            while (true) {
                Socket s = serverSocket.accept();
                saveFile(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "ERORR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveFile(Socket socket) throws Exception {
        int confirm = JOptionPane.showConfirmDialog(null, "You Have an incoming message from " + socket.getInetAddress().getHostName() + ". Do you accept?", "Incoming File", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            FileOutputStream fos = null;
            byte[] buffer = new byte[BUFFER_SIZE];

            // 1. Read file name.
            Object o = ois.readObject();

            final long size = ois.readLong();
            Thread a = null;
            if (o instanceof String) {
                String incoming = o.toString();
                JFileChooser backup = new JFileChooser(new File(incoming));
                backup.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int resultVal = backup.showSaveDialog(null);

                if (resultVal == JFileChooser.APPROVE_OPTION) {
                    File file = backup.getSelectedFile();
                    fos = new FileOutputStream(file.getAbsolutePath() + incoming);
                    final String filename = file.getName();

                    a = new Thread(new Runnable() {

                        public void run() {
                            synchronized (this) {
                                p = new FileProgress("Saving " + filename, size);
                                p.setVisible(true);
                                notify();
                                //System.out.println("notified");
                            }
                        }
                    });
                    a.start();


                } else {
                    throwException("User Cancelled");
                }
            } else {
                throwException("Something is wrong");
            }

            // 2. Read file to the end.
            Integer bytesRead = 0;
            //System.out.println("Waiting....");
            synchronized (a) {
                a.wait();
            }
            //System.out.println("Done waiting....");
            do {
                o = ois.readObject();

                if (!(o instanceof Integer)) {
                    throwException("Something is wrong");
                }

                bytesRead = (Integer) o;

                o = ois.readObject();

                if (!(o instanceof byte[])) {
                    throwException("Something is wrong");
                }

                buffer = (byte[]) o;

                // 3. Write data to output file.
                fos.write(buffer, 0, bytesRead);

                p.increaseSize(bytesRead);
            } while (bytesRead == BUFFER_SIZE);

            fos.close();
            ois.close();
            oos.close();
            JOptionPane.showMessageDialog(null, "File Saved Success!", "Success", JOptionPane.INFORMATION_MESSAGE);
            p.setComplete();
        }
    }

    public static void throwException(String message) throws Exception {
        throw new Exception(message);
    }

    public static void main(String[] args) {
        new FileRecieving().start();
    }
}
