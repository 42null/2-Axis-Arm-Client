import javax.swing.*;
import java.awt.*;

public class TickToeButton extends JButton {

        private String _name;
        private int _toggleStageSelected = 0;//0 = default
        private Color _color = Settings.STARTING_COLOR;
        public Color getColor(){return _color;}

        public TickToeButton(String name_) {
            this._name = name_;
            this.setName(_name);
        }


        public void leftClick(){
            _color = Settings.PLAYERS_COLOR;
        }

}
