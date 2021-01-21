package views.employee.manager.employee_panels;

import models.DataVerification;
import models.WindowMethods;
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
import java.util.Properties;

public class Hire_employee_panel {
    private WindowMethods windowMethods = new WindowMethods();
    private JLabel login, pass, name, surname, phone, hired, salary, job_type, contract_type;
    private JPasswordField pass2;
    private JTextField login2, name2, surname2, phone2, salary2;
    private JButton back, add;
    private JCheckBox pass_box;
    private JPanel center, down, login_pane, pass_pane, name_pane, surname_pane, phone_pane,hired_pane, salary_pane, job_type_pane, contract_type_pane;
    private dataBaseConnection dataBase = new dataBaseConnection();
    private String user;
    private JComboBox job_type2, contract_type2;
    private UtilDateModel model;
    private JDatePanelImpl datePanel;
    private JDatePickerImpl datePicker;
    private LocalDate now = LocalDate.now();
    private String[] jobTypes = {"magazynier", "kierownik"};
    private String[] contractTypes = {"umowa o pracę", "umowa zlecenie"};

    public void create(String data){
        windowMethods.window = new JFrame("Dodaj pracownika");
        windowMethods.settings();
        user = data;
        add_components();
        windowMethods.window.setVisible(true);
    }
    private void labels(){
        login = new JLabel("Wybierz login (max 30 znaków): ");
        pass = new JLabel("Wybierz hasło (max 20 znaków): ");
        name = new JLabel("Imię (max 20 znaków): ");
        surname = new JLabel("Nazwisko (max 30 znaków): ");
        phone = new JLabel("Numer telefonu: ");
        hired = new JLabel("Data zatrudnienia: ");
        salary = new JLabel("Pensja brutto: ");
        job_type = new JLabel("Stanowisko: ");
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
        login2 = windowMethods.setJTextField(login2, "LOGIN");
        pass2 = windowMethods.setJPasswordField(pass2, "HASŁO");
        pass_box = windowMethods.setPassCheckBox(pass_box, pass2, "Pokaż hasło");
        name2 = windowMethods.setJTextField(name2, "IMIĘ");
        surname2 = windowMethods.setJTextField(surname2, "NAZWISKO");
        phone2 = windowMethods.setJTextField(phone2, "NUMER TELEFONU");
        salary2 = windowMethods.setJTextField(salary2, "PENSJA");
        job_type2 = new JComboBox(jobTypes);
        job_type2.setSelectedIndex(0);
        job_type2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        contract_type2 = new JComboBox(contractTypes);
        job_type2.setSelectedIndex(0);
        job_type2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        back = new JButton("Powrót");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Employees mg = new Employees();
                windowMethods.exit();
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
                        ResultSet rs = dataBase.getStmt().executeQuery(
                                "SELECT Login FROM Uzytkownik WHERE Login = " + "'" +login2.getText() + "'"
                        );
                        if(rs.next()){
                            JOptionPane.showMessageDialog(windowMethods.window, "Użytkownik o podanym loginie już istnieje!",
                                    "Błąd rejestracji!", JOptionPane.ERROR_MESSAGE);
                            rs.close();
                        }
                        else{
                            String tmp = String.copyValueOf(pass2.getPassword());
                            int month = datePicker.getModel().getMonth()+1;
                            String selectedDate = datePicker.getModel().getYear() + "-" + Integer.toString(month) + "-" + datePicker.getModel().getDay();
                            float salary = Float.parseFloat(salary2.getText());
                            System.out.println(salary);
                            dataBase.newEmployee(login2.getText(), tmp, name2.getText(), surname2.getText(), phone2.getText(), selectedDate, salary, job_type2.getSelectedItem().toString(), contract_type2.getSelectedItem().toString());
                            JOptionPane.showMessageDialog(windowMethods.window, "Rejestracja przebiegła pomyślnie!");
                            //przejście do okna logowania
                            Employees ee = new Employees();
                            windowMethods.exit();
                            ee.create(user);
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        });
        model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Dzisiaj");
        p.put("text.month", "Miesiąc");
        p.put("text.year", "Rok");
        datePanel = new JDatePanelImpl(model, p);
        model.setDate(now.getYear(), now.getMonthValue()-1, now.getDayOfMonth());
        model.setSelected(true);
        datePicker = new JDatePickerImpl(datePanel, new DateComponentFormatter());

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
        name2.setText(name2.getText().trim().replaceAll("\\s{2,}", " "));
        surname2.setText(surname2.getText().trim().replaceAll("\\s{2,}", " "));
        verify.fieldCheck(name2, 1, 20, false, true);
        verify.fieldCheck(surname2, 1, 30, false, true);
        verify.phoneCheck(phone2);
        verify.sumCheck(salary2);
        verify.errorPhone(verify.phone_correctness, phone2, windowMethods.window);
        verify.errorMessage();
        if(verify.phone_correctness && verify.error_counter == 0) return true;
        else return false;
    }
}
