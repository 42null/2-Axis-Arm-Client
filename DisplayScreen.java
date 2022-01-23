import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
//        _largePanel.add(makeUnit(new JLabel(theme_.get_title()), _gridBag, _bagConstraints));

        System.out.println("---");

//        BufferedImage img = ImageIO.read(new File("tick-tack-toe_generic.png"));
//        JLabel image1 = new JLabel(new ImageIcon(img));
//
//        _largePanel.add(makeUnit(image1, gridbag, c));
//        _largePanel.add(makeUnit(new JButton("Button2"), gridbag, c));
//        _largePanel.add(makeUnit(new JButton("Button3"), gridbag, c));
//
//        c.gridwidth = GridBagConstraints.REMAINDER; //end row
//        _largePanel.add(makeUnit(new JButton("Button4"), gridbag, c));
//
//        c.weightx = 0.0;                //reset to the default
//        _largePanel.add(makeUnit(new JButton("Button5"), gridbag, c)); //another row
//
//        c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last in row
//        _largePanel.add(makeUnit(new JButton("Button6"), gridbag, c));
//
//        c.gridwidth = GridBagConstraints.REMAINDER; //end row
//        _largePanel.add(makeUnit(new JButton("Button7"), gridbag, c));
//
//        c.gridwidth = 1;                //reset to the default
//        c.gridheight = 2;
//        c.weighty = 1.0;
//        _largePanel.add(makeUnit(new JButton("Button8"), gridbag, c));
//
//        c.weighty = 0.0;                //reset to the default
//        c.gridwidth = GridBagConstraints.REMAINDER; //end row
//        c.gridheight = 1;               //reset to the default
//        _largePanel.add(makeUnit(new JButton("Button9"), gridbag, c));
//        _largePanel.add(makeUnit(new JButton("Button10"), gridbag, c));

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
