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
    private String beginWho_logged = "Zalogowany: ", user;
    private JButton log_out, product_list, add_product, delete_product, edit_product, employees_list, hire_employee, fire_employee, edit_employee;
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
        window.setSize(600, 450);
        window.setLocation(400, 80);
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
    }
    private void components() throws SQLException {
        who_logged = new JLabel(beginWho_logged);
        who_logged.setFont(who_logged.getFont().deriveFont(20.0f));
        current_time = new JLabel(time.getTime());
        current_time.setFont(current_time.getFont().deriveFont(20.0f));
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
        product_list = new JButton("Lista produktów");
        product_list.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        add_product = new JButton("Dodaj nowy produkt");
        add_product.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Add_product win = new Add_product();
                time.stopClock();
                exit();
                try {
                    win.create(user);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        edit_product = new JButton("<html>Edytuj <br /> właściwości<br />produktu</html>");
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
        employees_list = new JButton("Lista pracowników");
        employees_list.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        hire_employee = new JButton("<html> Zatrudnij <br /> pracownika </html>");
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
        edit_employee = new JButton("<html> Edytuj dane <br /> pracownika </html>");
        edit_employee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
    private void panels() throws SQLException {
        components();
        up = new JPanel();
        up.setLayout(new BoxLayout(up, BoxLayout.PAGE_AXIS));
        up.setPreferredSize(new Dimension(600, 50));
        up.add(who_logged);
        up.add(current_time);

        center = new JPanel();
        center.setPreferredSize(new Dimension(600, 300));
        center.setLayout(new GridLayout(2,4));
        center.add(employees_list);
        center.add(hire_employee);
        center.add(edit_employee);
        center.add(fire_employee);
        center.add(product_list);
        center.add(add_product);
        center.add(edit_product);
        center.add(delete_product);

        down = new JPanel();
        down.setLayout(new BorderLayout());
        down.setPreferredSize(new Dimension(600, 50));
        down.add(log_out, BorderLayout.CENTER);
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
