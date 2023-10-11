import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class loginGUI extends JFrame implements ActionListener {
    private JTextField serverNameField, portNumberField, dbNameField, usernameField, passwordField;

    // The `loginGUI()` method is the constructor for the `loginGUI` class. It is
    // responsible for
    // creating and initializing the graphical user interface (GUI) components of
    // the login form.
    public loginGUI() {
        setTitle("JDBC Class Library");
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the frame on the screen
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Adding labels and text fields
        serverNameField = addLabelAndTextField("Server Name:", gbc);
        portNumberField = addLabelAndTextField("Port Number:", gbc);
        dbNameField = addLabelAndTextField("Database Name:", gbc);
        usernameField = addLabelAndTextField("Username:", gbc);
        passwordField = addLabelAndTextField("Password:", gbc);

        // Adding a submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(this);
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        this.add(submitButton, gbc);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * The function "addLabelAndTextField" adds a label and a text field to a
     * container using
     * GridBagLayout and returns the created text field.
     * 
     * @param labelText The labelText parameter is a String that represents the text
     *                  that will be
     *                  displayed on the JLabel.
     * @param gbc       GridBagConstraints object that specifies the constraints for
     *                  the layout of the
     *                  components in the container.
     * @return The method is returning a JTextField object.
     */
    private JTextField addLabelAndTextField(String labelText, GridBagConstraints gbc) {
        gbc.gridx = 0;
        JLabel label = new JLabel(labelText);
        this.add(label, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 1;
        JTextField textField = new JTextField(15); // Adjust the size of the text fields
        this.add(textField, gbc);
        gbc.gridy++;

        return textField; // Return the created JTextField to be assigned to the instance variable
    }

    /**
     * This function handles the submit button click event and constructs a
     * connection URL for
     * connecting to a SQL Server database.
     * 
     * @param e The "e" parameter in the actionPerformed method is an ActionEvent
     *          object that
     *          represents the event that occurred, such as a button click.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle the submit button click event
        String serverName = serverNameField.getText();
        String portNumber = portNumberField.getText();
        String dbName = dbNameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Construct the connection URL with user input
        String connectionUrl = "jdbc:sqlserver://" + serverName + ":" + portNumber + ";databaseName=" + dbName
                + ";user=" + username + ";password=" + password + ";" + "encrypt=true;trustServerCertificate=true";
        if (connectedToDatabase(connectionUrl)) { // if valid, connect.
            App.connectURL(connectionUrl); // from App class.

        } else {
            showErrorMessage("Failed to Connect to Database."); // else show error message.
        }
    }

    /**
     * The function checks if a connection to a database can be established and
     * returns true if
     * successful, false otherwise.
     * 
     * @param connectionUrl The connectionUrl parameter is a string that represents
     *                      the URL or address
     *                      of the database that you want to connect to. It
     *                      typically includes information such as the
     *                      database type (e.g., MySQL, Oracle, etc.), the host or
     *                      server name, the port number, and any
     *                      additional parameters required to establish the
     * @return The method is returning a boolean value. If the connection to the
     *         database is
     *         successful, it will return true. If there is an error connecting to
     *         the database, it will return
     *         false.
     */
    private boolean connectedToDatabase(String connectionUrl) {
        try (java.sql.Connection con = java.sql.DriverManager.getConnection(connectionUrl);
                java.sql.Statement stmt = con.createStatement()) {
            return true;
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            // Handle connection errors
            return false;
        }
    }

    /**
     * The function creates a pop-up window to display an error message in a Java
     * application.
     * 
     * @param message The message parameter is a string that represents the error
     *                message that you want
     *                to display.
     */
    private void showErrorMessage(String message) {
        JFrame errorFrame = new JFrame("Error");
        JOptionPane.showMessageDialog(errorFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // The code you provided is defining getter methods for the server name, port
    // number, database name,
    // username, and password fields in the loginGUI class. These getter methods
    // allow other parts of
    // the code to access the values entered in these fields.
    public String getServerName() {
        return serverNameField.getText();
    }

    public String getPortNumber() {
        return portNumberField.getText();
    }

    public String getDbName() {
        return dbNameField.getText();
    }

    public String getUsername() {
        return usernameField.getText();
    }

    protected String getPassword() {
        return passwordField.getText();
    }

}
