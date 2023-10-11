import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class App {

    /**
     * The main function creates an instance of the loginGUI class to display a
     * login interface for
     * accessing a database.
     */
    public static void main(String[] args) {
        loginGUI loginGUI = new loginGUI(); // login to Database GUI
    }

    /**
     * The function connects to a database using a given connection URL and creates
     * a new GUI for SQL
     * queries.
     * 
     * @param connectionUrl The connectionUrl parameter is a string that represents
     *                      the URL of the
     *                      database that you want to connect to. It typically
     *                      includes information such as the database
     *                      type, host, port, and database name.
     */
    public static void connectURL(String connectionUrl) {
        try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {

            System.out.println("Connected to Database");
            sqlGUI sqlGUI = new sqlGUI(connectionUrl); // new GUI
        }
        // Handle any errors that may have occurred.
        catch (SQLException a) {
            a.printStackTrace();
        }
    }

}
