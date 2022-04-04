import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class TickToeButton extends JButton {

        class RoundButton extends LineBorder {
                private int r;

                RoundButton(int r){
                        super(Color.BLACK, 15,false);
                        super.thickness = 15;
                        this.r = r;
                }
                public Insets getBorderInsets(Component c) {
                        return new Insets(this.r+1, this.r+1, this.r+2, this.r);
                }
                public boolean isBorderOpaque() {
                        return true;
                }
                public void paintBorder(Component c, Graphics g, int x, int y,
                                        int width, int height) {
                        g.drawRoundRect(x, y, width-1, height-1, r, r);
                }
        }

        private String _id;
        private int _toggleStageSelected = 0;//0 = empty, 1 = computer, 2 = player
        private Color _color = Settings.STARTING_COLOR;
        public Color getColor(){return _color;}

        public TickToeButton(String name_) {
            this._id = name_;
            this.setName(_id);
            this.setBorder(new RoundButton(Settings.DEFAULT_RADIUS_FOR_TICKTOE_BUTTONS));
        }

        public int getTileOwner(){return _toggleStageSelected;}

//SETTERS
        public void setColor(Color newColor_){
                _color = newColor_;
                this.setBackground(_color);
        }
        public void setOwner(int newOwner_){
                _toggleStageSelected=newOwner_;
                switch(getTileOwner()){
                        case 1:
                                setColor(Settings.COMPUTERS_COLOR);
                                break;
                        case 2:
                                setColor(Settings.PLAYERS_COLOR);
                                break;
                        default:
                                setColor(Settings.STARTING_COLOR);
                }
        }
//END SETTERS

        public void leftClick(){        }

}
