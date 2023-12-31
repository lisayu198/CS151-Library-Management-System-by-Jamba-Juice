package src;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserFrame extends JFrame {
    // class variables
    private JLabel firstNameLabel, lastNameLabel, emailLabel, usernameLabel;
    private JTextField firstNameField, lastNameField, emailField, usernameField;
    private User user;
    JList<String> userCheckoutBookList = new JList<>(new DefaultListModel<>());
    // Model for JList to modify the contents inside the usersBooksList
    DefaultListModel<String> usersBooksModel = (DefaultListModel<String>) userCheckoutBookList.getModel();
    // JList for library bookList with a model to modify the contents of the librarybookList
    JList<String> libraryBookList = new JList<>(new DefaultListModel<>());
    DefaultListModel<String> libraryBooksModel = (DefaultListModel<String>) libraryBookList.getModel();
    // Check in button
    JButton checkinButton = new JButton("Check in");
    // checkedout button panel
    JButton checkOutButton = new JButton("check out");
    String bookFile = WelcomeScreen.BOOKS_TXT_PATH;
    List<String> bookInfoList = new ArrayList<>();  // Arraylist of bookInfo


    JPanel displayBooksPanel = new JPanel();        // the bookLists panel
    JPanel checkedoutBooksPanel = new JPanel();     // left panel inside bookLists panel
    JPanel libraryBooksPanel = new JPanel();        // right panel inside bookLists panel
    JPanel userInfoPanel = new JPanel();            // userInfo panel at the top
    JPanel bigPanel = new JPanel();                 // big panel

    // constructor
    public UserFrame(User user) {
        this.user = user;
        this.setTitle("USER PAGE"); // Title of frame on top

        this.setSize(new Dimension(800, 500));  // Hardcode the size of the frame
        // When you x out the UserFrame, it will go back to welcome screen and write to file
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                userLogout();
            }
        });

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
        // Using a border layout
        bigPanel.setLayout(new BorderLayout());

        bigPanel.add(userInfoPanel, BorderLayout.NORTH);    //Make the userInfoPanel in the north (the top)
        bigPanel.add(displayBooksPanel, BorderLayout.CENTER);   // Set the displayBooksPanel in the center of the frame

        add(bigPanel);      // adds to userFrame

    }

    // Build the bookList panel contents
    public void buildBookListsPanel() {
        // Use box layout on the x_axis
        BoxLayout layout = new BoxLayout(displayBooksPanel, BoxLayout.X_AXIS);
        displayBooksPanel.setLayout(layout);    // center the layout

        displayBooksPanel.add(userInfoPanel);   // add userInfoPanel to the frame

        /*
        Add panels and create a space in between the two panels on the horizontal axis
         */
        displayBooksPanel.add(checkedoutBooksPanel);
        displayBooksPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        displayBooksPanel.add(libraryBooksPanel);
    }

    // Build UserInfo contents
    public void buildUserInfoPanel() {
        // Build a boxlayout by the x_axis
        BoxLayout layout = new BoxLayout(userInfoPanel, BoxLayout.X_AXIS);
        userInfoPanel.setLayout(layout);
        // display the user's first and last name, plus the username
        userInfoPanel.add(Box.createHorizontalGlue());  // Horizontal glue on the left pushes everything to the right
        JLabel firstNameLastName = new JLabel(user.getFirstName() + " " + user.getLastName() + "; "); // add the first name and last name
        JLabel userName = new JLabel(String.valueOf(user.getLibraryCardNum())); // Add the library card number
        userInfoPanel.add(firstNameLastName);
        userInfoPanel.add(userName);

        // Logout button; return to homescreen
        // send new checkoutBooksList to file
        JButton logoutButton = new JButton("Logout");
        userInfoPanel.add(logoutButton);
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userLogout();
            }
        });

    }

    // Logout and WritetoFile method
    public void userLogout() {
        try {
            WelcomeScreen.writeToFile();    // updates userfile
            Book.writeToFile();             // updates bookfile
            WelcomeScreen welcomeScreen = new WelcomeScreen();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        dispose();
    }


    // Build libraryBooks contents
    private void buildLibraryBooksPanel() {
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

        // Library bookList: Button only works when a book is selected for library list
        // make the books info list for display
        bookInfoList = this.readBookListFromFile();
        libraryBookList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                checkOutButton.setEnabled(true);
                for (String value : bookInfoList) {
                    if (value != null && libraryBookList.getSelectedValue() != null &&
                            value.startsWith(libraryBookList.getSelectedValue())) {
                        JOptionPane.showMessageDialog(UserFrame.this, value, "book info", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }
            }
        });


        // Library Button Panel
        JPanel libraryButtonsPanel = new JPanel();
        BoxLayout buttonLayout = new BoxLayout(libraryButtonsPanel, BoxLayout.X_AXIS);
        libraryButtonsPanel.setLayout(buttonLayout);
        // Add buttons to buttonsPanel
        libraryButtonsPanel.add(Box.createHorizontalGlue());
        libraryButtonsPanel.add(checkOutButton);
        checkOutButton.setEnabled(false);

        // ActionListener is used when the checkOutButton is clicked
        checkOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = libraryBookList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String bookRemoved = libraryBooksModel.getElementAt(selectedIndex);
                    // Check if user already checked out book from library
                    for (Book currentBook : UserFrame.this.user.getBorrowedBooks()) {
                        // If same book was alrdy checked out by user, show an error
                        if (currentBook.getTitle().equals(bookRemoved)) {
                            JOptionPane.showMessageDialog(null, "Error, cannot check out the same book");
                            return;
                        }
                    }

                    // fine, now remove book from library and add to user's booklist
                    // remove checkedOut book from library
                    libraryBooksModel.remove(selectedIndex);
                    // Refreshes the label on the UI
                    libraryBooksLabel.updateUI();
                    // add book user's checkedout book list
                    usersBooksModel.addElement(bookRemoved);
                    checkOutButton.setEnabled(false);

                    // Goes through bookList and sets checkedIn to false of the book checkedout
                    for (Book book : Book.getBooks()) {
                        if (book.getTitle().equalsIgnoreCase(bookRemoved)) {
                            book.setCheckedIn(false);
                            UserFrame.this.user.getBorrowedBooks().add(book);
                            break;
                        }
                    }
                }


                updateFiles();
            }
        });

        libraryBooksPanel.add(libraryButtonsPanel);
        libraryBooksPanel.add(Box.createVerticalGlue());

    }

    // Update file method
    public void updateFiles() {
        try {
            WelcomeScreen.writeToFile();    // updates userfile
            Book.writeToFile();             // updates bookfile
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
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

        // Button only works when a book is selected for user BookList
        userCheckoutBookList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                checkinButton.setEnabled(true);
            }
        });

        // Button Panel
        JPanel checkedoutBooksButtonsPanel = new JPanel();
        BoxLayout buttonLayout = new BoxLayout(checkedoutBooksButtonsPanel, BoxLayout.X_AXIS);
        checkedoutBooksButtonsPanel.setLayout(buttonLayout);

        checkinButton.setEnabled(false);
        // Add buttons to buttonsPanel
        checkedoutBooksButtonsPanel.add(Box.createHorizontalGlue());
        checkedoutBooksButtonsPanel.add(checkinButton);

        // Check in button actionListener
        checkinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = userCheckoutBookList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String bookRemoved = usersBooksModel.getElementAt(selectedIndex);
                    // remove checkedIn book from user's bookList
                    usersBooksModel.remove(selectedIndex);
                    userCheckoutBookList.updateUI();
                    for (Book book : Book.BOOKS) {
                        if (book.getTitle().equalsIgnoreCase(bookRemoved.trim())) {
                            book.setCheckedIn(true);
                            // Must remove book by Index, unlike adding book by object
                            UserFrame.this.user.getBorrowedBooks().remove(selectedIndex);
                            break;
                        }
                    }

                    checkinButton.setEnabled(false);
                    // add book back to Library (books available)
                    libraryBooksModel.addElement(bookRemoved);
                    updateFiles();
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
        libraryBooksModel.clear();
        for (int ii = 0; ii < bookList.size(); ii++) {
            if (bookList.get(ii).isCheckedIn()) {
                libraryBooksModel.addElement(bookList.get(ii).getTitle());
            }
        }
    }


    /**
     * basically reads the books on the file to be used by the update method below
     */
    private List<String> readBookListFromFile() {
        List<String> bookList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(bookFile))) {
            String line;
            StringBuilder bookInfo = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                bookInfo.append(line).append("\n");
                if (line.isEmpty()) {
                    bookList.add(bookInfo.toString());
                    bookInfo.setLength(0);
                }
            }

            // Add the last one if not added
            if (bookInfo.length() != 0) {
                bookList.add(bookInfo.toString());
                bookInfo.setLength(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        return bookList;
    }

}
