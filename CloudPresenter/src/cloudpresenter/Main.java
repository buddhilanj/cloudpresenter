/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudpresenter;

import cloudpresenter.broadcast.PresenceSniffer;
import cloudpresenter.broadcast.PresenseBroadcaster;
import cloudpresenter.gui.MainScreen;
import cloudpresenter.gui.ViewImageUI;
import cloudpresenter.imagestream.ImageStream;
import cloudpresenter.network.ScreenSharingServer;
import cloudpresenter.servers.RemoteKeyReciever;
import cloudpresenter.servers.RemoteMouseReciever;
import de.javasoft.plaf.synthetica.SyntheticaBlueMoonLookAndFeel;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Nawanjana
 */
public class Main {

    public final static ViewImageUI screen = null;
    public final static ImageStream is = new ImageStream(100, 2000);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new SyntheticaBlueMoonLookAndFeel());
            PresenseBroadcaster ps;
            PresenceSniffer sniff;
            try {
                sniff = new PresenceSniffer(4000);
                sniff.start();
            } catch (UnknownHostException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                ps = new PresenseBroadcaster(4000);
                ps.joinGroup(16);
                ps.start();
            } catch (UnknownHostException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

            RemoteKeyReciever rr = new RemoteKeyReciever();
            rr.start();
            RemoteMouseReciever rmr =new RemoteMouseReciever();
            rmr.start();
            MainScreen ms = new MainScreen();
            ms.setVisible(true);
            //        is.start();
            //        is.expireImages();
            //        while (true) {
            //            try {
            //                Thread.sleep(100);
            //                boolean tryagain = false;
            //                BufferedImage screenimage = null;
            //                do {
            //                    try {
            //                        screenimage = is.retrieveLatest().image;
            //                        tryagain = false;
            //                    } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            //                        tryagain = true;
            //                    }
            //                } while (tryagain);
            //                screen.setImage(screenimage);
            //                screen.repaint();
            //                if (!screen.isVisible()) {
            //                    screen.setVisible(true);
            //                }
            //            } catch (InterruptedException ex) {
            //                System.err.println(ex);
            //                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            //            }
            //        }
        } catch (ParseException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
