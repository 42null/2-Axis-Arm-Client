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

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScreenUI extends JPanel implements ActionListener {
    TickToeButton[] _boardButtons;
    private static GameLogic _gameLogic;

//MUTABLE LABELS
    private JLabel _underGameBoard;
    JPanel totalBox = new JPanel();


    public ScreenUI(GameLogic gameLogic_) {
        super(new BorderLayout());

        this._gameLogic = gameLogic_;
        _boardButtons = gameLogic_.getBoardButtons();



        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel buttonRow = new JPanel();
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

//        buttonRow.add(createYAlignmentExample(true));
//        buttonRow.add(videoButtons());
//        buttonRow.add(mainTabVideoFeedBox(true));
        buttonRow.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 3));
//        buttonRow.setPreferredSize(new Dimension(buttonRow.getWidth(),buttonRow.getHeight()+10000));

        tabbedPane.addTab(Settings.DEFAULT_PAGE_HEADERS[0], buttonRow);

        JPanel labelAndComponent = new JPanel();
        //Use default FlowLayout.
//        labelAndComponent.add(createLabelAndComponent(false));
//        labelAndComponent.add(createLabelAndComponent(true));
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


        JButton tickTackToeComponent = new JButton("Computer: <Place Status Here>", createImageIcon("images/tick-tack-toe_generic.png"));
        tickTackToeComponent.setSize(200,80);
        tickTackToeComponent.setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
        tickTackToeComponent.setVerticalTextPosition(AbstractButton.BOTTOM);
        tickTackToeComponent.setHorizontalTextPosition(AbstractButton.CENTER);
        tickTackToeComponent.setAlignmentX(CENTER_ALIGNMENT);

//        String title;
//        if (changeAlignment) {
//            title = "Desired";
//            button1.setAlignmentY(BOTTOM_ALIGNMENT);
//            button2.setAlignmentY(BOTTOM_ALIGNMENT);
//        } else {
//            title = "";
//        }

//        GridBagLayout gridLayout = new GridBagLayout();

//        JPanel pane = new JPanel(gridLayout);
        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(3,3));

        pane.setBorder(BorderFactory.createTitledBorder("Live Video Feed"));
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(tickTackToeComponent);
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

//        button1.setVerticalTextPosition(AbstractButton.BOTTOM);
//        button1.setHorizontalTextPosition(AbstractButton.CENTER);
//        button2.setVerticalTextPosition(AbstractButton.BOTTOM);
//        button2.setHorizontalTextPosition(AbstractButton.CENTER);
//        button3.setVerticalTextPosition(AbstractButton.BOTTOM);
//        button3.setHorizontalTextPosition(AbstractButton.CENTER);
//        button4.setVerticalTextPosition(AbstractButton.BOTTOM);
//        button4.setHorizontalTextPosition(AbstractButton.CENTER);

//        pane.add(button1);
//        pane.add(button2);
//        pane.add(button3);
//        pane.add(button4);

//        pane.add(makeUnit(button1, gridLayout, _bagConstraints)); //another row
//        pane.add(makeUnit(new JButton("Button3"), gridLayout, _bagConstraints));
//
//        _bagConstraints.gridwidth = GridBagConstraints.REMAINDER; //end row
//        pane.add(makeUnit(new JButton("Button4"), gridLayout, _bagConstraints));
//
//        _bagConstraints.weightx = 0.0;                //reset to the default
//        pane.add(makeUnit(new JButton("Button5"), gridLayout, _bagConstraints)); //another row
//
//        _bagConstraints.gridwidth = GridBagConstraints.RELATIVE; //next-to-last in row
//        pane.add(makeUnit(new JButton("Button6"), gridLayout, _bagConstraints));
//
//        _bagConstraints.gridwidth = GridBagConstraints.REMAINDER; //end row
//        pane.add(makeUnit(new JButton("Button7"), gridLayout, _bagConstraints));

//        pane.add(button1);
//        pane.add(button2);
//        pane.add(button3);
//        pane.add(button4);

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
    static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame(Settings.DEFAULT_MAIN_SCREEN_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        ScreenUI newContentPane = new ScreenUI(_gameLogic);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

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
        int boxNumber = Integer.parseInt((source.substring("TickToeButton[".length(),source.indexOf(','))));
        System.out.println("User clicked '" + boxNumber + "'");

        updateLabel(_underGameBoard, _gameLogic.leftClickedBoardButton(boxNumber));
        if(!(_gameLogic.getMoveNumber() % 2 == 0))//Don't run if the player did not make a move
            updateLabel(_underGameBoard, _gameLogic.playComputerMove(GameLogic.ComputerPlayStyles.RANDOM));

//        TickToeButton selectedButton = _boardButtons[boxNumber];
////
//        selectedButton.setBackground(selectedButton.getColor());
//        selectedButton.leftClick();
//        selectedButton.setBackground(selectedButton.getColor());
    }

    private void updateLabel(JLabel label_, String message_){
        System.out.println("message_ = "+message_);
        label_.setText(message_);
    }
}
