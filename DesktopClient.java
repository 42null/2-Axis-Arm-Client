import java.awt.*;

public class DesktopClient {
    GameLogic tickTackToeController = new GameLogic();
    DisplayController dispControl = new DisplayController();
    DisplayScreen mainWindow;

    public static void main(String[] args) {
        new DesktopClient();
    }

    public DesktopClient(){
        mainWindow = new DisplayScreen();
//        dispControl.asighnScreen(mainWindow);
//        dispControl.screen1.

//        mainWindow.createMainScreen();
        createMainScreen(mainWindow);
    }

    public void createMainScreen(DisplayScreen dispScreen_){
        dispScreen_.addTopBar(DisplayScreen.Topbars.THEME1);


    }


}
