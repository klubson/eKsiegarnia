package models;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class WindowMethods {
    public JFrame window;
    private Dimension dimension = new Dimension(250, 20);
    private dataBaseConnection dataBase = new dataBaseConnection();
    private String beginWho_logged = "Zalogowany: ";

    public JTextField setJTextField(JTextField field, String name){
        field = new JTextField();
        field.setName(name);
        field.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        field.setPreferredSize(dimension);
        return field;
    }
    public JPasswordField setJPasswordField(JPasswordField field, String name){
        field = new JPasswordField();
        field.setName(name);
        field.setPreferredSize(dimension);
        field.setEchoChar('\u25CF');
        field.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        return field;
    }
    public JCheckBox setPassCheckBox(JCheckBox check, JPasswordField passwordField ,String text){
        check = new JCheckBox(text);
        JCheckBox finalCheck = check;
        check.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(finalCheck.isSelected()){
                    passwordField.setEchoChar((char)0);
                }
                else{
                    passwordField.setEchoChar('\u25CF');
                }
            }
        });
        return finalCheck;
    }
    public JCheckBox setSortCheckBox(JCheckBox first, JCheckBox second){
        JCheckBox finalAsc = first;
        first.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(finalAsc.isSelected()){
                    second.setSelected(false);
                }
            }
        });
        return finalAsc;
    }
    public void setWho_logged(JLabel who_logged, String data) throws SQLException {
        dataBase.setStmt();
        ResultSet rs = dataBase.getStmt().executeQuery(
                "SELECT Imie, Nazwisko FROM Uzytkownik WHERE LOGIN = '" + data + "'"
        );
        rs.next();
        String name = rs.getString(1) + " " + rs.getString(2);
        who_logged.setText(beginWho_logged + name);
        rs.close();
        dataBase.getStmt().close();
    }
    public void settings(){
        window.setSize(600, 600);
        window.setLocation(400, 80);
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
    }
    public void exit(){
        window.setVisible(false);
        window.dispose();
    }
}
