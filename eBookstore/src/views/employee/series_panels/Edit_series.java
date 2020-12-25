package views.employee.series_panels;

import models.DataVerification;
import models.WindowMethods;
import models.dataBaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Edit_series {
    private WindowMethods windowMethods = new WindowMethods();
    private JButton back, edit;
    private JLabel title, tomes;
    private JTextField tomes2;
    private JPanel title_pane, tomes_pane, center, down;
    private dataBaseConnection dataBase = new dataBaseConnection();
    private String user, beginTitle = "Tytuł serii: ", toEdit;
    private boolean isManager;

    public void create(String data, String titleToEdit, boolean mode){
        windowMethods.window = new JFrame("Edytuj serię książek");
        windowMethods.settings();
        windowMethods.window.setSize(600, 300);
        user = data;
        toEdit = titleToEdit;
        isManager = mode;
        add_components();
        title.setText(beginTitle + titleToEdit);
        windowMethods.window.setVisible(true);
    }
    private void components(){
        title = new JLabel();
        tomes = new JLabel("Liczba tomów: ");
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
        edit = new JButton("Edytuj");
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
                                "UPDATE Seria SET Liczba_tomow = " + tom + "WHERE Tytul = '"
                                + toEdit + "'"
                        );
                        JOptionPane.showMessageDialog(windowMethods.window, "Seria edytowana pomyślnie");
                        System.out.println("Edytowano " + changes + "krotkę");
                        dataBase.getStmt().close();
                        Series ss = new Series();
                        windowMethods.exit();
                        ss.create(user, isManager);
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
        windowMethods.window.add(center, BorderLayout.CENTER);
        windowMethods.window.add(down, BorderLayout.SOUTH);
    }
    private boolean check(){
        DataVerification verify = new DataVerification();
        verify.numberCheck(tomes2, 0, 3);
        verify.errorMessage();
        if(verify.error_counter == 0) return true;
        else return false;
    }
}
