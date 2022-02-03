import java.awt.*;

public class Settings {

//DEFAULTS
    public static final int DEFAULT_MAIN_WINDOW_WIDTH = 1280, DEFAULT_MAIN_WINDOW_HEIGHT = 720;
    public static final String DEFAULT_MAIN_SCREEN_TITLE = "Polodna Robotics Desktop App";
    public static final String[] DEFAULT_PAGE_HEADERS = {"Main","Advanced","About"};
//STRINGS FOR DISPLAY MESSAGES
    public static final String ROBOT_IS_MOVING = "Please wait, the robot is still moving";
    public static final String ROBOT_IS_DECIDING = "Please wait, robot is still thinking";
    public static final String GAME_IS_OVER_COMPUTER_WIN = "<COMPUTER WIN MESSAGE HERE>";
    public static final String GAME_IS_OVER_PLAYER_WIN = "<PLAYER WIN MESSAGE HERE>";
    public static final String PLAYER_MOVE_WAS_VALID = "Move was valid, waiting for computer.";
    public static final String PLAYER_MOVE_WAS_NOT_VALID_PLAYER_OWNS = "That's already your space.";
    public static final String PLAYER_MOVE_WAS_NOT_VALID_COMPUTER_OWNS = "The computer has already taken that space.";
//COLORS
    public static final Color STARTING_COLOR = new Color(86, 150, 116);
    public static final Color STARTING_UNDER_GAME_BOARD_COLOR = new Color(0xB4B4B4);

//    TICKTOEBUTTON
    public static final Color PLAYERS_COLOR = Color.BLUE;
    public static final Color COMPUTERS_COLOR = Color.RED;
    public static final int DEFAULT_RADIUS_FOR_TICKTOE_BUTTONS = 80;//In pixels
    public static final int DEFAULT_BORDER_WIDTH_FOR_TICKTOE_BUTTONS = 3;//In pixels
}
