package views.employee.publisher_panels;

import models.DataVerification;
import models.WindowMethods;
import models.dataBaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Edit_publisher {
    private WindowMethods windowMethods = new WindowMethods();
    private JLabel name, country;
    private JTextField name2, country2;
    private JButton back, add;
    private JPanel center, down, name_pane, country_pane;
    private String user;
    private dataBaseConnection dataBase = new dataBaseConnection();
    private int ID;
    private boolean isManager;

    public void create(String data, int publisherID, boolean mode) throws SQLException {
        windowMethods.window = new JFrame("Edytuj wydawnictwo");
        windowMethods.settings();
        windowMethods.window.setSize(600, 300);
        user = data;
        ID = publisherID;
        isManager = mode;
        add_components();
        setPublisherData(ID);
        windowMethods.window.setVisible(true);
    }
    private void setPublisherData(int id) throws SQLException {
        dataBase.setStmt();
        ResultSet rs = dataBase.getStmt().executeQuery(
                "SELECT Nazwa, Kraj_pochodzenia FROM Wydawnictwo WHERE ID_wydawnictwa = '" +
                        id + "'"
        );
        rs.next();
        name2.setText(rs.getString(1));
        country2.setText(rs.getString(2));
        rs.close();
        dataBase.getStmt().close();
    }
    private void components(){
        name = new JLabel("Nazwa wydawnictwa (max 30 znaków): ");
        country = new JLabel("Kraj pochodzenia (max 30 znaków): ");
        name2 = windowMethods.setJTextField(name2, "NAZWA");
        country2 = windowMethods.setJTextField(country2, "KRAJ POCHODZENIA");
        back = new JButton("Powrót");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Publishers pb = new Publishers();
                windowMethods.exit();
                try {
                    pb.create(user, isManager);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        add = new JButton("Edytuj");
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //sprawdzenie i edycja
                if(check()){
                    try {
                        dataBase.setStmt();
                        dataBase.getConn().setAutoCommit(true);
                        int changes = dataBase.getStmt().executeUpdate(
                                "UPDATE Wydawnictwo SET Nazwa = '" + name2.getText() +
                                        "', Kraj_pochodzenia = '" + country2.getText() +
                                        "' WHERE ID_wydawnictwa = " + ID
                        );
                        JOptionPane.showMessageDialog(windowMethods.window, "Wydawnictwo edytowane pomyślnie");
                        System.out.println("Zaktualizowano " + changes + " rekord");
                        dataBase.getStmt().close();
                        Publishers pb = new Publishers();
                        windowMethods.exit();
                        pb.create(user, isManager);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        });
    }
    private void panels(){
        components();
        name_pane = new JPanel();
        name_pane.add(name);
        name_pane.add(name2);
        country_pane = new JPanel();
        country_pane.add(country);
        country_pane.add(country2);

        center = new JPanel();
        center.add(name_pane);
        center.add(country_pane);

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
        verify.fieldCheck(name2, 1, 30, true, true);
        verify.fieldCheck(country2, 0, 30, false, true);
        verify.errorMessage();
        if(verify.error_counter == 0) return true;
        else return false;
    }
}
