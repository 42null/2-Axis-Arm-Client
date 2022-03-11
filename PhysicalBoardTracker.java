import java.awt.*;


public class PhysicalBoardTracker {
/*
        {ARM}
 C                  P
 V                  V
[ 9]  [0][1][2]  [12]
[10]  [3][4][5]  [13]
[11]  [6][7][8]  [14]

*/
    private PhysicalSpace[] _spaces = new PhysicalSpace[15];

        // inner class
    class PhysicalSpace {
        private int storageType = 0;
        private int ownerValue = 0;// 0 for empty, 1 for computer, 2 for player //TODO: Make more efficient than int
        //        private Rect2d ownedArea = new Rect2d();
        private org.opencv.core.Point ownedTopRight;
        private org.opencv.core.Point ownedTopLeft;
        protected int position = -1;

        public PhysicalSpace(int position) {
            this.position = position;
            this.ownerValue = 0;
        }

        public PhysicalSpace(int position, int owner_) {
            this.position = position;
            this.ownerValue = owner_;
        }

        public int getOwner() {
            return ownerValue;
        }

        public void setBorderArea(org.opencv.core.Point topRight, org.opencv.core.Point bottomLeft) {

            System.out.println("topRight.x = " + topRight.x);
            System.out.println("topRight.y = " + topRight.y);
            System.out.println("bottomLeft.x = " + bottomLeft.x);
            System.out.println("bottomLeft.y = " + bottomLeft.y);
//            ownedArea.set(new double[]{topRight.x,topRight.y,bottomLeft.x,bottomLeft.y});//new double[]{topRight.x, topRight.y, bottomLeft.x, bottomLeft.y});
            ownedTopRight = topRight;
            ownedTopLeft = bottomLeft;//new double[]{topRight.x, topRight.y, bottomLeft.x, bottomLeft.y});
//            ownedArea.width = (int) (bottomLeft.x - topRight.x);
//            ownedArea.height = (int) (bottomLeft.y - topRight.y);

        }

        public boolean isWithinArea(org.opencv.core.Point centerPoint) {
//            System.out.println("Looking within this area: "+ownedArea.x);
//            System.out.println(ownedTopRight.x);
//            System.out.println(ownedTopLeft.y);

            if (ownedTopRight.x > centerPoint.x && centerPoint.x > ownedTopLeft.x) {
                if (ownedTopRight.x > centerPoint.x && centerPoint.x > ownedTopLeft.x) {
                    return true;
                }
                if (ownedTopRight.y > centerPoint.y && centerPoint.y > ownedTopLeft.y) {
                    return true;
                }
            }
            return false;
        }

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

    public void detectAndSetAllSpaces(org.opencv.core.Point[] pointsToLook, int width, int height){
        for (int i = 0; i < 9; i++) {
//            System.out.println(pointsToLook[0].x);
//            System.out.println(pointsToLook[0].y);
            _spaces[i] = new PhysicalSpace(i);
            org.opencv.core.Point topRight = new org.opencv.core.Point((int) pointsToLook[0].x+width/2,(int) pointsToLook[0].y+height/2);
            org.opencv.core.Point bottomLeft = new org.opencv.core.Point((int) pointsToLook[0].x-width/2,(int) pointsToLook[0].y-height/2);
            _spaces[i].setBorderArea( topRight, bottomLeft);
        }


    }

    public void checkSpaces(org.opencv.core.Point[] pointsToLook){
        for (int i = 0; i < pointsToLook.length; i++) {
            for (int j = 0; j < 1; j++) {
                if(_spaces[j].isWithinArea(pointsToLook[i])){
                    System.out.println("There is a game piece located at position #"+_spaces[j].position);
                }
            }
        }
    }
    
    
    public void scheduleNextMovement(Point location1_, Point location2_){

    }

}