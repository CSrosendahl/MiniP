import java.io.*;

import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ClientHandler implements  Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private String clientUsername;
    private boolean chat;
    private boolean welcomeMessage = true;
    String time;
    private static SimpleDateFormat timeDateFormat = new SimpleDateFormat("hh:mm:ss");







    public ClientHandler(Socket socket) {

        try{
            Time();
            this.socket = socket;
           this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
           this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           this.clientUsername = bufferedReader.readLine();
           clientHandlers.add(this);
                   } catch (IOException e) {
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }

    @Override
    public void run() {





        String messageFromClient;
        String actionFromClient;



        while (socket.isConnected() && !chat){

            try {



                if(welcomeMessage) {
                    bufferedWriter.write("Welcome " + clientUsername + " you have joined the lobby!" + "\n" + "Commands: Join | Quit");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    welcomeMessage = false;

                } else {
                    bufferedWriter.write("Please use the commands" + "\n" +"Commands: Join | Quit");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }


                actionFromClient = bufferedReader.readLine();

                if(actionFromClient.equalsIgnoreCase("Join")){

                    bufferedWriter.write("You joined the group chatroom!");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    chat = true;
                }





            } catch (Exception e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
        while(socket.isConnected() && chat) {
            try {

                            messageFromClient = bufferedReader.readLine();
                            broadcastMessage(messageFromClient);




            } catch (IOException e) {
                closeEverythingInChat(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void broadcastMessage(String messageToSend) {
        for(ClientHandler clientHandler : clientHandlers) {
            try{
                if(!clientHandler.clientUsername.equals(clientUsername)) {
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();

                }
            } catch (IOException e) {
                closeEverything(socket,bufferedReader,bufferedWriter);
            }
        }
    }

    public void removeClientHandler()  {
        clientHandlers.remove(this);
        broadcastMessage("[" + time + "] "+ "[SERVER]: " + clientUsername + " has left the chat ");

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

    public void closeEverythingInChat(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
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
    public void Time(){
        Date timeInMillis = new Date(System.currentTimeMillis());
        time = timeDateFormat.format(timeInMillis);
    }
}
