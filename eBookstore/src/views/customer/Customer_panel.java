package views.customer;

import models.CartInfo;
import models.Current_date;
import models.WindowMethods;
import models.dataBaseConnection;
import views.Start_window;
import views.customer.Product_panels.Products_customer;
import views.customer.cartsPanels.CurrentCartPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
//TODO sprawdzić co przy włączaniu po przez X
public class Customer_panel {
    private WindowMethods windowMethods = new WindowMethods();
    private JButton log_out, shopping_history, current_cart, product_list, author_list, series_list, edit_profile;
    private JPanel up, center, down;
    private JLabel who_logged, current_time;
    private String user;
    private dataBaseConnection dataBase = new dataBaseConnection();
    private Current_date time = new Current_date();
    private CartInfo cart;

    public void createCommon(String data)  throws SQLException
    {
        windowMethods.window = new JFrame("Panel klienta");
        windowMethods.settings();
        windowMethods.window.setSize(600, 450);
        add_components();
        user = data;
        windowMethods.setWho_logged(who_logged, user);
        windowMethods.window.setVisible(true);
        time.clock(current_time);
    }

    public void create(String data) throws SQLException {

        dataBase = new dataBaseConnection();
        createCommon(data);

        dataBase.getConn().setAutoCommit(false);
        cart= new CartInfo(dataBase.newCart(user));

    }

    public void createFromBack(String data , dataBaseConnection dataBase,CartInfo cart) throws SQLException{
        this.dataBase = dataBase;
        this.cart = cart;
        createCommon( data);
    }
    private void components() throws SQLException {
        who_logged = new JLabel();
        who_logged.setFont(who_logged.getFont().deriveFont(20.0f));
        current_time = new JLabel(time.getTime());
        current_time.setFont(current_time.getFont().deriveFont(20.0f));
        log_out = new JButton("Wyloguj się");
        log_out.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dataBase.getConn().rollback();
                } catch (SQLException ex) {
                    System.out.println("Błąd przy rollbacku przy wylogowywaniu się klienta");
                    ex.printStackTrace();
                }
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
        shopping_history = new JButton("Historia zakupów");
        shopping_history.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //historia zamówień
                new ShoppingHistoryPanel(user , dataBase , cart);
                time.stopClock();
                windowMethods.exit();
            }
        });
        current_cart = new JButton("Twój koszyk");
        current_cart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //obecny koszyk zakupowy
                new CurrentCartPanel(user , dataBase , cart);
                time.stopClock();
                windowMethods.exit();
            }
        });
        product_list = new JButton("Produkty w ofercie");
        product_list.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Products_customer pc = new Products_customer();

                time.stopClock();
                windowMethods.exit();
                try {
                    pc.create(user , dataBase , cart);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        author_list = new JButton("Autorzy");
        author_list.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Authors_customer ac = new Authors_customer();
                time.stopClock();
                windowMethods.exit();
                try {
                    ac.create(user,cart);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        series_list = new JButton("Serie książek");
        series_list.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Series_customer sc = new Series_customer();
                time.stopClock();
                windowMethods.exit();
                try {
                    sc.create(user,cart);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        edit_profile = new JButton("Edytuj profil");
        edit_profile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Edit_customer_profile ecp = new Edit_customer_profile();
                time.stopClock();
                windowMethods.exit();
                try {
                    ecp.create(user);
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
        center.add(series_list);
        center.add(current_cart);
        center.add(shopping_history);
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
