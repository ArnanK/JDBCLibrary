import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class App {

    public static void main(String[] args) {
        loginGUI loginGUI = new loginGUI();
    }
    
    
    public static void connectURL(String connectionUrl){
        try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt=con.createStatement();){
            System.out.println("Connected to Database");
            sqlGUI sqlGUI = new sqlGUI(connectionUrl);
        }
        // Handle any errors that may have occurred.
        catch (SQLException a) {
            a.printStackTrace();
        }
    }

}

