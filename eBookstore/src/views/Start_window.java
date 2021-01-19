package views;
import models.Image;
import models.dataBaseConnection;
import models.WindowMethods;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;


public class Start_window {
    private JButton log, reg, exit;
    private JPanel up, center, down;
    private Image image;
    private dataBaseConnection dataBase = new dataBaseConnection();
    private WindowMethods windowMethods = new WindowMethods();

    public void create() throws SQLException {
        windowMethods.window = new JFrame("eBookstore");
        windowMethods.settings();
        add_components();
        dataBase.connect();
        windowMethods.window.setVisible(true);
    }
    private void components(){
        log = new JButton("Zaloguj się");
        log.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Log_panel log = new Log_panel();
                windowMethods.exit();
                log.create();
            }
        });
        reg = new JButton("Zarejestruj się");
        reg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Reg_panel reg = new Reg_panel();
                windowMethods.exit();
                reg.create();
            }
        });
        exit = new JButton("Wyjdź");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dataBase.disconnect();
                    System.out.println("Do widzenia!");
                    //exit();
                    windowMethods.exit();
                    System.exit(0);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        exit.setPreferredSize(new Dimension(140, 60));
        image = new Image("eksiegarnia.jpg");
    }
    private void panels(){
        components();
        up = new JPanel();
        up.add(image);
        up.setPreferredSize(new Dimension(600, 300));
        center = new JPanel();
        center.setLayout(new GridLayout(3,2));
        center.add(reg);
        center.add(log);
        down = new JPanel();
        down.setPreferredSize(new Dimension(600, 100));
        down.add(exit);
    }
    private void add_components(){
        panels();
        windowMethods.window.add(up, BorderLayout.NORTH);
        windowMethods.window.add(center, BorderLayout.CENTER);
        windowMethods.window.add(down, BorderLayout.SOUTH);

    }
}
