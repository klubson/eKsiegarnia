package views.employee.manager;

import models.dataBaseConnection;
import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;

public class Hire_employee_panel extends JFrame{
    private JFrame window;
    private JLabel login, pass, name, surname, phone, hired, salary, job_type, contract_type;
    private JPasswordField pass2;
    private JTextField login2, name2, surname2, phone2, salary2, job_type2, contract_type2;
    private JButton back, add;
    private int dim_wdt = 250, dim_ht = 20;
    private Dimension dimension = new Dimension(250, 20);
    private JCheckBox pass_box;
    private JPanel center, down, login_pane, pass_pane, name_pane, surname_pane, phone_pane,hired_pane, salary_pane, job_type_pane, contract_type_pane;
    private dataBaseConnection dataBase = new dataBaseConnection();
    private boolean phone_correctness;
    private String message = "W następujących polach wykryto błędy: ", user;
    private UtilDateModel model;
    private JDatePanelImpl datePanel;
    private JDatePickerImpl datePicker;
    private LocalDate now = LocalDate.now();

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
        hired = new JLabel("Data zatrudnienia: ");
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
        hired_pane = new JPanel();
        hired_pane.add(hired);
        hired_pane.add(datePicker);
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
        center.add(hired_pane);
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
                Employees mg = new Employees();
                exit();
                mg.create(user);
            }
        });
        add = new JButton("Dodaj pracownika");
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(check()){
                    try {
                        dataBase.setStmt();
                        dataBase.getConn().setAutoCommit(true);
                        ResultSet rs = dataBase.getStmt().executeQuery(
                                "SELECT Login FROM Uzytkownik WHERE Login = " + "'" +login2.getText() + "'"
                        );
                        if(rs.next()){
                            JOptionPane.showMessageDialog(window, "Użytkownik o podanym loginie już istnieje!",
                                    "Błąd rejestracji!", JOptionPane.ERROR_MESSAGE);
                            rs.close();
                        }
                        else{
                            String tmp = String.copyValueOf(pass2.getPassword());
                            String selectedDate = datePicker.getModel().getYear() + "-" + datePicker.getModel().getMonth() + "-" + datePicker.getModel().getDay();
                            float salary = Float.parseFloat(salary2.getText());
                            dataBase.newEmployee(login2.getText(), tmp, name2.getText(), surname2.getText(), phone2.getText(), selectedDate, salary, job_type2.getText(), contract_type2.getText());
                            JOptionPane.showMessageDialog(window, "Rejestracja przebiegła pomyślnie!");
                            //przejście do okna logowania
                            Manager_panel win = new Manager_panel();
                            exit();
                            win.create(user);
                        }
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
        model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Dzisiaj");
        p.put("text.month", "Miesiąc");
        p.put("text.year", "Rok");
        datePanel = new JDatePanelImpl(model, p);
        model.setDate(now.getYear(), now.getMonthValue()-1, now.getDayOfMonth());
        model.setSelected(true);
        datePicker = new JDatePickerImpl(datePanel, new DateComponentFormatter());

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
        ArrayList<String> error_fields = new ArrayList<String>();
        ArrayList<String> error_pass_fields = new ArrayList<String>();
        fieldCheck(login2, 30, error_fields);
        passFieldCheck(pass2, 20, error_pass_fields);
        fieldCheck(name2, 20, error_fields);
        fieldCheck(surname2, 20, error_fields);
        boolean number = phoneCheck(phone2);
        jobTypeCheck(job_type2, 20, error_fields);
        salaryCheck(salary2, error_fields);
        contractCheck(contract_type2, error_fields);

        if(!number) {
            JOptionPane.showMessageDialog(window, "Numer telefonu musi składać się z cyfr!",
                    "Nieprawidłowy numer telefonu!", JOptionPane.ERROR_MESSAGE);
            phone2.setText("");
        }

        if(error_fields.size() != 0 || error_pass_fields.size() != 0){
            JOptionPane.showMessageDialog(window, message, "Błąd!", JOptionPane.ERROR_MESSAGE);
            message = "W następujących polach wykryto błędy: ";
        }
        if (number && error_fields.size() == 0 && error_pass_fields.size() == 0) return true;
        else return false;

    }
    private void fieldCheck(JTextField field, int size, ArrayList<String> error_fields){
        if(field.getText().length() == 0 || field.getText().length() > size){
            message += "\n" + field.getName();
            error_fields.add(field.getName());
        }
    }
    private void passFieldCheck(JPasswordField field, int size, ArrayList<String> error_pass_fields){
        if(field.getPassword().length == 0 || field.getPassword().length > size){
            message += "\n" + field.getName();
            error_pass_fields.add(field.getName());
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
    private void jobTypeCheck(JTextField field, int size, ArrayList<String> error_fields){
        if(!(field.getText().equals("magazynier") || field.getText().equals("kierownik"))){
            message += "\n" + field.getName();
            error_fields.add(field.getName());
        }
    }
    private void salaryCheck(JTextField field, ArrayList<String> error_fields){
        if (!field.getText().matches("[0-9]+[.]?[0-9]{1,2}")){
            message += "\n" + field.getName();
            error_fields.add(field.getName());
        }

    }
    private void contractCheck(JTextField field, ArrayList<String> error_fields){
        if(!field.getText().equals("praca")){
            message += "\n" + field.getName();
            error_fields.add(field.getName());
        }
    }

    private void exit(){
        window.setVisible(false);
        window.dispose();
    }
}
