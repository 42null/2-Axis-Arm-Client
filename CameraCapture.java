import java.awt.*;
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
    private static int circleThreshold = 70;//67
    private static DisplayModes displayType = DisplayModes.NORMAL;
    public static Point[] detectedCorners = new Point[2];//TODO: Change
    private static Point[] allCircleLocations = null;
    private static Dimension circleDimensions = new Dimension(35,35);
    private boolean overlayCorners = true;
    public boolean keepCorners = false;
    public static Point[] savedCorners = new Point[2];
    public static Dimension savedCircleDimensions = new Dimension();
    public static boolean saveNextCorners = false;

    enum DisplayModes {
        NORMAL(1),
        GREYSCALE(2);

        final int id;
        DisplayModes(int modeId) { this.id = modeId; }
    }

    //GETTERS
    public long getCurrentThreadID() {return currentThreadID;}
    public static int getCircleSizeRange(boolean isMin_) {return circleSizeRange[ isMin_ ? 0:1 ];}
    public static int getCircleThreshold() {return circleThreshold;}
    public Point[] getCirclePoints() {return allCircleLocations;}
    public Point[] getSavedCorners() {return savedCorners;}
    public Dimension getCircleDimensions() {return circleDimensions;}
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
    //TODO: Merge with settingsWasUpdate?
    public void setOutputDisplay(DisplayModes newId){
        this.displayType = newId;
        System.out.println(this.displayType);
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
//        original_.submat(new Rect(100,100,100,100). processedMat;
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

        Core.inRange(rgb, new Scalar(0, 0, 90), new Scalar(90, 100, 255), processedMat);//Now blue
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
        Imgproc.GaussianBlur(gray, gray, new Size(25, 25), 0, 0);


//        Imgproc.cvtColor(gray, original_, Imgproc.COLOR_GRAY2BGRA);

        Mat circles = new Mat();

        double cannyThreshhold = 10;
        int circleProximity = 2 * circleSizeRange[0];
        int accumulatorScale = 2;


        Imgproc.HoughCircles(gray, circles, Imgproc.CV_HOUGH_GRADIENT, accumulatorScale, circleProximity, cannyThreshhold, circleThreshold, circleSizeRange[0], circleSizeRange[1]);
        
        
        Mat returnMat;
        switch(this.displayType){
            case NORMAL:
                returnMat = original_;
                break;
            case GREYSCALE:
                returnMat = gray;
                break;
            default:
                returnMat = original_;
        }
        detectedCorners = new Point[2];//TODO: Make it find corners instead of just using first two points and not run every time

        int detectedRadius = 0;
        allCircleLocations = new Point[circles.cols()];
        for (int i = 0; i < circles.cols(); i++) {
            double[] data = circles.get(0, i);

            int x = (int) data[0];
            int y = (int) data[1];
            int r = (int) data[2];

            Point center = new Point(x, y);
            if(i < 2){
                detectedCorners[i]=new Point(x,y);
                circleDimensions.width = 2*r;//TODO: Make work with circles
                circleDimensions.height = 2*r;//TODO: Make work with circles

                if(i==1){
                    if(detectedCorners[0].y < detectedCorners[1].y){
                        Point tmpPoint = detectedCorners[0];
                        detectedCorners[0] = detectedCorners[1];
                        detectedCorners[1] = tmpPoint;
                    }
                }
            }
            if(i == 0){detectedRadius = r;}
            if(i == 1){detectedRadius+= r; detectedRadius/=2;}

//            Imgproc.circle(overlay, center, (int)r, new Scalar(0,255,0), 3);
//            Imgproc.circle(returnMat, center, (int)r, new Scalar(Color.MAGENTA.getRed(),Color.MAGENTA.getGreen(),Color.MAGENTA.getBlue()) , 3);//Magenta circle at the size of the circle


            if(savedCorners[1]==null){
                Imgproc.line(returnMat, new Point(center.x-(int)r/2,center.y), new Point(center.x+(int)r/2,center.y), new Scalar(Color.MAGENTA.getRed(),Color.MAGENTA.getGreen(),Color.MAGENTA.getBlue()) , 4);//Magenta line at the size of the circle
                Imgproc.line(returnMat, new Point(center.x,center.y-(int)r/2), new Point(center.x,center.y+(int)r/2), new Scalar(Color.MAGENTA.getRed(),Color.MAGENTA.getGreen(),Color.MAGENTA.getBlue()) , 4);//Magenta line at the size of the circle
            }else{
                Imgproc.line(returnMat, new Point(center.x-(int)r/2,center.y), new Point(center.x+(int)r/2,center.y), new Scalar(0,255,255,255) , 1);//Yellow line at the size of the circle
                Imgproc.line(returnMat, new Point(center.x,center.y-(int)r/2), new Point(center.x,center.y+(int)r/2), new Scalar(0,255,255,255) , 1);//Yellow line at the size of the circle
            }

            //InnerArea
            if(false && i == 1)
                Imgproc.rectangle(returnMat,detectedCorners[0], detectedCorners[1], new Scalar(150,150,150,150), -1);

            //Add to saved posistions
            allCircleLocations[i] = center.clone();//TODO:
        }


        if(!keepCorners && detectedCorners[0]!=null && detectedCorners[1]!=null){
            detectedCorners[0].x-=25;
            detectedCorners[0].y+=25;
            detectedCorners[1].x+=25;
            detectedCorners[1].y-=25;
        }

        if(saveNextCorners && detectedCorners[0]!=null && detectedCorners[1]!=null){
            savedCorners = detectedCorners.clone();
            savedCircleDimensions = new Dimension(circleDimensions.width,circleDimensions.height);//TODO: find a better way
            saveNextCorners = false;
        }
//        Imgproc.rectangle(returnMat, new Point(50,100),new Point(60,110), new Scalar(Settings.CYAN_DARKER.getRed(),Settings.CYAN_DARKER.getGreen(),Settings.CYAN_DARKER.getBlue(), 255), 2);
//        Imgproc.rectangle(returnMat, new Point(156.0,171.0),new Point(206.0,221.0), new Scalar(255,255,0,255), 5);//@@@



        if(savedCorners[0]!=null && savedCorners[1]!=null && overlayCorners){
            Point[] withExtraBorderSavedCorners = new Point[]{new Point(savedCorners[0].x-savedCircleDimensions.width*.1, savedCorners[0].y+savedCircleDimensions.height*.1), new Point(savedCorners[1].x+savedCircleDimensions.width*.1,savedCorners[1].y-savedCircleDimensions.height*.1)};
            returnMat = drawOnCorners(returnMat, withExtraBorderSavedCorners);
            if(PhysicalBoardTracker._physicalSpaces[0]!=null){
                PhysicalBoardTracker.cheatingBoundsOverlay(returnMat);
            }
        }else if(detectedCorners[0] != null && detectedCorners[1] != null && overlayCorners){
            if(!keepCorners){
                returnMat = drawOnCorners(returnMat, detectedCorners);
            }

        }

        return returnMat;
    }

    public Mat drawOnCorners(Mat mat, Point[] corners){

        Point[] tmpPoints = corners.clone();

        if(corners[1].y > corners[0].y){
            corners[0] = tmpPoints[1];
            corners[1] = tmpPoints[0];
        }

        int r = 10;
        Imgproc.line(mat, new Point(corners[0].x-(int)r/2,corners[0].y), new Point(corners[0].x+(int)r/2,corners[0].y), new Scalar(0,255,255,255) , 1);//Yellow line at the size of the circle
        Imgproc.line(mat, new Point(corners[0].x,corners[0].y-(int)r/2), new Point(corners[0].x,corners[0].y+(int)r/2), new Scalar(0,255,255,255) , 1);//Yellow line at the size of the circle
        Imgproc.line(mat, new Point(corners[1].x-(int)r/2,corners[1].y), new Point(corners[1].x+(int)r/2,corners[1].y), new Scalar(0,255,255,255) , 1);//Yellow line at the size of the circle
        Imgproc.line(mat, new Point(corners[1].x,corners[1].y-(int)r/2), new Point(corners[1].x,corners[1].y+(int)r/2), new Scalar(0,255,255,255) , 1);//Yellow line at the size of the circle

        Imgproc.rectangle(mat, new Point(corners[0].x-10, corners[0].y-10), new Point(corners[0].x+10, corners[0].y+10), toScalar(Color.ORANGE), 2);
        Imgproc.rectangle(mat, new Point(corners[1].x-10, corners[1].y-10), new Point(corners[1].x+10, corners[1].y+10), toScalar(Color.ORANGE), 2);

        Imgproc.rectangle(mat, new Point(corners[0].x, corners[0].y), new Point(corners[1].x, corners[1].y), new Scalar(0, 165, 255,50), 0);

        int width = (int) (corners[0].x - corners[1].x);
        int height = (int) (corners[0].y - corners[1].y);

        Imgproc.line(mat, new Point(corners[0].x-width/3, corners[0].y), new Point(corners[0].x-width/3, corners[1].y), new Scalar(0, 165, 255,50), 1);
        Imgproc.line(mat, new Point(corners[0].x-width*2/3, corners[0].y), new Point(corners[0].x-width*2/3, corners[1].y), new Scalar(0, 165, 255,50), 1);
        Imgproc.line(mat, new Point(corners[1].x, corners[0].y-height/3), new Point(corners[0].x, corners[0].y-height/3), new Scalar(0, 165, 255,50), 1);
        Imgproc.line(mat, new Point(corners[1].x, corners[0].y-2*height/3), new Point(corners[0].x, corners[0].y-2*height/3), new Scalar(0, 165, 255,50), 1);

//        Imgproc.rectangle(mat, new Point(corners[1].x-2*width/3.5, corners[0].y), new Point(corners[1].x-width/3.5, corners[0].y-height/3), new Scalar(20,20,200,50), 6);

        Imgproc.rectangle(mat, new Point(corners[1].x-2*width/3.5, corners[0].y), new Point(corners[1].x-width/3.5, corners[1].y), new Scalar(255, 165, 255,50), 1);
        Imgproc.line(mat, new Point(corners[1].x-2*width/3.5, corners[0].y-height/3), new Point(corners[1].x-width/3.5, corners[0].y-height/3), new Scalar(255, 165, 255,50), 1);
        Imgproc.line(mat, new Point(corners[1].x-2*width/3.5, corners[0].y-2*height/3), new Point(corners[1].x-width/3.5, corners[0].y-2*height/3), new Scalar(255, 165, 255,50), 1);

        Imgproc.rectangle(mat, new Point(corners[0].x+width/4, corners[0].y), new Point(corners[0].x+2*width/3.5, corners[1].y), new Scalar(255, 165, 255,50), 1);
        Imgproc.line(mat, new Point(corners[0].x+width/4, corners[0].y-height/3), new Point(corners[0].x+2*width/3.5, corners[0].y-height/3), new Scalar(255, 165, 255,50), 1);
        Imgproc.line(mat, new Point(corners[0].x+width/4, corners[0].y-2*height/3), new Point(corners[0].x+2*width/3.5, corners[0].y-2*height/3), new Scalar(255, 165, 255,50), 1);

        return mat;
    }


    public void toggleDisplayCorners(){
        overlayCorners = !overlayCorners;
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

    public static Scalar toScalar(Color color){//TODO: Move method location
        return new Scalar(color.getRed(),color.getGreen(),color.getBlue());
//        return new Scalar(color.getRGB());
    }
}