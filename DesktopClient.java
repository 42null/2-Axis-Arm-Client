import sun.awt.X11.Screen;

import java.awt.*;

public class DesktopClient {
    public GameLogic tickTackToeController = new GameLogic(new PhysicalBoardTracker());
//    ScreenUI screenUI = new ScreenUI(tickTackToeController);


//    DisplayController dispControl = new DisplayController();
//    DisplayScreen mainWindow;

    public static void main(String[] args) {
        new DesktopClient();
    }

    public DesktopClient(){
//        mainWindow = new DisplayScreen();
////        dispControl.asighnScreen(mainWindow);
////        dispControl.screen1.
//
////        mainWindow.createMainScreen();
//        createMainScreen(mainWindow);
        ArduinoConnector arduinoConnector = new ArduinoConnector();
        ScreenUI screenUI = new ScreenUI(tickTackToeController, arduinoConnector);

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                System.out.println("screenUI.createAndShowGUI()");
                screenUI.createAndShowGUI();
                System.out.println("Camera thread id = "+screenUI.startCameraThread());
            }
        });
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                System.out.println("Starting thread for Arduino");
                arduinoConnector.start();
                System.out.println("Arduino thread id = "+arduinoConnector.currentThread().getId());
            }
        });
    }

    public void createMainScreen(DisplayScreen dispScreen_){
        dispScreen_.addTopBar(DisplayScreen.Topbars.THEME1);


    }


}
