import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.*;

import org.opencv.core.*;
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

    static VideoCapture video;

    public CameraCapture(JLabel streamBox_){
        this.streamingBox = streamBox_;
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        this.video = new VideoCapture(-1);
    }

    /**
     * Thread run under the main() class
     */
    public void run(){
        try {
            keepLooping = true;
            this.currentThreadID = Thread.currentThread().getId();
            System.out.println("Thread " + this.currentThreadID + " is running");

            while(keepLooping){
                Mat displayMat = lookForCircles(getNewImageFromStream());
                MatOfByte matOfByte = new MatOfByte();
                Imgcodecs.imencode(".jpg", displayMat, matOfByte);
                streamingBox.setIcon(new ImageIcon(ImageIO.read(new ByteArrayInputStream(matOfByte.toArray()))));
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Mat lookForCircles(Mat original_){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat processedMat = original_.clone();
//        long t0 = System.currentTimeMillis();
        Mat rgb = new Mat();
        Mat hsv = new Mat();
        Mat gray = new Mat();

/*COOL - YELLOW FLIP
        Core.inRange(rgb, new Scalar(0, 0, 100), new Scalar(100, 100, 255), processedMat);
        Mat newMat = new Mat();
        Core.bitwise_not(original_, processedMat);*/

        Imgproc.cvtColor( processedMat, rgb, Imgproc.COLOR_BGRA2RGB );
        Imgproc.cvtColor( rgb, hsv, Imgproc.COLOR_RGB2HSV );

        Core.inRange(rgb, new Scalar(0, 0, 100), new Scalar(100, 100, 255), processedMat);//Now blue
//        Imgproc.GaussianBlur( processedMat, processedMat, new Size( 25, 25 ), 0, 0 );
        Mat newMat = new Mat();
        Core.bitwise_not(processedMat, newMat);
        original_.copyTo(newMat, newMat);
        //        processedMat.copyTo(original_);
        return newMat;
    }

    public Mat getNewImageFromStream() {
        try {
            Mat frameMat = new Mat();
            this.video.read(frameMat);
            Imgproc.resize(frameMat, frameMat, new Size(640, 480));
            MatOfByte matOfByte = new MatOfByte();
            Imgcodecs.imencode(".jpg", frameMat, matOfByte);
//            return new ImageIcon(ImageIO.read(new ByteArrayInputStream(matOfByte.toArray())));
            return frameMat;
        }catch(Exception e){e.printStackTrace();}
        return new Mat();
//        return new ImageIcon("CameraCapture did not work");
    }

}