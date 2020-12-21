package views.employee.manager.employee_panels;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Properties;

public class Edit_employee {
    private WindowMethods windowMethods = new WindowMethods();
    private JButton back, edit;
    private JLabel name, hired, salary, job_type, contract_type;
    private JTextField hired2, salary2, job_type2, contract_type2;
    private JPanel center, down, hired_pane, salary_pane, job_type_pane, contract_type_pane;
    private String user, user_name, toEdit, message = "W następujących polach wykryto błędy: ";
    private Dimension dimension = new Dimension(250, 20);
    private dataBaseConnection dataBase = new dataBaseConnection();
    private UtilDateModel model;
    private JDatePanelImpl datePanel;
    private JDatePickerImpl datePicker;
    private LocalDate now = LocalDate.now();
    private int error_counter;

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
        //DateFormat df = new SimpleDateFormat("yyyy/mm/dd");
        String date = rs.getDate(1).toString();
        datePicker.getModel().setDate(Integer.parseInt(date.substring(0,4)), Integer.parseInt(date.substring(5,7))-1, Integer.parseInt(date.substring(8,10)));
        salary2.setText(Float.toString(rs.getFloat(2)));
        job_type2.setText(rs.getString(3));
        contract_type2.setText(rs.getString(4));
        user_name = rs.getString(5) + " " + rs.getString(6);
        name.setText("Imię i Nazwisko: " + user_name);
        rs.close();
        dataBase.getStmt().close();
    }
    private void labels(){
        hired = new JLabel("Data zatrudnienia: ");
        salary = new JLabel("Pensja brutto: ");
        job_type = new JLabel("Stanowisko (max 20 znaków): ");
        contract_type = new JLabel("Typ umowy (max 30 znaków):");
        name = new JLabel();
    }
    private void components(){
        salary2 = windowMethods.setJTextField(salary2, "PENSJA");
        job_type2 = windowMethods.setJTextField(job_type2, "STANOWISKO");
        contract_type2 = windowMethods.setJTextField(contract_type2, "TYP UMOWY");

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
                        dataBase.getConn().setAutoCommit(true);
                        String selectedDate = datePicker.getModel().getYear() + "-" + datePicker.getModel().getMonth() + "-" + datePicker.getModel().getDay();
                        float salary = Float.parseFloat(salary2.getText());
                        System.out.println(salary);
                        int changes = dataBase.getStmt().executeUpdate(
                                "UPDATE Pracownik SET P_Data_zatrudnienia = '" + selectedDate +
                                        "', P_Pensja_Brutto = " + salary + ", P_Stanowisko = '"
                                + job_type2.getText() + "', P_Typ_umowy = '" + contract_type2.getText() +
                                        "' WHERE Login = '" + toEdit + "'"
                        );
                        JOptionPane.showMessageDialog(windowMethods.window, "Pracownik " + user_name + " edytowany pomyślnie");
                        System.out.println("Zaktualizowano " + changes + " rekord");
                        dataBase.getStmt().close();
                        Employees ee = new Employees();
                        windowMethods.exit();
                        ee.create(user);
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
        center.add(name);
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
        error_counter = 0;
        jobTypeCheck(job_type2);
        salaryCheck(salary2);
        contractCheck(contract_type2);
        if(error_counter != 0){
            JOptionPane.showMessageDialog(windowMethods.window, message, "Błąd!", JOptionPane.ERROR_MESSAGE);
            message = "W następujących polach wykryto błędy: ";
        }
        if (error_counter == 0) return true;
        else return false;

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
}
