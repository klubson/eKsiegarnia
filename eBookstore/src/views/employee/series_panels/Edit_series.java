package views.employee.series_panels;

import models.dataBaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Edit_series extends JFrame {
    private JFrame window;
    private JButton back, edit;
    private JLabel title, tomes;
    private JTextField tomes2;
    private JPanel title_pane, tomes_pane, center, down;
    private dataBaseConnection dataBase = new dataBaseConnection();
    private String user, message = "W następujących polach wykryto błędy: ", beginTitle = "Tytuł serii: ", toEdit;
    private Dimension dimension = new Dimension(250, 20);
    private int error_counter;
    private boolean isManager;

    public void create(String data, String titleToEdit, boolean mode){
        window = new JFrame("Edytuj serię książek");
        settings();
        user = data;
        toEdit = titleToEdit;
        isManager = mode;
        add_components();
        title.setText(beginTitle + titleToEdit);
        window.setVisible(true);
    }
    private void settings(){
        window.setSize(600, 300);
        window.setLocation(400, 80);
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
    }
    private void components(){
        title = new JLabel();
        tomes = new JLabel("Liczba tomów: ");
        back = new JButton("Powrót");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Series ss = new Series();
                exit();
                try {
                    ss.create(user, isManager);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        edit = new JButton("Dodaj");
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(check()){
                    try {
                        dataBase.getConn().setAutoCommit(true);
                        dataBase.setStmt();
                        int tom;
                        if(tomes2.getText().equals("")) tom = 0;
                        else tom = Integer.parseInt(tomes2.getText());
                        int changes = dataBase.getStmt().executeUpdate(
                                "UPDATE Seria SET Liczba_tomow = " + tom
                        );
                        JOptionPane.showMessageDialog(window, "Seria edytowana pomyślnie");
                        System.out.println("Edytowano " + changes + "krotkę");
                        dataBase.getStmt().close();
                        Series ss = new Series();
                        exit();
                        ss.create(user, isManager);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        });
        tomes2 = new JTextField();
        tomes2.setName("LICZBA TOMÓW");
        tomes2.setPreferredSize(dimension);
        tomes2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
    private void panels(){
        components();
        title_pane = new JPanel();
        title_pane.add(title);

        tomes_pane = new JPanel();
        tomes_pane.add(tomes);
        tomes_pane.add(tomes2);

        center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
        center.add(title_pane);
        center.add(tomes_pane);

        down = new JPanel();
        down.setLayout(new BorderLayout());
        down.add(back, BorderLayout.WEST);
        down.add(edit, BorderLayout.EAST);
    }
    private void add_components(){
        panels();
        window.add(center, BorderLayout.CENTER);
        window.add(down, BorderLayout.SOUTH);
    }
    private boolean check(){
        error_counter = 0;
        numberCheck(tomes2, 0, 3);

        System.out.println(error_counter);

        if(error_counter != 0){
            JOptionPane.showMessageDialog(window, message, "Błąd!", JOptionPane.ERROR_MESSAGE);
            message = "W następujących polach wykryto błędy: ";
            return false;
        }
        else return true;
    }
    private void numberCheck(JTextField field, int min_size, int max_size){
        if(field.getText().length() < min_size || field.getText().length() > max_size){
            message += "\n" + field.getName();
            error_counter++;
        }
        for (int i = 0; i < field.getText().length(); i++) {
            if (!Character.isDigit(field.getText().charAt(i))) {
                message += "\n" + field.getName();
                error_counter++;
            }
        }
    }
    private void exit(){
        window.setVisible(false);
        window.dispose();
    }
}
