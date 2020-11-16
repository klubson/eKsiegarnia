package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Reg_panel extends JFrame {
    private JFrame window;
    private JLabel login, pass, pass_again, name, surname, phone, e_mail, address;
    private JTextField login2, name2, surname2, phone2, e_mail2, address2;
    private JPasswordField pass2, pass_again2;
    private JCheckBox pass_box, pass_box2;
    private JButton back, sign_up;
    private JPanel center, down, login_pane, pass_pane, pass_pane2, name_pane, surname_pane, phone_pane, e_mail_pane, address_pane;
    private int dim_wdt = 250, dim_ht = 20;

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
        login = new JLabel("Wybierz swój login: ");
        pass = new JLabel("Wybierz swoje hasło: ");
        pass_again = new JLabel("Powtórz hasło: ");
        name = new JLabel("Imię: ");
        surname = new JLabel("Nazwisko: ");
        phone = new JLabel("Numer telefonu: ");
        e_mail = new JLabel("Adres e-mail: ");
        address = new JLabel("Adres zamieszkania: ");
    }
    private void components(){
        back = new JButton("Powrót");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
                Start_window win2 = new Start_window();
                win2.create();
        }
    });
        sign_up = new JButton("Zarejestruj się");
        sign_up.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
        login2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        login2.setPreferredSize(new Dimension(dim_wdt, dim_ht));
        pass2 = new JPasswordField();
        pass2.setEchoChar('\u25CF');
        pass2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        pass2.setPreferredSize(new Dimension(dim_wdt, dim_ht));
        pass_again2 = new JPasswordField();
        pass_again2.setEchoChar('\u25CF');
        pass_again2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        pass_again2.setPreferredSize(new Dimension(dim_wdt, dim_ht));
        name2 = new JTextField();
        name2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        name2.setPreferredSize(new Dimension(dim_wdt, dim_ht));
        surname2 = new JTextField();
        surname2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        surname2.setPreferredSize(new Dimension(dim_wdt, dim_ht));
        phone2 = new JTextField();
        phone2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        phone2.setPreferredSize(new Dimension(dim_wdt, dim_ht));
        e_mail2 = new JTextField();
        e_mail2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        e_mail2.setPreferredSize(new Dimension(dim_wdt, dim_ht));
        address2 = new JTextField();
        address2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        address2.setPreferredSize(new Dimension(dim_wdt, dim_ht));
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
        window.add(center, BorderLayout.CENTER);
        window.add(down, BorderLayout.SOUTH);
    }
    private void exit(){
        window.setVisible(false);
        window.dispose();
    }
}
