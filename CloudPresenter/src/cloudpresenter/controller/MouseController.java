/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudpresenter.controller;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Nawanjana
 */
public class MouseController extends Thread {

    public static float ACELX = 0;
    public static float ACELY = 0;
    private static MouseController instance;
    private static boolean ready = false;
    private static boolean running = false;

    private MouseController() {
        this.start();
    }

    public synchronized void newUpdate(String data[]) {
        if (data != null) {
            if (data.length > 3 && !data[1].equals("Close")) {
                ACELX = Float.parseFloat(data[2]);
                ACELY = Float.parseFloat(data[3]);
                ready = true;
            } else {
                ACELX = 0f;
                ACELY = 0f;
                ready = false;
            }
        }
    }

    public static synchronized MouseController getInstance() {
        if (instance == null) {
            instance = new MouseController();
        }
        return instance;
    }

    @Override
    public void run() {
        if (running) {
            JOptionPane.showMessageDialog(null, "Instance Already Running", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            running=true;
            try {
                while (true) {
                    if (ready) {
                        Robot r = new Robot();
                        int changingX = MouseInfo.getPointerInfo().getLocation().x + ((int) (ACELX * 10));
                        int changingY = MouseInfo.getPointerInfo().getLocation().y + ((int) (ACELY * 10));
                        r.mouseMove(changingX, changingY);
                    }
                    //System.out.println(MouseInfo.getPointerInfo().getLocation().x+":"+MouseInfo.getPointerInfo().getLocation().y+"              "+ACELX+":"+ACELY);
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MouseController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (AWTException ex) {
                Logger.getLogger(MouseController.class.getName()).log(Level.SEVERE, null, ex);
                running = false;
            } finally {
                running = false;
            }
        }
    }
}
