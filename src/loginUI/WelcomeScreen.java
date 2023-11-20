package src.loginUI;

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

    // Hashmap to store users with email as key
    private static HashMap<String, src.loginUI.User> emailMap = new HashMap<>();

    // Hashmap to store users with username as key and UserObject as value
    private static HashMap<Integer, src.loginUI.User> libraryCardMap = new HashMap<>();

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
        File file = new File("src/loginUI/Users.txt");

        Scanner scanner = new Scanner(file);
        if (scanner.hasNextLine()) {
            String firstLine = scanner.nextLine();
            System.out.println("First line: " + firstLine);
        } else {
            System.out.println("The file is empty.");
        }
        scanner.close();

        Scanner fileInput = new Scanner(file);
        while (fileInput.hasNextLine()) {
            src.loginUI.User newUser = new src.loginUI.User();
            newUser.setFirstName(fileInput.nextLine());
            System.out.println("firstname " + newUser.getFirstName());
            newUser.setLastName(fileInput.nextLine());
            System.out.println("lastname " + newUser.getLastName());
            newUser.setEmail(fileInput.nextLine());
            System.out.println("Email " + newUser.getEmail());
            newUser.setPassword(fileInput.nextLine());
            System.out.println("Password " + newUser.getPassword());
            newUser.setLibraryCardNum(fileInput.nextInt());
            System.out.println("librarynum " + newUser.getLibraryCardNum());

            System.out.println("Flusing " + fileInput.nextLine());   // flush the newline after nextInt

            // Store to libraryCardMap
            libraryCardMap.put(newUser.getLibraryCardNum(), newUser);

            // if there is next line, it is checkout book isbn
            while (fileInput.hasNextLine()) {
                try {
                    String tempLine = fileInput.nextLine();
                    if (tempLine.length() == 0) {
                        break;
                    }
                    System.out.println("Starting of books: " + tempLine);
                    if (WelcomeScreen.isInteger(tempLine)) {
                        Long a;
                        a = Long.valueOf(tempLine);
                        for (int k = 0; k < src.loginUI.Book.BOOKS.size(); k++) {
                            if (src.loginUI.Book.BOOKS.get(k).getIsbn().longValue() == a) {
                                newUser.getBorrowedBooks().add(src.loginUI.Book.BOOKS.get(k));
                                System.out.println("Book checked out: " + src.loginUI.Book.BOOKS.get(k).getTitle());
                                break;
                            }
                        }
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        fileInput.close();
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void writeToFile() throws IOException {
        File e = new File("src/loginUI/Users.txt");
        PrintWriter out = new PrintWriter(e);
        out.print(writeToFileHelper());
        out.close();
    }

    // Loop through Hashmap using keySet
    private static StringBuilder writeToFileHelper() {
        StringBuilder a = new StringBuilder("");
        // Put all keys into an array and loop through
        for (Integer key : libraryCardMap.keySet()) {
            a.append(libraryCardMap.get(key).getFirstName() + "\n");
            a.append(libraryCardMap.get(key).getLastName() + "\n");
            a.append(libraryCardMap.get(key).getEmail() + "\n");
            a.append(libraryCardMap.get(key).getPassword() + "\n");
            a.append(libraryCardMap.get(key).getLibraryCardNum() + "\n");
            // a.append(libraryCardMap.get(i).phoneNum + "\n");
            for (Book J : libraryCardMap.get(key).getBorrowedBooks()) {
                a.append(J.getIsbn() + "\n");
            }
            a.append("\n");
        }
        return a;
    }


    // Getter for users Email DB
    public static HashMap<String, src.loginUI.User> getUsersEmailDB() {
        return emailMap;
    }

    // Getter for users Username DB
    public static HashMap<Integer, src.loginUI.User> getLibraryCardNumDB() {
        return libraryCardMap;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == signupButton) {
            src.loginUI.SignupFrame signupFrame = new src.loginUI.SignupFrame();
            dispose();
        } else if (e.getSource() == loginButton) {
            src.loginUI.LoginFrame loginFrame = new src.loginUI.LoginFrame();
            dispose();
        }
    }


    public static void main(String[] args) throws IOException {
        WelcomeScreen welcomeScreen = new WelcomeScreen();
    }

}
