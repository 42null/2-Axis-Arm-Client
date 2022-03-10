import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

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
        private Rect ownedArea = new Rect();

        public PhysicalSpace() {this.ownerValue=0;}
        public PhysicalSpace(int owner_) {this.ownerValue=owner_;}

        public int getOwner(){return ownerValue;}

        public void setBorderArea(org.opencv.core.Point topRight, org.opencv.core.Point bottomLeft){
            System.out.println("HERE");
            ownedArea.set(new double[]{topRight.x,topRight.y,bottomLeft.x,bottomLeft.y});//new double[]{topRight.x, topRight.y, bottomLeft.x, bottomLeft.y});
//            ownedArea.width = (int) (bottomLeft.x - topRight.x);
//            ownedArea.height = (int) (bottomLeft.y - topRight.y);

        }

        public boolean isWithinArea(org.opencv.core.Point centerPoint){
//            System.out.println(ownedArea.x);
//            System.out.println(ownedArea.y);
////            System.out.println();
//            System.out.println(ownedArea.height);
//            System.out.println(centerPoint.x);
//            System.out.println(centerPoint.y);

            if(ownedArea.contains(centerPoint)){
                    return true;
//                }
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

    public void detectAndSetAllSpaces(org.opencv.core.Point[] pointsToLook){
        int width = (int) (pointsToLook[0].x - pointsToLook[1].x);
        int height = (int) (pointsToLook[0].y - pointsToLook[1].y);

        System.out.println(pointsToLook[0].x);
        System.out.println(pointsToLook[0].y);

        _spaces[0] = new PhysicalSpace();
        _spaces[0].setBorderArea( pointsToLook[0], pointsToLook[0]);

//        Imgproc.rectangle(returnMat, , new Scalar(20,20,200,50), 6);


//        _spaces[0].setBorderArea();
    }

    public void checkSpaces(org.opencv.core.Point[] pointsToLook){
        for (int i = 0; i < pointsToLook.length; i++) {
            for (int j = 0; j < 1; j++) {println
                if(_spaces[j].isWithinArea(pointsToLook[i])){
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                }
            }
        }
    }
    
    
    public void scheduleNextMovement(Point location1_, Point location2_){

    }

}