package views.employee.author_panels;

import models.dataBaseConnection;
import views.employee.publisher_panels.Publishers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Add_author extends JFrame {
    private JFrame window;
    private JLabel name, surname, country;
    private JButton add, back;
    private JTextField name2, surname2, country2;
    private JPanel center, down, name_pane, surname_pane, country_pane;
    private String user, message = "W następujących polach wykryto błędy: ";
    private Dimension dimension = new Dimension(250, 20);
    private int error_counter = 0;
    private dataBaseConnection dataBase = new dataBaseConnection();
    private boolean isManager;

    public void create(String data, boolean mode){
        window = new JFrame("Dodaj autora");
        settings();
        user = data;
        isManager = mode;
        add_components();
        window.setVisible(true);
    }
    private void settings(){
        window.setSize(600, 300);
        window.setLocation(400, 80);
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
    }
    private void labels(){
        name = new JLabel("Imię (max 20 znaków): ");
        surname = new JLabel("Nazwisko (max 30 znaków): ");
        country = new JLabel("Kraj pochodzenia (max 30 znaków): ");
    }
    private void components(){
        back = new JButton("Powrót");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Authors au = new Authors();
                exit();
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
                        JOptionPane.showMessageDialog(window, "Autor dodany pomyślnie");
                        Authors au = new Authors();
                        exit();
                        au.create(user, isManager);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        });
        name2 = new JTextField();
        name2.setName("IMIĘ");
        name2.setPreferredSize(dimension);
        name2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        surname2 = new JTextField();
        surname2.setName("NAZWISKO");
        surname2.setPreferredSize(dimension);
        surname2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        country2 = new JTextField();
        country2.setName("KRAJ POCHODZENIA");
        country2.setPreferredSize(dimension);
        country2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
        window.add(center, BorderLayout.CENTER);
        window.add(down, BorderLayout.SOUTH);
    }
    private boolean check(){
        error_counter = 0;
        fieldCheck(name2, 1, 20, true, false);
        fieldCheck(surname2, 1, 30, true, true);
        fieldCheck(country2, 0, 30, true, true);

        if(error_counter != 0){
            JOptionPane.showMessageDialog(window, message, "Błąd!", JOptionPane.ERROR_MESSAGE);
            message = "W następujących polach wykryto błędy: ";
        }
        if (error_counter == 0) return true;
        else return false;

    }
    private void fieldCheck(JTextField field, int min_size, int max_size, boolean digitsEnabled, boolean spaceEnabled){
        if(field.getText().length() < min_size || field.getText().length() > max_size){
            message += "\n" + field.getName();
            error_counter++;
        }
        else{
            if(digitsEnabled && spaceEnabled){
                for(int i = 0; i < field.getText().length(); i++){
                    if(!Character.isLetterOrDigit(field.getText().charAt(i)) && !Character.isSpaceChar(field.getText().charAt(i))){
                        message += "\n" + field.getName();
                        error_counter++;
                        break;
                    }
                }
            }
            else if(!digitsEnabled && spaceEnabled){
                for(int i = 0; i < field.getText().length(); i++){
                    if(!Character.isLetter(field.getText().charAt(i)) && !Character.isSpaceChar(field.getText().charAt(i))){
                        message += "\n" + field.getName();
                        error_counter++;
                        break;
                    }
                }
            }
            else if(digitsEnabled && !spaceEnabled){
                for(int i = 0; i < field.getText().length(); i++){
                    if(!Character.isLetterOrDigit(field.getText().charAt(i))){
                        message += "\n" + field.getName();
                        error_counter++;
                        break;
                    }
                }
            }
            else{
                for(int i = 0; i < field.getText().length(); i++){
                    if(!Character.isLetter(field.getText().charAt(i))){
                        message += "\n" + field.getName();
                        error_counter++;
                        break;
                    }
                }
            }
        }
    }
    private void exit(){
        window.setVisible(false);
        window.dispose();
    }
}
