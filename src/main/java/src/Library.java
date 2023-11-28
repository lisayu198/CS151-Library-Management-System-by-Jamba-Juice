package src;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Library {

    /**
    * library panel UI
    */
    String[] array = new String[20]; //new String[] array (unused)
    private List<String> bookList = new ArrayList<>(); //new private List with String parameter for list of books
    String bookFile = WelcomeScreen.BOOKS_TXT_PATH; //String for book text file (directory)
    JFrame libraryFrame = new JFrame("library frame"); //JFrame for library
    JPanel panel = new JPanel(new GridLayout(1, 2)); //main panel to store both the library (remove books) and book (add books) panels
    JButton removeBook = new JButton("remove book"); //JButton to remove books
    /*
    ImageIcon userIcon = new ImageIcon("C:\\Users\\anisp\\Downloads\\FreddyFazbear_2__54364.jpg");
    ImageIcon hehe = new ImageIcon("C:\\Users\\anisp\\Downloads\\feeby jumpscare.png");
    Image scale = userIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
    ImageIcon updatedIcon = new ImageIcon(scale);
    JButton userAccount = new JButton(updatedIcon);
    */
    JLabel titleLabel = new JLabel("list of available books"); //title label for the library panel
    JList<String> bookJList; //JList for the list of books

    /**
    * book panel UI
    */
    JLabel descriptionLabel = new JLabel("enter book info: "); //description label for the panel
    public JTextField bookInput = new JTextField(20); //text field for the book title
    public JTextField authorInput = new JTextField(20); //text field for the author name
    public JTextField isbnInput = new JTextField(20); //text field for the isbn number 
    public JComboBox bookCondition = new JComboBox(Book.CONDITION.values()); //drop down for the book's condition
    JLabel bookLabel = new JLabel("book title?"); //label for the book title text box
    JLabel authorLabel = new JLabel("author?"); //label for the author name text box
    JLabel isbnLabel = new JLabel("isbn?"); //label for the isbn number text box
    JLabel bookConditionLabel = new JLabel("book condition?"); //label for the condition drop down
    JButton addBook = new JButton("add book"); //button to add book to JList

    /**
     * https://www.javamex.com/tutorials/threads/invokelater.shtml
     * link for SwingUtilities.invokeLater
     * calls libraryUI method first, then readBookListFromFile, then updateBookList
     */
    public Library() {
        SwingUtilities.invokeLater(() -> { //special method to call certain methods in order
            LibraryUI(); //gets called first
            readBookListFromFile(); //once LibraryUI() finishes running, this method runs
            updateBookList(); //runs last after readBookListFromFile()
        });

    }

    /**
     * main frame UI method
     * creates the library frame and adds the library and book panel to the frame
     */
    public void LibraryUI() {
        bookList = readBookListFromFile(); //book list array calls the readBookListFromFile() method

        libraryFrame.add(panel); //add the main panel to the library frame
        libraryFrame.setSize(800, 500); //set the frame's size to 800x500
        libraryFrame.setVisible(true); //make sure the frame is visible
        libraryFrame.setLocationRelativeTo(null); //when the application opens, the frame opens in the middle of the screen, not the top left corner
        
        libraryFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //does nothing when x is pressed
        /**
        * https://docs.oracle.com/javase/8/docs/api/java/awt/event/WindowListener.html
        * link for WindowListener and WindowAdapter()
        */
        libraryFrame.addWindowListener(new WindowAdapter() { //new listener interface for the window 
            @Override
            public void windowClosing(WindowEvent e) { //method to check if the window is closed
                try {
                    WelcomeScreen welcomeScreen = new WelcomeScreen(); //goes back to the Welcome Screen
                } catch (IOException ex) {
                    throw new RuntimeException(ex); //runtime exception if close does not work as expected
                }
                libraryFrame.dispose(); //close the frame but keep the application running
            }
        });
        panel.add(createLibraryPanel()); //call the createLibraryPanel() method and add it to the main panel
        panel.add(createBookPanel()); //call the createBookPanel() method and add it to the main panel

        /**
         * http://www.java2s.com/Tutorial/Java/0260__Swing-Event/DetectingDoubleandTripleClicks.htm
         * link for clicking in Java Swing
         */
        bookJList.addMouseListener(new MouseAdapter() { //new listener interface for any mouse actions
            @Override
            public void mouseClicked(MouseEvent click) { //method to check if the mouse/touch pad is clicked
                if (click.getClickCount() >= -1) { //if the click count is greater than or equal to -1
                    for (String value : bookList) { //print all values of the bookList
                        if (value != null && bookJList.getSelectedValue() != null && //if the book value is not null and the selected book in the JList is not null and the book value aligns with the selected JList value
                                value.startsWith(bookJList.getSelectedValue())) { 
                            JOptionPane.showMessageDialog(libraryFrame, value, "book info", JOptionPane.INFORMATION_MESSAGE); //pop up displaying the selected book's information
                            break; //break the loop
                        }
                    }
                }
            }
        });

        // userAccount.addActionListener(this::actionPerformed); 
        removeBook.addActionListener(this::actionPerformed); //add an action listener to the remove book button
        addBook.addActionListener(this::addBookAction); //add an action listener to the add book button
    }

    /**
     * creates the library panel (left) with constraints for all J components
     */
    private JPanel createLibraryPanel() {
        JPanel libraryPanel = new JPanel(new GridBagLayout()); //new JPanel for the JList with a grid bag layout
        libraryPanel.setBackground(Color.decode("#fec7d7")); //set the background colour to light pink
        /**
         * this is constraints for the header
         * insets is basically like a spacer/margins
         * fill makes sure the label isn't more on one side; aka fills up space on the x axis
         * gridwidth is how long it is
         * !! i will refer to rows as gridy and columns as gridx !!
         */
        GridBagConstraints textConstraints = new GridBagConstraints(); //new grid bag constraints for the library header
        textConstraints.gridx = 0; //set the label to the leftmost column
        textConstraints.gridy = 0; //set the label to the top most row
        textConstraints.insets = new Insets(10, 60, 10, 10); //create spacers
        textConstraints.fill = GridBagConstraints.HORIZONTAL; //make the constraints fill up leftover space on the x-axis
        textConstraints.gridwidth = 5; //set the constraints to a size of 5
        libraryPanel.add(titleLabel, textConstraints); //add the label to the panel with the constraints

        /**
         * working JList for the books
         */
        bookJList = new JList<>(); //establish bookJList as a JList object
        bookJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //make sure the user can only select one value/book at a time
        bookJList.setLayoutOrientation(JList.VERTICAL); //make the JList vertical (scroll up and down)
        bookJList.setVisibleRowCount(10); //can only see 10 rows/books at a time
        /**
         * constraints for the list of books
         * setEditable is false so prof can't edit the available books
         * setLineWrap so if the title is too long it'll
         * look like this (I hope)
         * JScrollPane so if there's a lot of books we can scroll up and down the little text area
         */
        GridBagConstraints listConstraints = new GridBagConstraints(); //new grid bag constraints for the JList
        listConstraints.gridx = 0; //set the constraints to the leftmost column
        listConstraints.gridy = 1; //set the constraints to the second row, below the header
        listConstraints.insets = new Insets(10, 10, 10, 10); //create spacers
        listConstraints.fill = GridBagConstraints.BOTH; //make sure the constraints fill up space on both the x and y axis
        listConstraints.gridwidth = 8; //set the constraints to be size 8 (wider)
        libraryPanel.add(new JScrollPane(bookJList), listConstraints); //make bookJList a scroll pane and add it to the library panel with the constraints
        // Library bookString: Button only works when a book is selected for library list
        bookJList.addListSelectionListener(new ListSelectionListener() { //add list selection listener interface to bookJList
            public void valueChanged(ListSelectionEvent e) { //method for if a book is selected and remove book button is clicked
                removeBook.setEnabled(true); //remove button action is true
            }
        });
        updateBookList(); //updates book array list here

        /**
         * constraints for the remove button
         */
        GridBagConstraints bookButtonConstraints = new GridBagConstraints(); //new grid bag constraints for the remove button
        bookButtonConstraints.gridx = GridBagConstraints.RELATIVE; //set the x-axis location to its default position
        bookButtonConstraints.gridy = 2; //set the button to the third row, below the JList
        bookButtonConstraints.insets = new Insets(10, 10, 10, 10); //create spacers
        bookButtonConstraints.fill = GridBagConstraints.EAST; //make the constraints fill up space east of the button (right side)
        bookButtonConstraints.gridwidth = 2; //make the constraints have a width of 2
        libraryPanel.add(addBook, bookButtonConstraints); //add the add book button to the library panel with the constraints (not used)
        bookButtonConstraints.gridx = GridBagConstraints.RELATIVE; //set the x-axis location to its default position
        libraryPanel.add(removeBook, bookButtonConstraints); //add the remove book button to the library panel with the constraints
        removeBook.setEnabled(false); //set the button's usability to false until valueChanged() method is ran

        /**
         * constraints for feeby (might remove, is useless)
         */
        /*GridBagConstraints iconConstraints = new GridBagConstraints();
        iconConstraints.gridx = GridBagConstraints.RELATIVE;
        iconConstraints.gridy = 0;
        iconConstraints.anchor = GridBagConstraints.NORTHEAST;
        iconConstraints.insets = new Insets(10, 10, 10, 10);
        userAccount.setPreferredSize(new Dimension(30, 30));
        // libraryPanel.add(userAccount, iconConstraints);
        */
        
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER); //set the library label to the center of the panel 
        titleLabel.setForeground(Color.decode("#0e172c")); //set the text colour to black

        return libraryPanel; //returns the updated library panel

    }

    /**
     * creates panel for adding books to the list (right)
     */
    private JPanel createBookPanel() {
        JPanel bookPanel = new JPanel(new GridBagLayout()); //new panel for adding books using the grid bag layout

        /**
         * constraints for everything in the add book panel
         */
        GridBagConstraints bookConstraints = new GridBagConstraints(); //new grid bag constraints for everything in this panel
        bookConstraints.insets = new Insets(10, 10, 10, 60); //create spacers
        bookPanel.setBackground(Color.decode("#fec7d7")); //change the background colour to light pink
        bookLabel.setLabelFor(bookInput); //set the book title label to the book title text field
        authorLabel.setLabelFor(authorInput); //set the author name label to the author name text field
        isbnLabel.setLabelFor(isbnInput); //set the isbn number label to the isbn number text field
        bookConditionLabel.setLabelFor(bookCondition); //set the book condition label to the book condition drop down

        bookConstraints.gridx = 0; //set the constraints to the left most column
        bookConstraints.gridy = 0; //set the constraints to the top most row
        bookConstraints.gridwidth = 2; //set the constraints to have a width of 2
        bookConstraints.anchor = GridBagConstraints.CENTER; //anchor the constraints to the center of the panel 
        bookPanel.add(descriptionLabel, bookConstraints); //add the book panel description label to the panel with the constraints

        /**
         * adding the book title label and input
         */
        bookConstraints.gridx = 0; //set the constraints x position to the left most column
        bookConstraints.gridy = 1; //set the constraints y position to the second row (below the description label)
        bookConstraints.gridwidth = 1; //set the width to 1
        bookPanel.add(bookLabel, bookConstraints); //add the book title label to the panel with the constraints
        bookConstraints.gridy = 2; //set the y position to the third row (below the book title label)
        bookConstraints.fill = GridBagConstraints.HORIZONTAL; //fill up empty space on the x-axis
        bookConstraints.weightx = 1.0; //set spacing on the x-axis to 1.0
        bookPanel.add(bookInput, bookConstraints); //add the book title text field to the panel with the constraints

        /**
         * adding author label and input
         */
        bookConstraints.gridx = 1; //set the constraints x position to the second column (next to the book title label)
        bookConstraints.gridy = 1; //set the constraints y position to the second row (next to the book title label)
        bookPanel.add(authorLabel, bookConstraints); //add the author label to the panel with the constraints
        bookConstraints.gridy = 2; //set the constraints y position to the third row (below the author label)
        bookPanel.add(authorInput, bookConstraints); //add the author text field to the panel with the constraints

        /**
         * adding isbn label and input
         */
        bookConstraints.gridx = 0; //set the constraints x position to the left most column
        bookConstraints.gridy = 3; //set the constraints y position to the fourth row (below the book title text field)
        bookPanel.add(isbnLabel, bookConstraints); //add the isbn label to the panel with the constraints
        bookConstraints.gridy = 4; //set the constraints y position to the fifth column (below the isbn label)
        bookPanel.add(isbnInput, bookConstraints); //add the isbn text field to the panel with the constraints 

        /**
         * adding book condition drop down
         */
        bookConstraints.gridx = 1; //set the constraints x position to the second column (next to the book title label)
        bookConstraints.gridy = 3; //set the constraints y position to the fourth row (below the author text field)
        bookPanel.add(bookConditionLabel, bookConstraints); //add the book condition label to the panel with the constraints
        bookConstraints.gridy = 4; //set the constraints y position to the fifth row (below the book condition label)
        bookPanel.add(bookCondition, bookConstraints); //add the book condition drop down to the panel with the constraints

        /**
         * adding add book button
         */
        bookConstraints.gridx = 0; //set the constraints x position to the left most column
        bookConstraints.gridy = 5; //set the constraints y position to the 6th row (below the isbn text field)
        addBook.setPreferredSize(new Dimension(5, 30)); //set the button size to 5x30
        bookPanel.add(addBook, bookConstraints); //add the book to the panel with the constraints

        return bookPanel; //return the updated book panel
    }

    /**
     * method for library panel
     * removes selected book from the JList
     * https://docs.oracle.com/javase/8/docs/api/javax/swing/DefaultListModel.html
     * link for DefaultListModel usage
     */
    private void actionPerformed(ActionEvent buttonClicked) {
        int selectedIndex = bookJList.getSelectedIndex(); //new int object for the selected book in the JList

        // JList has a model that you use to update/maintain the contents of a JList
        if (selectedIndex != -1) { //if the selected index is not -1
            DefaultListModel<String> model = (DefaultListModel<String>) bookJList.getModel(); //creates a default model (copy, basically) of the JList
            model.removeElementAt(selectedIndex); //remove the selected book from the model (JList)
            bookList.remove(selectedIndex); //remove the selected book from the arraylist
            updateBookListFile(); //call this method to update the book text file
        }


    }

    /**
     * duplicate code to test methods and implementations
     */
   /* private void actionPerformed(ActionEvent buttonClicked) {
        String selectedBook = bookString.getSelectedValue();

        if(selectedBook != null){
            bookList.remove(selectedBook);
            updateBookList();
        }
    }*/


    /**
     * method for book panel
     * adds a book to JList
     * retrieves user inputted values (title, author, isbn, condition), and prints all information into Books.txt
     * and updates the bookList array
     */
    public void addBookAction(ActionEvent buttonClicked) {
        String bookTitle = bookInput.getText(); //string for bookTitle, retrieves inputted text in the book title text field
        String authorName = authorInput.getText(); //string for authorName, retrieves inputted text in the author name text field
        String isbn = isbnInput.getText(); //string for isbn, retrieves inputted text in the isbn text field
        Book.CONDITION condition = (Book.CONDITION) bookCondition.getSelectedItem(); //gets the book's selected condition from the drop down

        if (!bookTitle.isEmpty() && !authorName.isEmpty() && !isbn.isEmpty()) { //if the text fields are not empty
            String newBook = String.format("%s\n%s\n%s\ntrue\n%s\n1\n\n", bookTitle, authorName, isbn, condition); //new string format for the new book
            bookList.add(newBook); //add the new book to the array list
            updateBookListFile(); //call this method to update the book text file
            updateBookList(); //call this method to update the book array list

            // reset
            bookInput.setText(""); //set text field to empty
            authorInput.setText(""); //set text field to empty
            isbnInput.setText(""); //set text field to empty
        }
    }


    /**
     * updates the book text file with newly added books
     * https://www.geeksforgeeks.org/io-bufferedwriter-class-methods-java/#
     * link for BufferedWriter class
     * https://www.geeksforgeeks.org/filewriter-class-in-java/
     * link for FileWriter class
     */
    private void updateBookListFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(bookFile))) { //new BufferedWriter class that has a new FileWriter class that takes the book text file as its parameter
            for (String newBook : bookList) { //for every book in the array list
                writer.write(newBook); //print the book in the book text file
            }
            //writer.write("\n");
        } catch (IOException e) { //exception if the try/catch fails
            e.printStackTrace(); //prints information about the error
            JOptionPane.showMessageDialog(libraryFrame, "Error" + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE); //popup for the error message
        }
    }


    /**
     * basically reads the books on the file to be used by the update method below
     * https://www.geeksforgeeks.org/java-io-bufferedreader-class-java/#
     * link for buffered reader class usage
     * https://www.geeksforgeeks.org/stringbuilder-class-in-java-with-examples/
     * link for string builder usage
     * https://www.geeksforgeeks.org/stringbuilder-append-method-in-java-with-examples/
     * link for .append()
     */
    private List<String> readBookListFromFile() {
        bookList = new ArrayList<>(); //establish bookList as an arraylist
        try (BufferedReader reader = new BufferedReader(new FileReader(bookFile))) { //new BufferedReader class with a FileReader class that takes the book text file as a parameter
            String line; //new string object
            StringBuilder bookInfo = new StringBuilder(); //new string builder for book information
            while ((line = reader.readLine()) != null) { //while the string object (line)'s read line is not null
                bookInfo.append(line).append("\n"); //add the book info the the line and then add an empty after it
                if (line.isEmpty()) { //if the line is empty
                    bookList.add(bookInfo.toString()); //convert the bookInfo into a string and add it to the book array list
                    bookInfo.setLength(0); //set the book info's length to 0
                }
            }

            // Add the last one if not added
            if (bookInfo.length() != 0) { //if the book info's length is not 0
                bookList.add(bookInfo.toString()); //convert bookInfo into a string and add it to the book array list
                bookInfo.setLength(0); //set the book info's length to 0
            }
        } catch (IOException e) { //catch exception if try/catch fails
            e.printStackTrace(); //prints where the error happened
            JOptionPane.showMessageDialog(libraryFrame, "debuggy there seems to be an error :( " + e.getMessage(),
                    "whomp", JOptionPane.ERROR_MESSAGE); //pop up stating the error
        }
        return bookList; //return the updated book array list
    }


  /*  private void updateBookList() {
        SwingUtilities.invokeLater(() -> {
            bookString.setListData(bookList.toArray(new String[0]));
        });
    }*/

    /**
     * updates the list of books and prints it to the application
     */
    private void updateBookList() {
        SwingUtilities.invokeLater(() -> { //makes sure everything in this method is ran in order
            DefaultListModel<String> model = new DefaultListModel<>(); //new default list model
            List<String> bookTitles = new ArrayList<>(); //create an arraylist for the book titles only
            for (String bookInfo : bookList) { //for all book information in the book array list
                String[] lines = bookInfo.split("\n"); //new String[] with the books information, with a new line in between entry
                if (lines.length > 0) { //if lines length is greater than 0
                    bookTitles.add(lines[0]); //add the first line to the book titles array list
                    model.addElement(lines[0]); //add the first line to the default list model
                }
            }
            // DefaultListModel<String> model = new DefaultListModel<>();
            // model.addAll(bookTitles);
            bookJList.setModel(model); //set the default list model to the JList
        });
    }

    //method to get the book array list
    public List<String> getBookList() {
        return bookList; //returns the book array list
    }

    //method to show the selected book's information
    private void showBookDetails(int selectedIndex) {
        String selectedBook = bookList.get(selectedIndex); //new string object for the selected book in the JList
        JOptionPane.showMessageDialog(libraryFrame, selectedBook, "Book Details", JOptionPane.INFORMATION_MESSAGE); //pop up to display the selected book's information
    }


}
