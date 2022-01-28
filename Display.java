//import javax.swing.*;
//import java.awt.*;
//
//public class Display extends Canvas implements Runnable{
//    private static final long serialVersionUID = 1L;
//
//    private Thread thread;
//    JFrame frame;
//    private static String title = "3D Renderer";
//    private static final int WIDTH = 800;
//    private static final int HEIGHT = 600;
//    private boolean running = false;
//
//    public Display(){
//        this.frame = new JFrame();
//
//        Dimension frameDimensions = new Dimension(WIDTH, HEIGHT);
//        this.setPreferredSize(frameDimensions);
//    }
//
//    public synchronized void start(){
//        this.running = true;
//        this.thread = new Thread(this, "Display");
//        this.thread.start();
//    }
//    public synchronized void stop() throws InterruptedException {
//        running = false;
//        this.thread.join();
//    }
//
//    @Override
//    public void run() {
//    }
//
//
//}
