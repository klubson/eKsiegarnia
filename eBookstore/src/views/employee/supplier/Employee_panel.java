package views.employee.supplier;
import models.Current_date;
import models.WindowMethods;
import models.dataBaseConnection;
import views.Start_window;
import views.employee.Edit_employee_profile;
import views.employee.author_panels.Authors;
import views.employee.product_panels.Products;
import views.employee.publisher_panels.Publishers;
import views.employee.series_panels.Series;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Employee_panel {
    private WindowMethods windowMethods = new WindowMethods();
    private JLabel who_logged, current_time;
    private String beginWho_logged = "Zalogowany: ", user;
    private JButton log_out, product_list, publisher_list, author_list, series_list, edit_profile;
    private JPanel up, center, down;
    private Current_date time = new Current_date();

    public void create(String data) throws SQLException {
        windowMethods.window = new JFrame("Magazynier");
        windowMethods.settings();
        windowMethods.window.setSize(600, 450);
        add_components();
        user = data;
        windowMethods.setWho_logged(who_logged, user);
        windowMethods.window.setVisible(true);
        time.clock(current_time);
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
                exit();
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
                exit();
                try {
                    pr.create(user, false);
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
                exit();
                try {
                    pb.create(user, false);
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
                exit();
                try {
                    au.create(user, false);
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
                exit();
                try {
                    ss.create(user, false);
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
                exit();
                try {
                    eep.create(user, false);
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
    private void exit(){
        time.stopClock();
        windowMethods.window.setVisible(false);
        windowMethods.window.dispose();
    }
}
