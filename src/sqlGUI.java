import java.awt.GridLayout;
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
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class sqlGUI extends JFrame implements ActionListener {
    JMenuBar menuBar = new JMenuBar();

    public sqlGUI(String connectionUrl) {
        setTitle(connectionUrl);
        setSize(800, 600); // Set the size to 800x600
        setLocationRelativeTo(null); // Center the frame on the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createMenuBar();
        setVisible(true);
        setJMenuBar(menuBar); // sets menu bar to the JFrame menu bar.

    }

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

    private String readQuery(String sqlCommands, String connectionUrl) {
        StringBuilder result = new StringBuilder();

        try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
            ResultSet rs = stmt.executeQuery(sqlCommands);

            // Get ResultSetMetaData to obtain information about the columns
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Append column names to the result string
            for (int i = 1; i <= columnCount; i++) {
                result.append(metaData.getColumnName(i)).append("\t");
            }
            result.append("\n"); // Move to the next line after appending column names

            // Append data to the result string
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    result.append(rs.getString(i)).append("\t");
                }
                result.append("\n"); // Move to the next line after appending data
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

}
