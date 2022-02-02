import javax.swing.*;
import java.awt.*;

public class TickToeButton extends JButton {

        private String _id;
        private int _toggleStageSelected = 0;//0 = empty, 1 = computer, 2 = player
        private Color _color = Settings.STARTING_COLOR;
        public Color getColor(){return _color;}

        public TickToeButton(String name_) {
            this._id = name_;
            this.setName(_id);
        }

        public int getTileOwner(){return _toggleStageSelected;}

//SETTERS
        public void setColor(Color newColor_){
                _color = newColor_;
                this.setBackground(_color);
        }
        public void setOwner(int newOwner_){
                _toggleStageSelected=newOwner_;
        }
//END SETTERS

        public void leftClick(){        }

}
