import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.*;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class CameraCapture extends Thread {
    private long currentThreadID = -1;//-1 = unset
    private boolean keepLooping = false;//TODO: Switch to interval
    private JLabel streamingBox;

//GETTERS
    public long getCurrentThreadID() {return currentThreadID;}
//END GETTERS

        public void run(){
            try {
                keepLooping = true;
                this.currentThreadID = Thread.currentThread().getId();
                System.out.println("Thread " + this.currentThreadID + " is running");

                while(keepLooping){
                    streamingBox.setIcon(getFrame());
                }


            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

    static VideoCapture _video;
    public static BufferedImage _bufImage = null;

    public CameraCapture(JLabel streamBox_){
        this.streamingBox = streamBox_;
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        _video = new VideoCapture(-1);
    }


    public ImageIcon getFrame() {
        try {
            Mat frameMat = new Mat();
            _video.read(frameMat);
            Imgproc.resize(frameMat, frameMat, new Size(640, 480));
            MatOfByte m = new MatOfByte();
            Imgcodecs.imencode(".jpg", frameMat, m);
            byte[] byteArray = m.toArray();
            BufferedImage bufImage = null;
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
            JFrame frame = new JFrame("TEST");
            frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
            return new ImageIcon(bufImage);
        }catch(Exception e){e.printStackTrace();}

        return new ImageIcon("CameraCapture did not work");
    }




//    public static void showResult(Mat img) {
//        Imgproc.resize(img, img, new Size(640, 480));
//        MatOfByte m = new MatOfByte();
//        Imgcodecs.imencode(".jpg", img, m);
//        byte[] byteArray = m.toArray();
//        BufferedImage bufImage = null;
//        try {
//            InputStream in = new ByteArrayInputStream(byteArray);
//            bufImage = ImageIO.read(in);
//            JFrame frame = new JFrame();
//            frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
//            frame.pack();
//            frame.setVisible(true);
//            try {
//                Thread.sleep(200);
//            } catch (InterruptedException e) {
////                e.printStackTrace();
//            }
//            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}