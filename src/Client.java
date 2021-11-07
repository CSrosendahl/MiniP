import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Client  {

 private Socket socket;
 private BufferedReader buffReader;
 private BufferedWriter buffWriter;
 private String username;
 private boolean joinedRoom = false;
 private final static int portNumb = 8989;
 static String time;
 private final static SimpleDateFormat timeDateFormat = new SimpleDateFormat("hh:mm:ss");

 public Client(Socket socket, String username) {
     try {
         Time();
         this.socket = socket;
         this.buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
         this.buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         this.username = username;

     } catch (IOException e) {
        closeClient(socket, buffReader, buffWriter);
     }
 }
 public void sendMessage() {
     try {
         buffWriter.write(username);
         buffWriter.newLine();
         buffWriter.flush();

         Scanner scanner = new Scanner(System.in);
         String messSend;
         while(socket.isConnected() && !joinedRoom){
             messSend = scanner.nextLine();
             buffWriter.write(messSend);
             buffWriter.newLine();
             buffWriter.flush();
             if (messSend.equalsIgnoreCase("join")){
                 joinedRoom=true;
             }
             if (messSend.equalsIgnoreCase("quit")){
                 socket.close();
             }



         }
         while(socket.isConnected() && joinedRoom) {
             messSend = scanner.nextLine();
             buffWriter.write("[" + time + "]" + "[" + username + "]: " + messSend);
             buffWriter.newLine();
             buffWriter.flush();

             if (messSend.equalsIgnoreCase("quit")){

                 socket.close();
             }

         }

     } catch (IOException e) {
         closeClient(socket, buffReader, buffWriter);

     }
 }
 public void receivedMessage(){
     new Thread(() -> {
        String receivedBroadcast;
        while(socket.isConnected()) {
            try {

                receivedBroadcast = buffReader.readLine();
                System.out.println(receivedBroadcast);




            } catch (IOException e) {
                closeClient(socket, buffReader, buffWriter);
            }
        }
     }).start();
 }

 public void closeClient(Socket socket, BufferedReader buffReader, BufferedWriter buffWriter) {


     try {
         if(buffReader != null) {
             buffReader.close();
         }
         if(buffWriter != null) {
             buffWriter.close();
         }
         if(socket != null) {
             socket.close();

         }
     } catch (IOException e) {
         e.printStackTrace();
     }
 }
 public static void main(String[] args) throws IOException {
     Time();
     Scanner scanner = new Scanner(System.in);
     System.out.println("Enter your username for the group chat: ");
     String username = scanner.nextLine();
     Socket socket = new Socket("localhost",portNumb);
     Client client = new Client(socket,username);
     client.receivedMessage();
     client.sendMessage();
 }
    public static void Time(){
        Date timeInMillis = new Date(System.currentTimeMillis());
        time = timeDateFormat.format(timeInMillis);
    }
}
