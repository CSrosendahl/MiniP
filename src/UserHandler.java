import java.io.*;

import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class UserHandler implements  Runnable{

    public static ArrayList<UserHandler> userHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader buffReader;
    private BufferedWriter buffWriter;
    private String username;
    private boolean chat;
    private boolean welcomeMessage = true;
    public String time;
    private final static SimpleDateFormat timeDateFormat = new SimpleDateFormat("hh:mm:ss");
    public String[] profanity = new String[] {"shit", "fuck", "cunt", "dick", "bitch", "piss"};

    public UserHandler(Socket socket) {

        try{
            Time();
            this.socket = socket;
           this.buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
           this.buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           this.username = buffReader.readLine();
           userHandlers.add(this);
                   }
        catch (IOException e) {
            closeUser(socket, buffReader, buffWriter);
        }
    }

    @Override
    public void run() {


        String messClient;
        String actClient;



        while (socket.isConnected() && !chat){

            try {


                if(welcomeMessage) {
                    buffWriter.write("Welcome " + username + " you have joined the lobby!" + "\n" + "Commands: Join | Quit");
                    buffWriter.newLine();
                    buffWriter.flush();
                    welcomeMessage = false;

                } else {
                    buffWriter.write("Please use the commands" + "\n" +"Commands: Join | Quit");
                    buffWriter.newLine();
                    buffWriter.flush();
                }


                actClient = buffReader.readLine();

                if(actClient.equalsIgnoreCase("Join")){

                    buffWriter.write("You joined the chat! Please dont swear " + username +"!" + "\n" + "Type quit to close the server!");
                    buffWriter.newLine();
                    buffWriter.flush();
                    broadcast("[" + time + "]: "  + username + " has joined the chat!");
                    chat = true;
                }
                if(actClient.equalsIgnoreCase("quit")){

                    buffWriter.write("You have left the server");
                    buffWriter.newLine();
                    buffWriter.flush();
                    socket.close();


                }


            } catch (Exception e) {
                closeUser(socket, buffReader, buffWriter);
                break;
            }
        }
        while(socket.isConnected() && chat) {
            try {

                messClient = buffReader.readLine();
                String profanityCheck = messClient.toLowerCase(Locale.ROOT);
                for (String s : profanity) {
                    if (profanityCheck.contains(s)) {
                        messClient = "[" + time + "]" + "[" + username + "]: " + "I LOVE U <3";
                        break;
                    }
                }
                if(messClient.equalsIgnoreCase("[" + time + "]" + "[" + username + "]: " + "quit")){

                    System.out.println("A user has left the server: " + username);

                    socket.close();
                }else {
                    broadcast(messClient);
                }





            } catch (IOException e) {
                closeUserInChat(socket, buffReader, buffWriter);
                break;
            }
        }
    }

    public void broadcast(String broadcastMess) {
        for(UserHandler userHandler : userHandlers) {
            try{
                if(!userHandler.username.equals(username)) {
                    userHandler.buffWriter.write(broadcastMess);
                    userHandler.buffWriter.newLine();
                    userHandler.buffWriter.flush();

                }
            } catch (IOException e) {
                closeUser(socket, buffReader, buffWriter);
            }
        }
    }

    public void removeUser()  {
        userHandlers.remove(this);
        broadcast("[" + time + "]: "+ username + " has left the chat ");

    }
    public void closeUser(Socket socket, BufferedReader buffReader, BufferedWriter buffWriter) {
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

    public void closeUserInChat(Socket socket, BufferedReader buffReader, BufferedWriter buffWriter) {
        removeUser();
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
    public void Time(){
        Date timeInMillis = new Date(System.currentTimeMillis());
        time = timeDateFormat.format(timeInMillis);
    }
}
