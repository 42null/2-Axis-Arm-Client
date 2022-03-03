import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;
import javax.swing.*;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class CameraCapture extends Thread {
    private long currentThreadID = -1;//-1 = unset
    private boolean keepLooping = false;//TODO: Switch to interval
    private JLabel streamingBox;
    private static int[] circleSizeRange = {20, 50};
    private static int circleThreshold = 70;



    //GETTERS
    public long getCurrentThreadID() {return currentThreadID;}
    public static int getCircleSizeRange(boolean isMin_) {return circleSizeRange[ isMin_ ? 0:1 ];}
    public static int getCircleThreshold() {return circleThreshold;}
    //END GETTERS
    //SETTERS
    public static void changeCircleMinRadius(int newLow_) {circleSizeRange[0]=newLow_;}
    public static void changeCircleMaxRadius(int newHigh_) {circleSizeRange[1]=newHigh_;}
    public static void changeCircleThreshold(int newValue_) {circleThreshold=newValue_;}
    //END SETTERS

    static VideoCapture video;

    public CameraCapture(JLabel streamBox_){
        this.streamingBox = streamBox_;
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        this.video = new VideoCapture(-1);
    }

    public static void settingWasUpdated(final String title_, final int newValue_) {
        switch(title_){
            case Settings.VISION_SETTING_MESSAGE_MIN:
                changeCircleMinRadius(newValue_);//TODO: Right now is static, change for future.
                break;
            case Settings.VISION_SETTING_MESSAGE_MAX:
                changeCircleMaxRadius(newValue_);//TODO: Right now is static, change for future.
                break;
            case Settings.VISION_SETTING_MESSAGE_THRESHOLD:
                changeCircleThreshold(newValue_);//TODO: Right now is static, change for future.
                break;
            default:
                System.out.println("!!!ERROR!!!: settingWasUpdated was not able to find a setter matching the provided title \""+title_+"\"");
        }

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
                Mat displayMat = filterForColor(getNewImageFromStream());

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

    public Mat filterForColor(Mat original_){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat processedMat = original_.clone();
//        long t0 = System.currentTimeMillis();
        Mat rgb = new Mat();
        Mat hsv = new Mat();
        Mat gray = new Mat();

        Mat overlay = new Mat();

/*COOL - YELLOW FLIP
        Core.inRange(rgb, new Scalar(0, 0, 100), new Scalar(100, 100, 255), processedMat);
        Mat newMat = new Mat();
        Core.bitwise_not(original_, processedMat);*/

        Imgproc.cvtColor( processedMat, rgb, Imgproc.COLOR_BGRA2RGB );
        Imgproc.cvtColor( rgb, hsv, Imgproc.COLOR_RGB2HSV );

        Core.inRange(rgb, new Scalar(0, 0, 90), new Scalar(90, 90, 255), processedMat);//Now blue
        Mat newMat = new Mat();
        Core.bitwise_not(processedMat, newMat);
        Core.bitwise_not(newMat, newMat);
        Mat evenNewMat = new Mat();// = new Mat(processedMat.cols(),processedMat.rows(),1);
//        Imgproc.floodFill(evenNewMat, evenNewMat, new Point(evenNewMat.cols() + 2, evenNewMat.rows() + 2), new Scalar(255,100,100));
//        newMat.setTo(new Scalar(0,220,0));
        original_.copyTo(evenNewMat, newMat);
        Imgproc.cvtColor(evenNewMat,evenNewMat,Imgproc.COLOR_BGR2Lab);
        Mat evenEvenNewMat = new Mat();
        evenNewMat.copyTo(evenEvenNewMat,newMat);
        evenEvenNewMat.copyTo(original_,newMat);


        // Imgproc.GaussianBlur(gray, gray, new Size(20, 20), 1);
        gray = processedMat;
        Imgproc.GaussianBlur(gray, gray, new Size(25, 25), 0, 0);


//        Imgproc.cvtColor(gray, original_, Imgproc.COLOR_GRAY2BGRA);

        Mat circles = new Mat();

        double cannyThreshhold = 10;
        int circleProximity = 2 * circleSizeRange[0];
        int accumulatorScale = 2;


        Imgproc.HoughCircles(gray, circles, Imgproc.CV_HOUGH_GRADIENT, accumulatorScale, circleProximity, cannyThreshhold, circleThreshold, circleSizeRange[0], circleSizeRange[1]);

        // Imgproc.Canny(gray, gray, Math.max(1, cannyThreshhold/2), cannyThreshhold); // approximates internal
        // HoughCircles processing



        for (int i = 0; i < Math.min(5, circles.cols()); i++) {
            double[] data = circles.get(0, i);

            int x = (int) data[0];
            int y = (int) data[1];
            int r = (int) data[2];

            Point center = new Point(x, y);

            // all circle outline
//            Imgproc.circle(overlay, center, (int)r, new Scalar(0,255,0), 3);
            Imgproc.circle(original_, center, (int)r, new Scalar(Color.MAGENTA.getRed(),Color.MAGENTA.getGreen(),Color.MAGENTA.getBlue()) , 3);
        }

        Imgproc.rectangle(original_, new Point(50,100),new Point(60,110), new Scalar(Settings.CYAN_DARKER.getRed(),Settings.CYAN_DARKER.getGreen(),Settings.CYAN_DARKER.getBlue(), 255), 2);

        return original_;
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