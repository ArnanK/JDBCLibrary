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

    // Event Handler
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
                + ";user=" + username + ";password=" + password + ";";        
        if(connectedToDatabase(connectionUrl)){
            App.connectURL(connectionUrl);
            
        }
        else{
            showErrorMessage("Failed to Connect to Database.");
        }
    }

    private boolean connectedToDatabase(String connectionUrl) {
        try (java.sql.Connection con = java.sql.DriverManager.getConnection(connectionUrl);
             java.sql.Statement stmt = con.createStatement()) {
            // You can perform SQL operations here
            return true;
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            // Handle connection errors
            return false;
        }
    }
    // Method to show an error message
    private void showErrorMessage(String message) {
        JFrame errorFrame = new JFrame("Error");
        JOptionPane.showMessageDialog(errorFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }


    // Getters for user input
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

    public String getPassword() {
        return passwordField.getText();
    }


}
