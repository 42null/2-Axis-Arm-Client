import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ScreenUI extends JPanel implements ActionListener {
    TickToeButton[] _boardButtons;
    private static GameLogic _gameLogic;
    private CameraCapture _cameraController;

    //MUTABLE LABELS
    private JLabel _underGameBoard;
    private JPanel simulatedSideBoard = new JPanel();

    private JTabbedPane tabbedPane = new JTabbedPane();
    private JPanel streamPanel = new JPanel();
    private JLabel tickToeStream = new JLabel("Computer: <Place Status Here>");
    private JPanel optionsPannel = new JPanel();//new GridBagLayout());
    GridBagConstraints constraints = new GridBagConstraints();

    private JPanel visionSettings = Setting.initializeSettingsPanel( "Settings",
            new Setting( Settings.VISION_SETTING_MESSAGE_MIN, 1, 255, CameraCapture.getCircleSizeRange(true) ),//Setting to 0 will crash in an unrecoverable state for the view
            new Setting( Settings.VISION_SETTING_MESSAGE_MAX, 0, 255, CameraCapture.getCircleSizeRange(false) ),
            new Setting( Settings.VISION_SETTING_MESSAGE_THRESHOLD, 1, 255, CameraCapture.getCircleThreshold() ));//Setting to 0 will crash in an unrecoverable state for the view

    public enum Borders{
        WIN_BY_COMPUTER_SQUARE(Settings.COMPUTERS_COLOR, 5, true),
        WIN_BY_PLAYER_SQUARE(Settings.PLAYERS_COLOR, 5, true);
        //        LineBorder border ;
        public Color color;
        public int thickness;
        public boolean rounded;
        Borders(Color computersColor_, int thickness_, boolean roundedCorners_) {
//            this.border = new LineBorder(computersColor_, thickness_,roundedCorners_);
            this.color = computersColor_;
            this.thickness = thickness_;
            this.rounded = roundedCorners_;
        }
    }

    public static class Setting {
        String title;
        JLabel titleLabel, valueLabel;
        public AtomicInteger value;
        JSlider jComponent;

        public Setting(final String title, final int min, final int max, int value) {
            this.title = title;
            this.value = new AtomicInteger(value);
            titleLabel = new JLabel(title);
            valueLabel = new JLabel(String.valueOf((this.value.get())));
            jComponent = new JSlider(min, max, value);
            int test = 0;
            jComponent.addChangeListener(e -> {
                this.value.set(jComponent.getValue());
                valueLabel.setText("" + this.value.get());
                CameraCapture.settingWasUpdated(this.title, this.value.get());//TODO: Right now is static, change for future.
            });
        }

        public static JPanel initializeSettingsPanel(final String title, final Setting... settings ) {
            Map<String, Setting> map = new HashMap<>();
            JPanel returnPanel = new JPanel();
            try {
                SwingUtilities.invokeAndWait( new Runnable() {
                    @Override
                    public void run() {
                        returnPanel.setLayout(new GridLayout(0, 3));
                        for ( Setting setting : settings ) {
                            map.put( setting.title, setting );
                            returnPanel.add( setting.titleLabel );
                            setting.jComponent.setBackground(Settings.CYAN_DARKER);
                            returnPanel.add( setting.jComponent );
                            returnPanel.add( setting.valueLabel );
                        }
//                        controlerFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
//                        controlerFrame.setAlwaysOnTop( false );
//                        controlerFrame.setContentPane( panel );
//                        controlerFrame.pack();
//                        controlerFrame.setVisible( true );
                        returnPanel.setBackground(Settings.CYAN_DARKER);
                    }
                } );
            }catch(Exception e){
                e.printStackTrace();
                returnPanel.add(new JLabel("ERROR: See terminal stackTrace"));
            }
            return returnPanel;
        }

    }

    public ScreenUI(GameLogic gameLogic_) {
        super(new BorderLayout());

        this._gameLogic = gameLogic_;
        _boardButtons = gameLogic_.getBoardButtons();

        _cameraController = new CameraCapture(tickToeStream);

//        optionsPannel.setAlignmentY(CENTER_ALIGNMENT);
        optionsPannel.setPreferredSize(new Dimension(1_280,720));
        optionsPannel.setLayout(new GridBagLayout());
        optionsPannel.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 3));
        constraints.fill = GridBagConstraints.HORIZONTAL;

        //Use default FlowLayout.
        visionSettings.setBorder(BorderFactory.createLineBorder(Color.MAGENTA,5));

        simulatedSideBoard = new JPanel();
        simulatedSideBoard.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
        simulatedSideBoard.add(tickTackToeGameButtons());
        _underGameBoard = new JLabel("<html>It's <u>your</u> turn <br/>(COLOR)</html>",SwingConstants.CENTER);
//        setVertical(totalBox);
//        alignCenter(_underGameBoard);
        _underGameBoard.setOpaque(true);
        _underGameBoard.setBackground(Settings.STARTING_UNDER_GAME_BOARD_COLOR);
//        _underGameBoard.setSize(_underGameBoard.getWidth(),_underGameBoard.getHeight()+50);
        simulatedSideBoard.add(_underGameBoard);
//Instructions under vitural board
        JPanel instructions1 = new JPanel();
        createInstructionsBox(instructions1, "Instructions will go here, after I get this box to be under the game board",5,Settings.STARTING_UNDER_GAME_BOARD_COLOR);
//        setVertical(totalBox);
//        alignCenter(_underGameBoard);
        instructions1.setOpaque(true);
//        instructions1.setSize(_underGameBoard.getWidth(),_underGameBoard.getHeight()+50);
        instructions1.setBackground(Settings.CYAN_DARKER);

        JLabel title = new JLabel("Title statement here");
        JLabel bottom = new JLabel("Bottom statement here");
//        title.setForeground(Color.RED);
        title.setBackground(new Color(100, 20, 70));
        title.setBorder(BorderFactory.createLineBorder(new Color(100, 20, 70), 3));
        bottom.setBorder(BorderFactory.createLineBorder(new Color(100, 20, 70), 3));
//        title.setSize(new Dimension(optionsPannel.getHeight()/8, optionsPannel.getWidth()));
//        title.setHorizontalTextPosition(AbstractButton.CENTER);



        title.setBackground(Color.RED);
//        title.setAlignmentX(.5f);
//        title.setPreferredSize(new Dimension((int)optionsPannel.getPreferredSize().getWidth(),title.getHeight()));
        System.out.println((int)Math.round(optionsPannel.getPreferredSize().getWidth())+"::::::::::::::::::::::::::::");
        title.setSize(new Dimension((int)Math.round(optionsPannel.getPreferredSize().getWidth()),200));
        addToOptionsPanel(title,1.0,1,0,0, 100,1,true);
        addToOptionsPanel(bottom,1.0,1,0,3,100,1,false);

//====//
//        optionsPannel.add(mainTabVideoBoxWithSettings(), constraints);
        JPanel mainTabVideoBoxWithSettings = mainTabVideoBoxWithSettings();
//        mainTabVideoBoxWithSettings@@@
//        mainTabVideoBoxWithSettings.setSize(100,100);
//        mainTabVideoBoxWithSettings.
        addToOptionsPanel(mainTabVideoBoxWithSettings,-.5,-2,1,1, 1,3,true);
//        totalBox.        setBackground(new Color(100, 20, 70));
//        optionsPannel.add(totalBox, constraints);
        addToOptionsPanel(simulatedSideBoard,-2,.25,99,2, 1,1,true);
        addToOptionsPanel(instructions1,-2,-2,99,3, 1,1,true);

//        optionsPannel.add(streamPanel);//TODO: DOES NOT HAVE ANYTHING RIGHT NOW

        tabbedPane.addTab(Settings.DEFAULT_PAGE_HEADERS[0], optionsPannel);

        JPanel labelAndComponent = new JPanel();
        tabbedPane.addTab(Settings.DEFAULT_PAGE_HEADERS[1], labelAndComponent);

        JPanel buttonAndComponent = new JPanel();
        //Use default FlowLayout.
        buttonAndComponent.add(createYAlignmentExample(false));
        buttonAndComponent.add(createYAlignmentExample(true));
        tabbedPane.addTab(Settings.DEFAULT_PAGE_HEADERS[2], buttonAndComponent);

        //Add tabbedPane to this panel.
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void addToOptionsPanel(JComponent element, double requestVertical, double requestHorizontal, int x, int y, int width, int height, boolean alignTop){
        constraints.ipady = 0;       //reset to default//TODO: Make changeable
        constraints.weightx = requestHorizontal==-1? 0.0 : requestHorizontal;
        constraints.weighty = requestVertical;   //request any extra vertical space
//        constraints.insets = new Insets(10,0,0,0);  //top padding
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = width;
        constraints.gridheight = height;
        if(alignTop)
            constraints.anchor = GridBagConstraints.PAGE_START; //start of space
        else
            constraints.anchor = GridBagConstraints.PAGE_END; //end of space
        optionsPannel.add(element, constraints);
    }


    private void createInstructionsBox(JPanel panel, String text, int width, Color backgroundColor){
        //Convert string into sections

        JLabel instructions = new JLabel();
        instructions.setBackground(backgroundColor);
        updateLabel(instructions,text,width);
        panel.add(instructions);
    }

    protected JPanel mainTabVideoBoxWithSettings() {
        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(3,6));

        pane.setBorder(BorderFactory.createTitledBorder("Live Video Feed"));
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

        tickToeStream.setSize(500,500);//TODO: Make controllable
        tickToeStream.setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
        tickToeStream.setVerticalTextPosition(AbstractButton.BOTTOM);
        tickToeStream.setHorizontalTextPosition(AbstractButton.CENTER);
        tickToeStream.setAlignmentX(CENTER_ALIGNMENT);
//        tickToeStream.setAlignmentX(LEFT_ALIGNMENT);

        JLabel mouseCoordinate = new JLabel("Mouse Coordinate: ");
        tickToeStream.addMouseListener(new MouseAdapter() {//TODO: Move?
            public void mouseClicked(MouseEvent me) {
                mouseCoordinate.setText("Mouse Coordinate: ("+me.getX()+","+me.getY()+")");   // No more compilation error here
            }
        });
        pane.add(tickToeStream);
////TODO: Not working
//mouseCoordinate.setVerticalTextPosition(AbstractButton.TOP);
//mouseCoordinate.setHorizontalTextPosition(AbstractButton.LEFT);
//mouseCoordinate.setAlignmentX(LEFT_ALIGNMENT);
//mouseCoordinate.setBackground(Settings.PLAIN_GREEN);
        pane.add(mouseCoordinate);

        GridBagConstraints _bagConstraints = new GridBagConstraints();

        _bagConstraints.weightx = 0.0;//reset to the default

    //SETTINGS
        ArrayList<JButton> optionsBarButtons = new ArrayList<>();
        optionsBarButtons.add(new JButton("Grayscale"));//TODO: Make it grab from a strings file
        optionsBarButtons.add(new JButton("Colorscale"));
        optionsBarButtons.add(new JButton("Toggle Corners"));
        optionsBarButtons.add(new JButton("Set Positions"));
        optionsBarButtons.add(new JButton("Check Positions"));
        optionsBarButtons.add(new JButton("RESET GAME"));

        JPanel optionPane = new JPanel();
        optionPane.add(new JLabel("<html><center>Video</center>Options</html>",SwingConstants.CENTER));
        //Add list of buttons and setup their actionListeners
        for (JButton button: optionsBarButtons) {
            button.setName(button.getText());
            button.addActionListener(this);
            optionPane.add(button);
        }
        pane.add(optionPane);
        pane.add(visionSettings);
        return pane;
    }

    protected Component makeUnit(Component component,
                                 GridBagLayout gridbag,
                                 GridBagConstraints c) {
        gridbag.setConstraints(component, c);
        return component;
    }

    private void setVertical(JComponent component_){ component_.setLayout(new BoxLayout(component_, BoxLayout.Y_AXIS)); }
    private void alignCenter(JComponent component_){ component_.setAlignmentX(CENTER_ALIGNMENT); }

    public JPanel videoButtons(){
        JPanel pane2 = new JPanel();
        pane2.setLayout(new BoxLayout(pane2, BoxLayout.Y_AXIS));


//        JButton button1 = new JButton("Start Game", createImageIcon("images/middle.gif"));
//        JButton button2 = new JButton("Make Move", createImageIcon("images/middle.gif"));
//        JButton button3 = new JButton("Something Else", createImageIcon("images/middle.gif"));
//        JButton button4 = new JButton("Yet Another Button or box", createImageIcon("images/middle.gif"));
//        button1.setVerticalTextPosition(AbstractButton.BOTTOM);
//        button1.setHorizontalTextPosition(AbstractButton.CENTER);
//        button2.setVerticalTextPosition(AbstractButton.BOTTOM);
//        button2.setHorizontalTextPosition(AbstractButton.CENTER);
//        button3.setVerticalTextPosition(AbstractButton.BOTTOM);
//        button3.setHorizontalTextPosition(AbstractButton.CENTER);
//        button4.setVerticalTextPosition(AbstractButton.BOTTOM);
//        button4.setHorizontalTextPosition(AbstractButton.CENTER);
//        pane2.add(button1);
//        pane2.add(button2);
//        pane2.add(button3);
//        pane2.add(button4);
        return pane2;
    }

    protected JPanel createLabelAndComponent(boolean doItRight) {
        JPanel pane = new JPanel();

        JComponent component = new JPanel();
        Dimension size = new Dimension(150,100);
        component.setMaximumSize(size);
        component.setPreferredSize(size);
        component.setMinimumSize(size);
        TitledBorder border = new TitledBorder(
                new LineBorder(Color.black),
                "A JPanel",
                TitledBorder.CENTER,
                TitledBorder.BELOW_TOP);
        border.setTitleColor(Color.black);
        component.setBorder(border);

        JLabel label = new JLabel("This is a JLabel");
        String title;
        if (doItRight) {
            title = "Matched";
            label.setAlignmentX(CENTER_ALIGNMENT);
        } else {
            title = "Mismatched";
        }

        pane.setBorder(BorderFactory.createTitledBorder(title));
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(label);
        pane.add(component);
        return pane;
    }

    protected JPanel createYAlignmentExample(boolean doItRight) {
        JPanel pane = new JPanel();
        String title;

        JComponent component1 = new JPanel();
        Dimension size = new Dimension(100, 50);
        component1.setMaximumSize(size);
        component1.setPreferredSize(size);
        component1.setMinimumSize(size);
        TitledBorder border = new TitledBorder(
                new LineBorder(Color.black),
                "A JPanel",
                TitledBorder.CENTER,
                TitledBorder.BELOW_TOP);
        border.setTitleColor(Color.black);
        component1.setBorder(border);

        JComponent component2 = new JPanel();
        size = new Dimension(100, 50);
        component2.setMaximumSize(size);
        component2.setPreferredSize(size);
        component2.setMinimumSize(size);
        border = new TitledBorder(new LineBorder(Color.black),
                "A JPanel",
                TitledBorder.CENTER,
                TitledBorder.BELOW_TOP);
        border.setTitleColor(Color.black);
        component2.setBorder(border);

        if (doItRight) {
            title = "Matched";
        } else {
            component1.setAlignmentY(TOP_ALIGNMENT);
            title = "Mismatched";
        }

        pane.setBorder(BorderFactory.createTitledBorder(title));
        pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
        pane.add(component1);
        pane.add(component2);
        return pane;
    }

    /**
     *     Return id of the thread after creation
     */
    public long startCameraThread(){
        _cameraController.start();
        return _cameraController.getCurrentThreadID();
    }



    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = ScreenUI.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    private JPanel tickTackToeGameButtons(){
//TODO: PORT BETTER
        JPanel frame = new JPanel();
        frame.setBorder(BorderFactory.createTitledBorder("Game Board"));

        //        GENERATE BUTTONS USING GLOBALISH ARRAY
        for(int i = 0; i< _boardButtons.length; i++){//USE STRINGS TO TURN INTO BUTTONS
            TickToeButton tmpButton = new TickToeButton(i+"", i);
            tmpButton.setPreferredSize(new Dimension(80,80));
            tmpButton.setBackground(Settings.STARTING_COLOR);
            _boardButtons[i] = tmpButton;
        }

        frame.setLayout(new GridLayout(3,3));
//      ADD ALL BUTTONS TO FRAME
        for (int i = 0; i < _boardButtons.length; i++) {
            TickToeButton tmpButton = _boardButtons[i];
            tmpButton.addActionListener(this);
            frame.add(tmpButton);
        }
        frame.setVisible(true);
        return frame;
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    void createAndShowGUI() {


        //Create and set up the window.
        JFrame frame = new JFrame(Settings.DEFAULT_MAIN_SCREEN_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.

//        ScreenUI newContentPane = new ScreenUI(_gameLogic);
//        newContentPane.setOpaque(true); //content panes must be opaque
//        frame.setContentPane(newContentPane);

//        ScreenUI newContentPane = new ScreenUI(_gameLogic);
        setOpaque(true); //content panes must be opaque
        frame.setContentPane(this);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

//    public static void main(String[] args) {
//        //Schedule a job for the event-dispatching thread:
//        //creating and showing this application's GUI.
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                createAndShowGUI();
//            }
//        });
//    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        String source = actionEvent.getSource().toString();

        //DETECT BUTTON TYPE
        if(source.startsWith("TickToeButton")){

            int boxNumber = Integer.parseInt((source.substring("TickToeButton[".length(),source.indexOf(','))));
            System.out.println("--------------------User clicked '" + boxNumber + "'");
            playSpaceAndComputerMove(boxNumber, 2);

        }else if(source.startsWith("javax.swing.JButton")){
//            System.out.println("source = " + source);
            if(source.substring(20).startsWith("Grayscale")){
                System.out.println("test");
                _cameraController.setOutputDisplay(CameraCapture.DisplayModes.GREYSCALE);
            }else if(source.substring(20).startsWith("Colorscale")){
                _cameraController.setOutputDisplay(CameraCapture.DisplayModes.NORMAL);
            }else if(source.substring(20).startsWith("Toggle Corners")){
//                _cameraController.displayCorners();
                _cameraController.keepCorners = !_cameraController.keepCorners;
            }else if(source.substring(20).startsWith("Set Positions")){
//                _gameLogic.locatePhysicalLocations(_cameraController.getCirclePoints(), _cameraController.getCircleDimensions().width, _cameraController.getCircleDimensions().height);
                if(_cameraController.getSavedCorners()[1]!=null){
                    _gameLogic.locatePhysicalLocations(_cameraController.getSavedCorners(), Math.abs(_cameraController.getCircleDimensions().width), _cameraController.getCircleDimensions().height);
                }
//                Point[] p = _cameraController.getCirclePoints();
//                System.out.println(p[0].x+","+p[0].y);
//                System.out.println(p[1].x+","+p[1].y);
                _cameraController.saveNextCorners=true;
            }else if(source.substring(20).startsWith("Check Positions")){
//                ArrayList<org.opencv.core.Point> combinedFrames = new ArrayList<>();
//                for (int i = 0; i < 5; i++) {
//                    org.opencv.core.Point[] tmpFrameLocations = _cameraController.getCirclePoints();
//                    for (int j = 0; j < tmpFrameLocations.length; j++) {
//
//                        combinedFrames.add(tmpFrameLocations[j]);
//                    }
//                }
//                _gameLogic.checkSpaces((org.opencv.core.Point[]) combinedFrames.toArray());
                if(_cameraController.getCirclePoints()!=null) {
                    System.out.println("running .checkSpaces()");
                    System.out.println(PhysicalBoardTracker._physicalSpaces +"::::::::::");
//                    for (PhysicalBoardTracker.PhysicalSpace geek : PhysicalBoardTracker._physicalSpaces)
//                        System.out.print(geek.getPosition()+", ");
                    System.out.println("{{{"+_cameraController.getCirclePoints());
                    int pos = _gameLogic.checkSpaces(_cameraController.getCirclePoints()).getPosistion();
                    System.out.println("pos = " + pos);
                    PhysicalBoardTracker.PhysicalSpace newTile = PhysicalBoardTracker._physicalSpaces[pos];
                    playSpaceAndComputerMove(newTile.position,2);
                }
            }else if(source.substring(20).startsWith("RESET GAME")){
                _gameLogic.resetGame();
            }

        }


    }

    public void playSpaceAndComputerMove(int space, int player){
        String moveResponse;
        if(!_gameLogic.isGameOver()) {
            if (!(_gameLogic.getMoveNumber() % 2 == 0)) {
                moveResponse = _gameLogic.leftClickedBoardButton(space);
                /*if(moveResponse != "")*/
                updateLabel(_underGameBoard, moveResponse,32);
                _gameLogic.playMove(true, space);
            }
        }
        if(!_gameLogic.isGameOver()) {
            if (!(_gameLogic.getMoveNumber() % 2 == 1)) {//Don't run if the player did not make a move
                moveResponse = _gameLogic.playComputerMove(GameLogic.ComputerPlayStyles.RANDOM);
                //            System.currentTimeMillis();
                //            if(moveResponse != ""){
                updateLabel(_underGameBoard, moveResponse,32);
                _gameLogic.playMove(false, space);
                //                }
            }
        }
    }



    public void window(BufferedImage img, String text, int x, int y) {
        JFrame frame0 = new JFrame();
        frame0.getContentPane().add(this);
        frame0.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame0.setTitle(text);
        frame0.setSize(img.getWidth(), img.getHeight() + 30);
        frame0.setLocation(x, y);
        frame0.setVisible(true);
    }




    private void updateLabel(JLabel label_, String message_, int maxWidth_){
                /*
        "abcdefghijklmnopqrstuvwzyz012345".length(); // 6789"; If it exceeds this amount, it will soft crash
         */
        if(message_.length() > maxWidth_){
            //SPLIT UP THE RESPONSE
//            String[] words = displayStatement.split("\\s+");
            String[] words = message_.split(" ");
            message_ = "";
            int goalPerRow = message_.length() / maxWidth_;
            int lengthOfLineCurrently = 0;
            for (String word: words) {
                if(lengthOfLineCurrently+word.length()<=maxWidth_){//Space Included
                    message_+=(" "+word);
                    lengthOfLineCurrently += word.length()+1;
                }else{
                    message_+="<br/>"+word;
                    lengthOfLineCurrently = word.length()+1;
                }
            }
        }
        message_ = "<html>"+message_+"</html>";
        System.out.println("message_ = "+message_);
        label_.setText(message_);
    }
}
