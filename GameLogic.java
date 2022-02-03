import java.util.Random;

/**
 * A class created to deal with the inputs and outputs of a tick-tack-toe board
 */

public class GameLogic {
    private TickToeButton[] _sharedButtons = new TickToeButton[9];
//STATUS OF BOARD
    private boolean _robotIsMoving = false;
    private boolean _robotIsDeciding = false;
    private boolean _gameIsOver = false;
    private Random _random = new Random();
    private int _moveNumber = 0;

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
//END GETTERS


    public GameLogic(){
//    public GameLogic(ScreenUI screenUI_){
//        this._screenUI = screenUI_;
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

        TickToeButton currentButton = _sharedButtons[buttonNumber_];
        switch(currentButton.getTileOwner()){
            case(0):

                currentButton.setOwner(2);
                currentButton.setColor(Settings.PLAYERS_COLOR);
                displayStatement = Settings.PLAYER_MOVE_WAS_VALID;
                _moveNumber++;
                checkForWin(buttonNumber_);
                break;
            case(1):
                displayStatement = Settings.PLAYER_MOVE_WAS_NOT_VALID_COMPUTER_OWNS;
                break;
            case(2):
                displayStatement = Settings.PLAYER_MOVE_WAS_NOT_VALID_PLAYER_OWNS;
                break;
        }

        /*
        "abcdefghijklmnopqrstuvwzyz012345".length(); // 6789"; If it exceeds this amount, it will soft crash
         */
        if(displayStatement.length() > 32){
            //SPLIT UP THE RESPONSE
//            String[] words = displayStatement.split("\\s+");
            String[] words = displayStatement.split(" ");
            displayStatement = "";
            int goalPerRow = displayStatement.length() / 32;
            int lengthOfLineCurrently = 0;
            for (String word: words) {
                if(lengthOfLineCurrently+word.length()<31){//Space Included
                    displayStatement+=(" "+word);
                    lengthOfLineCurrently += word.length()+1;
                }else{
                    displayStatement+="<br/>"+word;
                    lengthOfLineCurrently = word.length()+1;
                }
            }
        }
        return "<html>"+displayStatement+"</html>";
    }

    public String playComputerMove(ComputerPlayStyles mode_){
        if(_moveNumber > 8){
            return "Game is already over";
        }

        int buttonNumber = _random.nextInt(9);
        String displayStatement = "Error: Computer did not write out to this return statement.";

        if(mode_ == ComputerPlayStyles.RANDOM){
            TickToeButton currentButton = _sharedButtons[buttonNumber];
            while(currentButton.getTileOwner() != 0) {
                System.out.println("Attempt at random square was not successfull: \"Already Owned\"");
                currentButton = _sharedButtons[_random.nextInt(9)];
            }
            currentButton.setOwner(1);
            currentButton.setColor(Settings.COMPUTERS_COLOR);
            displayStatement = "Computer made a move";
            _moveNumber++;
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
