import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

public class Library {
    Hashtable userHashtable = new Hashtable();
    JFrame libraryFrame = new JFrame();
    JPanel libraryPanel = new JPanel(new GridBagLayout());
    JButton addBook = new JButton();
    JButton removeBook = new JButton();
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
    public Library(){
        libraryPanel.setBackground(Color.white);
        libraryFrame.add(libraryPanel);
        libraryFrame.setSize(700,500);
        libraryFrame.setLayout(null);
        libraryFrame.setVisible(true);
        libraryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}
