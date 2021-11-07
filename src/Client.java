import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Client  {

 private Socket socket;
 private BufferedReader bufferedReader;
 private BufferedWriter bufferedWriter;
 private String userName;
 private boolean joinedRoom = false;
 private final static int portNumb = 8989;
 static String time;
 private final static SimpleDateFormat timeDateFormat = new SimpleDateFormat("hh:mm:ss");

 public Client(Socket socket, String userName) {
     try {
         Time();
         this.socket = socket;
         this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
         this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         this.userName = userName;

     } catch (IOException e) {
        closeEverything(socket,bufferedReader,bufferedWriter);
     }
 }
 public void sendMessage() {
     try {
         bufferedWriter.write(userName);
         bufferedWriter.newLine();
         bufferedWriter.flush();

         Scanner scanner = new Scanner(System.in);
         String messageToSend;
         while(socket.isConnected() && !joinedRoom){
             messageToSend = scanner.nextLine();
             bufferedWriter.write(messageToSend);
             bufferedWriter.newLine();
             bufferedWriter.flush();
             if (messageToSend.equalsIgnoreCase("join")){
                 joinedRoom=true;
             }
             if (messageToSend.equalsIgnoreCase("luk")){
                 socket.close();
             }



         }
         while(socket.isConnected() && joinedRoom) {
             messageToSend = scanner.nextLine();
             bufferedWriter.write("[" + time + "]" + "[" + userName + "]: " + messageToSend);
             bufferedWriter.newLine();
             bufferedWriter.flush();

             if (messageToSend.equalsIgnoreCase("luk")){

                 socket.close();
             }

         }

     } catch (IOException e) {
         closeEverything(socket,bufferedReader,bufferedWriter);

     }
 }
 public void listenForMessage(){
     new Thread(() -> {
        String msgFromGroupChat;
        while(socket.isConnected()) {
            try {

                msgFromGroupChat = bufferedReader.readLine();
                System.out.println(msgFromGroupChat);




            } catch (IOException e) {
                closeEverything(socket,bufferedReader,bufferedWriter);
            }
        }
     }).start();
 }

 public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {


     try {
         if(bufferedReader != null) {
             bufferedReader.close();
         }
         if(bufferedWriter != null) {
             bufferedWriter.close();
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
     String userName = scanner.nextLine();
     Socket socket = new Socket("localhost",portNumb);
     Client client = new Client(socket,userName);
     client.listenForMessage();
     client.sendMessage();
 }
    public static void Time(){
        Date timeInMillis = new Date(System.currentTimeMillis());
        time = timeDateFormat.format(timeInMillis);
    }
}
