package src.loginUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class UserFrame extends JFrame {
    // class variables
    private JLabel firstNameLabel, lastNameLabel, emailLabel, usernameLabel;
    private JTextField firstNameField, lastNameField, emailField, usernameField;
    private User user;
    JList<String> userCheckoutBookList = new JList<>(new DefaultListModel<>());
    DefaultListModel<String> usersBooksModel = (DefaultListModel<String>) userCheckoutBookList.getModel();
    JList<String> libraryBookList = new JList<>(new DefaultListModel<>());
    DefaultListModel<String> libraryBooksModel = (DefaultListModel<String>) libraryBookList.getModel();


    JPanel displayBooksPanel = new JPanel();        // the bookLists panel
    JPanel checkedoutBooksPanel = new JPanel();     // left panel inside bookLists panel
    JPanel libraryBooksPanel = new JPanel();        // right panel inside bookLists panel
    JPanel userInfoPanel = new JPanel();            // userInfo panel at the top
    JPanel bigPanel = new JPanel();                 // big panel

    // constructor
    public UserFrame(User user) {
        this.user = user;
        this.setTitle("USER PAGE");

        this.setSize(new Dimension(800, 500));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getUserBookList();          // get borrowed bookList from current user
        getLibraryBooksList();      // get library books


        this.buildUserInfoPanel();                 // build content inside the userInfoPanel
        this.buildCheckedoutBooksPanel();        // build the content inside the checkedOutBooksPanel
        this.buildLibraryBooksPanel();          // build the content inside the libraryBooksPanel

        this.buildBookListsPanel();                 // build bookLists panel frame which has checkedoutBooksPanel and librryBooksPanel
        this.buildBigContents();                // build the big panel


        setLocationRelativeTo(null);    // center the JFrame on the screen
        setVisible(true);               // Show the Signup JFrame
    }

    // Build the contents of the bigPanel
    public void buildBigContents() {
        // BoxLayout layout = new BoxLayout(bigPanel, BoxLayout.Y_AXIS);
        bigPanel.setLayout(new BorderLayout());

        bigPanel.add(userInfoPanel, BorderLayout.NORTH);
        bigPanel.add(displayBooksPanel, BorderLayout.CENTER);

        add(bigPanel);      // adds to userFrame

    }

    // Build the bookList panel contents
    public void buildBookListsPanel() {
        BoxLayout layout = new BoxLayout(displayBooksPanel, BoxLayout.X_AXIS);
        displayBooksPanel.setLayout(layout);

        displayBooksPanel.add(userInfoPanel);

        // displayBooksPanel.add(Box.createHorizontalGlue());
        displayBooksPanel.add(checkedoutBooksPanel);
        displayBooksPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        displayBooksPanel.add(libraryBooksPanel);
        // displayBooksPanel.add(Box.createHorizontalGlue());

        // add(displayBooksPanel);
    }

    // Build UserInfo contents
    public void buildUserInfoPanel() {
        BoxLayout layout = new BoxLayout(userInfoPanel, BoxLayout.X_AXIS);
        userInfoPanel.setLayout(layout);
        // display the user's first and last name, plus the username
        userInfoPanel.add(Box.createHorizontalGlue());
        JLabel firstNameLastName = new JLabel(user.getFirstName() + " " + user.getLastName() + "; ");
        JLabel userName = new JLabel(String.valueOf(user.getLibraryCardNum()));
        userInfoPanel.add(firstNameLastName);
        userInfoPanel.add(userName);

        // Logout button; return to homescreen
        // send new checkoutBooksList to file
        JButton logoutButton = new JButton("Logout");
        userInfoPanel.add(logoutButton);
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    WelcomeScreen.writeToFile();
                    WelcomeScreen welcomeScreen = new WelcomeScreen();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            }
        });

    }

    // Build libraryBooks contents
    public void buildLibraryBooksPanel() {
        BoxLayout layout = new BoxLayout(libraryBooksPanel, BoxLayout.Y_AXIS);
        libraryBooksPanel.setLayout(layout);

        // List of books in LIBRARY
        JLabel libraryBooksLabel = new JLabel("Books in Library: ");
        libraryBooksLabel.setFont(new Font("Ariel", Font.BOLD, 24));
        libraryBooksPanel.add(Box.createHorizontalGlue());
        libraryBooksPanel.add(libraryBooksLabel);
        libraryBooksPanel.add(Box.createHorizontalGlue());

        // JList of books user checked out, with a scroll Pane
        libraryBooksPanel.add(Box.createHorizontalGlue());
        libraryBooksPanel.add(new JScrollPane(libraryBookList));
        libraryBooksPanel.add(Box.createHorizontalGlue());

        // Library Button Panel
        JPanel libraryButtonsPanel = new JPanel();
        BoxLayout buttonLayout = new BoxLayout(libraryButtonsPanel, BoxLayout.X_AXIS);
        libraryButtonsPanel.setLayout(buttonLayout);
        // checkedout button panel
        JButton checkOutButton = new JButton("check out");
        // Add buttons to buttonsPanel
        libraryButtonsPanel.add(Box.createHorizontalGlue());
        libraryButtonsPanel.add(checkOutButton);

        checkOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = libraryBookList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String bookRemoved = libraryBooksModel.getElementAt(selectedIndex);
                    // remove checkedOut book from library
                    libraryBooksModel.remove(selectedIndex);
                    // add book user's checkedout book list
                    usersBooksModel.addElement(bookRemoved);

                    for (Book book : Book.getBooks()) {
                        if (book.getTitle().equals(bookRemoved)) {
                            UserFrame.this.user.getBorrowedBooks().add(book);
                            break;
                        }
                    }
                }
            }
        });

        libraryBooksPanel.add(libraryButtonsPanel);
        libraryBooksPanel.add(Box.createVerticalGlue());

    }


    // Build checkedoutBooks contents
    public void buildCheckedoutBooksPanel() {
        BoxLayout layout = new BoxLayout(checkedoutBooksPanel, BoxLayout.Y_AXIS);
        checkedoutBooksPanel.setLayout(layout);

        // checkedoutBooksPanel.add(Box.createVerticalGlue());

        // List of books user checked out Label
        JLabel checkedoutBooksLabel = new JLabel("Checked out books: ");
        checkedoutBooksLabel.setFont(new Font("Ariel", Font.BOLD, 24));
        checkedoutBooksPanel.add(Box.createHorizontalGlue());
        checkedoutBooksPanel.add(checkedoutBooksLabel);
        checkedoutBooksPanel.add(Box.createHorizontalGlue());

        // JList of books user checked out, with a scroll Pane
        JPanel booksUserCheckedOutPanel = new JPanel();
        booksUserCheckedOutPanel.setBackground(Color.decode("#ADD8E6"));
        checkedoutBooksPanel.add(Box.createHorizontalGlue());
        checkedoutBooksPanel.add(new JScrollPane(userCheckoutBookList));
        checkedoutBooksPanel.add(Box.createHorizontalGlue());


        // Button Panel
        JPanel checkedoutBooksButtonsPanel = new JPanel();
        BoxLayout buttonLayout = new BoxLayout(checkedoutBooksButtonsPanel, BoxLayout.X_AXIS);
        checkedoutBooksButtonsPanel.setLayout(buttonLayout);
        // Check in button
        JButton checkinButton = new JButton("Check in");
        // Add buttons to buttonsPanel
        checkedoutBooksButtonsPanel.add(Box.createHorizontalGlue());
        checkedoutBooksButtonsPanel.add(checkinButton);

        checkinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = userCheckoutBookList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String bookRemoved = usersBooksModel.getElementAt(selectedIndex);
                    // remove checkedIn book from user's bookList
                    usersBooksModel.remove(selectedIndex);
                    // add book back to Library (books available)
                    libraryBooksModel.addElement(bookRemoved);

                }
            }
        });


        checkedoutBooksPanel.add(checkedoutBooksButtonsPanel);

        checkedoutBooksPanel.add(Box.createVerticalGlue());

    }


    // Load booklist for current user
    public void getUserBookList() {
        for (int ii = 0; ii < this.user.getBorrowedBooks().size(); ii++) {
            usersBooksModel.addElement(this.user.getBorrowedBooks().get(ii).getTitle());
        }
    }

    // Load booklist from library
    public void getLibraryBooksList() {
        ArrayList<Book> bookList = Book.getBooks();
        for (int ii = 0; ii < bookList.size(); ii++) {
            if (!isBookCheckedOut(bookList.get(ii).getTitle())) {
                libraryBooksModel.addElement(bookList.get(ii).getTitle());
            }
        }
    }

    private boolean isBookCheckedOut(String bookName) {
        for (int ii = 0; ii < usersBooksModel.size(); ii++) {
            if (usersBooksModel.get(ii).equals(bookName)) {
                return true;
            }
        }

        return false;
    }

}
