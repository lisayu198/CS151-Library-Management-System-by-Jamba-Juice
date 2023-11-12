import javax.swing.*;
import java.awt.*;
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
    JButton addBook = new JButton();
    JButton removeBook = new JButton();
    JLabel titleLabel = new JLabel("list of available bookies");
    JTextArea availableBooks = new JTextArea();
    public Library(){
        SwingUtilities.invokeLater(() -> {
            readBookListFromFile();
            updateBookList();
        });

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
        textConstraints.insets = new Insets(10,10,10,10);
        textConstraints.fill = GridBagConstraints.HORIZONTAL;
        textConstraints.gridwidth = 2;
        libraryPanel.add(titleLabel, textConstraints);

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
        availableBooks.setEditable(false);
        availableBooks.setLineWrap(true);
        libraryPanel.add(new JScrollPane(availableBooks), bookConstraints);
        updateBookList(); //prints here

        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.decode("#0e172c"));



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
        List<String> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(bookFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(libraryFrame, "debuggy there seems to be an error :( " + e.getMessage(),
                    "whomp", JOptionPane.ERROR_MESSAGE);
        }
        return list;
    }

    /**
     * updates the list of books and prints it to the application
     */
    private void updateBookList() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String book : bookList) {
            stringBuilder.append(book).append("\n");
        }
        availableBooks.setText(stringBuilder.toString());
    }

}
