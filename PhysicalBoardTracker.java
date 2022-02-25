import java.awt.*;

public class PhysicalBoardTracker {
/*
        {ARM}
 C                  P
 V                  V
[0]  [ 6][ 7][ 8]  [3]
[1]  [ 9][10][11]  [4]
[2]  [12][13][14]  [5]
*/
    private PhysicalSpace[] _spaces = new PhysicalSpace[15];

        // inner class
    class PhysicalSpace {
        private int storageType = 0;
        private int ownerValue = 0;// 0 for empty, 1 for computer, 2 for player //TODO: Make more efficient than int

        public PhysicalSpace() {this.ownerValue=0;}
        public PhysicalSpace(int owner_) {this.ownerValue=owner_;}

        public int getOwner(){return ownerValue;}
    }

    public int getNextAvailable(int owner_){//0 for empty, 1 for computer, 2 for player
        for(int i = 0; i < 5; i++) {//TODO: Make check owner side first
            if(_spaces[i].getOwner()==owner_){
                return i;
            }
        }
        return -1;
    }

    public void resetBoard(){

    }



    public void scheduleNextMovement(Point location1_, Point location2_){

    }

}