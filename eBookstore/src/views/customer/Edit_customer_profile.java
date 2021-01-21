package views.customer;

import models.CartInfo;
import models.DataVerification;
import models.WindowMethods;
import models.dataBaseConnection;
import views.employee.manager.Manager_panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Edit_customer_profile {
    private WindowMethods windowMethods = new WindowMethods();
    private JLabel login, pass, pass_again, name, surname, phone, email, address;
    private JPasswordField pass2, pass_again2;
    private JTextField login2, name2, surname2, phone2, email2, address2;
    private JButton back, edit;
    private JCheckBox pass_box, pass_box2;
    private JPanel center, down, login_pane, pass_pane,pass_again_pane, name_pane, surname_pane, phone_pane,email_pane, address_pane;
    private dataBaseConnection dataBase = new dataBaseConnection();
    private Connection conn;
    private Statement stmt ;
    private String user;
    private dataBaseConnection tempDataBase;
    private CartInfo cartInfo;

    public void create(String data , dataBaseConnection dataBase , CartInfo cartInfo) throws SQLException {
        windowMethods.window = new JFrame("Edytuj profil");
        windowMethods.settings();
        Properties connectionProps = new Properties();
        connectionProps.put("user", "inf141246");
        connectionProps.put("password", "inf141246");
        String connectionString = "jdbc:oracle:thin:@//admlab2.cs.put.poznan.pl:1521/"+
                "dblab02_students.cs.put.poznan.pl";
        try {
            conn = DriverManager.getConnection(connectionString,
                    connectionProps);
            //System.out.println("Połączono z bazą danych");
        } catch (SQLException ex) {
            Logger.getLogger(dataBaseConnection.class.getName()).log(Level.SEVERE,
                    "Nie udało się połączyć z bazą danych", ex);
            //System.exit(-1);
        }
        user = data;
        tempDataBase = dataBase;
        this.cartInfo = cartInfo;
        add_components();
        setData(user);



        windowMethods.window.setVisible(true);

    }
    private void setData(String data) throws SQLException {
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
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
        rs = stmt.executeQuery(
                "SELECT k_Adres_e_mail, k_Adres FROM Klient WHERE Login = '" + user + "'"
        );
        rs.next();
        email2.setText(rs.getString(1));
        address2.setText(rs.getString(2));
        stmt.close();
        rs.close();
        stmt.close();
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
                    cp.createFromBack(user,tempDataBase,cartInfo);
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
                        int type = findLoggedUser(login2.getText(), user);
                        if(type == 0){
                            JOptionPane.showMessageDialog(windowMethods.window, "Podany login jest już zajęty! Wybierz inny login!", "Błąd", JOptionPane.ERROR_MESSAGE);
                        }
                        else{
                            stmt = conn.createStatement();
                            String tmp = String.copyValueOf(pass2.getPassword());
                            int changes;
                            if(type == 1){
                                changes = stmt.executeUpdate(
                                        "UPDATE Uzytkownik SET Login = '" + login2.getText() + "', Haslo = '"
                                                + tmp + "', Imie = '" + name2.getText() + "', Nazwisko = '" + surname2.getText() + "'," +
                                                "Nr_kontaktowy = '" + phone2.getText() + "' WHERE Login = '" + user + "'"
                                );
                                System.out.println("Edytowano " + changes + " rekord");
                                changes = stmt.executeUpdate(
                                        "UPDATE Klient SET k_adres_e_mail = '" + email2.getText() + "', k_adres = '"
                                                + address2.getText() + "' WHERE Login = '" + user + "'"
                                );
                                System.out.println("Edytowano " + changes + " rekord");
                            }
                            else if(type == 2){//to miała być obsługa edycji profilu z zmianą loginu ala nie działa :(
                                conn.setAutoCommit(false);
                                changes = stmt.executeUpdate(
                                        "UPDATE Uzytkownik SET Login = '" + login2.getText() + "', Haslo = '"
                                                + tmp + "', Imie = '" + name2.getText() + "', Nazwisko = '" + surname2.getText() + "'," +
                                                "Nr_kontaktowy = '" + phone2.getText() + "' WHERE Login = '" + user + "'"
                                );
                                System.out.println("Edytowano " + changes + " rekord");
                                changes = stmt.executeUpdate(
                                        "UPDATE Klient SET Login = '" + login2.getText() + "', k_adres_e_mail = '" + email2.getText() + "', k_adres = '"
                                                + address2.getText() + "' WHERE Login = '" + user + "'"
                                );
                                System.out.println("Edytowano " + changes + " rekord");

                                changes = stmt.executeUpdate(
                                        "UPDATE koszyk_zakupowy SET Klient_Login = '" + login2.getText() + "' WHERE Klient_Login = '" + user + "'"
                                );
                                System.out.println("Edytowano " + changes + " rekordów");
                                conn.commit();
                                conn.setAutoCommit(true);


                                user = login2.getText();
                            }


                            stmt.close();
                            JOptionPane.showMessageDialog(windowMethods.window, "Klient " + name2.getText() + " " +
                                    surname2.getText() + " edytowany pomyślnie!");
                            Customer_panel cp = new Customer_panel();
                            windowMethods.exit();
                            cp.createFromBack(user,tempDataBase,cartInfo);
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
        verify.passFieldCheck(pass_again2, 20);
        verify.fieldCheck(name2, 1, 20, false, true);
        verify.fieldCheck(surname2, 1 ,20, false, true);
        verify.phoneCheck(phone2);
        verify.emailCheck(email2, 30);
        verify.addressCheck(address2, 50);
        verify.errorPhone(verify.phone_correctness, phone2, windowMethods.window);
        verify.samePass(pass2, pass_again2);
        verify.errorMessage();
        verify.errorPass(verify.pass_correctness, pass2, pass_again2);
        verify.errorAddress();
        if(verify.phone_correctness && verify.pass_correctness && verify.address_correctness && verify.error_counter == 0) return true;
        else return false;
    }

    private int findLoggedUser(String login , String user) throws SQLException {
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT Login FROM Uzytkownik WHERE Login = " + "'" + login + "'");
        if(rs.next()) {
            //System.out.println("Znaleziono login: " + rs.getString(1));
            if(rs.getString(1).equals(user)){
                //System.out.println("taki sam login");
                rs.close();
                stmt.close();
                return 1;
            }
            else{
                rs.close();
                stmt.close();
                return 0;
            }
        }
        else {
            System.out.println("nie znaleziono");
            rs.close();
            stmt.close();
            return 2;
        }
    }
}
