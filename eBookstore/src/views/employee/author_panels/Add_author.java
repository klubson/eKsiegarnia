package views.employee.author_panels;

import models.DataVerification;
import models.WindowMethods;
import models.dataBaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Add_author {
    private WindowMethods windowMethods = new WindowMethods();
    private JLabel name, surname, country;
    private JButton add, back;
    private JTextField name2, surname2, country2;
    private JPanel center, down, name_pane, surname_pane, country_pane;
    private String user;
    private dataBaseConnection dataBase = new dataBaseConnection();
    private boolean isManager;

    public void create(String data, boolean mode){
        windowMethods.window = new JFrame("Dodaj autora");
        windowMethods.settings();
        windowMethods.window.setSize(600, 300);
        user = data;
        isManager = mode;
        add_components();
        windowMethods.window.setVisible(true);
    }
    private void labels(){
        name = new JLabel("Imię (max 20 znaków): ");
        surname = new JLabel("Nazwisko (max 30 znaków): ");
        country = new JLabel("Kraj pochodzenia (max 30 znaków): ");
    }
    private void components(){
        name2 = windowMethods.setJTextField(name2, "IMIĘ");
        surname2 = windowMethods.setJTextField(surname2, "NAZWISKO");
        country2 = windowMethods.setJTextField(country2, "KRAJ POCHODZENIA");
        back = new JButton("Powrót");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Authors au = new Authors();
                windowMethods.exit();
                try {
                    au.create(user, isManager);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        add = new JButton("Dodaj");
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //sprawdź i dodaj
                if(check()){
                    try {
                        dataBase.getConn().setAutoCommit(true);
                        dataBase.newAuthor(name2.getText(), surname2.getText(), country2.getText());
                        JOptionPane.showMessageDialog(windowMethods.window, "Autor dodany pomyślnie");
                        Authors au = new Authors();
                        windowMethods.exit();
                        au.create(user, isManager);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        });
    }
    private void panels(){
        labels();
        components();

        name_pane = new JPanel();
        name_pane.add(name);
        name_pane.add(name2);

        surname_pane = new JPanel();
        surname_pane.add(surname);
        surname_pane.add(surname2);

        country_pane = new JPanel();
        country_pane.add(country);
        country_pane.add(country2);

        center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
        center.add(name_pane);
        center.add(surname_pane);
        center.add(country_pane);
        down = new JPanel();
        down.setLayout(new BorderLayout());
        down.add(back, BorderLayout.WEST);
        down.add(add, BorderLayout.EAST);
    }
    private void add_components(){
        panels();
        windowMethods.window.add(center, BorderLayout.CENTER);
        windowMethods.window.add(down, BorderLayout.SOUTH);
    }
    private boolean check(){
        DataVerification verify = new DataVerification();
        verify.fieldCheck(name2, 1, 20, false, true);
        verify.fieldCheck(surname2, 1, 30, false, true);
        verify.fieldCheck(country2, 0, 30, false, true);
        verify.errorMessage();
        if(verify.error_counter == 0) return true;
        else return false;
    }
}
