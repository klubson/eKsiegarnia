package views;
import models.dataBaseConnection;
import models.Awt1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class Reg_panel extends JFrame {
    private JFrame window;
    private JLabel login, pass, pass_again, name, surname, phone, e_mail, address;
    private JTextField login2, name2, surname2, phone2, e_mail2, address2;
    private JPasswordField pass2, pass_again2;
    private JCheckBox pass_box, pass_box2;
    private JButton back, sign_up;
    private JPanel center, down, login_pane, pass_pane, pass_pane2, name_pane, surname_pane, phone_pane, e_mail_pane, address_pane;
    private int dim_wdt = 250, dim_ht = 20, error_counter;
    private Dimension dimension = new Dimension(250, 20);
    private String message = "W następujących polach wykryto błędy: ";
    private boolean phone_correctness;
    private dataBaseConnection dataBase = new dataBaseConnection();
//    private JLabel[] tab_JLabel = {login, pass, pass_again, name, surname, phone, e_mail, address};
//    private JPanel[] tab_JPanel = {login_pane, pass_pane, pass_pane2, name_pane, surname_pane, phone_pane, e_mail_pane, address_pane};
//    private JTextField[] tab_JTextField = {login2, name2, surname2, phone2, e_mail2, address2};
//    private JPasswordField[] tab_JPassField = {pass2, pass_again2};
//    private String[] tab_JLabelNames = {"Wybierz swój login (max 30 znaków): ", "Wybierz swoje hasło (max 20 znaków): ", "Powtórz hasło: ", "Imię (max 20 znaków): ", "Nazwisko (max 30 znaków): ", "Numer telefonu: ", "Adres e-mail (max 30 znaków): ", "Adres zamieszkania (max 50 znaków): "};
//    private String[] tab_JTextFieldNames = {"LOGIN", "IMIĘ", "NAZWISKO", "NR TELEFONU", "ADRES E-MAIL", "ADRES ZAMIESZKANIA"};
//    private String[] tab_JPassFieldNames = {"HASŁO", "POWTÓRZ HASŁO"};

    public void create(){
        window = new JFrame("Rejestracja");
        settings();
        add_components();
        window.setVisible(true);
    }

    private void settings(){
        window.setSize(600, 600);
        window.setLocation(400, 80);
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
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
//        for (int i = 0; i < tab_JLabel.length; i++){
//            tab_JLabel[i] = new JLabel(tab_JLabelNames[i]);
//        }
    }
    private void components(){
        back = new JButton("Powrót");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
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
                            JOptionPane.showMessageDialog(window, "Użytkownik o podanym loginie już istnieje!",
                                    "Błąd rejestracji!", JOptionPane.ERROR_MESSAGE);
                            rs.close();
                        }
                        else {
                            rs.close();
                            rs = dataBase.getStmt().executeQuery(
                                    "SELECT k_Adres_e_mail FROM Klient WHERE k_Adres_e_mail = " + "'" +e_mail2.getText() + "'"
                            );
                            if(rs.next()){
                                JOptionPane.showMessageDialog(window, "Użytkownik o podanym adresie " +
                                        "e-mail już istnieje!", "Błąd rejestracji!", JOptionPane.ERROR_MESSAGE);
                                rs.close();
                            }
                            else{
                                String tmp = String.copyValueOf(pass2.getPassword());
                                dataBase.newClient(login2.getText(), tmp, name2.getText(), surname2.getText(), phone2.getText(), e_mail2.getText(), address2.getText());
                                JOptionPane.showMessageDialog(window, "Rejestracja przebiegła pomyślnie!");
                                //przejście do okna logowania
                                Log_panel win = new Log_panel();
                                exit();
                                win.create();
                            }
                        }


                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }


                }
            }
        });
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

        login2 = new JTextField();
        login2.setName("LOGIN");
        login2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        login2.setPreferredSize(new Dimension(dim_wdt, dim_ht));
        pass2 = new JPasswordField();
        pass2.setName("HASŁO");
        pass2.setEchoChar('\u25CF');
        pass2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        pass2.setPreferredSize(new Dimension(dim_wdt, dim_ht));
        pass_again2 = new JPasswordField();
        pass_again2.setName("POWTÓRZ HASŁO");
        pass_again2.setEchoChar('\u25CF');
        pass_again2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        pass_again2.setPreferredSize(new Dimension(dim_wdt, dim_ht));
        name2 = new JTextField();
        name2.setName("IMIĘ");
        name2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        name2.setPreferredSize(new Dimension(dim_wdt, dim_ht));
        surname2 = new JTextField();
        surname2.setName("NAZWISKO");
        surname2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        surname2.setPreferredSize(new Dimension(dim_wdt, dim_ht));
        phone2 = new JTextField();
        phone2.setName("NUMER TELEFONU");
        phone2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        phone2.setPreferredSize(new Dimension(dim_wdt, dim_ht));
        e_mail2 = new JTextField();
        e_mail2.setName("ADRES E-MAIL");
        e_mail2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        e_mail2.setPreferredSize(new Dimension(dim_wdt, dim_ht));
        address2 = new JTextField();
        address2.setName("ADRES ZAMIESZKANIA");
        address2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        address2.setPreferredSize(new Dimension(dim_wdt, dim_ht));
//        for (int i = 0; i < tab_JTextField.length; i++){
//            tab_JTextField[i] = new JTextField();
//            tab_JTextField[i].setName(tab_JTextFieldNames[i]);
//            tab_JTextField[i].addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//
//                }
//            });
//            tab_JTextField[i].setPreferredSize(dimension);
//        }
//        for (int i = 0; i < tab_JPassField.length; i++){
//            tab_JPassField[i] = new JPasswordField();
//            tab_JPassField[i].setName(tab_JPassFieldNames[i]);
//            tab_JPassField[i].setEchoChar('\u25CF');
//            tab_JPassField[i].addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//
//                }
//            });
//            tab_JPassField[i].setPreferredSize(dimension);
//        }
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

//        for (int i = 0; i < tab_JPanel.length; i++){
//            tab_JPanel[i] = new JPanel();
//            tab_JPanel[i].add(tab_JLabel[i]);
//            if(i == 1 || i == 2){
//                tab_JPanel[i].add(tab_JPassField[i-1]);
//            }
//            else if (i == 0){
//                tab_JPanel[i].add(tab_JTextField[i]);
//            }
//            else{
//                tab_JPanel[i].add(tab_JTextField[i-2]);
//            }
//        }

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
//        for (int i = 0; i < tab_JPanel.length; i++){
//            center.add(tab_JPanel[i]);
//        }

        down = new JPanel();
        down.setLayout(new BorderLayout());
        down.add(back, BorderLayout.WEST);
        down.add(sign_up, BorderLayout.EAST);
    }
    private void add_components(){
        panels();
        window.add(center, BorderLayout.CENTER);
        window.add(down, BorderLayout.SOUTH);
    }
    private void exit(){
        window.setVisible(false);
        window.dispose();
    }
    private void fieldCheck(JTextField field, int size, boolean digitsEnabled, boolean spaceEnabled){
        if(field.getText().length() == 0 || field.getText().length() > size){
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
        String tmp = String.copyValueOf(field.getPassword());
        if(tmp.length() == 0 || tmp.length() > size){
            message += "\n" + field.getName();
            error_counter++;
        }
        else{
            for(int i = 0; i < tmp.length(); i++){
                if(!Character.isLetterOrDigit(tmp.charAt(i))){
                    message += "\n" + field.getName();
                    error_counter++;
                    break;
                }
            }
        }
    }
    private boolean samePass(){
        if (Arrays.toString(pass2.getPassword()).equals(Arrays.toString(pass_again2.getPassword()))) return true;
        else return false;
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
    private boolean check(){
        error_counter = 0;
        fieldCheck(login2, 30, true, true);
        passFieldCheck(pass2, 20);
        passFieldCheck(pass_again2, 20);
        fieldCheck(name2, 20, false, true);
        fieldCheck(surname2, 20, false, true);
        boolean number = phoneCheck(phone2);
        fieldCheck(e_mail2, 30, true, false);
        fieldCheck(address2, 50, true, true);
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

}
