package views.employee.manager.employee_panels;

import models.dataBaseConnection;
import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import views.employee.manager.Manager_panel;
import views.employee.manager.employee_panels.Employees;

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
    private int error_counter;
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
                            int month = datePicker.getModel().getMonth()+1;
                            String selectedDate = datePicker.getModel().getYear() + "-" + Integer.toString(month) + "-" + datePicker.getModel().getDay();
                            float salary = Float.parseFloat(salary2.getText());
                            System.out.println(salary);
                            dataBase.newEmployee(login2.getText(), tmp, name2.getText(), surname2.getText(), phone2.getText(), selectedDate, salary, job_type2.getText(), contract_type2.getText());
                            JOptionPane.showMessageDialog(window, "Rejestracja przebiegła pomyślnie!");
                            //przejście do okna logowania
                            Employees ee = new Employees();
                            exit();
                            ee.create(user);
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
        salary2.setPreferredSize(dimension);
        job_type2 = new JTextField();
        job_type2.setName("STANOWISKO");
        job_type2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        job_type2.setPreferredSize(dimension);
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
        contract_type2.setPreferredSize(dimension);
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
        error_counter = 0;
        fieldCheck(login2, 1, 30, true, false);
        passFieldCheck(pass2, 20);
        fieldCheck(name2, 1, 20, true, true);
        fieldCheck(surname2, 1, 30, true, true);
        boolean number = phoneCheck(phone2);
        jobTypeCheck(job_type2);
        salaryCheck(salary2);
        contractCheck(contract_type2);

        if(!number) {
            JOptionPane.showMessageDialog(window, "Numer telefonu musi składać się z cyfr!",
                    "Nieprawidłowy numer telefonu!", JOptionPane.ERROR_MESSAGE);
            phone2.setText("");
        }

        if(error_counter != 0){
            JOptionPane.showMessageDialog(window, message, "Błąd!", JOptionPane.ERROR_MESSAGE);
            message = "W następujących polach wykryto błędy: ";
        }
        if (number && error_counter == 0) return true;
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
    private void jobTypeCheck(JTextField field){
        if(!(field.getText().equals("magazynier") || field.getText().equals("kierownik"))){
            message += "\n" + field.getName();
            error_counter++;
        }
    }
    private void salaryCheck(JTextField field){
        if (!field.getText().matches("[0-9]+[.]?[0-9]{1,2}")){
            message += "\n" + field.getName();
            error_counter++;
        }
    }
    private void contractCheck(JTextField field){
        if(!field.getText().equals("praca")){
            message += "\n" + field.getName();
            error_counter++;
        }
    }
    private void exit(){
        window.setVisible(false);
        window.dispose();
    }
}
