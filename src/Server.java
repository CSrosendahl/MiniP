import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Server {
    private final ServerSocket serverSocket;
    public String time;
    private final static SimpleDateFormat timeDateFormat = new SimpleDateFormat("hh:mm:ss");
    private final static int portNumb = 8989;





    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }


    public void runServer() {
        try{

            Time();
            System.out.println("The chat server started at " + time + "!");


            while(!serverSocket.isClosed()) {
               Socket socket = serverSocket.accept();
               System.out.println("[" + time + "] "+"A new user has entered the lobby");
               UserHandler userHandler = new UserHandler(socket);




               Thread thread = new Thread(userHandler);
               thread.start();
            }
        } catch (IOException e) {
            closeServer();
        }
    }
    public void closeServer() {
        try{
            if(serverSocket != null) {
                serverSocket.close();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(portNumb);
        Server server = new Server(serverSocket);
        server.runServer();
    }
    public void Time(){
        Date timeInMillis = new Date(System.currentTimeMillis());
        time = timeDateFormat.format(timeInMillis);
    }



}
