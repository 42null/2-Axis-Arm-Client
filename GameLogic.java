import org.opencv.core.Point;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * A class created to deal with the inputs and outputs of a tick-tack-toe board
 */

public class GameLogic {
    private TickToeButton[] _sharedButtons = new TickToeButton[9];
    private PhysicalBoardTracker _boardTracker;

//STATUS OF BOARD
    private boolean _robotIsMoving = false;
    private boolean _robotIsDeciding = false;
    private boolean _gameIsOver = false;
    private Random _random = new Random();
    private int _moveNumber = 1;
    private String _stadiumBannerText = "For future stadium board segment";
    private int currentColorBeingLookedAt = 2;

    public void locatePhysicalLocations(Point[] pointsToSearch, int width, int height) {
        _boardTracker.detectAndSetAllSpaces(pointsToSearch, width, height);
    }

    /**
     * Return changed space
     * @param pointsToSearch
     * @return
     */
    public TickToeButton checkSpaces(Point[] pointsToSearch){
//        int[] foundTiles = _boardTracker.checkForAllPosistions(pointsToSearch).stream().mapToInt(i -> i).toArray();
        ArrayList<Integer> foundTiles = _boardTracker.checkForAllPosistions(pointsToSearch);
        ArrayList<Integer> foundNewTiles = _boardTracker.checkForNewPieces(foundTiles);
        System.out.println("foundNewTiles = "+foundNewTiles);
        TickToeButton newlyFoundTile = _sharedButtons[foundNewTiles.get(0)];//TODO: Wrap to make safe with null
        return newlyFoundTile;
    }



    enum ComputerPlayStyles{
        RANDOM(0),
        RANDOM_WITH_BLOCK(1);

        private final int _representiveNumber;
        ComputerPlayStyles(int number_) {
            this._representiveNumber = number_;
        }
    }
//GETTERS
    public int getMoveNumber(){return _moveNumber;}
    public boolean isGameOver(){return _gameIsOver;}
//END GETTERS


    public GameLogic(PhysicalBoardTracker boardTracker){
        this._boardTracker = boardTracker;
    }

    public TickToeButton[] getBoardButtons(){return _sharedButtons;}

    public String leftClickedBoardButton(int buttonNumber_){
        String displayStatement = "ERROR: Did not change my statement";

        if(_robotIsMoving){
            displayStatement = Settings.ROBOT_IS_MOVING;
        }else if(_robotIsDeciding){
            displayStatement = Settings.ROBOT_IS_DECIDING;
        }else if(_gameIsOver){
            //TODO: Finish this if
        }

        String moveResponse = playMove(true, buttonNumber_);
        /*if(moveResponse != "")*/ displayStatement = moveResponse;
//        else return "";
        return moveResponse;
    }

    public String playMove(boolean playerMove_, int buttonPicked_) {//0 for no win, 1 for a win, -1 for unable to obtain location, 2 = game over
        if (_moveNumber > 8) return "All spaces have been filled";

        String moveResponse = "";
        int locationsOwner = _sharedButtons[buttonPicked_].getTileOwner();
        if (locationsOwner == 0) {//Not owned by anyone yet
            _sharedButtons[buttonPicked_].setOwner(playerMove_? 2:1);
            _sharedButtons[buttonPicked_].setColor(playerMove_ ? Settings.PLAYERS_COLOR : Settings.COMPUTERS_COLOR);
            moveResponse = playerMove_? Settings.PLAYER_MOVE_WAS_VALID : "Computer made a move";
            _moveNumber++;
            switch (checkForWin(buttonPicked_)) {
                case (0):// No one won
                    break;
                case (1):// Computer won
                    moveResponse = Settings.GAME_IS_OVER_COMPUTER_WIN;
                    _gameIsOver = true;
                    break;
                case (2):// Player won
                    moveResponse = Settings.GAME_IS_OVER_PLAYER_WIN;
                    _gameIsOver = true;
                    break;
            }
        }else if(locationsOwner == (playerMove_ ? 2 : 1)) {//Owned by the player already
            if(playerMove_) moveResponse = Settings.PLAYER_MOVE_WAS_NOT_VALID_PLAYER_OWNS;
        }else{//Owned by opposing player
            if (playerMove_) moveResponse = Settings.PLAYER_MOVE_WAS_NOT_VALID_COMPUTER_OWNS;
        }

        return moveResponse;
    }


    public String playComputerMove(ComputerPlayStyles mode_){
        int buttonNumber;

//        if(moveResponse != "") displayStatement = moveResponse;
//        else return "Error: Computer did not write out to this return statement.\"";

        if(mode_ == ComputerPlayStyles.RANDOM){
            buttonNumber = _random.nextInt(9);
            while(_sharedButtons[buttonNumber].getTileOwner() != 0) {
                System.out.println("Attempt at random square was not successfull: \"Already Owned by someone\"");
                buttonNumber = _random.nextInt(9);
            }
            String moveResponse = playMove(false, buttonNumber);
            return moveResponse;
        }else{
        }
        return "That mode does not exist";
    }

    /**
     * Returns an integer representing the win state
     * @param buttonNumber_
     * @return 0 for no win,  1  or 2 for a win (the player number)
     */
    public int checkForWin(int buttonNumber_){
        int buttonX = buttonNumber_ % 3;
        int buttonY = buttonNumber_ / 3;
        int checkingFor = _sharedButtons[buttonNumber_].getTileOwner();//Should never be 0

        //CHECK HORIZONTAL
        for(int i=0;i<3;i++){
            if(_sharedButtons[buttonY*3+i].getTileOwner()==checkingFor){
                if(i==2){
                    System.out.println("HORIZONTAL WIN by "+(checkingFor==2?"player":"computer"));
                    for(int j=0;j<3;j++) { _sharedButtons[buttonY*3+j].setBorder(BorderFactory.createLineBorder(Color.GREEN, 5)); ; }
                    return checkingFor;
                }
            }else{
                i = 3;
            }
        }
        //CHECK VERTICAL
        for(int i=0;i<3;i++){
            if(_sharedButtons[3*i+buttonX].getTileOwner()==checkingFor){
                if(i==2){
                    System.out.println("VERTICAL WIN by "+(checkingFor==2?"player":"computer"));
                    for(int j=0;j<3;j++) { _sharedButtons[3*j+buttonX].setBorder(BorderFactory.createLineBorder(Color.GREEN, 5)); ; }
                    return checkingFor;
                }
            }else{
                i = 3;
            }
        }
        //CHECK DIAGONAL (\)
        if(buttonNumber_ % 4 == 0){
            for(int i=0;i<3;i++){
                if(_sharedButtons[3*i+i].getTileOwner()==checkingFor){
                    if(i==2){
                        System.out.println("DIAGONAL WIN \\ by "+(checkingFor==2?"player":"computer"));
                        for(int j=0;j<3;j++) { _sharedButtons[3*j+j].setBorder(BorderFactory.createLineBorder(Color.GREEN, 5)); ; }
                        return checkingFor;
                    }
                }else{
                    i = 3;
                }
            }
        }
        //CHECK DIAGONAL (/)
        if(buttonNumber_ % 4 == 2){
            for(int i=0;i<3;i++){
                if(_sharedButtons[3*i+2-i].getTileOwner()==checkingFor){
                    if(i==2){
                        System.out.println("DIAGONAL WIN / by "+(checkingFor==2?"player":"computer"));
                        for(int j=0;j<3;j++) { _sharedButtons[3*j+2-j].setBorder(BorderFactory.createLineBorder(Color.GREEN, 5)); ; }
                        return checkingFor;
                    }
                }else{
                    i = 3;
                }
            }
        }

        return 0;
    }

    public void resetGame(){
        for (int i = 0; i < _sharedButtons.length; i++) {
            _gameIsOver = false;
            _sharedButtons[i].setOwner(0);
            _sharedButtons[i].setBackground(Settings.STARTING_COLOR);
            _sharedButtons[i].setBorder(new TickToeButton.RoundButton(Settings.DEFAULT_RADIUS_FOR_TICKTOE_BUTTONS));
            _moveNumber = -1;
            currentColorBeingLookedAt = 2;
        }
    }

}
