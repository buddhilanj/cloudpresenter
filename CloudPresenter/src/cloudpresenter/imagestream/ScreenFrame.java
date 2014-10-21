/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudpresenter.imagestream;

import cloudpresenter.Statistics;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Nawanjana
 */
public class ScreenFrame {

    public final long timestamp;
    public BufferedImage image;

//    public static void main(String[] args) {
//        try {
//            ScreenFrame sc = new ScreenFrame();
//        } catch (IOException ex) {
//            Logger.getLogger(ScreenFrame.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (AWTException ex) {
//            Logger.getLogger(ScreenFrame.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    public ScreenFrame() throws IOException, AWTException {
//        Statistics.screenRecieving++;
//        Statistics.currentscreens++;
        timestamp = new Date().getTime();
        image = imageCapture();
    }

    public ScreenFrame(byte[] frame, long timestamp) throws IOException, AWTException {
//        Statistics.screenRecieving++;
//        Statistics.currentscreens++;
        this.timestamp = timestamp;
        ByteArrayInputStream bias = new ByteArrayInputStream(frame);
        synchronized (this) {
            image = ImageIO.read(bias);
        }
        if (image == null) {
            System.err.println("Null Image Added!!!" + frame.length);
        }
        bias.close();
    }

    public ScreenFrame(int fullzise, long timestamp) throws IOException, AWTException {
//        Statistics.screenRecieving++;
//        Statistics.currentscreens++;
        this.timestamp = timestamp;
        image = ImageIO.read(new ByteArrayInputStream(new byte[fullzise]));
    }

    public static BufferedImage imageCapture() throws AWTException, IOException {
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRectangle = new Rectangle(screenSize);
        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(screenRectangle);
        return image;
    }

    @Override
    protected void finalize() throws Throwable {
//        Statistics.screendestryed++;
//        Statistics.currentscreens--;
//        System.out.println(""+Statistics.screenRecieving+":"+Statistics.screendestryed+":"+Statistics.currentscreens);
        super.finalize();
    }
}
