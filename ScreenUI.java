/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * BoxAlignmentDemo.java requires the following files:
 *   images/middle.gif
 *   images/geek-cght.gif
 *
 * This demo shows how to specify alignments when you're using
 * a BoxLayout for components with maximum sizes and different
 * default alignments.
 */

import org.opencv.core.Mat;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class ScreenUI extends JPanel implements ActionListener {
    TickToeButton[] _boardButtons;
    private static GameLogic _gameLogic;
    private static CameraCapture _cameraController;

    //MUTABLE LABELS
    private JLabel _underGameBoard;
    private JPanel totalBox = new JPanel();

    private JPanel streamPanel = new JPanel();
    private JLabel tickToeStream = new JLabel("Computer: <Place Status Here>");
    private JPanel buttonRow = new JPanel();
    private JTabbedPane tabbedPane = new JTabbedPane();


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

    public ScreenUI(GameLogic gameLogic_) {
        super(new BorderLayout());

        this._gameLogic = gameLogic_;
        _boardButtons = gameLogic_.getBoardButtons();

        _cameraController = new CameraCapture(tickToeStream);

        buttonRow.setAlignmentY(CENTER_ALIGNMENT);
        buttonRow.setPreferredSize(new Dimension(1_280,720));
        //Use default FlowLayout.
        buttonRow.add(mainTabVideoFeedBox(false));

        totalBox = new JPanel();
        totalBox.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
        totalBox.add(tickTackToeGameButtons());
        _underGameBoard = new JLabel("<html>It's <u>your</u> turn <br/>(COLOR)</html>",SwingConstants.CENTER);
        setVertical(totalBox);
        alignCenter(_underGameBoard);
        _underGameBoard.setOpaque(true);
        _underGameBoard.setBackground(Settings.STARTING_UNDER_GAME_BOARD_COLOR);
        _underGameBoard.setSize(_underGameBoard.getWidth(),_underGameBoard.getHeight()+50);
        totalBox.add(_underGameBoard);

        buttonRow.add(totalBox);

        buttonRow.add(streamPanel);

        buttonRow.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 3));

        tabbedPane.addTab(Settings.DEFAULT_PAGE_HEADERS[0], buttonRow);

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

    protected JPanel mainTabVideoFeedBox(boolean changeAlignment) {
        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(3,3));

        pane.setBorder(BorderFactory.createTitledBorder("Live Video Feed"));
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));


//        tickToeStream = new JLabel();//"Computer: <Place Status Here>", createImageIcon("images/tick-tack-toe_generic.png"));

        tickToeStream.setSize(500,500);
        tickToeStream.setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
        tickToeStream.setVerticalTextPosition(AbstractButton.BOTTOM);
        tickToeStream.setHorizontalTextPosition(AbstractButton.CENTER);
        tickToeStream.setAlignmentX(CENTER_ALIGNMENT);

        pane.add(tickToeStream);
//        GridLayout gridLayout = new GridLayout(5,1);
        GridBagConstraints _bagConstraints = new GridBagConstraints();
//        pane.setLayout(gridLayout);

        _bagConstraints.weightx = 0.0;                //reset to the default

        JButton button1 = new JButton("Grayscale", createImageIcon("images/middle.gif"));
        JButton button2 = new JButton("Make Move", createImageIcon("images/middle.gif"));
        JButton button3 = new JButton("Something Else", createImageIcon("images/middle.gif"));
        JButton button4 = new JButton("Yet Another Button or box", createImageIcon("images/middle.gif"));

        JPanel optionPane = new JPanel();
//        optionPane.setBorder(BorderFactory.createTitledBorder("Live Video Feed"));
        optionPane.add(new JLabel("<html><center>Video</center>Options</html>",SwingConstants.CENTER));
        optionPane.add(button1);
        optionPane.add(button2);
        optionPane.add(button3);
        optionPane.add(button4);
        pane.add(optionPane);

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


        JButton button1 = new JButton("Start Game", createImageIcon("images/middle.gif"));
        JButton button2 = new JButton("Make Move", createImageIcon("images/middle.gif"));
        JButton button3 = new JButton("Something Else", createImageIcon("images/middle.gif"));
        JButton button4 = new JButton("Yet Another Button or box", createImageIcon("images/middle.gif"));
        button1.setVerticalTextPosition(AbstractButton.BOTTOM);
        button1.setHorizontalTextPosition(AbstractButton.CENTER);
        button2.setVerticalTextPosition(AbstractButton.BOTTOM);
        button2.setHorizontalTextPosition(AbstractButton.CENTER);
        button3.setVerticalTextPosition(AbstractButton.BOTTOM);
        button3.setHorizontalTextPosition(AbstractButton.CENTER);
        button4.setVerticalTextPosition(AbstractButton.BOTTOM);
        button4.setHorizontalTextPosition(AbstractButton.CENTER);
        pane2.add(button1);
        pane2.add(button2);
        pane2.add(button3);
        pane2.add(button4);
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
            TickToeButton tmpButton = new TickToeButton(i+"");
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

//        ScreenUI newContentPane = new ScreenUI(_gameLogic);@@@
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
//        tickToeStream.setBorder(new LineBorder(Color.black,5));
//        tickToeStream.setIcon(_cameraController.getNewImageFromStream());
        Mat frame = new Mat();


        String source = actionEvent.getSource().toString();
        int boxNumber = Integer.parseInt((source.substring("TickToeButton[".length(),source.indexOf(','))));
        System.out.println("--------------------User clicked '" + boxNumber + "'");

        String moveResponse;
        if(!_gameLogic.isGameOver()) {
            if (!(_gameLogic.getMoveNumber() % 2 == 0)) {
                moveResponse = _gameLogic.leftClickedBoardButton(boxNumber);
                /*if(moveResponse != "")*/
                updateLabel(_underGameBoard, moveResponse);
            }
        }

        if(!_gameLogic.isGameOver()) {
            if (!(_gameLogic.getMoveNumber() % 2 == 1)) {//Don't run if the player did not make a move
                moveResponse = _gameLogic.playComputerMove(GameLogic.ComputerPlayStyles.RANDOM);
                //            System.currentTimeMillis();
                //            if(moveResponse != ""){
                updateLabel(_underGameBoard, moveResponse);
//                }
            }
        }
//        selectedButton.setBackground(selectedButton.getColor());
//        selectedButton.leftClick();
//        selectedButton.setBackground(selectedButton.getColor());
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







    private void updateLabel(JLabel label_, String message_){
                /*
        "abcdefghijklmnopqrstuvwzyz012345".length(); // 6789"; If it exceeds this amount, it will soft crash
         */
        if(message_.length() > 32){
            //SPLIT UP THE RESPONSE
//            String[] words = displayStatement.split("\\s+");
            String[] words = message_.split(" ");
            message_ = "";
            int goalPerRow = message_.length() / 32;
            int lengthOfLineCurrently = 0;
            for (String word: words) {
                if(lengthOfLineCurrently+word.length()<31){//Space Included
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
