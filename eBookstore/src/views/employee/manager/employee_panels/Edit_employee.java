package views.employee.manager.employee_panels;

import models.DataVerification;
import models.WindowMethods;
import models.dataBaseConnection;
import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import views.employee.supplier.Employee_panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Properties;

public class Edit_employee {
    private WindowMethods windowMethods = new WindowMethods();
    private JButton back, edit;
    private JLabel name, hired, salary, job_type, contract_type;
    private JTextField salary2;
    private JPanel center, down, name_pane, hired_pane, salary_pane, job_type_pane, contract_type_pane;
    private String user, user_name, toEdit;
    private JComboBox job_type2, contract_type2;
    private dataBaseConnection dataBase = new dataBaseConnection();
    private UtilDateModel model;
    private JDatePanelImpl datePanel;
    private JDatePickerImpl datePicker;
    private LocalDate now = LocalDate.now();
    private String[] jobTypes = {"magazynier", "kierownik"};
    private String[] contractTypes = {"umowa o pracę", "umowa zlecenie"};

    public void create(String data, String loginToEdit) throws SQLException {
        windowMethods.window = new JFrame("Edytuj dane pracownika");
        windowMethods.settings();
        windowMethods.window.setSize(600, 400);
        user = data;
        toEdit = loginToEdit;
        add_components();
        setEmployee(loginToEdit);
        windowMethods.window.setVisible(true);
    }
    private void setEmployee(String login) throws SQLException {
        dataBase.setStmt();
        ResultSet rs = dataBase.getStmt().executeQuery(
                "SELECT P_Data_zatrudnienia, P_Pensja_Brutto, P_Stanowisko, " +
                        "P_Typ_umowy, Imie, Nazwisko FROM Pracownik " +
                        "JOIN Uzytkownik USING(Login) WHERE Login = '" + login + "'"
        );
        rs.next();
        String date = rs.getDate(1).toString();
        datePicker.getModel().setDate(Integer.parseInt(date.substring(0,4)), Integer.parseInt(date.substring(5,7))-1, Integer.parseInt(date.substring(8,10)));
        salary2.setText(Float.toString(rs.getFloat(2)));
        job_type2.setSelectedItem(rs.getString(3));
        contract_type2.setSelectedItem(rs.getString(4));
        user_name = rs.getString(5) + " " + rs.getString(6);
        name.setText("Imię i Nazwisko: " + user_name);
        rs.close();
        dataBase.getStmt().close();
    }
    private void labels(){
        hired = new JLabel("Data zatrudnienia: ");
        salary = new JLabel("Pensja brutto: ");
        job_type = new JLabel("Stanowisko: ");
        contract_type = new JLabel("Typ umowy:");
        name = new JLabel();
    }
    private void components(){
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
        edit = new JButton("Edytuj");
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //sprawdź poprawność danych i edytuj dane
                if(check()){
                    try {
                        dataBase.setStmt();
                        int month = datePicker.getModel().getMonth() + 1;
                        String selectedDate = datePicker.getModel().getYear() + "-" + Integer.toString(month) + "-" + datePicker.getModel().getDay();
                        float salary = Float.parseFloat(salary2.getText());
                        System.out.println(salary);
                        int changes = dataBase.getStmt().executeUpdate(
                                "UPDATE Pracownik SET P_Data_zatrudnienia = '" + selectedDate +
                                        "', P_Pensja_Brutto = " + salary + ", P_Stanowisko = '"
                                + job_type2.getSelectedItem().toString() + "', P_Typ_umowy = '" + contract_type2.getSelectedItem().toString() +
                                        "' WHERE Login = '" + toEdit + "'"
                        );
                        JOptionPane.showMessageDialog(windowMethods.window, "Pracownik " + user_name + " edytowany pomyślnie");
                        System.out.println("Zaktualizowano " + changes + " rekord");
                        ResultSet rs = dataBase.getStmt().executeQuery(
                                "SELECT Imie, Nazwisko FROM Uzytkownik WHERE Login = '" + user + "'"
                        );
                        rs.next();
                        String current_employee = rs.getString(1) + " " + rs.getString(2);
                        rs.close();
                        if(current_employee.equals(user_name)){
                            ResultSet rs2 = dataBase.getStmt().executeQuery(
                                    "SELECT P_Stanowisko FROM Pracownik WHERE Login = '" + user + "'"
                            );
                            rs2.next();
                            String job = rs2.getString(1);
                            rs2.close();
                            if(job.equals("magazynier")){
                                dataBase.getStmt().close();
                                Employee_panel ep = new Employee_panel();
                                windowMethods.exit();
                                ep.create(user);
                            }
                            else if(job.equals("kierownik")){
                                dataBase.getStmt().close();
                                Employees ee = new Employees();
                                windowMethods.exit();
                                ee.create(user);
                            }
                        }
                        else{
                            dataBase.getStmt().close();
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
    private void panels(){
        labels();
        components();
        name_pane = new JPanel();
        name_pane.add(name);
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

        center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
        center.add(name_pane);
        center.add(hired_pane);
        center.add(salary_pane);
        center.add(job_type_pane);
        center.add(contract_type_pane);

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
        verify.sumCheck(salary2);
        verify.errorMessage();
        if(verify.error_counter == 0) return true;
        else return false;
    }
}
