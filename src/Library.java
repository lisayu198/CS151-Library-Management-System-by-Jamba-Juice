package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Library {

    /**
     * library panel UI
     */
    String[] array = new String[20]; //new string[] array (unused)
    List<String> bookList; //new private List with String parameter for books
    String bookFile = "Books.txt"; //new string for book text file (file directory)
    JFrame libraryFrame = new JFrame("library frame"); //JFrame for library frame
    JPanel panel = new JPanel(new GridLayout(1, 2)); //main JPanel to store both library and book panel
    JButton removeBook = new JButton("removeth thyst books"); //JButton for removing a book
    //ImageIcon userIcon = new ImageIcon("C:\\Users\\anisp\\Downloads\\FreddyFazbear_2__54364.jpg");
    //ImageIcon hehe = new ImageIcon("C:\\Users\\anisp\\Downloads\\feeby jumpscare.png");
    //Image scale = userIcon.getImage().getScaledInstance(30,30, Image.SCALE_SMOOTH);
    //ImageIcon updatedIcon = new ImageIcon(scale);
    //JButton userAccount = new JButton(updatedIcon);
    JLabel titleLabel = new JLabel("list of available bookies"); //JLabel for the library panel description
    JList<String> bookJList; //JList with string parameter for the books

    /**
     * book panel UI
     */
    JLabel descriptionLabel = new JLabel("pls enter bok info ty"); //JLabel for book panel description
    JTextField bookInput = new JTextField(20); //text field for inputting book title
    JTextField authorInput = new JTextField(20); //text field for inputting author name
    JTextField isbnInput = new JTextField(20); //text field for inputting book isbn
    JComboBox bookCondition = new JComboBox(Book.CONDITION.values()); //drop down for book condition
    JLabel bookLabel = new JLabel("book title?"); //JLabel for book title
    JLabel authorLabel = new JLabel("author?"); //JLabel for author name
    JLabel isbnLabel = new JLabel("isbn?"); //JLabel for book isbn
    JLabel bookConditionLabel = new JLabel("book condition?"); //JLabel for book condition drop down
    JButton addBook = new JButton("addeth thyst books"); //button for adding a book

    /**
     * https://www.javamex.com/tutorials/threads/invokelater.shtml
     * link for SwingUtilities.invokeLater
     * calls libraryUI method first, then readBookListFromFile, then updateBookList
     */
    public Library(){
        SwingUtilities.invokeLater(() -> {
            LibraryUI();
            readBookListFromFile();
            updateBookList();
        });

    }

    /**
     * main frame UI method
     * creates the library frame and adds the library and book panel to the frame
     */
    public void LibraryUI(){
        bookList = readBookListFromFile(); //book list array calls the readBookListFromFile method

        libraryFrame.add(panel); //add main to library frame
        libraryFrame.setSize(800,500); //set the library frame to 800x500
        libraryFrame.setVisible(true); //make sure library frame is visible
        libraryFrame.setLocationRelativeTo(null); //make library frame open in the middle of computer screen instead of the corner
        libraryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //library frame closes but application does not close

        panel.add(createLibraryPanel()); //call createLibraryPanel method and add it to the main panel
        panel.add(createBookPanel()); //call createBookPanel method and add it to the main panel

        /**
         * http://www.java2s.com/Tutorial/Java/0260__Swing-Event/DetectingDoubleandTripleClicks.htm
         * link for double clicking in Java Swing
         */
        bookJList.addMouseListener(new MouseAdapter() { //add mouse listener to the JList object
            @Override
            public void mouseClicked(MouseEvent click) { //create mouseclick event
                if (click.getClickCount() == 2) { //check for double click
                    int selectedIndex = bookJList.getSelectedIndex(); //which book in JList is selected
                    System.out.println(selectedIndex); //debug
                    if (selectedIndex != -1) { //make sure its not >0
                        String bookTitle = bookList.get(selectedIndex); //retrieves matching book from bookList array
                        System.out.println(bookTitle); //debug
                        JOptionPane.showMessageDialog(libraryFrame, bookTitle, "book info", JOptionPane.INFORMATION_MESSAGE); //JOptionPane that shows the book's information
                    }
                }
            }
        });

        //userAccount.addActionListener(this::actionPerformed); (don't worry about this professor)
        removeBook.addActionListener(this::actionPerformed); //add action listener to the remove book button
        addBook.addActionListener(this::addBookAction); //add action listener to the add book button
    }

    /**
     * creates the library panel (left) with constraints for all J components
     */
    private JPanel createLibraryPanel(){
        JPanel libraryPanel = new JPanel(new GridBagLayout()); //create new JPanel for library panel with GridBagLayout
        libraryPanel.setBackground(Color.decode("#fec7d7")); //sets the background colour to light pink
        /**
         * this is constraints for the header
         * insets is basically like a spacer/margins
         * fill makes sure the label isn't more on one side; aka fills up space on the x axis
         * gridwidth is how long it is
         */
        GridBagConstraints textConstraints = new GridBagConstraints();
        textConstraints.gridx = 0;
        textConstraints.gridy = 0;
        textConstraints.insets = new Insets(10,60,10,10); //creates spacers
        textConstraints.fill = GridBagConstraints.HORIZONTAL; //fills the constraints on the x-axis
        textConstraints.gridwidth = 5; //sets how wide the constraints are
        libraryPanel.add(titleLabel, textConstraints); //adds the label with the constraints to the library panel

        /**
         * working JList for the books
         */
        bookJList = new JList<>(); //establish bookJList as JList
        bookJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //set the selection mode to single selection only
        bookJList.setLayoutOrientation(JList.VERTICAL); //set the JList to vertical (scroll up/down)
        bookJList.setVisibleRowCount(10); //set visible row count to 10 at a time
        /**
         * constraints for the list of books
         * setEditable is false so prof can't edit the available books
         * setLineWrap so if the title is too long it'll
         * look like this (I hope)
         * JScrollPane so if there's a lot of books we can scroll up and down the little text area
         */
        GridBagConstraints listConstraints = new GridBagConstraints();
        listConstraints.gridx = 0;
        listConstraints.gridy = 1;
        listConstraints.insets = new Insets(10,10,10,10);
        listConstraints.fill = GridBagConstraints.BOTH;
        listConstraints.gridwidth = 8;
        libraryPanel.add(new JScrollPane(bookJList), listConstraints);
        updateBookList(); //prints here

        /**
         * constraints for the remove button
         */
        GridBagConstraints bookButtonConstraints = new GridBagConstraints();
        bookButtonConstraints.gridx = GridBagConstraints.RELATIVE;
        bookButtonConstraints.gridy = 2;
        bookButtonConstraints.insets = new Insets(10,10,10,10);
        bookButtonConstraints.fill = GridBagConstraints.EAST;
        bookButtonConstraints.gridwidth = 2;
        libraryPanel.add(addBook, bookButtonConstraints);
        bookButtonConstraints.gridx = GridBagConstraints.RELATIVE;
        libraryPanel.add(removeBook, bookButtonConstraints);

        /**
         * constraints for feeby (might remove, is useless)
         */
        GridBagConstraints iconConstraints = new GridBagConstraints();
        iconConstraints.gridx = GridBagConstraints.RELATIVE;
        iconConstraints.gridy = 0;
        iconConstraints.anchor = GridBagConstraints.NORTHEAST;
        iconConstraints.insets = new Insets(10, 10, 10, 10);
        //userAccount.setPreferredSize(new Dimension(30,30));
        //libraryPanel.add(userAccount, iconConstraints);

        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.decode("#0e172c"));

        return libraryPanel;

    }

    /**
     * creates panel for adding books to the list (right)
     */
    private JPanel createBookPanel(){
        JPanel bookPanel = new JPanel(new GridBagLayout());

        /**
         * constraints for everything
         */
        GridBagConstraints bookConstraints = new GridBagConstraints();
        bookConstraints.insets = new Insets(10,10,10,60);
        bookPanel.setBackground(Color.decode("#fec7d7"));
        bookLabel.setLabelFor(bookInput);
        authorLabel.setLabelFor(authorInput);
        isbnLabel.setLabelFor(isbnInput);
        bookConditionLabel.setLabelFor(bookCondition);

        bookConstraints.gridx = 0;
        bookConstraints.gridy = 0;
        bookConstraints.gridwidth = 2;
        bookConstraints.anchor = GridBagConstraints.CENTER;
        bookPanel.add(descriptionLabel, bookConstraints);

        /**
         * adding the book title label and input
         */
        bookConstraints.gridx = 0;
        bookConstraints.gridy = 1;
        bookConstraints.gridwidth = 1;
        bookPanel.add(bookLabel, bookConstraints);
        bookConstraints.gridy = 2;
        bookConstraints.fill = GridBagConstraints.HORIZONTAL;
        bookConstraints.weightx = 1.0;
        bookPanel.add(bookInput, bookConstraints);

        /**
         * adding author label and input
         */
        bookConstraints.gridx = 1;
        bookConstraints.gridy = 1;
        bookPanel.add(authorLabel, bookConstraints);
        bookConstraints.gridy = 2;
        bookPanel.add(authorInput, bookConstraints);

        /**
         * adding isbn label and input
         */
        bookConstraints.gridx = 0;
        bookConstraints.gridy = 3;
        bookPanel.add(isbnLabel, bookConstraints);
        bookConstraints.gridy = 4;
        bookPanel.add(isbnInput, bookConstraints);

        /**
         * adding book condition drop down
         * https://stackoverflow.com/questions/1459069/populating-swing-jcombobox-from-enum
         * link for how to use JComboBox with enums
         */
        bookConstraints.gridx = 1;
        bookConstraints.gridy = 3;
        bookPanel.add(bookConditionLabel, bookConstraints);
        bookConstraints.gridy = 4;
        bookPanel.add(bookCondition, bookConstraints);

        /**
         * adding add book button
         */
        bookConstraints.gridx = 0;
        bookConstraints.gridy = 5;
        addBook.setPreferredSize(new Dimension(5,30));
        bookPanel.add(addBook, bookConstraints);

        return bookPanel;
    }

    /**
     * method for library panel
     * removes selected book from the JList
     * https://docs.oracle.com/javase/8/docs/api/javax/swing/DefaultListModel.html
     * link for DefaultListModel usage
     */
    private void actionPerformed(ActionEvent buttonClicked) {
        int selectedIndex = bookJList.getSelectedIndex();
        if (selectedIndex != -1) {
            DefaultListModel<String> model = (DefaultListModel<String>) bookJList.getModel(); //default list model to remove books from JList
            model.removeElementAt(selectedIndex); //removes the book at selected index from JList
            bookList.remove(selectedIndex); //removes the same book from the array list
            updateBookListFile(); //updates the book text file
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
    void addBookAction(ActionEvent buttonClicked) {
        String bookTitle = bookInput.getText();
        String authorName = authorInput.getText();
        String isbn = isbnInput.getText();
        Book.CONDITION condition = (Book.CONDITION) bookCondition.getSelectedItem();

        if (!bookTitle.isEmpty() && !authorName.isEmpty() && !isbn.isEmpty()) {
            String newBook = String.format("%s\n%s\n%s\n%s", bookTitle, authorName, isbn, condition);
            bookList.add(newBook);
            updateBookListFile();
            updateBookList();
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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(bookFile))) {
            for (String newBook : bookList) { //for every newBook in the bookList array
                writer.write(newBook); //write the newBook into the book text file
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(libraryFrame, "man something went wrong :(" + e.getMessage(),
                    "eeeeeeee", JOptionPane.ERROR_MESSAGE);
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
        bookList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(bookFile))) {
            String line;
            StringBuilder currentBook = new StringBuilder(); //new string builder to store current book
            while ((line = reader.readLine()) != null) { //make sure the line string is not null
                currentBook.append(line).append("\n"); //connects current book with line and new line
                if (line.isEmpty()) { //if line is empty, add current book to line and remove excess space
                    bookList.add(currentBook.toString().trim());
                    currentBook.setLength(0); //return currentBook character size back to 0
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(libraryFrame, "debuggy there seems to be an error :( " + e.getMessage(),
                    "whomp", JOptionPane.ERROR_MESSAGE);
        }
        return bookList;
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
        SwingUtilities.invokeLater(() -> {
            List<String> bookTitles = new ArrayList<>(); //create new array list for book titles
            for (String bookInfo : bookList) { //for every bookInfo in the bookList array list
                String[] lines = bookInfo.split("\n"); //new string[] with book info + new line
                if (lines.length > 0) {
                    bookTitles.add(lines[0]);
                }
            }
            DefaultListModel<String> model = new DefaultListModel<>(); //new list model
            model.addAll(bookTitles); //add all book titles to model
            bookJList.setModel(model); //set model to the JList
        });
    }






}
