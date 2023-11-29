package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

public class WelcomeScreen extends JFrame implements ActionListener {
    private JPanel panel;
    private JButton signupButton;
    private JButton loginButton;

    public static final String USERS_TXT_PATH = "src/main/java/src/Users.txt";
    public static final String ORIG_USERS_TXT_PATH = "src/main/java/src/UsersCopy.txt";
    public static final String BOOKS_TXT_PATH = "src/main/java/src/Books.txt";
    public static final String ORIG_BOOKS_TXT_PATH = "src/main/java/src/BooksCopy.txt";

    // Hashmap to store users with email as key
    private static HashMap<String, User> emailMap = new HashMap<>();

    // Hashmap to store users with username as key and UserObject as value
    private static HashMap<String, User> libraryCardMap = new HashMap<>();

    // constructor
    public WelcomeScreen() throws IOException {
        // load all books, users need to reference books
        loadBooks();
        // get existing username and password
        loadUsers();

        panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(layout);
        panel.setBackground(Color.decode("#ADD8E6"));

        signupButton = new JButton("Signup");

        loginButton = new JButton("Login");
        loginButton.setForeground(Color.BLUE);

        signupButton.addActionListener(this);
        loginButton.addActionListener(this);

        // Layout the button in the panel
        panel.add(Box.createVerticalGlue());

        // Label for login button
        JLabel loginLabel = new JLabel("Welcome!!!");
        loginLabel.setFont(new Font("Ariel", Font.BOLD, 24));
        JPanel loginLabelPanel = new JPanel();
        loginLabelPanel.setBackground(Color.decode("#ADD8E6"));
        BoxLayout layoutButton0 = new BoxLayout(loginLabelPanel, BoxLayout.X_AXIS);
        loginLabelPanel.add(Box.createHorizontalGlue());
        loginLabelPanel.add(loginLabel);
        loginLabelPanel.add(Box.createHorizontalGlue());
        panel.add(loginLabelPanel);

        // Login button
        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(Color.decode("#ADD8E6"));
        BoxLayout layout1 = new BoxLayout(loginPanel, BoxLayout.X_AXIS);
        loginPanel.add(Box.createHorizontalGlue());
        loginPanel.add(loginButton);
        loginPanel.add(Box.createHorizontalGlue());
        panel.add(loginPanel);

        // Space between the two buttons
        panel.add(Box.createRigidArea(new Dimension(0, 50)));

        // Label for signup button
        JLabel signupLabel = new JLabel("Not a user? Click here to sign up: ");
        JPanel signupLabelPanel = new JPanel();
        signupLabelPanel.setBackground(Color.decode("#ADD8E6"));
        BoxLayout layoutButton1 = new BoxLayout(signupLabelPanel, BoxLayout.X_AXIS);
        signupLabelPanel.add(Box.createHorizontalGlue());
        signupLabelPanel.add(signupLabel);
        signupLabelPanel.add(Box.createHorizontalGlue());
        panel.add(signupLabelPanel);

        // Signup button
        JPanel signupPanel = new JPanel();
        signupPanel.setBackground(Color.decode("#ADD8E6"));
        BoxLayout layout2 = new BoxLayout(signupPanel, BoxLayout.X_AXIS);
        signupPanel.add(Box.createHorizontalGlue());
        signupPanel.add(signupButton);
        signupPanel.add(Box.createHorizontalGlue());
        panel.add(signupPanel);

        panel.add(Box.createVerticalGlue());

        add(panel);

        setTitle("Jamba Juice Library Login Page");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);    // center the JFrame on the screen
        setVisible(true);

    }

    // load books from backend database file
    public void loadBooks() {
        try {
            Book.populateCatalogue();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    // load users from backend database file
    public void loadUsers() throws IOException {
        // clear previous users
        libraryCardMap.clear();

        // Read from backend database file
        File file = new File(WelcomeScreen.USERS_TXT_PATH);

        // Use a scanner to read the file
        Scanner fileInput = new Scanner(file);
        while (fileInput.hasNextLine()) {   // while file still has lines, keep scanning
            String temp = fileInput.nextLine();
            if (temp.trim().length() == 0) {    // once there are no more lines, break
                break;  // just empty lines; no more record
            }
            User newUser = new User();  // create a new user object
            newUser.setFirstName(temp); // set the first thing read in to firstName
            newUser.setLastName(fileInput.nextLine());  // files reads line by line, so do .nextLine
            newUser.setEmail(fileInput.nextLine());
            newUser.setPassword(fileInput.nextLine());
            newUser.setLibraryCardNum(fileInput.nextLine());

            // Store to libraryCardMap
            libraryCardMap.put(newUser.getLibraryCardNum(), newUser);

            // if there is next line, it is checkout book isbn
            while (fileInput.hasNextLine()) {
                try {
                    String tempLine = fileInput.nextLine(); // first thing we read from txt
                    if (tempLine.length() == 0) {   // break when no more lines to read
                        break;
                    }
                    // Read books.txt
                    // store books into Users' borrowedBooks array
                    for (int k = 0; k < Book.BOOKS.size(); k++) {
                        if (Book.BOOKS.get(k).getIsbn().equals(tempLine)) {     // find books by ISBN
                            newUser.getBorrowedBooks().add(Book.BOOKS.get(k));  // add book to user by ISBN
                            break;
                        }
                    }

                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        fileInput.close();
    }

    // Write user info back to file
    public static void writeToFile() throws IOException {
        File e = new File(WelcomeScreen.USERS_TXT_PATH);    // open file
        PrintWriter out = new PrintWriter(e);
        out.print(writeToFileHelper()); // write back to file
        out.close();
    }

    // Loop through Hashmap using keySet
    private static StringBuilder writeToFileHelper() {
        StringBuilder a = new StringBuilder("");
        // Put all keys into an array and loop through
        for (String key : libraryCardMap.keySet()) {
            a.append(libraryCardMap.get(key).getFirstName() + "\n");
            a.append(libraryCardMap.get(key).getLastName() + "\n");
            a.append(libraryCardMap.get(key).getEmail() + "\n");
            a.append(libraryCardMap.get(key).getPassword() + "\n");
            a.append(libraryCardMap.get(key).getLibraryCardNum() + "\n");
            for (Book J : libraryCardMap.get(key).getBorrowedBooks()) {
                a.append(J.getIsbn() + "\n");
            }
            a.append("\n");
        }
        return a;
    }


    // Getter for users Email DB
    public static HashMap<String, User> getUsersEmailDB() {
        return emailMap;
    }

    // Getter for users Username DB
    public static HashMap<String, User> getLibraryCardNumDB() {
        return libraryCardMap;
    }

    // For the buttons
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == signupButton) {    // if signupButton is clicked, open signupFrame
            SignupFrame signupFrame = new SignupFrame();
            dispose();
        } else if (e.getSource() == loginButton) {  // if loginButton is clicked, open loginFrame
            LoginFrame loginFrame = new LoginFrame();
            dispose();
        }
    }

    // Main is WelcomeScreen
    public static void main(String[] args) throws IOException {
        WelcomeScreen welcomeScreen = new WelcomeScreen();
    }

}
