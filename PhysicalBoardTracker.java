import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

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
    public static PhysicalSpace[] _physicalSpaces = new PhysicalSpace[15]; //TODO: Make not static and private once cheater bounds view is finished
    public static int numberOfPlaySpaces = 9;//First 9 spaces are part of the game, rest are storage

    class PhysicalSpace {
        private int storageType = 0;
        private int ownerValue = 0;// 0 for empty, 1 for computer, 2 for player //TODO: Make more efficient than int
        //        private Rect2d ownedArea = new Rect2d();
        private org.opencv.core.Point ownedTopRight;
        private org.opencv.core.Point ownedTopLeft;
        protected int position = -1;
        private boolean isPlaySpace = false;
        private int screenPosistionX =-1, screenPosistionY=-1;//1 for not set yet

        public PhysicalSpace(int position) {
            this.position = position;
            this.ownerValue = 0;
            if(position < numberOfPlaySpaces){
                this.isPlaySpace = true;
            }
        }

        public PhysicalSpace(int position, int owner_) {
            this.position = position;
            this.ownerValue = owner_;
            if(position < numberOfPlaySpaces){
                this.isPlaySpace = true;
            }
        }

        public int getOwner() {
            return ownerValue;
        }
//        public void setOwner(int newOwner){ownerValue = newOwner;}
        public int getPosition() {return position; }
        public boolean getIsPlaySpace(){return isPlaySpace;}
        public int getX(){return screenPosistionX;}
        public void setX(int newPos){screenPosistionX=newPos;}
        public int getY(){return screenPosistionY;}
        public void setY(int newPos){screenPosistionY=newPos;}

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
            setX((int)Math.round(bottomLeft.x+(topRight.x-bottomLeft.x)/2));
            setY((int)Math.round(topRight.y+(bottomLeft.y-topRight.y)/2));

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

    public int getNextStoreSpaceLocation(int ownerLookingFor_){//0 for empty, 1 for computer, 2 for player//@@@
        for(int i = numberOfPlaySpaces; i < _physicalSpaces.length; i++) {//TODO: Make check owner side first
            if(_physicalSpaces[i].getOwner()==ownerLookingFor_){
                return i;
            }
        }
        System.out.println("Error in PhysicalBoardTracker, could not find owner "+ownerLookingFor_+" in the storage spots.");
        return -1;
    }

    public void printKnownSpaces(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print("["+i*3+j+","+_physicalSpaces[i*3+j].getOwner()=="_"?0:_physicalSpaces[i*3+j].getOwner()==1?"C":"P"+"]");
            }
        }
    }


    public void resetBoard(){

    }

    public void detectAndSetAllSpaces(org.opencv.core.Point[] pointsToLook, int width, int height){
        int widthCorners = (int) Math.round((pointsToLook[1].x-pointsToLook[0].x));
        int heightCorners = (int) Math.round((pointsToLook[1].y-pointsToLook[0].y));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                _physicalSpaces[i*3+j] = new PhysicalSpace(i*3+j);

                org.opencv.core.Point topRight = new org.opencv.core.Point(Math.round((pointsToLook[0].x+(j*widthCorners/3))+width*.2),Math.round((pointsToLook[1].y+(height/2)-(i*heightCorners/3))-height*.2));
                org.opencv.core.Point bottomLeft = new org.opencv.core.Point(Math.round((pointsToLook[0].x+width+(j*widthCorners/3)+width*.2)),Math.round((pointsToLook[1].y+1.5*(height)-(i*heightCorners/3)-height*.2)));


                System.out.println("<<<POINT #"+(i*3+j)+">>>");
                System.out.println(topRight.x+","+topRight.y);
                System.out.println(bottomLeft.x+","+bottomLeft.y);
//                System.out.println("width  =" + width);
//                System.out.println("height =" + height);

                _physicalSpaces[i*3+j].setBorderArea( topRight, bottomLeft);
            }
        }
//Left side
        for (int i = 0; i < 3; i++) {
            _physicalSpaces[9+i] = new PhysicalSpace(9+i);

            org.opencv.core.Point topRight = new org.opencv.core.Point(Math.round((pointsToLook[0].x - (2 * widthCorners / 3)) + width * .2), Math.round((pointsToLook[1].y+(height/2)-(i*heightCorners/3))-height*.2));
            org.opencv.core.Point bottomLeft = new org.opencv.core.Point(Math.round((pointsToLook[0].x+width-(2*widthCorners/3)+width*.2)),Math.round((pointsToLook[1].y+1.5*(height)-(i*heightCorners/3)-height*.2)));

        System.out.println("<<<POINT #"+(9+i)+">>>");
            System.out.println(topRight.x + "," + topRight.y);
            System.out.println(bottomLeft.x + "," + bottomLeft.y);

            _physicalSpaces[9+i].setBorderArea( topRight, bottomLeft);
        }
//Right side
        for (int i = 0; i < 3; i++) {
            _physicalSpaces[12+i] = new PhysicalSpace(12+i);

            org.opencv.core.Point topRight = new org.opencv.core.Point(Math.round((pointsToLook[1].x + (2 * widthCorners / 3)) -width*.2), Math.round((pointsToLook[1].y+(height/2)-(i*heightCorners/3))-height*.2));
            org.opencv.core.Point bottomLeft = new org.opencv.core.Point(Math.round((pointsToLook[1].x-width+(2*widthCorners/3)-width*.2)),Math.round((pointsToLook[1].y+1.5*(height)-(i*heightCorners/3)-height*.2)));

            System.out.println("<<<POINT #"+(12+i)+">>>");
            System.out.println(topRight.x + "," + topRight.y);
            System.out.println(bottomLeft.x + "," + bottomLeft.y);

            _physicalSpaces[12+i].setBorderArea( topRight, bottomLeft);
        }
    }

    public static void cheatingBoundsOverlay(Mat drawOnto){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Scalar color = new Scalar((j==0?255:0),(j==1?255:0),(j==2?255:0),255);
                Imgproc.rectangle(drawOnto, new org.opencv.core.Point(
                        _physicalSpaces[i*3+j].ownedTopLeft.x, _physicalSpaces[i*3+j].ownedTopLeft.y
                ), new org.opencv.core.Point(
                        _physicalSpaces[i*3+j].ownedTopRight.x, _physicalSpaces[i*3+j].ownedTopRight.y
                ), color , 2);
            }
        }
        Scalar color = new Scalar(64,224,208,255);
        for (int i = 0; i < 3; i++) {
            Imgproc.rectangle(drawOnto, new org.opencv.core.Point(
                    _physicalSpaces[9+i].ownedTopLeft.x, _physicalSpaces[9+i].ownedTopLeft.y
            ), new org.opencv.core.Point(
                    _physicalSpaces[9+i].ownedTopRight.x, _physicalSpaces[9+i].ownedTopRight.y
            ), color , 2);
        }
        color = new Scalar(64,224,208,255);
        for (int i = 0; i < 3; i++) {
            Imgproc.rectangle(drawOnto, new org.opencv.core.Point(
                    _physicalSpaces[12+i].ownedTopLeft.x, _physicalSpaces[12+i].ownedTopLeft.y
            ), new org.opencv.core.Point(
                    _physicalSpaces[12+i].ownedTopRight.x, _physicalSpaces[12+i].ownedTopRight.y
            ), color , 2);
        }
    }

    public ArrayList<Integer> checkForAllPosistions(org.opencv.core.Point[] pointsToLook){
        System.out.println("Looking for point matches");
        ArrayList<Integer> locatedSpots = new ArrayList<Integer>();

        for (int i = 0; i < pointsToLook.length; i++) {
            System.out.println("Looking for a area that contains "+pointsToLook[i].x + ", " + pointsToLook[i].y);
            for (int j = 0; j < 9; j++) {
                if(_physicalSpaces[j].isWithinArea(pointsToLook[i])){
                    System.out.println("There is a game piece located at position #"+ _physicalSpaces[j].position);
//                    System.out.println("This is "+_spaces[j].ownedTopRight + " & "+_spaces[j].ownedTopLeft);
                    locatedSpots.add(j);
                }else{
//                    System.out.println("There is NOT a game piece located at position #"+_spaces[j].position);
                }
            }
        }
        return locatedSpots;
    }
    
    public ArrayList<Integer> checkForNewPieces(ArrayList<Integer> allDetectedLocations){
        ArrayList<Integer> newPosistions = new ArrayList<Integer>();
        System.out.println("Checking last know locations for different pieces.");

        for (int i = 0; i < allDetectedLocations.size(); i++) {
            if(_physicalSpaces[allDetectedLocations.get(i)].ownerValue != 2 ){//TODO: Check if it changes color
                newPosistions.add(allDetectedLocations.get(i));
            }
        }

        System.out.println("newPosistions =" + newPosistions);
        return newPosistions;
    }
//    public setNewPieces


    public void scheduleNextMovement(Point location1_, Point location2_){

    }

}