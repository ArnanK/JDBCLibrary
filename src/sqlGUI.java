import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

public class sqlGUI extends JFrame implements ActionListener {
    JMenuBar menuBar = new JMenuBar();// menu

    // The `sqlGUI` constructor is initializing the GUI window for the SQL
    // application.
    public sqlGUI(String connectionUrl) {
        setTitle(connectionUrl);
        setSize(800, 600); // Set the size to 800x600
        setLocationRelativeTo(null); // Center the frame on the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createMenuBar(); // create menubar
        setVisible(true);
        setJMenuBar(menuBar); // sets menu bar to the JFrame menu bar.

    }

    // The `createMenuBar()` method is responsible for creating the menu bar for the
    // SQL application.
    // It creates a menu bar with a single menu called "File". Inside the "File"
    // menu, it adds two menu
    // items: "Open" and "Quit".
    private void createMenuBar() {
        JMenuItem item;
        JMenu fileMenu = new JMenu("File");

        item = new JMenuItem("Open"); // Open...
        item.addActionListener(this);
        fileMenu.add(item);

        fileMenu.addSeparator();

        item = new JMenuItem("Quit"); // Quit
        item.addActionListener(this);
        fileMenu.add(item);

        menuBar.add(fileMenu);
    } // createMenu

    /**
     * The actionPerformed method removes all content from the container, checks the
     * action command of
     * the event, and performs different actions based on the menu name.
     * 
     * @param event The event parameter is an ActionEvent object that represents the
     *              action that
     *              occurred, such as a button click or menu selection.
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        this.getContentPane().removeAll();
        String menuName;
        menuName = event.getActionCommand();
        if (menuName.equals("Open"))
            openFile();
        else if (menuName.equals("Quit"))
            System.exit(0);
    }

    /**
     * The function opens a file dialog and reads the selected file if the user
     * approves, otherwise it
     * displays a message indicating that the dialog was canceled.
     */
    private void openFile() {
        JFileChooser chooser;
        int status;
        chooser = new JFileChooser();
        status = chooser.showOpenDialog(null);
        if (status == JFileChooser.APPROVE_OPTION)
            readSource(chooser.getSelectedFile());
        else
            JOptionPane.showMessageDialog(null, "Open File dialog canceled");
    }

    /**
     * The function reads the contents of a file, executes SQL commands, and
     * displays the SQL commands
     * and results in a split pane.
     * 
     * @param file The `file` parameter is of type `File` and represents the source
     *             file that contains
     *             SQL commands to be executed.
     */
    private void readSource(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder sqlCommands = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sqlCommands.append(line).append("\n");
            }

            // SQL contains sql commands
            String content = readQuery(sqlCommands.toString(), getTitle());

            // Create a split pane
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

            // Left pane (SQL commands)
            JTextArea sqlCommandsTextArea = new JTextArea(sqlCommands.toString());
            JScrollPane sqlScrollPane = new JScrollPane(sqlCommandsTextArea);
            splitPane.setLeftComponent(sqlScrollPane);

            // Right pane (Results)
            JTextArea resultsTextArea = new JTextArea(content);
            JScrollPane resultsScrollPane = new JScrollPane(resultsTextArea);
            splitPane.setRightComponent(resultsScrollPane);

            // Set the preferred size for better initial division of the split pane
            splitPane.setDividerLocation(0.5);
            splitPane.setResizeWeight(0.5);

            // Add the split pane to the frame
            add(splitPane);

            revalidate(); // Ensure the changes are reflected
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error reading file: " + e.getMessage());
        }
    }

    /**
     * The function reads SQL queries, executes them, and returns the result as a
     * formatted string.
     * 
     * @param sqlCommands   A string containing one or more SQL queries separated by
     *                      semicolons (;).
     * @param connectionUrl The `connectionUrl` parameter is a string that specifies
     *                      the URL of the
     *                      database connection. It typically includes information
     *                      such as the database type, host, port,
     *                      and database name. For example, for a MySQL database
     *                      running on localhost with port 3306 and
     *                      database name "mydb", the connection
     * @return The method is returning a string that contains the result of
     *         executing the SQL queries
     *         provided as input.
     */

    private String readQuery(String sqlCommands, String connectionUrl) {
        StringBuilder result = new StringBuilder();

        try (Connection con = DriverManager.getConnection(connectionUrl);
                Statement stmt = con.createStatement()) {

            String[] queries = sqlCommands.split(";"); // User must know to use ; only at the end of queries.

            for (String query : queries) {
                query = query.trim();
                if (!query.isEmpty()) {
                    try (ResultSet rs = stmt.executeQuery(query)) {
                        ResultSetMetaData metaData = rs.getMetaData();
                        int columnCount = metaData.getColumnCount();

                        // Append column names to the result string
                        for (int i = 1; i <= columnCount; i++) {
                            result.append(String.format("| %-20s ", metaData.getColumnName(i)));
                        }
                        result.append("|\n");

                        // Append a separator line
                        for (int i = 1; i <= columnCount; i++) {
                            result.append("+---------------------");
                        }
                        result.append("+\n");

                        // Append data to the result string
                        while (rs.next()) {
                            for (int i = 1; i <= columnCount; i++) {
                                result.append(String.format("| %-20s ", rs.getString(i)));
                            }
                            result.append("|\n");
                        }

                        // Add a separator between queries
                        result.append("+---------------------");
                        result.append("\n");
                    } catch (SQLException e) {
                        // Log the error or display a user-friendly error message
                        e.printStackTrace();
                        result.append("Error executing query: ").append(e.getMessage());
                        result.append("\n");
                    }
                }
            }

        } catch (SQLException e) {
            // Log the error or display a user-friendly error message
            e.printStackTrace();
            result.append("Error establishing connection: ").append(e.getMessage());
            result.append("\n");
        }

        return result.toString();
    }

}
