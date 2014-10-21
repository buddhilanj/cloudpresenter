/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudpresenter.imagestream;

import cloudpresenter.controller.ImageStreamManager;
import java.awt.AWTException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nawanjana
 */
public final class ImageStream extends Thread {

    private InetAddress host = null;
    //public Queue<ScreenFrame> imagestream = new LinkedList<ScreenFrame>();
    public LinkedList<ScreenFrame> imagestream = new LinkedList<ScreenFrame>();
    private LinkedList<IncompleteScreens> incomplete = new LinkedList<IncompleteScreens>();
    private int size = 0;
    private int interval = 0;
    private int buffer = 0;
    private long lastresponce;

    public ImageStream() {
        this.interval = 40;
        this.buffer = 1000;
        this.lastresponce = new Date().getTime();
        expireImages();
    }

    public ImageStream(int interval, int buffer) {
        this.interval = interval;
        this.buffer = buffer;
        this.lastresponce = new Date().getTime();
        expireImages();
    }

    public ImageStream(int interval, int buffer, String name) throws UnknownHostException {
        this.interval = interval;
        this.buffer = buffer;
        this.host = InetAddress.getByName(name);
        this.lastresponce = new Date().getTime();
        expireImages();
        checkExpire();
    }

//    public static void main(String[] args) {
//        ImageStream is = new ImageStream();
//        is.start();
//        while (true) {
//            System.out.println("length:" + is.imagestream.size());
//            if (!is.imagestream.isEmpty()) {
//                long current = new Date().getTime();
//                ScreenFrame earliest = is.imagestream.peek();
//                System.out.println(current + "-" + earliest.timestamp + " > " + is.buffer + ":" + ((current - earliest.timestamp) > is.buffer));
//            }
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(ImageStream.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
    @Override
    public void run() {
        //expireImages();
        while (true) {
            try {
                addImage(new ScreenFrame());
                Thread.sleep(interval);
            } catch (InterruptedException ex) {
                Logger.getLogger(ImageStream.class.getName()).log(Level.SEVERE, null, ex);
            } catch (AWTException ex) {
                Logger.getLogger(ImageStream.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ImageStream.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private synchronized boolean addImage(ScreenFrame image) {
        this.lastresponce = new Date().getTime();
        return imagestream.add(image);
    }

    private synchronized boolean addIncomplete(IncompleteScreens frm) {
        this.lastresponce = new Date().getTime();
        return incomplete.add(frm);
    }

    private synchronized boolean addImage(ScreenFrame image, boolean b) {
        this.lastresponce = new Date().getTime();
        return imagestream.add(image);
    }

    public synchronized boolean removeImage(ScreenFrame image) {
        for (ScreenFrame frame : imagestream) {
            if (frame.timestamp == image.timestamp) {
                imagestream.remove(frame);
                return true;
            }
        }
        return false;
    }

    public boolean addPartData(byte[] data, long timestamp, int count, int size, int fullsize) throws IOException, AWTException {
        this.lastresponce = new Date().getTime();
        IncompleteScreens frm;
        synchronized (this) {
            frm = getScreenByTimestamp(timestamp);
            if (frm == null) {
                frm = new IncompleteScreens(fullsize, timestamp);
                addIncomplete(frm);
            }
        }
        if (frm == null) {
            System.err.println("Add part failed");
            return false;
        } else {
            if (frm.completeFrame(data, count, size)) {
                if (frm.isComplete()) {
                    addImage(frm.getImage());
                    incomplete.remove(frm);
                    frm.destroy();

                }
                return true;
            } else {
                return false;
            }
        }
    }

    public synchronized ScreenFrame retrieveLatest() throws ArrayIndexOutOfBoundsException {
        if ((imagestream.isEmpty())) {
            throw new ArrayIndexOutOfBoundsException("The Queue is Empty");
        } else {
            return imagestream.getLast();
        }
    }

    public synchronized ScreenFrame getNewest() throws ArrayIndexOutOfBoundsException, NoSuchFieldException {
        //System.out.println("Stream Size:" + imagestream.size() + "-- Incomplete Size:" + incomplete.size());
        if ((imagestream.isEmpty())) {
            throw new ArrayIndexOutOfBoundsException("The Queue is Empty");

        } else {
            long now = new Date().getTime();
            long smallestdif = now;
            ScreenFrame latest = null;
            for (ScreenFrame frm : imagestream) {
                long newdif = now - frm.timestamp;
                if (smallestdif > newdif) {
                    smallestdif = newdif;
                    latest = frm;
                }
            }
            if (latest == null) {
                //System.out.println("No Nearest Frame Exists");
                throw new NoSuchFieldException("No Nearest Frame Exists");
            }
            return latest;
        }
    }

    public synchronized ScreenFrame getOldest() throws ArrayIndexOutOfBoundsException, NoSuchFieldException {

        if ((imagestream.isEmpty())) {
            throw new ArrayIndexOutOfBoundsException("The Queue is Empty");

        } else {
            long now = new Date().getTime();
            long largestdif = 0;
            ScreenFrame latest = null;
            for (ScreenFrame frm : imagestream) {
                long newdif = now - frm.timestamp;
                if (largestdif < newdif) {
                    largestdif = newdif;
                    latest = frm;
                }
            }
            if (latest == null) {
                System.out.println("No Nearest Frame Exists");
                throw new NoSuchFieldException("No Old Frame Exists");
            }
            return latest;
        }
    }

    public synchronized IncompleteScreens getOldestIncomplete() throws ArrayIndexOutOfBoundsException, NoSuchFieldException {
        //System.out.println("Incomplete Size" + incomplete.size());

        if ((incomplete.isEmpty())) {
            throw new ArrayIndexOutOfBoundsException("The Queue is Empty");

        } else {
            long now = new Date().getTime();
            long largestdif = 0;
            IncompleteScreens latest = null;
            for (IncompleteScreens frm : incomplete) {
                long newdif = now - frm.timestamp;
                if (largestdif < newdif) {
                    largestdif = newdif;
                    latest = frm;
                }
            }
            if (latest == null) {
                //System.out.println("No Nearest Frame Exists");
                throw new NoSuchFieldException("No Old Frame Exists");
            }
            return latest;
        }
    }

    public void expireImages() {
        new Thread(new Runnable() {

            public void run() {
                while (true) {
                    //System.out.println("Interval :"+interval);
                    new Thread(new Runnable() {

                        public void run() {
                            expireImage();
                        }
                    }).start();
                    new Thread(new Runnable() {

                        public void run() {
                            expireIncompleted();
                        }
                    }).start();
                    try {
                        Thread.sleep(interval);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ImageStream.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }).start();
    }

    public void expire() {
        ImageStreamManager.removeStream(this);
    }

    public void checkExpire() {
        new Thread(new Runnable() {

            public void run() {
                while (true) {
                    try {
                        if (new Date().getTime() - lastresponce > 6000) {
                            expire();
                        }
                        Thread.sleep(2500);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ImageStream.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }).start();
    }

    public void expireImage() {
        if (!imagestream.isEmpty()) {
            long current = new Date().getTime();
            ScreenFrame earliest = null;
            boolean tryagain = true;
            try {
                while (tryagain) {
                    try {
                        earliest = this.getOldest();
                        tryagain = false;
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        Thread.sleep(100);
                    } catch (NoSuchFieldException ex) {
                        Thread.sleep(100);
                    }
                }

                if ((current - earliest.timestamp) > buffer) {
                    imagestream.remove(earliest);
                }
                Thread.sleep(100);
            } catch (InterruptedException ex1) {
                Logger.getLogger(ImageStream.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    public void expireIncompleted() {
        if (!incomplete.isEmpty()) {
            long current = new Date().getTime();
            IncompleteScreens earliest = null;
            boolean tryagain = true;
            try {
                while (tryagain) {
                    try {
                        earliest = this.getOldestIncomplete();
                        tryagain = false;
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        Thread.sleep(100);
                    } catch (NoSuchFieldException ex) {
                        Thread.sleep(100);
                    }
                }
            } catch (InterruptedException ex1) {
                Logger.getLogger(ImageStream.class.getName()).log(Level.SEVERE, null, ex1);
            }

            if ((current - earliest.timestamp) > buffer) {
                incomplete.remove(earliest);
            }
        }
    }

    public String getHostIP() {
        if (host == null) {
            return "127.0.0.1";
        }
        return host.getHostAddress();
    }

    public synchronized IncompleteScreens getScreenByTimestamp(long timestamp) {
        for (IncompleteScreens frame : incomplete) {
            if (frame.timestamp == timestamp) {
                if (frame == null) {
                    System.out.println(Thread.currentThread().getName() + ":alert");
                }
                return frame;
            }
        }
        return null;
    }

    public void StreamForView(BufferedImage image) {
    }

    public synchronized StreamImagesOnScreen openDisplay() {
        StreamImagesOnScreen newthread = new StreamImagesOnScreen(this);
        newthread.start();
        return newthread;
//        while (true) {
//            BufferedImage screenimage = null;
//            boolean tryagain = true;
//            while (tryagain) {
//                try {
//                    screenimage = getNewest().image;
//                    tryagain = false;
//                } catch (ArrayIndexOutOfBoundsException ex) {
//                    System.out.println("Trying:No Images");
//                    tryagain = true;
//                } catch (NoSuchFieldException ex) {
//                    System.out.println("Trying:No near image");
//                    tryagain = true;
//                }
//            }
//            System.out.println("Got Screen");
//            if (screenimage != null) {
//                screen.setImage(screenimage);
//                screen.repaint();
//                if (!screen.isVisible()) {
//                    screen.setVisible(true);
//                }
//                System.out.println("Dislpayed");
//            } else {
//                System.out.println("Image Null Skipped");
//            }
//        }
    }
}
