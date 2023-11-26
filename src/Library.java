import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Library {

    String[] array = new String[20];
    private List<String> bookList;
    String bookFile = "src/Books.txt";
    JFrame libraryFrame = new JFrame("library frame");
    JPanel panel = new JPanel(new GridLayout(1, 2));
    JButton removeBook = new JButton("remove book");
    ImageIcon userIcon = new ImageIcon("C:\\Users\\anisp\\Downloads\\FreddyFazbear_2__54364.jpg");
    ImageIcon hehe = new ImageIcon("C:\\Users\\anisp\\Downloads\\feeby jumpscare.png");
    Image scale = userIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
    ImageIcon updatedIcon = new ImageIcon(scale);
    JButton userAccount = new JButton(updatedIcon);
    JLabel titleLabel = new JLabel("list of available books");
    JList<String> bookJList;

    JLabel descriptionLabel = new JLabel("Enter book info: ");
    JTextField bookInput = new JTextField(20);
    JTextField authorInput = new JTextField(20);
    JTextField isbnInput = new JTextField(20);
    JComboBox bookCondition = new JComboBox(Book.CONDITION.values());
    JLabel bookLabel = new JLabel("book title?");
    JLabel authorLabel = new JLabel("author?");
    JLabel isbnLabel = new JLabel("isbn?");
    JLabel bookConditionLabel = new JLabel("book condition?");
    JButton addBook = new JButton("add book");

    public Library() {
        SwingUtilities.invokeLater(() -> {
            LibraryUI();
            readBookListFromFile();
            updateBookList();
        });

    }

    public void LibraryUI() {
        bookList = readBookListFromFile();

        libraryFrame.add(panel);
        libraryFrame.setSize(800, 500);
        libraryFrame.setVisible(true);
        libraryFrame.setLocationRelativeTo(null);
        // When you x out the UserFrame, it will go back to welcome screen
        libraryFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        libraryFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    WelcomeScreen welcomeScreen = new WelcomeScreen();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                libraryFrame.dispose();
            }
        });
        panel.add(createLibraryPanel());
        panel.add(createBookPanel());

        /**
         * http://www.java2s.com/Tutorial/Java/0260__Swing-Event/DetectingDoubleandTripleClicks.htm
         * link for double clicking in Java Swing
         */
        bookJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent click) {
                if (click.getClickCount() >= -1) {
                    for (String value : bookList) {
                        if (value != null && bookJList.getSelectedValue() != null &&
                                value.startsWith(bookJList.getSelectedValue())) {
                            JOptionPane.showMessageDialog(libraryFrame, value, "book info", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        }
                    }
                }
            }
        });

        // userAccount.addActionListener(this::actionPerformed);
        removeBook.addActionListener(this::actionPerformed);
        addBook.addActionListener(this::addBookAction);
    }

    /**
     * creates the library panel (left)
     */
    private JPanel createLibraryPanel() {
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
        textConstraints.insets = new Insets(10, 60, 10, 10);
        textConstraints.fill = GridBagConstraints.HORIZONTAL;
        textConstraints.gridwidth = 5;
        libraryPanel.add(titleLabel, textConstraints);

        /**
         * working JList for the books
         */
        bookJList = new JList<>();
        bookJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookJList.setLayoutOrientation(JList.VERTICAL);
        bookJList.setVisibleRowCount(10);
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
        listConstraints.insets = new Insets(10, 10, 10, 10);
        listConstraints.fill = GridBagConstraints.BOTH;
        listConstraints.gridwidth = 8;
        libraryPanel.add(new JScrollPane(bookJList), listConstraints);
        // Library bookString: Button only works when a book is selected for library list
        bookJList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                removeBook.setEnabled(true);
            }
        });
        updateBookList(); //prints here

        /**
         * constraints for the remove button
         */
        GridBagConstraints bookButtonConstraints = new GridBagConstraints();
        bookButtonConstraints.gridx = GridBagConstraints.RELATIVE;
        bookButtonConstraints.gridy = 2;
        bookButtonConstraints.insets = new Insets(10, 10, 10, 10);
        bookButtonConstraints.fill = GridBagConstraints.EAST;
        bookButtonConstraints.gridwidth = 2;
        libraryPanel.add(addBook, bookButtonConstraints);
        bookButtonConstraints.gridx = GridBagConstraints.RELATIVE;
        libraryPanel.add(removeBook, bookButtonConstraints);
        removeBook.setEnabled(false);

        /**
         * constraints for feeby (might remove, is useless)
         */
        GridBagConstraints iconConstraints = new GridBagConstraints();
        iconConstraints.gridx = GridBagConstraints.RELATIVE;
        iconConstraints.gridy = 0;
        iconConstraints.anchor = GridBagConstraints.NORTHEAST;
        iconConstraints.insets = new Insets(10, 10, 10, 10);
        userAccount.setPreferredSize(new Dimension(30, 30));
        // libraryPanel.add(userAccount, iconConstraints);

        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.decode("#0e172c"));

        return libraryPanel;

    }

    /**
     * creates panel for adding books to the list (right)
     */
    private JPanel createBookPanel() {
        JPanel bookPanel = new JPanel(new GridBagLayout());

        /**
         * constraints for everything
         */
        GridBagConstraints bookConstraints = new GridBagConstraints();
        bookConstraints.insets = new Insets(10, 10, 10, 60);
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
        addBook.setPreferredSize(new Dimension(5, 30));
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

        // JList has a model that you use to update/maintain the contents of a JList
        if (selectedIndex != -1) {
            DefaultListModel<String> model = (DefaultListModel<String>) bookJList.getModel();
            model.removeElementAt(selectedIndex);
            bookList.remove(selectedIndex);
            updateBookListFile();
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
    private void addBookAction(ActionEvent buttonClicked) {
        String bookTitle = bookInput.getText();
        String authorName = authorInput.getText();
        String isbn = isbnInput.getText();
        Book.CONDITION condition = (Book.CONDITION) bookCondition.getSelectedItem();

        if (!bookTitle.isEmpty() && !authorName.isEmpty() && !isbn.isEmpty()) {
            String newBook = String.format("%s\n%s\n%s\ntrue\n%s\n1\n\n", bookTitle, authorName, isbn, condition);
            bookList.add(newBook);
            updateBookListFile();
            updateBookList();

            // reset
            bookInput.setText("");
            authorInput.setText("");
            isbnInput.setText("");
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
            for (String newBook : bookList) {
                writer.write(newBook);
            }
            //writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(libraryFrame, "Error" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * basically reads the books on the file to be used by the update method below
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
            DefaultListModel<String> model = new DefaultListModel<>();
            List<String> bookTitles = new ArrayList<>();
            for (String bookInfo : bookList) {
                String[] lines = bookInfo.split("\n");
                if (lines.length > 0) {
                    bookTitles.add(lines[0]);
                    model.addElement(lines[0]);
                }
            }
            // DefaultListModel<String> model = new DefaultListModel<>();
            // model.addAll(bookTitles);
            bookJList.setModel(model);
        });
    }

    public List<String> getBookList() {
        return bookList;
    }

    private void showBookDetails(int selectedIndex) {
        String selectedBook = bookList.get(selectedIndex);
        JOptionPane.showMessageDialog(libraryFrame, selectedBook, "Book Details", JOptionPane.INFORMATION_MESSAGE);
    }


}
