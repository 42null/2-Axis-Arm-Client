/**
 * A class created to deal with the inputs and outputs of a tick-tack-toe board
 */

public class GameLogic {
    private TickToeButton[] _sharedButtons = new TickToeButton[9];
//STATUS OF BOARD
    private boolean _robotIsMoving = false;
    private boolean _robotIsDeciding = false;
    private boolean _gameIsOver = false;



    public GameLogic(){
//    public GameLogic(ScreenUI screenUI_){
//        this._screenUI = screenUI_;
    }

    public TickToeButton[] getBoardButtons(){return _sharedButtons;}

    public String leftClickedBoardButton(int buttonNumber_){
        String displayStatement = "ERROR: Could not come up with response";

        if(_robotIsMoving){
            return Settings.ROBOT_IS_MOVING;
        }else if(_robotIsDeciding){
            return Settings.ROBOT_IS_DECIDING;
        }else if(_gameIsOver){
            //TODO: Finish this if
        }

        TickToeButton currentButton = _sharedButtons[buttonNumber_];
        switch(currentButton.getTileOwner()){
            case(0):

                currentButton.setOwner(2);
                currentButton.setColor(Settings.PLAYERS_COLOR);
                checkForWin(buttonNumber_);
                break;
            case(1):
                break;
            case(2):
                break;
        }
        return displayStatement;
    }


    public int checkForWin(int buttonNumber_){// 0 for no win,  1 for computer win, 2 for player win
        TickToeButton currentButton = _sharedButtons[buttonNumber_];
        int inARow = 0;
        int buttonX = buttonNumber_ % 3;
        int buttonY = buttonNumber_ / 3;


//        //CHECK ALL HORIZONTAL
//        for(int i=0;i<3;i++){
//            for(int j=0;j<3;j++){
//                switch(currentButton){
//                    case():
//                        break;
//                }
//            }
//        }

        System.out.println("X = "+(buttonX+1));
        System.out.println("Y = "+(buttonY+1));

        return 0;
    }


}
