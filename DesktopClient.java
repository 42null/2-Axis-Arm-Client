import sun.awt.X11.Screen;

import java.awt.*;

public class DesktopClient {
    public GameLogic tickTackToeController = new GameLogic();
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
        ScreenUI screenUI = new ScreenUI(tickTackToeController);

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                System.out.println("screenUI.createAndShowGUI()");
                screenUI.createAndShowGUI();
                System.out.println("Camera thread id = "+screenUI.startCameraThread());
            }
        });

    }

    public void createMainScreen(DisplayScreen dispScreen_){
        dispScreen_.addTopBar(DisplayScreen.Topbars.THEME1);


    }


}
