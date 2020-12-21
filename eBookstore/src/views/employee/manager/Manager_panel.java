package views.employee.manager;

import models.Current_date;
import models.WindowMethods;
import views.Start_window;
import views.employee.Edit_employee_profile;
import views.employee.author_panels.Authors;
import views.employee.publisher_panels.Publishers;
import views.employee.manager.employee_panels.Employees;
import views.employee.product_panels.Products;
import views.employee.series_panels.Series;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Manager_panel {
    private WindowMethods windowMethods = new WindowMethods();
    private JLabel who_logged, current_time;
    private String beginWho_logged = "Zalogowany: ", user;
    private JButton log_out, product_list, employees_list, publisher_list, author_list, series_list, edit_profile;
    private JPanel up, center, down;
    private Current_date time = new Current_date();

    public void create(String data) throws SQLException {
        windowMethods.window = new JFrame("Kierownik");
        windowMethods.settings();
        windowMethods.window.setSize(600, 450);
        add_components();
        user = data;
        windowMethods.setWho_logged(who_logged, user);
        windowMethods.window.setVisible(true);
        time.clock(current_time);
    }
//    private void setWho_logged(String data) throws SQLException {
//        dataBase.setStmt();
//        ResultSet rs = dataBase.getStmt().executeQuery(
//                "SELECT Imie, Nazwisko FROM Uzytkownik WHERE LOGIN = '" + data + "'"
//        );
//        rs.next();
//        String name = rs.getString(1) + " " + rs.getString(2);
//        who_logged.setText(beginWho_logged + name);
//        rs.close();
//        dataBase.getStmt().close();
//    }
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
                windowMethods.exit();
                System.out.println("Wylogowano");
                try {
                    win.create();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        product_list = new JButton("Produkty");
        product_list.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Products pr = new Products();
                time.stopClock();
                windowMethods.exit();
                try {
                    pr.create(user, true);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        employees_list = new JButton("Pracownicy");
        employees_list.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Employees ee = new Employees();
                time.stopClock();
                windowMethods.exit();
                try {
                    ee.create(user);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
        });

        publisher_list = new JButton("Wydawnictwa");
        publisher_list.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Publishers pb = new Publishers();
                time.stopClock();
                windowMethods.exit();
                try {
                    pb.create(user, true);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        author_list = new JButton("Autorzy");
        author_list.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Authors au = new Authors();
                time.stopClock();
                windowMethods.exit();
                try {
                    au.create(user, true);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        series_list = new JButton("Serie książek");
        series_list.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Series ss = new Series();
                time.stopClock();
                windowMethods.exit();
                try {
                    ss.create(user, true);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        edit_profile = new JButton("Edytuj profil");
        edit_profile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Edit_employee_profile eep = new Edit_employee_profile();
                time.stopClock();
                windowMethods.exit();
                try {
                    eep.create(user, true);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
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
        center.setLayout(new GridLayout(2,3));
        center.add(employees_list);
        center.add(product_list);
        center.add(author_list);
        center.add(publisher_list);
        center.add(series_list);
        center.add(edit_profile);

        down = new JPanel();
        down.setLayout(new BorderLayout());
        down.setPreferredSize(new Dimension(600, 50));
        down.add(log_out, BorderLayout.CENTER);
    }

    private void add_components() throws SQLException {
        panels();
        windowMethods.window.add(up, BorderLayout.NORTH);
        windowMethods.window.add(center, BorderLayout.CENTER);
        windowMethods.window.add(down, BorderLayout.SOUTH);
    }
}
