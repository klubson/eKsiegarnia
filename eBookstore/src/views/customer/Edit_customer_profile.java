package views.customer;

import models.DataVerification;
import models.WindowMethods;
import models.dataBaseConnection;
import views.employee.manager.Manager_panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Edit_customer_profile {
    private WindowMethods windowMethods = new WindowMethods();
    private JLabel login, pass, pass_again, name, surname, phone, email, address;
    private JPasswordField pass2, pass_again2;
    private JTextField login2, name2, surname2, phone2, email2, address2;
    private JButton back, edit;
    private JCheckBox pass_box, pass_box2;
    private JPanel center, down, login_pane, pass_pane,pass_again_pane, name_pane, surname_pane, phone_pane,email_pane, address_pane;
    private dataBaseConnection dataBase = new dataBaseConnection();
    private String user;

    public void create(String data) throws SQLException {
        windowMethods.window = new JFrame("Edytuj profil");
        windowMethods.settings();
        user = data;
        add_components();
        setData(user);
        windowMethods.window.setVisible(true);
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
        login2 = windowMethods.setJTextField(login2, "LOGIN");
        pass2 = windowMethods.setJPasswordField(pass2, "HASŁO");
        pass_box = windowMethods.setPassCheckBox(pass_box, pass2, "Pokaż hasło");
        pass_again2 = windowMethods.setJPasswordField(pass_again2, "POWTÓRZ HASŁO");
        pass_box2 = windowMethods.setPassCheckBox(pass_box2, pass_again2, "Pokaż hasło");
        name2 = windowMethods.setJTextField(name2, "IMIĘ");
        surname2 = windowMethods.setJTextField(surname2, "NAZWISKO");
        phone2 = windowMethods.setJTextField(phone2, "NUMER TELEFONU");
        email2 = windowMethods.setJTextField(email2, "ADRES E-MAIL");
        address2 = windowMethods.setJTextField(address2, "ADRES ZAMIESZKANIA");

        back = new JButton("Powrót");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Customer_panel cp = new Customer_panel();
                windowMethods.exit();
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
                        if(!dataBase.findLoggedUser(login2.getText(), user)){
                            JOptionPane.showMessageDialog(windowMethods.window, "Podany login jest już zajęty! Wybierz inny login!", "Błąd", JOptionPane.ERROR_MESSAGE);
                        }
                        else{
                            dataBase.setStmt();
                            String tmp = String.copyValueOf(pass2.getPassword());
                            int changes = dataBase.getStmt().executeUpdate(
                                    "UPDATE Uzytkownik SET Login = '" + login2.getText() + "', Haslo = '"
                                            + tmp + "', Imie = '" + name2.getText() + "', Nazwisko = '" + surname2.getText() + "'," +
                                            "Nr_kontaktowy = '" + phone2.getText() + "' WHERE Login = '" + user + "'"
                            );
                            System.out.println("Edytowano " + changes + " rekord");
                            dataBase.getStmt().close();
                            JOptionPane.showMessageDialog(windowMethods.window, "Klient " + name2.getText() + " " +
                                    surname2.getText() + " edytowany pomyślnie!");
                            Manager_panel mp = new Manager_panel();
                            windowMethods.exit();
                            mp.create(user);
                        }
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
        email_pane = new JPanel();
        email_pane.add(email);
        email_pane.add(email2);
        address_pane = new JPanel();
        address_pane.add(address);
        address_pane.add(address2);

        center.add(login_pane);
        center.add(pass_pane);
        center.add(pass_again_pane);
        center.add(name_pane);
        center.add(surname_pane);
        center.add(phone_pane);
        center.add(email_pane);
        center.add(address_pane);

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
        DataVerification verify = new DataVerification();
        verify.fieldCheck(login2, 1, 30, true, false);
        verify.passFieldCheck(pass2, 20);
        verify.fieldCheck(name2, 1, 20, false, true);
        verify.fieldCheck(surname2, 1 ,20, false, true);
        verify.phoneCheck(phone2);
        verify.samePass(pass2, pass_again2);
        verify.errorPhone(verify.phone_correctness, phone2, windowMethods.window);
        verify.errorPass(verify.pass_correctness, pass2, pass_again2);
        verify.errorMessage();
        if(verify.phone_correctness && verify.pass_correctness && verify.error_counter == 0) return true;
        else return false;
    }
}
