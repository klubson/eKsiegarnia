package views;
import models.Image;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Start_window extends JFrame {
    private JFrame window;
    private JButton log, reg, exit;
    private JPanel up, center, down;
    private Image image;

    public void create(){
        window = new JFrame("eBookstore");
        settings();
        add_components();
        window.setVisible(true);
    }

    private void settings(){
        window.setSize(600, 600);
        window.setLocation(400, 80);
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
    }
    private void components(){
        log = new JButton("Zaloguj się");
        log.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Log_panel log = new Log_panel();
                exit();
                log.create();
            }
        });
        //log.setPreferredSize(new Dimension(50, 50));
        reg = new JButton("Zarejestruj się");
        reg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Reg_panel reg = new Reg_panel();
                exit();
                reg.create();
            }
        });
        //reg.setPreferredSize(new Dimension(50, 50));
        exit = new JButton("Wyjdź");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
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
        window.add(up, BorderLayout.NORTH);
        window.add(center, BorderLayout.CENTER);
        window.add(down, BorderLayout.SOUTH);

    }
    private void exit(){
        window.setVisible(false);
        window.dispose();
    }
}
