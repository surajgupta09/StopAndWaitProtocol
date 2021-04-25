import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

class Frame implements Serializable {

    private static final long serialVersionUID = 1L;
    byte[] buffer;
    int frame_id;
    Frame() {
        buffer = new byte[R_Main.Buffer_Size];
    }
}

class Receiver extends Thread {

    private int PORT;

    public Receiver(int PORT){
        this.PORT = PORT;
    }

    public void run(){

//        ServerSocket receiver = null;
//        Socket connected = null;
//        try {
//            receiver = new ServerSocket(PORT);
//            connected = receiver.accept();
//            DataInputStream dis = new DataInputStream(connected.getInputStream());
//            R_Main.filename = dis.readUTF();
//            R_Main.filename = "copy.jpg";
//            dis.close();
//            connected.close();
//        }catch(IOException e) {
//            e.printStackTrace();
//        }

        File file = new File(R_Main.Sync_dir,R_Main.filename);
        FileOutputStream fileOutputStream = null;
        try{
            fileOutputStream  = new FileOutputStream(file);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        try{
            DatagramSocket datagramSocket = new DatagramSocket(PORT);
            byte[] buff = new byte[10000];
            DatagramPacket datagramPacket = new DatagramPacket(buff,10000);
            assert fileOutputStream != null;
            BufferedOutputStream br = new BufferedOutputStream(fileOutputStream);
            int old_frame_id = 1;
            byte[] ack = new byte[1];
            ack[0] = 1;
            DatagramPacket ackPacket = new DatagramPacket(ack,1);
            Frame frame;
            datagramSocket.setSoTimeout(R_Main.Timer);
            while(true){
                try {
                    datagramSocket.receive(datagramPacket);
                }catch(Exception e) {
                    continue;
                }
                ByteArrayInputStream bis = new ByteArrayInputStream(datagramPacket.getData());
                ObjectInput in = null;
                try{
                    in = new ObjectInputStream(bis);
                    frame = (Frame) in.readObject();
                }finally{
                    try{
                        if(in != null){
                            in.close();
                        }
                        bis.close();
                    }catch(IOException e){

                    }
                }
                System.out.println("RECEIVED DATA ::  FRAME ID : "+frame.frame_id+" FRAME_LENGTH : "+frame.buffer.length);
                if(old_frame_id != frame.frame_id){
                    br.write(frame.buffer,0,frame.buffer.length);
                    old_frame_id = frame.frame_id;
                    br.flush();
                }
                ack[0] = (frame.frame_id==1)? (byte)0:(byte)1;
                frame.frame_id = 1;
                ackPacket.setData(ack,0,1);
                ackPacket.setAddress(datagramPacket.getAddress());
                ackPacket.setPort(datagramPacket.getPort());
                datagramSocket.send(ackPacket);
                if(frame.buffer.length < R_Main.Buffer_Size){
                    break;
                }
                br.flush();
            }
            datagramSocket.close();
            br.close();
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        System.out.println("[DONE]");
    }
}

public class R_Main {
    static String filename = "receive.png";
    static int Buffer_Size = 1024;
    static String Sync_dir = "./";
    static int PORT = 8888;
    static int Timer = 500;
    public static void main ( String[] args ) throws InterruptedException {
        System.out.println("SERVER STARTED AT PORT: "+PORT);
        new Receiver(PORT).start();
        Thread.currentThread().join();
    }
}
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

class Frame implements Serializable {

    private static final long serialVersionUID = 1L;
    byte[] buffer;
    int frame_id;
    Frame() {
        buffer = new byte[R_Main.Buffer_Size];
    }
}

class Receiver extends Thread {

    private int PORT;

    public Receiver(int PORT){
        this.PORT = PORT;
    }

    public void run(){

//        ServerSocket receiver = null;
//        Socket connected = null;
//        try {
//            receiver = new ServerSocket(PORT);
//            connected = receiver.accept();
//            DataInputStream dis = new DataInputStream(connected.getInputStream());
//            R_Main.filename = dis.readUTF();
//            R_Main.filename = "copy.jpg";
//            dis.close();
//            connected.close();
//        }catch(IOException e) {
//            e.printStackTrace();
//        }

        File file = new File(R_Main.Sync_dir,R_Main.filename);
        FileOutputStream fileOutputStream = null;
        try{
            fileOutputStream  = new FileOutputStream(file);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        try{
            DatagramSocket datagramSocket = new DatagramSocket(PORT);
            byte[] buff = new byte[10000];
            DatagramPacket datagramPacket = new DatagramPacket(buff,10000);
            assert fileOutputStream != null;
            BufferedOutputStream br = new BufferedOutputStream(fileOutputStream);
            int old_frame_id = 1;
            byte[] ack = new byte[1];
            ack[0] = 1;
            DatagramPacket ackPacket = new DatagramPacket(ack,1);
            Frame frame;
            datagramSocket.setSoTimeout(R_Main.Timer);
            while(true){
                try {
                    datagramSocket.receive(datagramPacket);
                }catch(Exception e) {
                    continue;
                }
                ByteArrayInputStream bis = new ByteArrayInputStream(datagramPacket.getData());
                ObjectInput in = null;
                try{
                    in = new ObjectInputStream(bis);
                    frame = (Frame) in.readObject();
                }finally{
                    try{
                        if(in != null){
                            in.close();
                        }
                        bis.close();
                    }catch(IOException e){

                    }
                }
                System.out.println("RECEIVED DATA ::  FRAME ID : "+frame.frame_id+" FRAME_LENGTH : "+frame.buffer.length);
                if(old_frame_id != frame.frame_id){
                    br.write(frame.buffer,0,frame.buffer.length);
                    old_frame_id = frame.frame_id;
                    br.flush();
                }
                ack[0] = (frame.frame_id==1)? (byte)0:(byte)1;
                frame.frame_id = 1;
                ackPacket.setData(ack,0,1);
                ackPacket.setAddress(datagramPacket.getAddress());
                ackPacket.setPort(datagramPacket.getPort());
                datagramSocket.send(ackPacket);
                if(frame.buffer.length < R_Main.Buffer_Size){
                    break;
                }
                br.flush();
            }
            datagramSocket.close();
            br.close();
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        System.out.println("[DONE]");
    }
}

public class R_Main {
    static String filename = "receive.png";
    static int Buffer_Size = 1024;
    static String Sync_dir = "./";
    static int PORT = 8888;
    static int Timer = 500;
    public static void main ( String[] args ) throws InterruptedException {
        System.out.println("SERVER STARTED AT PORT: "+PORT);
        new Receiver(PORT).start();
        Thread.currentThread().join();
    }
}

