package views.employee.publisher_panels;

import models.dataBaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class Add_publisher extends JFrame {
    private JFrame window;
    private JLabel name, country;
    private JTextField name2, country2;
    private JButton back, add;
    private JPanel center, down, name_pane, country_pane;
    private String user, message = "W następujących polach wykryto błędy: ";
    private Dimension dimension = new Dimension(250, 20);
    private dataBaseConnection dataBase = new dataBaseConnection();
    private int error_counter;

    public void create(String data){
        window = new JFrame("Dodaj wydawnictwo");
        settings();
        user = data;
        add_components();
        window.setVisible(true);
    }
    private void settings(){
        window.setSize(600, 300);
        window.setLocation(400, 80);
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
    }
    private void components(){
        name = new JLabel("Nazwa wydawnictwa (max 30 znaków): ");
        country = new JLabel("Kraj pochodzenia (max 30 znaków): ");
        back = new JButton("Powrót");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Publishers pb = new Publishers();
                exit();
                try {
                    pb.create(user);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        add = new JButton("Dodaj");
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //sprawdzenie i dodanie
                if(check()){
                    try {
                        dataBase.newPublisher(name2.getText(), country2.getText());
                        JOptionPane.showMessageDialog(window, "Wydawca dodany pomyślnie");
                        Publishers pb = new Publishers();
                        exit();
                        pb.create(user);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        });
        name2 = new JTextField();
        name2.setPreferredSize(dimension);
        name2.setName("NAZWA");
        name2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        country2 = new JTextField();
        country2.setPreferredSize(dimension);
        country2.setName("KRAJ POCHODZENIA");
        country2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
    private void panels(){
        components();
        name_pane = new JPanel();
        name_pane.add(name);
        name_pane.add(name2);
        country_pane = new JPanel();
        country_pane.add(country);
        country_pane.add(country2);

        center = new JPanel();
        center.add(name_pane);
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
        fieldCheck(name2, 1, 30, true, true);
        fieldCheck(country2, 0, 30, false, true);
        System.out.println(error_counter);

        if(error_counter != 0){
            JOptionPane.showMessageDialog(window, message, "Błąd!", JOptionPane.ERROR_MESSAGE);
            message = "W następujących polach wykryto błędy: ";
            return false;
        }
        else return true;
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
