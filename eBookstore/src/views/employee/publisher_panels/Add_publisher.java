package views.employee.publisher_panels;

import models.WindowMethods;
import models.dataBaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Add_publisher {
    private WindowMethods windowMethods = new WindowMethods();
    private JLabel name, country;
    private JTextField name2, country2;
    private JButton back, add;
    private JPanel center, down, name_pane, country_pane;
    private String user, message = "W następujących polach wykryto błędy: ";
    private dataBaseConnection dataBase = new dataBaseConnection();
    private int error_counter;
    private boolean isManager;

    public void create(String data, boolean mode){
        windowMethods.window = new JFrame("Dodaj wydawnictwo");
        windowMethods.settings();
        windowMethods.window.setSize(600, 300);
        user = data;
        isManager = mode;
        add_components();
        windowMethods.window.setVisible(true);
    }
    private void components(){
        name = new JLabel("Nazwa wydawnictwa (max 30 znaków): ");
        country = new JLabel("Kraj pochodzenia (max 30 znaków): ");
        name2 = windowMethods.setJTextField(name2, "NAZWA");
        country2 = windowMethods.setJTextField(country2, "KRAJ POCHODZENIA");
        back = new JButton("Powrót");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Publishers pb = new Publishers();
                windowMethods.exit();
                try {
                    pb.create(user, isManager);
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
                        JOptionPane.showMessageDialog(windowMethods.window, "Wydawca dodany pomyślnie");
                        Publishers pb = new Publishers();
                        windowMethods.exit();
                        pb.create(user, isManager);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
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
        windowMethods.window.add(center, BorderLayout.CENTER);
        windowMethods.window.add(down, BorderLayout.SOUTH);
    }
    private boolean check(){
        error_counter = 0;
        fieldCheck(name2, 1, 30, true, true);
        fieldCheck(country2, 0, 30, false, true);
        System.out.println(error_counter);

        if(error_counter != 0){
            JOptionPane.showMessageDialog(windowMethods.window, message, "Błąd!", JOptionPane.ERROR_MESSAGE);
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
}
