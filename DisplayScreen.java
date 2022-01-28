import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.IOException;

import static java.awt.Component.CENTER_ALIGNMENT;

public class DisplayScreen implements Runnable{
    private Dimension _windowDimensions;
    private String _title = Settings.DEFAULT_MAIN_SCREEN_TITLE;
//MORE IN USE
    private Thread thread;
//    private JPanel _mainWindow;
//    JFXPanel _mainWindow = new JFXPanel();
    private JFrame _mainWindow;
    private JPanel _largePanel = new JPanel(new GridLayout(2, 2));
    private GridBagLayout _gridBag = new GridBagLayout();
    private GridBagConstraints _bagConstraints = new GridBagConstraints();



    private JButton startButton = new JButton("Start Game");
    private JButton stopButton = new JButton("End Game");

    //OTHER
    private boolean running = false;



    enum Topbars{
        THEME1("Main"),
        THEME2("Advanced: Camera"),
        THEME3("About");

        private String _title;

        Topbars(String titleOfBar_) {
            this._title = titleOfBar_;
        }

        public String get_title() {
            return _title;
        }
    }

    public void addTopBar(Topbars theme_) {

        _bagConstraints.fill = GridBagConstraints.BOTH;
        _bagConstraints.weightx = 1.0;
        _bagConstraints.weighty = 1.0;
        JLabel tmpLabel = new JLabel(theme_.get_title());
        tmpLabel.setVerticalAlignment(JLabel.TOP);
//        _bagConstraints.gridwidth = GridBagConstraints.REMAINDER; //end row

        _largePanel.add(makeUnit(tmpLabel, _gridBag, _bagConstraints));
//        _largePanel.add(tmpLabel);


//        BufferedImage img = ImageIO.read(new File("tick-tack-toe_generic.png"));
//        JLabel image1 = new JLabel(new ImageIcon(img));
//
//        _largePanel.add(makeUnit(image1, gridbag, c));
//        _bagConstraints.setVerticalAlignment(JLabel.TOP);
        _bagConstraints.weightx = 0.0;                //reset to the default

        _largePanel.add(makeUnit(new JButton("Button2"), _gridBag, _bagConstraints));

        _largePanel.add(createJPannelForVideo());
//        JFrame frame = new JFrame("Demo");
//        JLabel label;
//        label = new JLabel("This is demo label!");
//        label.setFont(new Font("Verdana", Font.PLAIN, 14));
//        label.setVerticalAlignment(JLabel.TOP);
//        Border border = BorderFactory.createLineBorder(Color.ORANGE);
//        label.setBorder(border);
//        frame.add(label);
//        frame.setSize(600,300);
//        frame.setVisible(true);
        _largePanel.add(makeUnit(new JButton("Button3"), _gridBag, _bagConstraints));

        _bagConstraints.gridwidth = GridBagConstraints.REMAINDER; //end row
        _largePanel.add(makeUnit(new JButton("Button4"), _gridBag, _bagConstraints));

        _bagConstraints.weightx = 0.0;                //reset to the default
        _largePanel.add(makeUnit(new JButton("Button5"), _gridBag, _bagConstraints)); //another row

        _bagConstraints.gridwidth = GridBagConstraints.RELATIVE; //next-to-last in row
        _largePanel.add(makeUnit(new JButton("Button6"), _gridBag, _bagConstraints));

        _bagConstraints.gridwidth = GridBagConstraints.REMAINDER; //end row
        _largePanel.add(makeUnit(new JButton("Button7"), _gridBag, _bagConstraints));
//
        _bagConstraints.gridwidth = 1;                //reset to the default
        _bagConstraints.gridheight = 2;
        _bagConstraints.weighty = 1.0;
        _largePanel.add(makeUnit(new JButton("Button8"), _gridBag, _bagConstraints));

        _bagConstraints.weighty = 0.0;                //reset to the default
        _bagConstraints.gridwidth = GridBagConstraints.REMAINDER; //end row
        _bagConstraints.gridheight = 1;               //reset to the default
        _largePanel.add(makeUnit(new JButton("Button9"), _gridBag, _bagConstraints));
        _largePanel.add(makeUnit(new JButton("Button10"), _gridBag, _bagConstraints));
        _largePanel.setVisible(true);

    }

    protected JPanel createJPannelForVideo() {
        JPanel pane = new JPanel();
        JComponent component = new JPanel();
        Dimension size = new Dimension(350, 350);
        component.setMaximumSize(size);
        component.setPreferredSize(size);
        component.setMinimumSize(size);
        TitledBorder border = new TitledBorder(new LineBorder(Color.black), "live feed would somehow be placed here", TitledBorder.CENTER,TitledBorder.BELOW_TOP);
        border.setTitleColor(Color.black);
        component.setBorder(border);

        JLabel label = new JLabel("Computer is deciding...");
        label.setAlignmentX(CENTER_ALIGNMENT);
        TitledBorder cameraViewBoarder = BorderFactory.createTitledBorder("Camera View");
        pane.setBorder(cameraViewBoarder);
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(label);
        pane.add(component);
        return pane;
    }

    public DisplayScreen(Dimension screenDimensions_){
        this._windowDimensions = screenDimensions_;
        try{buildMainWindow();}catch(Exception e){e.printStackTrace();}
    }
    public DisplayScreen(){
        _windowDimensions = new Dimension(Settings.DEFAULT_MAIN_WINDOW_WIDTH, Settings.DEFAULT_MAIN_WINDOW_HEIGHT);
        try{buildMainWindow();}catch(Exception e){e.printStackTrace();}
    }

    protected Component makeUnit(Component component,
                               GridBagLayout gridbag,
                               GridBagConstraints c) {
        gridbag.setConstraints(component, c);
        return component;
    }


    public void buildMainWindow() throws IOException {
        _mainWindow = new JFrame(Settings.DEFAULT_MAIN_SCREEN_TITLE);
        _mainWindow.setPreferredSize(_windowDimensions);

        GridBagLayout gridbag = new GridBagLayout();
        _bagConstraints = new GridBagConstraints();
        _largePanel.setLayout(new GridBagLayout());

        _largePanel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        _largePanel.setLayout(gridbag);
        _largePanel.setBackground(new Color(0xFF0000));
//        _largePanel.add(startButton);
//        _largePanel.add(stopButton);


//        _largePanel.setSize(300, 100);
//::



        _mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _mainWindow.add(_largePanel);
        _mainWindow.pack();
        _mainWindow.setLocation(400, 300);

//        _mainWindow.set(_title);
//        _mainWindow.add(new Button());
//        _mainWindow.pack();
//        _mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        _mainWindow.setLocationRelativeTo(null);
//        _mainWindow.setResizable(false);
        _mainWindow.setVisible(true);

//        _mainWindow.start();
    }

    public synchronized void start(){
        this.running = true;
        this.thread = new Thread(this, "Display");
        this.thread.start();
    }
    public synchronized void stop() throws InterruptedException {
        running = false;
        this.thread.join();
    }

    @Override
    public void run() {
    }

}
