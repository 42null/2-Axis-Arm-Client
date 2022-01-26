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

public class ScreenUI extends JPanel {
    public ScreenUI() {
        super(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel buttonRow = new JPanel();
        //Use default FlowLayout.
        buttonRow.add(mainTabVideoFeedBox(false));
        buttonRow.add(createYAlignmentExample(true));
        buttonRow.add(boxOfButtons());
//        buttonRow.add(mainTabVideoFeedBox(true));
        tabbedPane.addTab(Settings.DEFAULT_PAGE_HEADERS[0], buttonRow);

        JPanel labelAndComponent = new JPanel();
        //Use default FlowLayout.
        labelAndComponent.add(createLabelAndComponent(false));
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


        JButton button0 = new JButton("Computer: <Place Status Here>", createImageIcon("tick-tack-toe_generic.png"));
        button0.setVerticalTextPosition(AbstractButton.BOTTOM);
        button0.setHorizontalTextPosition(AbstractButton.CENTER);

        String title;
//        if (changeAlignment) {
//            title = "Desired";
//            button1.setAlignmentY(BOTTOM_ALIGNMENT);
//            button2.setAlignmentY(BOTTOM_ALIGNMENT);
//        } else {
//            title = "";
//        }

        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createTitledBorder("Live Video Feed"));
        pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
        pane.add(button0);

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
        pane.add(button1);
        pane.add(button2);
        pane.add(button3);
        pane.add(button4);

        return pane;
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

    private JPanel boxOfButtons(){
//TODO: PORT BETTER
        JButton[] _buttonArray;
        JPanel frame;
        frame = new JPanel();

        //        GENERATE BUTTONS FROM LIST
        int width = 3;
        String[] buttonStringArray = new String[width*width];
        _buttonArray = new JButton[buttonStringArray.length];
//        ArrayList<String> buttonStringTempList = new ArrayList<String>();

        for(int i=0; i<buttonStringArray.length; i++){//CREATE STRINGS TO LATER TURN INTO BUTTONS
            buttonStringArray[i] = i+"";
        }
        for(int i = 0; i< _buttonArray.length; i++){//USE STRINGS TO TURN INTO BUTTONS
            JButton tmpButton = new JButton(buttonStringArray[i]);
            tmpButton.setPreferredSize(new Dimension(80,80));
            tmpButton.setBackground(Settings.STARTING_COLOR);
            _buttonArray[i] = tmpButton;
        }



        frame.setLayout(new GridLayout((int) Math.sqrt(buttonStringArray.length),(int) Math.sqrt(buttonStringArray.length)));
//        frame.add(t1);
//        frame.add(l);
//        frame.add(chk_togFace);


//      ADD ALL BUTTONS TO FRAME
        for (int i = 0; i < _buttonArray.length; i++) {
            JButton tmpButton = _buttonArray[i];
//            tmpButton.addActionListener(this);
//            tmpButton.setAction();
//            tmpButton.setHideActionText(false);
//            tmpButton.setText(i+"*");
//            tmpButton.hashCode();
            frame.add(tmpButton);
        }

//        chk_togFace.addActionListener(this);
//        frame.pack();
        frame.setVisible(true);
        return frame;
    }


    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame(Settings.DEFAULT_MAIN_SCREEN_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        ScreenUI newContentPane = new ScreenUI();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
