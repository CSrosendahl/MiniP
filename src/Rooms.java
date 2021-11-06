
import java.util.ArrayList;

public class Rooms  {

    public ArrayList<Rooms> rooms = new ArrayList<>();
    public Server server;



    public String name;
    public int maxUsers;

    public Rooms(String name, int maxUsers) {
        this.name = name;
        this.maxUsers = maxUsers;


    }


    public void getRooms() {


        for (int i = 0; i < rooms.size(); i++) {

            System.out.println("Room "  + i + ": " + rooms.get(i));

        }


    }
    public void setRooms(String name, int number)  {


        rooms.add(new Rooms(name,number));




    }

}
