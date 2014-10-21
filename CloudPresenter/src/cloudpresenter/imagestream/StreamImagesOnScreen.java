/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudpresenter.imagestream;

import cloudpresenter.gui.ViewImageUI;

/**
 *
 * @author Nawanjana
 */
public class StreamImagesOnScreen extends Thread {

    private ViewImageUI screen;
    private final ImageStream stream;
    private boolean interrupt;
    private static boolean running;

    public StreamImagesOnScreen(ImageStream stream) {
        this.stream = stream;
        screen = new ViewImageUI(this);
    }

    public void interruptScreen() {
        interrupt = true;
    }

    @Override
    public void run() {
        interrupt = false;
        running = true;
        while (!interrupt) {
            try {
                ScreenFrame screenimage = null;
                boolean tryagain = true;
                while (tryagain) {
                    try {
                        screenimage = stream.getNewest();
                        tryagain = false;
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        tryagain = true;
                    } catch (NoSuchFieldException ex) {
                        tryagain = true;
                    }
                }
                if (screenimage != null) {
                    screen.setImage(screenimage.image);
                    screen.repaint();

                } else {
                    System.out.println("Image Null Skipped");
                }
                stream.imagestream.remove(screenimage);
            } catch (Exception ex) {
                System.err.println(ex);
            }

        }
        running = false;
    }

    public void showScreen() {
        if (running) {
            screen.setVisible(true);
        }
    }
}
