import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

public class Server {
    private ServerSocket serverSocket;
    //public Rooms roomClass;
    public ClientHandler clientHandler;

    public static LinkedList<String> Rooms = new LinkedList<>();







    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }


    public void startServer() {
        try{




            while(!serverSocket.isClosed()) {
               Socket socket = serverSocket.accept();
               System.out.println("A new user has entered the lobby");
               ClientHandler clientHandler = new ClientHandler(socket);




               Thread thread = new Thread(clientHandler);
               thread.start();
            }
        } catch (IOException e) {
            closeServerSocket();
        }
    }
    public void closeServerSocket() {
        try{
            if(serverSocket != null) {
                serverSocket.close();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(6969);
        Server server = new Server(serverSocket);
        server.startServer();





    }




}
