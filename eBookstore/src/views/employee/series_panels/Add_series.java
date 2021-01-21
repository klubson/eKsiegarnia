package views.employee.series_panels;

import models.DataVerification;
import models.WindowMethods;
import models.dataBaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Add_series {
    private WindowMethods windowMethods = new WindowMethods();
    private JButton back, add;
    private JLabel title, tomes;
    private JTextField title2, tomes2;
    private JPanel title_pane, tomes_pane, center, down;
    private dataBaseConnection dataBase = new dataBaseConnection();
    private String user;
    private  boolean isManager;

    public Add_series() {
    }

    public void create(String data, boolean mode){
        windowMethods.window = new JFrame("Dodaj serię książek");
        windowMethods.settings();
        windowMethods.window.setSize(600, 300);
        user = data;
        isManager = mode;
        add_components();
        windowMethods.window.setVisible(true);
    }
    private void components(){
        title = new JLabel("Tytuł serii (max 50 znaków): ");
        tomes = new JLabel("Liczba tomów: ");
        title2 = windowMethods.setJTextField(title2, "TYTUŁ SERII");
        tomes2 = windowMethods.setJTextField(tomes2, "LICZBA TOMÓW");
        back = new JButton("Powrót");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Series ss = new Series();
                windowMethods.exit();
                try {
                    ss.create(user, isManager);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        add = new JButton("Dodaj");
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(check()){
                    try {
                        dataBase.getConn().setAutoCommit(true);
                        dataBase.setStmt();
                        ResultSet rs = dataBase.getStmt().executeQuery(
                                "SELECT Tytul FROM Seria WHERE Tytul = '" + title2.getText() + "'"
                        );
                        if(rs.next()){
                            JOptionPane.showMessageDialog(windowMethods.window, "Seria o takim tytule już istnieje!", "Błąd", JOptionPane.ERROR_MESSAGE);
                            rs.close();
                            dataBase.getStmt().close();
                        }
                        else{
                            int tom;
                            if(tomes2.getText().equals("")) tom = 0;
                            else tom = Integer.parseInt(tomes2.getText());
                            int changes = dataBase.getStmt().executeUpdate(
                                    "INSERT INTO Seria VALUES('" + title2.getText() + "'," + tom + ")"
                            );
                            JOptionPane.showMessageDialog(windowMethods.window, "Seria dodana pomyślnie");
                            System.out.println("Dodano " + changes + " rekord");
                            rs.close();
                            dataBase.getStmt().close();
                            Series ss = new Series();
                            windowMethods.exit();
                            ss.create(user, isManager);
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        });
    }
    private void panels(){
        components();
        title_pane = new JPanel();
        title_pane.add(title);
        title_pane.add(title2);

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
        down.add(add, BorderLayout.EAST);
    }
    private void add_components(){
        panels();
        windowMethods.window.add(center, BorderLayout.CENTER);
        windowMethods.window.add(down, BorderLayout.SOUTH);
    }
    private boolean check(){
        DataVerification verify = new DataVerification();
        title2.setText(title2.getText().trim().replaceAll("\\s{2,}", " "));
        verify.fieldCheck(title2, 1, 50, true, true);
        verify.numberCheck(tomes2, 0, 3);
        verify.errorMessage();
        if(verify.error_counter == 0) return true;
        else return false;
    }
}
