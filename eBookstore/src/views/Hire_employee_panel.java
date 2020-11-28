package views;

import models.Awt1;
import models.dataBaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class Hire_employee_panel extends JFrame{
    private JFrame window;
    private JLabel login, pass, name, surname, phone, salary, job_type, contract_type;
    private JPasswordField pass2;
    private JTextField login2, name2, surname2, phone2, salary2, job_type2, contract_type2;
    private JButton back, add;
    private String user;
    private int dim_wdt = 250, dim_ht = 20;
    private JCheckBox pass_box;
    private JPanel center, down, login_pane, pass_pane, name_pane, surname_pane, phone_pane, salary_pane, job_type_pane, contract_type_pane;
    private dataBaseConnection dataBase = new dataBaseConnection();
    private boolean phone_correctness;
    private ArrayList<String> too_long_fields = new ArrayList<String>();
    private ArrayList<String> too_long_pass_fields = new ArrayList<String>();
    private String message = "W następujących polach wykryto błędy: ";

    public void create(String data){
        window = new JFrame("Dodaj pracownika");
        settings();
        user = data;
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
        name = new JLabel("Imię (max 20 znaków): ");
        surname = new JLabel("Nazwisko (max 30 znaków): ");
        phone = new JLabel("Numer telefonu: ");
        salary = new JLabel("Pensja brutto: ");
        job_type = new JLabel("Stanowisko (max 20 znaków): ");
        contract_type = new JLabel("Typ umowy (max 30 znaków):");
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
        name_pane = new JPanel();
        name_pane.add(name);
        name_pane.add(name2);
        surname_pane = new JPanel();
        surname_pane.add(surname);
        surname_pane.add(surname2);
        phone_pane = new JPanel();
        phone_pane.add(phone);
        phone_pane.add(phone2);
        salary_pane = new JPanel();
        salary_pane.add(salary);
        salary_pane.add(salary2);
        job_type_pane = new JPanel();
        job_type_pane.add(job_type);
        job_type_pane.add(job_type2);
        contract_type_pane = new JPanel();
        contract_type_pane.add(contract_type);
        contract_type_pane.add(contract_type2);

        center.add(login_pane);
        center.add(pass_pane);
        center.add(name_pane);
        center.add(surname_pane);
        center.add(phone_pane);
        center.add(salary_pane);
        center.add(job_type_pane);
        center.add(contract_type_pane);

        down = new JPanel();
        down.setLayout(new BorderLayout());
        down.add(back, BorderLayout.WEST);
        down.add(add, BorderLayout.EAST);
    }

    private void components(){
        back = new JButton("Powrót");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Manager_panel mg = new Manager_panel();
                exit();
                try {
                    mg.create(user);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        add = new JButton("Dodaj pracownika");
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
        salary2 = new JTextField();
        salary2.setName("PENSJA");
        salary2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        salary2.setPreferredSize(new Dimension(dim_wdt, dim_ht));
        job_type2 = new JTextField();
        job_type2.setName("STANOWISKO");
        job_type2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        job_type2.setPreferredSize(new Dimension(dim_wdt, dim_ht));
        contract_type2 = new JTextField();
        contract_type2.setName("TYP UMOWY");
        contract_type2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        contract_type2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        contract_type2.setPreferredSize(new Dimension(dim_wdt, dim_ht));
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
    }

    private void add_components(){
        panels();
        window.add(center, BorderLayout.CENTER);
        window.add(down, BorderLayout.SOUTH);
    }

    private boolean check(){
        fieldCheck(login2, 30);
        passFieldCheck(pass2, 20);
        fieldCheck(name2, 20);
        fieldCheck(surname2, 20);
        boolean number = phoneCheck(phone2);
        

        if(!number) {
            JOptionPane.showMessageDialog(window, "Numer telefonu musi składać się z cyfr!",
                    "Nieprawidłowy numer telefonu!", JOptionPane.ERROR_MESSAGE);
            phone2.setText("");
        }

        if(too_long_fields.size() != 0 || too_long_pass_fields.size() != 0){
            JOptionPane.showMessageDialog(window, message, "Błąd!", JOptionPane.ERROR_MESSAGE);
            message = "W następujących polach wykryto błędy: ";
            for (int i = 0; i < too_long_fields.size(); i++) {
                JTextField tmp = Awt1.getComponentByName(window, too_long_fields.get(i));
                tmp.setText("");
            }
            for (int i = 0; i < too_long_pass_fields.size(); i++) {
                JPasswordField tmp = Awt1.getComponentByName(window, too_long_pass_fields.get(i));
                tmp.setText("");
            }
        }
        if (number && too_long_fields.size() == 0 && too_long_pass_fields.size() == 0) return true;
        else return false;

    }
    private void fieldCheck(JTextField field, int size){
        if(field.getText().length() == 0 || field.getText().length() > size){
            message += "\n" + field.getName();
            too_long_fields.add(field.getName());
        }
    }
    private void passFieldCheck(JPasswordField field, int size){
        if(field.getPassword().length == 0 || field.getPassword().length > size){
            message += "\n" + field.getName();
            too_long_pass_fields.add(field.getName());
        }
    }
    private boolean phoneCheck(JTextField field) {
        phone_correctness = true;
        if(field.getText().length() != 9) return false;
        for (int i = 0; i < field.getText().length(); i++) {
            if (!Character.isDigit(field.getText().charAt(i))) {
                phone_correctness = false;
            }
        }
        return phone_correctness;
    }

    private void exit(){
        window.setVisible(false);
        window.dispose();
    }
}
