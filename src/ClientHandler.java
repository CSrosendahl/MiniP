import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

import static java.lang.Integer.parseInt;

public class ClientHandler implements  Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    public static LinkedList<String> roomsList = new LinkedList<>();
    private Socket socket;
    public Server server;
    public Rooms roomClass;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;
    private boolean chat;
    private boolean welcomeMessage = true;
    public static String roomName;
    public static int roomIndex;
    public boolean roomCreated = false;
    private boolean testBool = false;
    public int maxUsers;







    public ClientHandler(Socket socket) {

        try{
            this.socket = socket;
           this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
           this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           this.clientUsername = bufferedReader.readLine();
           clientHandlers.add(this);
           //broadcastMessage("SERVER: " + clientUsername + " has entered the chat");
        } catch (IOException e) {
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }

    @Override
    public void run() {


        //roomClass.rooms.add(new Rooms(roomName, maxUsers));
       // System.out.println("Number of rooms created is " + roomClass.rooms.size());




        String messageFromClient;
        String actionFromClient;
        int roomIndex;


        while (socket.isConnected() && !chat){

            try {



                if(welcomeMessage) {
                    bufferedWriter.write("Welcome " + clientUsername + " you have joined the lobby!" + "\n" + "Commands: Rooms | Create | Quit");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    welcomeMessage = false;

                } else {
                    bufferedWriter.write("Please use the commands" + "\n" + "Commands: Join | Create | Quit");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }


                actionFromClient = bufferedReader.readLine();

                if(actionFromClient.equalsIgnoreCase("rooms")){

                    bufferedWriter.write("Rooms: ");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    bufferedWriter.write("Type the number of the room you would like to join.");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    roomIndex = bufferedReader.read();

                        if(roomsList.isEmpty()){
                            bufferedWriter.write("Type the number of the room you would like to join.");
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                        }else{
                        bufferedWriter.write("You joined a chatroom! " + "\n" + "Room " + roomIndex + roomsList.get(roomIndex) + ": ");
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                        chat = true;}


                }
                else if(actionFromClient.equalsIgnoreCase("Create")){

                    bufferedWriter.write("Please enter the name of your room");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    roomName =  bufferedReader.readLine();
                    roomsList.add(roomName);
                    bufferedWriter.write("You created the chatroom: " + "\n" + "Room " + roomsList.indexOf(roomName) + ": "+ roomName);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    chat=true;

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
                closeEverything(socket, bufferedReader, bufferedWriter);
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
        broadcastMessage("SERVER: " + clientUsername + " has left the chat ");

    }
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
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
}
