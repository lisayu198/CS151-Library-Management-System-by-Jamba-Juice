package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupFrame extends JFrame implements ActionListener {
    // class variables
    private JLabel firstNameLabel, lastNameLabel, emailLabel, passwordLabel, confirmPasswordLabel;
    private JTextField firstNameField, lastNameField, emailField, passwordField, confirmPasswordField;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[a-zA-Z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    JPanel panel = new JPanel();

    // constructor
    public SignupFrame() {
        this.setTitle("SIGNUP PAGE");

        panel.setLayout(null);

        this.add(panel);
        this.setSize(new Dimension(500, 500));
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
        // First Name label
        firstNameLabel = new JLabel("First Name: ");
        // 100px from left, 8px from top, 100px width, 20px height
        firstNameLabel.setBounds(150, 8, 100, 20);
        panel.add(firstNameLabel);

        // First Name text field
        firstNameField = new JTextField();
        firstNameField.setBounds(150, 27, 193, 28);
        panel.add(firstNameField);

        // Last Name label
        lastNameLabel = new JLabel("Last Name: ");
        lastNameLabel.setBounds(150, 55, 100, 20);
        panel.add(lastNameLabel);

        // Last Name text field
        lastNameField = new JTextField();
        lastNameField.setBounds(150, 75, 193, 28);
        panel.add(lastNameField);

        // emailField label
        emailLabel = new JLabel("Email: ");
        emailLabel.setBounds(150, 102 , 100, 20);
        panel.add(emailLabel);

        // emailField text field
        emailField = new JTextField();
        emailField.setBounds(150, 123, 193, 28);
        panel.add(emailField);

        // passwordField label
        passwordLabel = new JLabel("Password: ");
        passwordLabel.setBounds(150, 149 , 100, 20);
        panel.add(passwordLabel);

        // passwordField field
        passwordField = new JPasswordField();
        passwordField.setBounds(150, 171, 193, 28);
        panel.add(passwordField);

        // confirmPasswordField label
        confirmPasswordLabel = new JLabel("Confirm Password: ");
        confirmPasswordLabel.setBounds(150, 196, 300, 20);
        panel.add(confirmPasswordLabel);

        // confirmPasswordField field
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(150, 219, 193, 28);
        panel.add(confirmPasswordField);

        // Signup button
        JButton button = new JButton("Sign up");
        button.setBounds(250, 250, 90, 25);
        button.addActionListener(this);
        panel.add(button);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        // validate the fields
        try {
            if (firstName.isEmpty() || lastName.isEmpty() || !isValidEmailAddress(email) || !isValidPassword(password)) {
                JOptionPane.showMessageDialog(this, "Please enter valid information");
            } else {
                // Create a user object
                int libraryCardNum = 0;
                User user = new User(firstName, lastName, email, password, libraryCardNum);

                // Generate the libraryCardNumber
                libraryCardNum = (int) (Math.random() * 9000 + 1000);
                user.setLibraryCardNum(libraryCardNum);

                // Add user to HashMap
                // If user object doesn't exist with email, then add email
                if (WelcomeScreen.getUsersEmailDB().get(email) == null) {
                    WelcomeScreen.getUsersEmailDB().put(email, user);
                    WelcomeScreen.getLibraryCardNumDB().put(user.getLibraryCardNum(), user);
                } else {
                    JOptionPane.showMessageDialog(this, "You have an account with that email already, please try log in");
                    return;
                }

                // keep regenerating username if already exists
                while (WelcomeScreen.getLibraryCardNumDB().get(libraryCardNum) != null) {
                    libraryCardNum = ((int) (Math.random() * 900000 + 100000));
                }
                // Set library card number for user
                user.setLibraryCardNum((libraryCardNum));
                WelcomeScreen.getLibraryCardNumDB().put(libraryCardNum, user);


                // Show username to user
                JOptionPane.showMessageDialog(this, "Your username is: " + libraryCardNum);

                // After user is done, go back to welcome screen
                WelcomeScreen welcomeScreen = new WelcomeScreen();
                dispose();

            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }


    // validate email address
    public boolean isValidEmailAddress(String email) throws Exception {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if(!matcher.matches()) {
            throw new EmailException();
        }
        return true;
    }

    // validate password
    public boolean isValidPassword(String password) throws Exception {
            // check if passwords are equal
            if(!passwordField.getText().equals(confirmPasswordField.getText())) {
                throw new PasswordsNoMatch();
            }
            if(!password.matches(".*[A-Z].*")) {
                throw new UpperCaseCharacterMissing();
            }
            if(!password.matches(".*[a-z].*")) {
                throw new LowerCaseCharacterMissing();
            }
            if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
                throw new SpecialCharacterMissing();
            }
            if (!password.matches(".*\\d.*")) {
                throw new NumberCharacterMissing();
            }
            if (password.length() < 8) {
                throw new Minimum8CharactersRequired();
            }
            return true;
    }

}
