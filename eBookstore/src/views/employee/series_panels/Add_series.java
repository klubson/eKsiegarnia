package views.employee.series_panels;

import models.WindowMethods;
import models.dataBaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Add_series {
    private WindowMethods windowMethods = new WindowMethods();
    private JButton back, add;
    private JLabel title, tomes;
    private JTextField title2, tomes2;
    private JPanel title_pane, tomes_pane, center, down;
    private dataBaseConnection dataBase = new dataBaseConnection();
    private String user, message = "W następujących polach wykryto błędy: ";
    private Dimension dimension = new Dimension(250, 20);
    private int error_counter;
    private  boolean isManager;

    public void create(String data, boolean mode){
        windowMethods.window = new JFrame("Dodaj serię książek");
        windowMethods.settings();
        windowMethods.window.setSize(600, 300);
        user = data;
        isManager = mode;
        add_components();
        windowMethods.window.setVisible(true);
    }
    private void components(){
        title = new JLabel("Tytuł serii (max 50 znaków): ");
        tomes = new JLabel("Liczba tomów: ");
        title2 = windowMethods.setJTextField(title2, "TYTUŁ SERII");
        tomes2 = windowMethods.setJTextField(tomes2, "LICZBA TOMÓW");
        back = new JButton("Powrót");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Series ss = new Series();
                windowMethods.exit();
                try {
                    ss.create(user, isManager);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        add = new JButton("Dodaj");
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(check()){
                    try {
                        dataBase.getConn().setAutoCommit(true);
                        dataBase.setStmt();
                        ResultSet rs = dataBase.getStmt().executeQuery(
                                "SELECT Tytul FROM Seria WHERE Tytul = '" + title2.getText() + "'"
                        );
                        if(rs.next()){
                            JOptionPane.showMessageDialog(windowMethods.window, "Seria o takim tytule już istnieje!", "Błąd", JOptionPane.ERROR_MESSAGE);
                            rs.close();
                            dataBase.getStmt().close();
                        }
                        else{
                            int tom;
                            if(tomes2.getText().equals("")) tom = 0;
                            else tom = Integer.parseInt(tomes2.getText());
                            int changes = dataBase.getStmt().executeUpdate(
                                    "INSERT INTO Seria VALUES('" + title2.getText() + "'," + tom + ")"
                            );
                            JOptionPane.showMessageDialog(windowMethods.window, "Seria dodana pomyślnie");
                            System.out.println("Dodano " + changes + "krotkę");
                            rs.close();
                            dataBase.getStmt().close();
                            Series ss = new Series();
                            windowMethods.exit();
                            ss.create(user, isManager);
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        });
    }
    private void panels(){
        components();
        title_pane = new JPanel();
        title_pane.add(title);
        title_pane.add(title2);

        tomes_pane = new JPanel();
        tomes_pane.add(tomes);
        tomes_pane.add(tomes2);

        center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
        center.add(title_pane);
        center.add(tomes_pane);

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
        fieldCheck(title2, 1, 50, true, true);
        numberCheck(tomes2, 0, 3);

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
    private void numberCheck(JTextField field, int min_size, int max_size){
        if(field.getText().length() < min_size || field.getText().length() > max_size){
            message += "\n" + field.getName();
            error_counter++;
        }
        for (int i = 0; i < field.getText().length(); i++) {
            if (!Character.isDigit(field.getText().charAt(i))) {
                message += "\n" + field.getName();
                error_counter++;
            }
        }
    }
}
