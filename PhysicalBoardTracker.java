import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class PhysicalBoardTracker {
/*
        {ARM}
 C                  P
 V                  V
[ 9]  [0][1][2]  [12]
[10]  [3][4][5]  [13]
[11]  [6][7][8]  [14]

*/
    public static PhysicalSpace[] _spaces = new PhysicalSpace[15]; //TODO: Make not static and private once cheater bounds view is finished

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
//        public void setOwner(int newOwner){ownerValue = newOwner;}

        public void setBorderArea(org.opencv.core.Point topRight, org.opencv.core.Point bottomLeft) {

//            System.out.println("topRight.x = " + topRight.x);
//            System.out.println("topRight.y = " + topRight.y);
//            System.out.println("bottomLeft.x = " + bottomLeft.x);
//            System.out.println("bottomLeft.y = " + bottomLeft.y);
//            ownedArea.set(new double[]{topRight.x,topRight.y,bottomLeft.x,bottomLeft.y});//new double[]{topRight.x, topRight.y, bottomLeft.x, bottomLeft.y});
            ownedTopRight = bottomLeft;//TODO:
            ownedTopLeft = topRight;//new double[]{topRight.x, topRight.y, bottomLeft.x, bottomLeft.y});
//            ownedArea.width = (int) (bottomLeft.x - topRight.x);
//            ownedArea.height = (int) (bottomLeft.y - topRight.y);

        }

        public boolean isWithinArea(org.opencv.core.Point centerPoint) {
//            System.out.println("Looking within this area: "+ownedArea.x);
//            System.out.println(ownedTopRight.x);
//            System.out.println(ownedTopLeft.y);
//            System.out.println("ownedTopRight.x = "+ownedTopRight.x);
//            System.out.println("centerPoint.x = "+centerPoint.x);
//            System.out.println("ownedTopLeft.x = "+ownedTopLeft.x);
            if (ownedTopRight.x > centerPoint.x && centerPoint.x > ownedTopLeft.x) {
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
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                _spaces[i*3+j] = new PhysicalSpace(i*3+j);
                int widthCorners = (int) Math.round((pointsToLook[1].x-pointsToLook[0].x));
                int heightCorners = (int) Math.round((pointsToLook[1].y-pointsToLook[0].y));

                org.opencv.core.Point topRight = new org.opencv.core.Point(Math.round((pointsToLook[0].x+(j*widthCorners/3))+width*.2),Math.round((pointsToLook[1].y+(height/2)-(i*heightCorners/3))-height*.2));
                org.opencv.core.Point bottomLeft = new org.opencv.core.Point(Math.round((pointsToLook[0].x+width+(j*widthCorners/3)+width*.2)),Math.round((pointsToLook[1].y+1.5*(height)-(i*heightCorners/3)-height*.2)));


                System.out.println("<<<POINT #"+(i*3+j)+">>>");
                System.out.println(topRight.x+","+topRight.y);
                System.out.println(bottomLeft.x+","+bottomLeft.y);
//                System.out.println("width  =" + width);
//                System.out.println("height =" + height);

                _spaces[i*3+j].setBorderArea( topRight, bottomLeft);
            }
        }

    }

    public static void cheatingBoundsOverlay(Mat drawOnto){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Scalar color = new Scalar((j==0?255:0),(j==1?255:0),(j==2?255:0),255);
                Imgproc.rectangle(drawOnto, new org.opencv.core.Point(
                        _spaces[i*3+j].ownedTopLeft.x,_spaces[i*3+j].ownedTopLeft.y
                ), new org.opencv.core.Point(
                        _spaces[i*3+j].ownedTopRight.x,_spaces[i*3+j].ownedTopRight.y
                ), color , 2);
            }
        }

    }

    public ArrayList<Integer> checkSpaces(org.opencv.core.Point[] pointsToLook){
        System.out.println("Looking for point matches");
        ArrayList<Integer> locatedSpots = new ArrayList<Integer>();

        for (int i = 0; i < pointsToLook.length; i++) {
            System.out.println("Looking for a area that contains "+pointsToLook[i].x + ", " + pointsToLook[i].y);
            for (int j = 0; j < 9; j++) {
                if(_spaces[j].isWithinArea(pointsToLook[i])){
                    System.out.println("There is a game piece located at position #"+_spaces[j].position);
//                    System.out.println("This is "+_spaces[j].ownedTopRight + " & "+_spaces[j].ownedTopLeft);
                    locatedSpots.add(j);
                }else{
//                    System.out.println("There is NOT a game piece located at position #"+_spaces[j].position);
                }
            }
        }
        return locatedSpots;
    }
    
    
    public void scheduleNextMovement(Point location1_, Point location2_){

    }

}