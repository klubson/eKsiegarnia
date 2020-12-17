package views.employee;
import models.dataBaseConnection;
import views.Start_window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Employee_panel extends JFrame {
    private JFrame window;
    private JLabel who_logged;
    private String beginWho_logged = "Zalogowany jako: ";
    private JButton log_out, add_product, delete_product, edit_product;
    private JPanel up, center, down;
    private dataBaseConnection dataBase;

    public void create(String data) throws SQLException {
        window = new JFrame("Pracownik");
        settings();
        add_components();
        setWho_logged(data);
        window.setVisible(true);
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
        panels();

    }
    private void panels(){
        buttons();
        up = new JPanel();
        up.add(who_logged);

        center = new JPanel();

        down = new JPanel();
        down.add(log_out);
    }
    private void buttons(){
        log_out = new JButton("Wyloguj się");
        log_out.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dataBase.getConn().close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                Start_window win = new Start_window();
                exit();
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
    }
    private void add_components() throws SQLException {
        components();
        window.add(up, BorderLayout.NORTH);
        window.add(center, BorderLayout.CENTER);
        window.add(down, BorderLayout.SOUTH);
    }
    private void exit(){
        window.setVisible(false);
        window.dispose();
    }
}
