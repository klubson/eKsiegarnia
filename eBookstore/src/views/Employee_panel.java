package views;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class Employee_panel extends JFrame {
    private JFrame window;


    public void create() throws SQLException {
        window = new JFrame("Pracownik");
        settings();
        add_components();
        window.setVisible(true);
    }
    private void settings(){
        window.setSize(600, 600);
        window.setLocation(400, 80);
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
    }
    private void add_components(){

    }
}
