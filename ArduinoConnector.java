import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;

public class ArduinoConnector extends Thread {
    private boolean keepLooping = false;
    private TTYUSBConnection devConnection = new TTYUSBConnection("/dev/ttyUSB0");

    class TTYUSBConnection {
        private final String streamName; //TODO: Make changeable possibly?
        private FileOutputStream outputStream; //TODO: Should I make it close each time?
        private FileInputStream inputStream; //TODO: Should I make it close each time?
        private Scanner inputScanner; //TODO: Should I make it close each time?

        public TTYUSBConnection(String connectionName) {
            this.streamName = connectionName;
            try{
                this.outputStream = new FileOutputStream(connectionName);
                this.inputStream = new FileInputStream(connectionName);
                this.inputScanner = new Scanner(this.inputStream);

            }catch(Exception e) {
                e.printStackTrace();
            }
        }

        public void sendMessage(String message) throws Exception {
            outputStream.write(message.getBytes());//            outputStream.close();
        }
        public void listen() throws Exception {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

//            while (inputScanner.hasNextLine()) {
            System.out.println(">>>>>"+inputScanner.nextLine());
//            }
        }
    }




    /**
     * Thread run under the main() class
     */
    public void run(){
        try{
            keepLooping = true;




            System.out.println("Thread " + this.currentThread().getId() + " is running");
            while(keepLooping){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                devConnection.sendMessage("This is the java program sending some bytes in an attempt to connect");
//                try {
//                    Thread.sleep(10000000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                while(keepLooping){
                    devConnection.listen();
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }


}