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


    public void startServer() {
        try{
            Time();



            while(!serverSocket.isClosed()) {
               Socket socket = serverSocket.accept();
               System.out.println("[" + time + "] "+"A new user has entered the lobby");
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
        ServerSocket serverSocket = new ServerSocket(portNumb);
        Server server = new Server(serverSocket);
        server.startServer();
    }
    public void Time(){
        Date timeInMillis = new Date(System.currentTimeMillis());
        time = timeDateFormat.format(timeInMillis);
    }



}
