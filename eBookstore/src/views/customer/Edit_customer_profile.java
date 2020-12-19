package views.customer;

import models.dataBaseConnection;
import views.employee.manager.Manager_panel;
import views.employee.supplier.Employee_panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class Edit_customer_profile extends JFrame {
    private JFrame window;
    private JLabel login, pass, pass_again, name, surname, phone, email, address;
    private JPasswordField pass2, pass_again2;
    private JTextField login2, name2, surname2, phone2, email2, address2;
    private JButton back, edit;
    private int error_counter;
    private Dimension dimension = new Dimension(250, 20);
    private JCheckBox pass_box, pass_box2;
    private JPanel center, down, login_pane, pass_pane,pass_again_pane, name_pane, surname_pane, phone_pane;
    private dataBaseConnection dataBase = new dataBaseConnection();
    private boolean phone_correctness;
    private String message = "W następujących polach wykryto błędy: ", user;

    public void create(String data) throws SQLException {
        window = new JFrame("Edytuj profil");
        settings();
        user = data;
        add_components();
        setData(user);
        window.setVisible(true);
    }
    private void settings(){
        window.setSize(600, 600);
        window.setLocation(400, 80);
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
    }
    private void setData(String data) throws SQLException {
        dataBase.setStmt();
        ResultSet rs = dataBase.getStmt().executeQuery(
                "SELECT Login, Haslo, Imie, Nazwisko, Nr_kontaktowy FROM Uzytkownik WHERE Login = '" + user + "'"
        );
        rs.next();
        login2.setText(rs.getString(1));
        pass2.setText(rs.getString(2));
        pass_again2.setText(rs.getString(2));
        name2.setText(rs.getString(3));
        surname2.setText(rs.getString(4));
        phone2.setText(rs.getString(5));
        rs.close();
        rs = dataBase.getStmt().executeQuery(
                "SELECT k_Adres_e_mail, k_Adres FROM Klient WHERE Login = '" + user + "'"
        );
        rs.next();
        email2.setText(rs.getString(1));
        address2.setText(rs.getString(2));
        dataBase.getStmt().close();
        rs.close();
        dataBase.getStmt().close();
    }
    private void labels(){
        login = new JLabel("Wybierz swój login (max 30 znaków): ");
        pass = new JLabel("Wybierz swoje hasło (max 20 znaków): ");
        pass_again = new JLabel("Wpisz hasło ponownie: ");
        name = new JLabel("Imię (max 20 znaków): ");
        surname = new JLabel("Nazwisko (max 30 znaków): ");
        phone = new JLabel("Numer telefonu: ");
        email = new JLabel("Adres e-mail (max 30 znaków): ");
        address = new JLabel("Adres (max 50 znaków): ");
    }
    private void components(){
        back = new JButton("Powrót");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Customer_panel cp = new Customer_panel();
                exit();
                try {
                    cp.create(user);
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
                        dataBase.getConn().setAutoCommit(true);
                        dataBase.setStmt();
                        String tmp = String.copyValueOf(pass2.getPassword());
                        int changes = dataBase.getStmt().executeUpdate(
                                "UPDATE Uzytkownik SET Login = '" + login2.getText() + "', Haslo = '"
                                        + tmp + "', Imie = '" + name2.getText() + "', Nazwisko = '" + surname2.getText() + "'," +
                                        "Nr_kontaktowy = '" + phone2.getText() + "' WHERE Login = '" + user + "'"
                        );
                        JOptionPane.showMessageDialog(window, "Pracownik " + name2.getText() + " " +
                                surname2.getText() + " edytowany pomyślnie!");
                        Manager_panel mp = new Manager_panel();
                        exit();
                        mp.create(user);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        });
        login2 = new JTextField();
        login2.setName("LOGIN");
        login2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        login2.setPreferredSize(dimension);
        pass2 = new JPasswordField();
        pass2.setName("HASŁO");
        pass2.setEchoChar('\u25CF');
        pass2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        pass2.setPreferredSize(dimension);
        pass_again2 = new JPasswordField();
        pass_again2.setName("POWTÓRZ HASŁO");
        pass_again2.setEchoChar('\u25CF');
        pass_again2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        pass_again2.setPreferredSize(dimension);
        name2 = new JTextField();
        name2.setName("IMIĘ");
        name2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        name2.setPreferredSize(dimension);
        surname2 = new JTextField();
        surname2.setName("NAZWISKO");
        surname2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        surname2.setPreferredSize(dimension);
        phone2 = new JTextField();
        phone2.setName("NUMER TELEFONU");
        phone2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        phone2.setPreferredSize(dimension);
        email2 = new JTextField();
        email2.setName("ADRES E-MAIL");
        email2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        email2.setPreferredSize(dimension);
        address2 = new JTextField();
        address2.setName("ADRES ZAMIESZKANIA");
        address2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        address2.setPreferredSize(dimension);
        pass_box = new JCheckBox("Pokaż hasło");
        pass_box.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(pass_box.isSelected()){
                    pass2.setEchoChar((char)0);
                }
                else pass2.setEchoChar('\u25CF');
            }
        });
        pass_box2 = new JCheckBox("Pokaż hasło");
        pass_box2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(pass_box2.isSelected()){
                    pass_again2.setEchoChar((char)0);
                }
                else pass_again2.setEchoChar('\u25CF');
            }
        });
    }
    private void panels(){
        labels();
        components();
        center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
        login_pane = new JPanel();
        login_pane.add(login);
        login_pane.add(login2);
        pass_pane = new JPanel();
        pass_pane.add(pass);
        pass_pane.add(pass2);
        pass_pane.add(pass_box);
        pass_again_pane = new JPanel();
        pass_again_pane.add(pass_again);
        pass_again_pane.add(pass_again2);
        pass_again_pane.add(pass_box2);
        name_pane = new JPanel();
        name_pane.add(name);
        name_pane.add(name2);
        surname_pane = new JPanel();
        surname_pane.add(surname);
        surname_pane.add(surname2);
        phone_pane = new JPanel();
        phone_pane.add(phone);
        phone_pane.add(phone2);

        center.add(login_pane);
        center.add(pass_pane);
        center.add(pass_again_pane);
        center.add(name_pane);
        center.add(surname_pane);
        center.add(phone_pane);

        down = new JPanel();
        down.setLayout(new BorderLayout());
        down.add(back, BorderLayout.WEST);
        down.add(edit, BorderLayout.EAST);
    }
    private void add_components(){
        panels();
        window.add(center, BorderLayout.CENTER);
        window.add(down, BorderLayout.SOUTH);
    }
    private boolean check(){
        error_counter = 0;
        fieldCheck(login2, 1, 30, true, false);
        passFieldCheck(pass2, 20);
        fieldCheck(name2, 1, 20, true, true);
        fieldCheck(surname2, 1, 30, true, true);
        boolean number = phoneCheck(phone2);
        boolean correct_passwords = samePass();
        if(!number) {
            JOptionPane.showMessageDialog(window, "Numer telefonu musi składać się z cyfr!",
                    "Nieprawidłowy numer telefonu!", JOptionPane.ERROR_MESSAGE);
            phone2.setText("");
        }
        if(!correct_passwords){
            JOptionPane.showMessageDialog(window, "Hasła nie są identyczne!",
                    "Błąd hasła!", JOptionPane.ERROR_MESSAGE);
            pass2.setText("");
            pass_again2.setText("");
        }
        if(error_counter != 0){
            JOptionPane.showMessageDialog(window, message, "Błąd!", JOptionPane.ERROR_MESSAGE);
            message = "W następujących polach wykryto błędy: ";
        }
        if (number && correct_passwords && error_counter == 0) return true;
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
    private void passFieldCheck(JPasswordField field, int size){
        if(field.getPassword().length == 0 || field.getPassword().length > size){
            message += "\n" + field.getName();
            error_counter++;
        }
    }
    private boolean phoneCheck(JTextField field) {
        phone_correctness = true;
        if(field.getText().length() != 9){
            if(field.getText().length() != 0) return false;
        }
        for (int i = 0; i < field.getText().length(); i++) {
            if (!Character.isDigit(field.getText().charAt(i))) {
                phone_correctness = false;
            }
        }
        return phone_correctness;
    }
    private boolean samePass(){
        if (Arrays.toString(pass2.getPassword()).equals(Arrays.toString(pass_again2.getPassword()))) return true;
        else return false;
    }
    private void exit(){
        window.setVisible(false);
        window.dispose();
    }
}
