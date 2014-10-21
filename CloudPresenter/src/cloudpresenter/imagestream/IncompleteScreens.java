/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudpresenter.imagestream;

import cloudpresenter.Statistics;
import java.awt.AWTException;
import java.io.IOException;
import static cloudpresenter.network.UDPHeader.*;

/**
 *
 * @author Nawanjana Bandara <Co-founder at Cluztech>
 */
public class IncompleteScreens {

    public final long timestamp;
    private byte[] image;
    public final boolean[] part;

    public IncompleteScreens(int fullsize, long timestamp) {
        this.timestamp = timestamp;
        image = new byte[fullsize];
        if (fullsize % bufferSize == 0) {
            part = new boolean[fullsize / bufferSize];
        } else {
            part = new boolean[(fullsize / bufferSize) + 1];
        }
//        Statistics.incompleteRecieving++;
//        Statistics.currentincomplete++;
    }

    public synchronized boolean completeFrame(byte data[], int count, int size) {
        try {
            if (!part[count]) {
                System.arraycopy(data, metaDataSize, image, count * bufferSize, size);
                part[count] = true;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void finalize() throws Throwable {
//        Statistics.incompletedestryed++;
//        Statistics.currentincomplete--;
//        System.out.println(""+Statistics.incompleteRecieving+":"+Statistics.incompletedestryed+":"+Statistics.currentincomplete);
        super.finalize();
    }

    public boolean isComplete() {
        boolean flag = true;
        for (boolean b : part) {
            if (!b) {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * @return the image
     */
    public ScreenFrame getImage() throws IOException, AWTException {
        ScreenFrame frame = new ScreenFrame(image, timestamp);
        return frame;
    }

    public void destroy() {
        image = null;
    }
}
