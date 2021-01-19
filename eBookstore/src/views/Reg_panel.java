package views;
import models.DataVerification;
import models.WindowMethods;
import models.dataBaseConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class Reg_panel extends JFrame {
    private JLabel login, pass, pass_again, name, surname, phone, e_mail, address;
    private JTextField login2, name2, surname2, phone2, e_mail2, address2;
    private JPasswordField pass2, pass_again2;
    private JCheckBox pass_box, pass_box2;
    private JButton back, sign_up;
    private JPanel center, down, login_pane, pass_pane, pass_pane2, name_pane, surname_pane, phone_pane, e_mail_pane, address_pane;
    private dataBaseConnection dataBase = new dataBaseConnection();
    private WindowMethods windowMethods = new WindowMethods();

    public void create(){
        windowMethods.window = new JFrame("Rejestracja");
        windowMethods.settings();
        add_components();
        windowMethods.window.setVisible(true);
    }
    private void labels(){
        login = new JLabel("Wybierz swój login (max 30 znaków): ");
        pass = new JLabel("Wybierz swoje hasło (max 20 znaków): ");
        pass_again = new JLabel("Powtórz hasło: ");
        name = new JLabel("Imię (max 20 znaków): ");
        surname = new JLabel("Nazwisko (max 30 znaków): ");
        phone = new JLabel("Numer telefonu: ");
        e_mail = new JLabel("Adres e-mail (max 30 znaków): ");
        address = new JLabel("Adres zamieszkania (max 50 znaków): ");
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
        e_mail2 = windowMethods.setJTextField(e_mail2, "ADRES E-MAIL");
        address2 = windowMethods.setJTextField(address2, "ADRES ZAMIESZKANIA");
        back = new JButton("Powrót");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                windowMethods.exit();
                try {
                    Start_window win2 = new Start_window();
                    win2.create();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
    });
        sign_up = new JButton("Zarejestruj się");
        sign_up.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (check()){
                    try {
                        dataBase.setStmt();
                        dataBase.getConn().setAutoCommit(true);
                        ResultSet rs = dataBase.getStmt().executeQuery(
                                "SELECT Login FROM Uzytkownik WHERE Login = " + "'" +login2.getText() + "'"
                        );

                        //if (dataBase.findUser(login2.getText())){
                        if(rs.next()){
                            JOptionPane.showMessageDialog(windowMethods.window, "Użytkownik o podanym loginie już istnieje!",
                                    "Błąd rejestracji!", JOptionPane.ERROR_MESSAGE);
                            rs.close();
                        }
                        else {
                            rs.close();
                            rs = dataBase.getStmt().executeQuery(
                                    "SELECT k_Adres_e_mail FROM Klient WHERE k_Adres_e_mail = " + "'" +e_mail2.getText() + "'"
                            );
                            if(rs.next()){
                                JOptionPane.showMessageDialog(windowMethods.window, "Użytkownik o podanym adresie " +
                                        "e-mail już istnieje!", "Błąd rejestracji!", JOptionPane.ERROR_MESSAGE);
                                rs.close();
                            }
                            else{
                                String tmp = String.copyValueOf(pass2.getPassword());
                                dataBase.newClient(login2.getText(), tmp, name2.getText(), surname2.getText(), phone2.getText(), e_mail2.getText(), address2.getText());
                                JOptionPane.showMessageDialog(windowMethods.window, "Rejestracja przebiegła pomyślnie!");
                                //przejście do okna logowania
                                var win = new Log_panel();
                                windowMethods.exit();
                                win.create();
                            }
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
        login_pane = new JPanel();
        login_pane.add(login);
        login_pane.add(login2);
        pass_pane = new JPanel();
        pass_pane.add(pass);
        pass_pane.add(pass2);
        pass_pane.add(pass_box);
        pass_pane2 = new JPanel();
        pass_pane2.add(pass_again);
        pass_pane2.add(pass_again2);
        pass_pane2.add(pass_box2);
        name_pane = new JPanel();
        name_pane.add(name);
        name_pane.add(name2);
        surname_pane = new JPanel();
        surname_pane.add(surname);
        surname_pane.add(surname2);
        phone_pane = new JPanel();
        phone_pane.add(phone);
        phone_pane.add(phone2);
        e_mail_pane = new JPanel();
        e_mail_pane.add(e_mail);
        e_mail_pane.add(e_mail2);
        address_pane = new JPanel();
        address_pane.add(address);
        address_pane.add(address2);

        center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
        center.add(login_pane);
        center.add(pass_pane);
        center.add(pass_pane2);
        center.add(name_pane);
        center.add(surname_pane);
        center.add(phone_pane);
        center.add(e_mail_pane);
        center.add(address_pane);

        down = new JPanel();
        down.setLayout(new BorderLayout());
        down.add(back, BorderLayout.WEST);
        down.add(sign_up, BorderLayout.EAST);
    }
    private void add_components(){
        panels();
        windowMethods.window.add(center, BorderLayout.CENTER);
        windowMethods.window.add(down, BorderLayout.SOUTH);
    }
    private boolean check(){
        DataVerification verify = new DataVerification();
        verify.fieldCheck(login2, 1, 30, true, true);
        verify.passFieldCheck(pass2, 20);
        verify.passFieldCheck(pass_again2, 20);
        verify.fieldCheck(name2, 1, 20, false, true);
        verify.fieldCheck(surname2, 1,20, false, true);
        verify.phoneCheck(phone2);
        verify.emailCheck(e_mail2, 30);
        //verify.fieldCheck(address2,1, 50, true, true);
        verify.addressCheck(address2, 50);
        verify.errorPhone(verify.phone_correctness, phone2, windowMethods.window);
        verify.samePass(pass2, pass_again2);
        verify.errorMessage();
        verify.errorPass(verify.pass_correctness, pass2, pass_again2);
        verify.errorAddress();
        if(verify.phone_correctness && verify.pass_correctness && verify.address_correctness && verify.error_counter == 0) return true;
        else return false;
    }
}
