package views.employee.manager.employee_panels;

import models.dataBaseConnection;
import views.employee.manager.Manager_panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class Employees extends JFrame {
    private JFrame window;
    private JCheckBox name_asc, name_desc, surname_asc, surname_desc, salary_asc, salary_desc, hired_asc, hired_desc;
    private JPanel up, center, down, down_center;
    private JButton hire, back, edit, filter, fire;
    private dataBaseConnection dataBase = new dataBaseConnection();
    private DefaultTableModel tableModel = new DefaultTableModel();
    private JScrollPane listScroller;
    private String sort_asc, sort_desc, user;
    private Vector<Vector<String>> data = new Vector<Vector<String>>();
    private Vector<String> columnNames = new Vector<String>();
    private JTable table;

    public void create(String data) throws SQLException {
        window = new JFrame("Pracownicy");
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
    private void getEmployeeList(int mode) throws SQLException {
        data.clear();
        dataBase.setStmt();
        dataBase.getConn().setAutoCommit(true);
        ResultSet rs = null;
        if(sort_asc == "" && sort_desc == "") mode = 1;
        if(mode == 1) {
            rs = dataBase.getStmt().executeQuery(
                    "SELECT Login, Nazwisko, Imie, P_Data_zatrudnienia, P_Pensja_Brutto, " +
                            "P_Stanowisko FROM Pracownik JOIN Uzytkownik USING (Login) ORDER BY Nazwisko"
            );
        }
        if(mode == 2){
            String query = "SELECT Login, Nazwisko, Imie, P_Data_zatrudnienia, P_Pensja_Brutto, " +
                    "P_Stanowisko FROM Pracownik JOIN Uzytkownik USING (Login) ORDER BY ";
            if(sort_asc != "" && sort_desc != ""){
                query += sort_asc + " ASC, ";
                query += sort_desc + " DESC";
            }
            else if(sort_asc != "") query += sort_asc + " ASC";
            else if(sort_desc != "") query += sort_desc + " DESC";

            rs = dataBase.getStmt().executeQuery(query);
        }
        while(rs.next()){
            Vector<String> vString = new Vector<String>();
            vString.add(rs.getString(1));
            vString.add(rs.getString(2));
            vString.add(rs.getString(3));
            Date dateSQL = rs.getDate(4);
            String date = dateSQL.toString();
            vString.add(date);
            vString.add(Float.toString(rs.getFloat(5)));
            vString.add(rs.getString(6));
            data.add(vString);
        }
        rs.close();
        dataBase.getStmt().close();
    }
    public void createTable(int mode) throws SQLException {
        getEmployeeList(mode);
        tableModel = new DefaultTableModel(data, columnNames);
        table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(600, 300));
        table.setFillsViewportHeight(true);
        table.changeSelection(0,0, false, false);
        listScroller = new JScrollPane(table);
        listScroller.setViewportView(table);
    }
    private void components() throws SQLException {
        filter = new JButton("Sortuj");
        filter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sort_asc = "";
                sort_desc = "";
                if(name_asc.isSelected()) sort_asc += "Imie, ";
                if(surname_asc.isSelected()) sort_asc += "Nazwisko, ";
                if(salary_asc.isSelected()) sort_asc += "P_Pensja_Brutto, ";
                if(hired_asc.isSelected()) sort_asc += "P_Data_zatrudnienia, ";
                if(sort_asc.length() != 0) sort_asc = sort_asc.substring(0, sort_asc.length() - 2);

                if(name_desc.isSelected()) sort_desc += "Imie, ";
                if(surname_desc.isSelected()) sort_desc += "Nazwisko, ";
                if(salary_desc.isSelected()) sort_desc += "P_Pensja_Brutto, ";
                if(hired_desc.isSelected()) sort_desc += "P_Data_zatrudnienia, ";
                if(sort_desc.length() != 0) sort_desc = sort_desc.substring(0, sort_desc.length() - 2);

                try {
                    createTable(2);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                listScroller.revalidate();
                listScroller.repaint();
                window.repaint();

            }
        });
        back = new JButton("Powrót");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Manager_panel win = new Manager_panel();
                exit();
                try {
                    win.create(user);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        hire = new JButton("Nowy pracownik");
        hire.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Hire_employee_panel he = new Hire_employee_panel();
                exit();
                he.create(user);
            }
        });
        edit = new JButton("Edytuj dane");
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Edit_employee ee = new Edit_employee();
                exit();
                try {
                    ee.create(user, data.get(table.getSelectedRow()).get(0));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        fire = new JButton("Zwolnij");
        fire.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedUser = data.get(table.getSelectedRow()).get(2) + " " + data.get(table.getSelectedRow()).get(1);
                int question = JOptionPane.YES_NO_OPTION;
                int questionResult = JOptionPane.showConfirmDialog(null, "Czy na pewno chcesz zwolnić "+ selectedUser + " ?", "UWAGA!", question);
                if (questionResult == 0){
                    try {
                        dataBase.setStmt();
                        ResultSet rs = dataBase.getStmt().executeQuery(
                                "SELECT Imie, Nazwisko FROM Uzytkownik " +
                                        "WHERE Login = '" + user + "'"
                        );
                        rs.next();
                        String loggedUser = rs.getString(1) + " " + rs.getString(2);
                        rs.close();
                        dataBase.getStmt().close();
                        if(selectedUser.equals(loggedUser)){
                            JOptionPane.showMessageDialog(window, "Nie możesz zwolnić samego siebie!", "Błąd!", JOptionPane.ERROR_MESSAGE);
                        }
                        else{
                            System.out.println("zaraz usuwanie pracownika");
                            dataBase.setStmt();
                            dataBase.getConn().setAutoCommit(true);
                            int changes = dataBase.getStmt().executeUpdate(
                                    "DELETE FROM Pracownik WHERE Login = '" + data.get(table.getSelectedRow()).get(0) + "'"
                            );
                            dataBase.getStmt().close();
                            System.out.println("pracownik usunięty, zaraz usuwanie użytkownika");
                            dataBase.setStmt();
                            int changes2 = dataBase.getStmt().executeUpdate(
                                    "DELETE FROM Uzytkownik WHERE Login = '" + data.get(table.getSelectedRow()).get(0) + "'"
                            );
                            dataBase.getStmt().close();
                            System.out.println("Zwolniono " + changes + " pracownika");
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    try {
                        Employees ee = new Employees();
                        exit();
                        ee.create(user);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        });
        name_asc = new JCheckBox("Imię - rosnąco");
        name_asc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(name_asc.isSelected()){
                    name_desc.setSelected(false);
                }
            }
        });
        name_desc = new JCheckBox("Imię - malejąco");
        name_desc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(name_desc.isSelected()){
                    name_asc.setSelected(false);
                }
            }
        });
        surname_asc = new JCheckBox("Nazwisko - rosnąco");
        surname_asc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(surname_asc.isSelected()){
                    surname_desc.setSelected(false);
                }
            }
        });
        surname_desc = new JCheckBox("Nazwisko - malejąco");
        surname_desc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(surname_desc.isSelected()){
                    surname_asc.setSelected(false);
                }
            }
        });
        salary_asc = new JCheckBox("Pensja - rosnąco");
        salary_asc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(salary_asc.isSelected()){
                    salary_desc.setSelected(false);
                }
            }
        });
        salary_desc = new JCheckBox("Pensja - malejąco");
        salary_desc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(salary_desc.isSelected()){
                    salary_asc.setSelected(false);
                }
            }
        });
        hired_asc = new JCheckBox("Zatrudniony - najwcześniej");
        hired_asc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(hired_asc.isSelected()){
                    hired_desc.setSelected(false);
                }
            }
        });
        hired_desc = new JCheckBox("Zatrudniony - najpóźniej");
        hired_desc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(hired_desc.isSelected()){
                    hired_asc.setSelected(false);
                }
            }
        });
        columnNames.add("Login");
        columnNames.add("Nazwisko");
        columnNames.add("Imię");
        columnNames.add("Data zatrudnienia");
        columnNames.add("Pensja");
        columnNames.add("Stanowisko");

        createTable(1);
    }
    private void panels() throws SQLException {
        components();

        up = new JPanel();
        up.setLayout(new GridLayout(3,3));
        up.add(name_asc);
        up.add(name_desc);
        up.add(surname_asc);
        up.add(surname_desc);
        up.add(salary_asc);
        up.add(salary_desc);
        up.add(hired_asc);
        up.add(hired_desc);
        up.add(filter);

        center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
        center.add(listScroller);
        down_center = new JPanel();
        down_center.add(hire);
        down_center.add(edit);

        down = new JPanel();
        down.setLayout(new BorderLayout());
        down.add(back, BorderLayout.WEST);
        down.add(down_center, BorderLayout.CENTER);
        down.add(fire, BorderLayout.EAST);
    }

    private void add_components() throws SQLException {
        panels();
        window.add(up, BorderLayout.NORTH);
        window.add(center, BorderLayout.CENTER);
        window.add(down, BorderLayout.SOUTH);
    }

    private void exit(){
        window.setVisible(false);
        window.dispose();
    }
}
