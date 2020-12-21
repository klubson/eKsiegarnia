package views.employee.author_panels;

import models.WindowMethods;
import models.dataBaseConnection;
import views.employee.manager.employee_panels.Employees;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Edit_author {
    private WindowMethods windowMethods = new WindowMethods();
    private JLabel name, surname, country;
    private JButton edit, back;
    private JTextField name2, surname2, country2;
    private JPanel center, down, name_pane, surname_pane, country_pane;
    private String user, message = "W następujących polach wykryto błędy: ";
    private int error_counter = 0, IDToEdit;
    private dataBaseConnection dataBase = new dataBaseConnection();
    private boolean isManager;

    public void create(String data, int ID, boolean mode) throws SQLException {
        windowMethods.window = new JFrame("Edytuj autora");
        windowMethods.settings();
        windowMethods.window.setSize(600, 300);
        user = data;
        IDToEdit = ID;
        isManager = mode;
        add_components();
        setAuthor(ID);
        windowMethods.window.setVisible(true);
    }
    private void setAuthor(int id) throws SQLException {
        dataBase.setStmt();
        ResultSet rs = dataBase.getStmt().executeQuery(
                "SELECT Imie, Nazwisko, Kraj_pochodzenia FROM Autor " +
                        "WHERE ID_Autora = " + id
        );
        rs.next();
        name2.setText(rs.getString(1));
        surname2.setText(rs.getString(2));
        country2.setText(rs.getString(3));
        rs.close();
        dataBase.getStmt().close();
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
        edit = new JButton("Edytuj");
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(check()){
                    try {
                        dataBase.setStmt();
                        dataBase.getConn().setAutoCommit(true);
                        int changes = dataBase.getStmt().executeUpdate(
                                "UPDATE Autor SET Imie = '" + name2.getText() +
                                        "', Nazwisko = '" + surname2.getText() +
                                        "', Kraj_pochodzenia = '" + country2.getText() +
                                        "' WHERE ID_Autora = " + IDToEdit
                        );
                        JOptionPane.showMessageDialog(windowMethods.window, "Autor " + name2.getText() + " " + surname2.getText() + " edytowany pomyślnie");
                        System.out.println("Zaktualizowano " + changes + " rekord");
                        dataBase.getStmt().close();
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
        down.add(edit, BorderLayout.EAST);
    }
    private void add_components(){
        panels();
        windowMethods.window.add(center, BorderLayout.CENTER);
        windowMethods.window.add(down, BorderLayout.SOUTH);
    }
    private boolean check(){
        error_counter = 0;
        fieldCheck(name2, 1, 20, true, false);
        fieldCheck(surname2, 1, 30, true, true);
        fieldCheck(country2, 0, 30, true, true);

        if(error_counter != 0){
            JOptionPane.showMessageDialog(windowMethods.window, message, "Błąd!", JOptionPane.ERROR_MESSAGE);
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
}
