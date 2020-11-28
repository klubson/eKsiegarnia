package views;

import models.Current_date;
import models.dataBaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Manager_panel extends JFrame {
    private JFrame window;
    private JLabel who_logged, current_time;
    private String beginWho_logged = "Zalogowany jako: ", user;
    private JButton log_out, add_product, delete_product, edit_product, hire_employee, fire_employee, edit_employee;
    private JPanel up, center, down;
    private dataBaseConnection dataBase;
    private Current_date time = new Current_date();

    public void create(String data) throws SQLException {
        window = new JFrame("Pracownik");
        settings();
        add_components();
        user = data;
        setWho_logged(user);
        window.setVisible(true);
        time.clock(current_time);
    }
    private void setWho_logged(String data){
        who_logged.setText(beginWho_logged + data);
    }
    private void settings(){
        window.setSize(600, 600);
        window.setLocation(400, 80);
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
    }
    private void components() throws SQLException {
        who_logged = new JLabel(beginWho_logged);
        current_time = new JLabel(time.getTime());
        log_out = new JButton("Wyloguj się");
        log_out.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Start_window win = new Start_window();
                time.stopClock();
                exit();
                System.out.println("Wylogowano");
                try {
                    win.create();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        add_product = new JButton("Dodaj nowy produkt");
        add_product.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        edit_product = new JButton("Edytuj właściwości produktu");
        edit_product.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        delete_product = new JButton("Usuń produkt");
        delete_product.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        hire_employee = new JButton("Zatrudnij pracownika");
        hire_employee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Hire_employee_panel he = new Hire_employee_panel();
                exit();
                he.create(user);

            }
        });
        fire_employee = new JButton("Zwolnij pracownika");
        fire_employee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        edit_employee = new JButton("Edytuj dane pracownika");
        edit_employee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
    private void panels() throws SQLException {
        components();
        up = new JPanel();
        up.setLayout(new GridLayout(1, 2));
        up.setPreferredSize(new Dimension(600, 150));
        up.add(who_logged);
        up.add(current_time);

        center = new JPanel();
        center.setPreferredSize(new Dimension(600, 400));
        center.setLayout(new GridLayout(2,3));
        center.add(hire_employee);
        center.add(edit_employee);
        center.add(fire_employee);
        center.add(add_product);
        center.add(edit_product);
        center.add(delete_product);

        down = new JPanel();
        down.setPreferredSize(new Dimension(600, 50));
        down.add(log_out);
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
