package views;
import models.Image;
import models.dataBaseConnection;
import views.customer.Customer_panel;
import views.employee.supplier.Employee_panel;
import views.employee.manager.Manager_panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class Log_panel extends JFrame {

    private JFrame window;
    private JCheckBox pass_visibility;
    private JLabel log, pass;
    private JPasswordField pass2;
    private JTextField log2;
    private JButton back, sign_in;
    private JPanel up, center, down, login, passwd;
    private int dim_wdt = 250, dim_ht = 20;
    private dataBaseConnection dataBase = new dataBaseConnection();
    private Image image = new Image("eksiegarnia.jpg");



    public void create(){
        window = new JFrame("Logowanie");
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
        pass_visibility = new JCheckBox("Pokaż hasło");
        pass_visibility.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(pass_visibility.isSelected()){
                    pass2.setEchoChar((char)0);
                }
                else pass2.setEchoChar('\u25CF');
            }
        });
        back = new JButton("Powrót");
        //back.setPreferredSize(new Dimension(100, 40));
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
                Start_window win2 = new Start_window();
                try {
                    win2.create();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        sign_in = new JButton("Zaloguj");
        //sign_in.setPreferredSize(new Dimension(100, 40));
        sign_in.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dataBase.setStmt();
                    ResultSet rs = dataBase.getStmt().executeQuery(
                           "SELECT Login FROM Uzytkownik WHERE Login = " + "'" + log2.getText() + "'"
                   );
                    //if(dataBase.findUser(log2.getText())){
                    if(rs.next()){
                        rs.close();
                        dataBase.setCstmt("{? = call checkPwd(?,?)}");
                        dataBase.getCstmt().setString(2, log2.getText());
                        String tmp = String.copyValueOf(pass2.getPassword());
                        dataBase.getCstmt().setString(3, tmp);
                        dataBase.getCstmt().registerOutParameter(1, Types.INTEGER);
                        dataBase.getCstmt().execute();
                        int pass_good = dataBase.getCstmt().getInt(1);
                        dataBase.getCstmt().close();
                        if(pass_good == 0){
                            JOptionPane.showMessageDialog(window, "Niepoprawne hasło!",
                                    "Błąd logowania!", JOptionPane.ERROR_MESSAGE);
                        }
                        else{
                            String data = log2.getText();
                            rs = dataBase.getStmt().executeQuery(
                                    "SELECT kto FROM Uzytkownik WHERE Login = " +
                                            "'" + log2.getText() + "'"
                            );
                            rs.next();
                            String userType = rs.getString(1);
                            rs.close();
                            if(userType.equals("k")){
                                System.out.println("Zalogowano jako (login): " + data);
                                Customer_panel cp = new Customer_panel();
                                exit();
                                cp.create(data);
                            }
                            else if(userType.equals("p")){
                                rs = dataBase.getStmt().executeQuery(
                                        "SELECT P_STANOWISKO FROM Pracownik WHERE Login = " +
                                                "'" + log2.getText() + "'"
                                );
                                rs.next();
                                String jobType = rs.getString(1);
                                System.out.println("Zalogowano jako (login): " + data);
                                rs.close();
                                dataBase.getStmt().close();
                                if (jobType.equals("kierownik")){
                                    Manager_panel new_wind = new Manager_panel();
                                    exit();
                                    new_wind.create(data);
                                }
                                else if(jobType.equals("magazynier")){
                                    Employee_panel new_wind = new Employee_panel();
                                    exit();
                                    new_wind.create(data);
                                }
                            }
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(window, "Brak użytkownika o podanym loginie!",
                                "Błąd logowania!", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        log = new JLabel("Login: ");
        pass = new JLabel("Hasło: ");
        log2 = new JTextField();
        log2.setPreferredSize(new Dimension(dim_wdt, dim_ht));
        log2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        pass2 = new JPasswordField();
        pass2.setPreferredSize(new Dimension(dim_wdt, dim_ht));
        pass2.setEchoChar('\u25CF');
        pass2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
    private void panels(){
        components();
        up = new JPanel();
        up.setPreferredSize(new Dimension(600, 400));
        up.add(image);
        login = new JPanel();
        login.add(log);
        login.add(log2);
        passwd = new JPanel();
        passwd.add(pass);
        passwd.add(pass2);
        passwd.add(pass_visibility);
        center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
        center.add(login);
        center.add(passwd);

        down = new JPanel();
        //down.setPreferredSize(new Dimension(600, 100));
        down.setLayout(new BorderLayout());
        down.add(back, BorderLayout.WEST);
        down.add(sign_in, BorderLayout.EAST);
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
