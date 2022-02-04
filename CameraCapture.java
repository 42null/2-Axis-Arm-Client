import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class CameraCapture {
    VideoCapture video;

    public CameraCapture(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        video = new VideoCapture(-1);
//        Mat f = new Mat();
//        while (true){
//            video.read(f);
//            showResult(f);
    }

    public JFrame getFrame() throws IOException {

        Mat frameMat = new Mat();
        video.read(frameMat);

//        VideoCapture video = new VideoCapture(0);
        Imgproc.resize(frameMat, frameMat, new Size(640, 480));
        MatOfByte m = new MatOfByte();
        Imgcodecs.imencode(".jpg", frameMat, m);
        byte[] byteArray = m.toArray();
        BufferedImage bufImage = null;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
            JFrame frame = new JFrame();
            frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
            return frame;
        }catch(Exception e){}

        return null;
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