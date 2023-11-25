import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class LoginFrame extends JFrame implements ActionListener {
    // Class Variables
    private JLabel passwordLabel, libraryCardNumLabel;
    private JTextField libraryCardNumField;
    private JButton loginButton;
    private JPasswordField passwordField;

    JPanel panel = new JPanel();

    public LoginFrame() {
        panel.setLayout(null);  // center the panel
        this.add(panel);
        this.setSize(new Dimension(500, 200));
        this.setResizable(false);
        this.setTitle("USER LOGIN");

        // When you x out the LoginFrame, it will go back to loginUI.WelcomeScreen
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    WelcomeScreen welcomeScreen = new WelcomeScreen();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            }
        });

        this.buildContent();        // build the content inside the Signup JFrame

        setLocationRelativeTo(null);    // center the JFrame on the screen
        setVisible(true);               // Show the Signup JFrame
    }

    // Build contents
    public void buildContent() {
        // Username label
        libraryCardNumLabel = new JLabel("Library Card Number: ");
        libraryCardNumLabel.setBounds(150, 8, 200, 20);
        panel.add(libraryCardNumLabel);

        // Username text field
        libraryCardNumField = new JTextField();
        libraryCardNumField.setBounds(150, 27, 193, 28);
        panel.add(libraryCardNumField);

        // Password label
        passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(150, 55, 70, 20);
        panel.add(passwordLabel);

        // Password field
        passwordField = new JPasswordField();
        passwordField.setBounds(150, 75, 193, 28);
        panel.add(passwordField);

        // Make the button
        loginButton = new JButton("Login");
        loginButton.setBounds(250, 110, 90, 25);
        loginButton.addActionListener(this);
        panel.add(loginButton);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String libraryCardNum = libraryCardNumField.getText();
        String password = passwordField.getText();

        // librarian login
        if (libraryCardNum.equals("Librarian") && password.equals("librarian123!")) {
            Library libraryFrame = new Library();
            dispose();
        }
        // user login
        else if (WelcomeScreen.getLibraryCardNumDB().get(libraryCardNum) == null) {
            JOptionPane.showMessageDialog(this, "User does not exist");
            return;
        } else if (!password.equals(WelcomeScreen.getLibraryCardNumDB().get(libraryCardNum).getPassword().toString())) {
            JOptionPane.showMessageDialog(this, "Wrong password!");
            return;
        } else {
            // User login - Display the user's profile on the screen
            UserFrame informationFrame = new UserFrame(WelcomeScreen.getLibraryCardNumDB().get(libraryCardNum));
            dispose();
        }

    }
}
