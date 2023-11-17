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

    String[] array = new String[20];
    private List<String> bookList;
    String bookFile = "loginUI/Books.txt";
    JFrame libraryFrame = new JFrame("library frame");
    JPanel panel = new JPanel(new GridLayout(1, 2));
    JButton removeBook = new JButton("removeth thyst books");
    ImageIcon userIcon = new ImageIcon("C:\\Users\\anisp\\Downloads\\FreddyFazbear_2__54364.jpg");
    ImageIcon hehe = new ImageIcon("C:\\Users\\anisp\\Downloads\\feeby jumpscare.png");
    Image scale = userIcon.getImage().getScaledInstance(30,30, Image.SCALE_SMOOTH);
    ImageIcon updatedIcon = new ImageIcon(scale);
    JButton userAccount = new JButton(updatedIcon);
    JLabel titleLabel = new JLabel("list of available bookies");
    JList<String> bookString;

    JLabel descriptionLabel = new JLabel("pls enter bok info ty");
    JTextField bookInput = new JTextField(20);
    JTextField authorInput = new JTextField(20);
    JTextField isbnInput = new JTextField(20);
    JLabel bookLabel = new JLabel("book title?");
    JLabel authorLabel = new JLabel("author?");
    JLabel isbnLabel = new JLabel("isbn?");
    JButton addBook = new JButton("addeth thyst books");

    public Library(){
        SwingUtilities.invokeLater(() -> {
            LibraryUI();
            readBookListFromFile();
            updateBookList();
        });

    }
    public void LibraryUI(){
        bookList = readBookListFromFile();

        libraryFrame.add(panel);
        libraryFrame.setSize(800,500);
        libraryFrame.setVisible(true);
        libraryFrame.setLocationRelativeTo(null);
        libraryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        panel.add(createLibraryPanel());
        panel.add(createBookPanel());

        bookString.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = bookString.getSelectedIndex();
                    if (selectedIndex != -1) {
                        showBookDetails(selectedIndex);
                    }
                }
            }
        });

        userAccount.addActionListener(this::actionPerformed);
        removeBook.addActionListener(this::actionPerformed);
        addBook.addActionListener(this::addBookAction);
    }

    /**
     * creates the library panel (left)
     */
    private JPanel createLibraryPanel(){
        JPanel libraryPanel = new JPanel(new GridBagLayout());
        libraryPanel.setBackground(Color.decode("#fec7d7"));
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

        /**
         * working JList for the books
         */
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
        GridBagConstraints listConstraints = new GridBagConstraints();
        listConstraints.gridx = 0;
        listConstraints.gridy = 1;
        listConstraints.insets = new Insets(10,10,10,10);
        listConstraints.fill = GridBagConstraints.BOTH;
        listConstraints.gridwidth = 8;
        libraryPanel.add(new JScrollPane(bookString), listConstraints);
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
        userAccount.setPreferredSize(new Dimension(30,30));
        libraryPanel.add(userAccount, iconConstraints);

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
         * adding add book button
         */
        bookConstraints.gridx = 1;
        bookConstraints.gridy = 4;
        addBook.setPreferredSize(new Dimension(5,30));
        bookPanel.add(addBook, bookConstraints);

        return bookPanel;
    }

    /**
     * method for library panel
     * removes selected book from the JList
     */
    private void actionPerformed(ActionEvent buttonClicked) {
        int selectedIndex = bookString.getSelectedIndex();

        if (selectedIndex != -1) {
            // Remove the selected book information from the bookList
            bookList.remove(selectedIndex);

            // Update the JList to reflect the changes
            updateBookList();
        }
    }

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
     */
    private void addBookAction(ActionEvent buttonClicked) {
        String bookTitle = bookInput.getText();
        String authorName = authorInput.getText();
        String isbn = isbnInput.getText();

        if (!bookTitle.isEmpty() && !authorName.isEmpty() && !isbn.isEmpty()) {
            bookList.add(bookTitle);

            updateBookListFile();
            updateBookList();
        }
    }



    /**
     * updates the booklist text file with newly added books
     */
    private void updateBookListFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(bookFile))) {
            for (String bookInfo : bookList) {
                writer.write(bookInfo);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(libraryFrame, "man something went wrong :(" + e.getMessage(),
                    "eeeeeeee", JOptionPane.ERROR_MESSAGE);
        }
    }


    /** booklist.txt is a temp file so I can test to see if it prints. will replace will nelly's book list
     * basically reads the books on the file to be used by the update method below
     *
     */
    private List<String> readBookListFromFile() {
        bookList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(bookFile))) {
            String line;
            StringBuilder bookInfo = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                bookInfo.append(line).append("\n");
                if (line.isEmpty()) {
                    bookList.add(bookInfo.toString());
                    bookInfo.setLength(0); // Clear the StringBuilder for the next book
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(libraryFrame, "debuggy there seems to be an error :( " + e.getMessage(),
                    "whomp", JOptionPane.ERROR_MESSAGE);
        }
        return bookList;
    }


    /**
     * updates the list of books and prints it to the application
     */
  /*  private void updateBookList() {
        SwingUtilities.invokeLater(() -> {
            bookString.setListData(bookList.toArray(new String[0]));
        });
    }*/
    private void updateBookList() {
        SwingUtilities.invokeLater(() -> {
            List<String> bookTitles = new ArrayList<>();
            for (String bookInfo : bookList) {
                // Split the book information by newline and add only the first line (title) to the list
                String[] lines = bookInfo.split("\n");
                if (lines.length > 0) {
                    bookTitles.add(lines[0]);
                }
            }
            bookString.setListData(bookTitles.toArray(new String[0]));
        });
    }
    private void showBookDetails(int selectedIndex) {
        String selectedBook = bookList.get(selectedIndex);
        JOptionPane.showMessageDialog(libraryFrame, selectedBook, "Book Details", JOptionPane.INFORMATION_MESSAGE);
    }




}
