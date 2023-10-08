import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class App {

    public static void main(String[] args) {
        loginGUI loginGUI = new loginGUI(); //login to Database GUI
    }
    

    /**
     * 
     * @param connectionUrl - connection string to connect to DB
     * Method connects to DB and instanstiates new GUI.
     * 
     */
    public static void connectURL(String connectionUrl){
        try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt=con.createStatement();){//utilizes JDBCSQL docs.
            System.out.println("Connected to Database");
            sqlGUI sqlGUI = new sqlGUI(connectionUrl); //new GUI
        }
        // Handle any errors that may have occurred.
        catch (SQLException a) {
            a.printStackTrace();
        }
    }

}

