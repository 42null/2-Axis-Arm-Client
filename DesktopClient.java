import java.awt.*;

public class DesktopClient {
    GameLogic tickTackToeController = new GameLogic();
    DisplayScreen mainWindow;


    public static void main(String[] args) {
        new DesktopClient();
    }

    public DesktopClient(){
        mainWindow = new DisplayScreen();
    }

}
