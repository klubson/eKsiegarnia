package views.customer.Product_panels;

import models.CartInfo;
import models.WindowMethods;
import models.dataBaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BuyPanel {
    protected WindowMethods windowMethods = new WindowMethods();
    private dataBaseConnection dataBase;
    protected JButton back , buy;
    private JLabel info;
    protected JSpinner amountSpinner;
    protected SpinnerNumberModel amountSpinnerModel;
    private JPanel center, down;
    private  int produktId ;
    private double productPrice;
    private CartInfo cart;
    private int maxAmount;
    public BuyPanel(dataBaseConnection dataBase, int produktId , double productPrice , CartInfo cart) {
        this.dataBase = dataBase;
        this.productPrice = productPrice;
        this.produktId = produktId;
        this.cart = cart;
        try {
            dataBase.setStmt();
            ResultSet rs = dataBase.getStmt().executeQuery(
                    "Select stan_magazyn from produkt where id_produktu = " + produktId
            );
            rs.next();
            maxAmount = rs.getInt(1);
            rs.close();
            dataBase.getStmt().close();
        } catch (SQLException e) {
            System.out.println("Błąd przy ustalaniu mak wartości spinnera");
            e.printStackTrace();
        }
        if(maxAmount > 0){
            windowMethods.window = new JFrame("Kupowanie");
            windowMethods.settings();
            windowMethods.window.setSize(400, 250);
            windowMethods.window.setLocation(800, 80);
            addComponents();
            windowMethods.window.setVisible(true);
        }

    }

    private void addComponents() {
        prepButtons();
        prepLabels();
        amountSpinnerModel = new SpinnerNumberModel(1 , 1, maxAmount , 1);
        amountSpinner = new JSpinner(amountSpinnerModel);
        center = new JPanel();
        center.add(info);
        center.add(amountSpinner);
        down = new JPanel();
        down.setLayout(new BorderLayout());
        down.add(back, BorderLayout.WEST);
        down.add(buy, BorderLayout.EAST);
        windowMethods.window.add(center, BorderLayout.CENTER);
        windowMethods.window.add(down, BorderLayout.SOUTH);
    }

    private void prepLabels()
    {
        info = new JLabel("Ilość sztuk");
    }

    private void prepButtons(){
        back = new JButton("Powrót");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                windowMethods.exit();
            }
        });

        buy = new JButton("Dodaj do koszyka");
        buy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //FOO
                int amount = (int) amountSpinnerModel.getNumber();
                dataBase.newCartItem(cart, produktId, productPrice *amount , amount);
                windowMethods.exit();
            }
        });
    }
}
