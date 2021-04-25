import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;

class Frame implements Serializable {

    private static final long serialVersionUID = 1L;
    byte[] buffer;
    int frame_id;
    Frame() {
        buffer = new byte[S_Main.Buffer_Size];
    }
}

class Sender extends Thread{

    private InetAddress IP_ADDRESS;
    private int PORT;
    Sender(InetAddress IP_ADDRESS, int PORT){
        this.IP_ADDRESS = IP_ADDRESS;
        this.PORT = PORT;
    }

    public void run(){

        File folder = new File(S_Main.Sync_dir);
//        Socket sender=null;
        File file = new File(folder,S_Main.filename);
//        try {
//            sender = new Socket(IP_ADDRESS, PORT);
//            DataOutputStream dos = new DataOutputStream( sender.getOutputStream() );
//            dos.writeUTF(S_Main.filename);
//            dos.close();
//            sender.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            DatagramSocket datagramSocket = new DatagramSocket();
            byte[] ack = new byte[1];
            DatagramPacket ackPacket = new DatagramPacket(ack,1);
            BufferedInputStream br = new BufferedInputStream(new FileInputStream(file));
            int read;
            datagramSocket.setSoTimeout(S_Main.Timer);
            ByteArrayOutputStream bos;
            ObjectOutput out;
            Frame frame = new Frame();
            frame.frame_id = 0;
            byte[] temp = new byte[S_Main.Buffer_Size];
            System.out.println(file.getTotalSpace());
            while((read = br.read(temp,0,S_Main.Buffer_Size)) != -1) {
                frame.buffer = Arrays.copyOf(temp, read);
                bos = new ByteArrayOutputStream();
                out = new ObjectOutputStream(bos);
                out.writeObject(frame);
                out.flush();
                byte[] buffer = bos.toByteArray();
                bos.close();
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, IP_ADDRESS, PORT);
                datagramPacket.setData(buffer, 0, buffer.length);
                while (true) {
                    datagramSocket.send(datagramPacket);
                    System.out.println("SENDING DATA ::  FRAME ID : " + frame.frame_id + " FRAME LENGTH : " + frame.buffer.length);
                    try {
                        datagramSocket.receive(ackPacket);
                    } catch (Exception e) {
                        continue;
                    }
                    byte recv_ack = ackPacket.getData()[0];
                    if (frame.frame_id != recv_ack) {
                        frame.frame_id = recv_ack == 1 ? 1 : 0;
                        break;
                    }
                }
            }
            datagramSocket.close();
            br.close();
        } catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("[DONE]");
    }
}


public class S_Main{

    static Scanner scan  = new Scanner(System.in);
    static String Sync_dir = "./";
    static int Timer = 500;
    static int Buffer_Size = 1024;
    static String filename = "image.png";
    final static String IP_ADDRESS = "";
    private final static int PORT = 8888;
    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        new Sender(InetAddress.getLocalHost(), PORT).start();
        Thread.currentThread().join();
        // Wait Thread
    }
}
