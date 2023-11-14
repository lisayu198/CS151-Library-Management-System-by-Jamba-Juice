package libraryPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.*;
import java.util.List;

public class Library {
    Hashtable userHashtable = new Hashtable();
    private List<String> bookList;
    String bookFile = "booklist.txt";
    JFrame libraryFrame = new JFrame("library frame");
    JPanel libraryPanel = new JPanel(new GridBagLayout());
    JButton removeBook = new JButton("clicketh me to removeth thyst books");
    ImageIcon userIcon = new ImageIcon("C:\\Users\\anisp\\Downloads\\FreddyFazbear_2__54364.jpg");
    Image scale = userIcon.getImage().getScaledInstance(30,30, Image.SCALE_SMOOTH);
    ImageIcon updatedIcon = new ImageIcon(scale);
    JButton userAccount = new JButton(updatedIcon);
    JLabel titleLabel = new JLabel("list of available bookies");
    JList<String> bookString;
    public Library(){
        SwingUtilities.invokeLater(() -> {
            LibraryUI();
            readBookListFromFile();
            updateBookList();
        });

    }
    public void LibraryUI(){
        bookList = readBookListFromFile();

        libraryPanel.setBackground(Color.decode("#fec7d7"));
        libraryFrame.add(libraryPanel);
        libraryFrame.setSize(700,500);
        libraryFrame.setVisible(true);
        libraryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /**
         * this is constraints for the header
         * insets is basically like a spacer/margins
         * fill makes sure the label isn't more on one side; aka fills up space on the x axis
         * gridwidth is how long it is
         */
        GridBagConstraints textConstraints = new GridBagConstraints();
        textConstraints.gridx = 0;
        textConstraints.gridy = 0;
        textConstraints.insets = new Insets(10,60,10,10);
        textConstraints.fill = GridBagConstraints.HORIZONTAL;
        textConstraints.gridwidth = 5;
        libraryPanel.add(titleLabel, textConstraints);

        bookString = new JList<>();
        bookString.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookString.setLayoutOrientation(JList.VERTICAL);
        bookString.setVisibleRowCount(10);
        /**
         * constraints for the list of books
         * setEditable is false so prof can't edit the available books
         * setLineWrap so if the title is too long it'll
         * look like this (I hope)
         * JScrollPane so if there's a lot of books we can scroll up and down the little text area
         */
        GridBagConstraints bookConstraints = new GridBagConstraints();
        bookConstraints.gridx = 0;
        bookConstraints.gridy = 1;
        bookConstraints.insets = new Insets(10,10,10,10);
        bookConstraints.fill = GridBagConstraints.BOTH;
        bookConstraints.gridwidth = 8;
        libraryPanel.add(new JScrollPane(bookString), bookConstraints);
        updateBookList(); //prints here

        GridBagConstraints addBookConstraints = new GridBagConstraints();
        addBookConstraints.gridx = 0;
        addBookConstraints.gridy = 2;
        addBookConstraints.insets = new Insets(10,10,10,10);
        addBookConstraints.fill = GridBagConstraints.BOTH;
        addBookConstraints.gridwidth = 2;
        libraryPanel.add(removeBook, addBookConstraints);

        GridBagConstraints iconConstraints = new GridBagConstraints();
        iconConstraints.gridx = GridBagConstraints.RELATIVE;
        iconConstraints.gridy = 0;
        iconConstraints.anchor = GridBagConstraints.NORTHEAST;
        iconConstraints.insets = new Insets(10, 10, 10, 10);
        userAccount.setPreferredSize(new Dimension(30,30));
        libraryPanel.add(userAccount, iconConstraints);

        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.decode("#0e172c"));
        
        userAccount.addActionListener(this::actionPerformed);
        removeBook.addActionListener(this::actionPerformed);
    }

    private void actionPerformed(ActionEvent buttonClicked) {
        String selectedBook = bookString.getSelectedValue();

        if(selectedBook != null){
            bookList.remove(selectedBook);
            updateBookList();
        }
    }

    //comment
    /*private void addUser(User user){

    }
    private void addBook(Book book){

    }*/
    private void removeBook(){

    }
    /*private void removeUser(User user){

    }*/
    private void searchBook(String title){

    }
    private void checkOutBook(String book){

    }
    private void returnBook(String book){

    }

    /** booklist.txt is a temp file so I can test to see if it prints. will replace will nelly's book list
     * basically reads the books on the file to be used by the update method below
     *
     * PS: IT SOMETIMES DOESNT PRINT IDK WHY IM FIGURING THAT OUT
     */
    private List<String> readBookListFromFile() {
        bookList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(bookFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                bookList.add(line);
            }
        } catch (IOException e) {
            //e.printStackTrace();
            JOptionPane.showMessageDialog(libraryFrame, "debuggy there seems to be an error :( " + e.getMessage(),
                    "whomp", JOptionPane.ERROR_MESSAGE);
        }
        return bookList;
    }

    /**
     * updates the list of books and prints it to the application
     */
    private void updateBookList() {
        SwingUtilities.invokeLater(() -> {
            bookString.setListData(bookList.toArray(new String[0]));
        });
    }

}
