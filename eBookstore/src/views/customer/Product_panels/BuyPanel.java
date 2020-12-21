package views.customer.Product_panels;

import models.CartInfo;
import models.WindowMethods;
import models.dataBaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BuyPanel {
    private WindowMethods windowMethods = new WindowMethods();
    private dataBaseConnection dataBase;
    private JButton back , buy;
    private JLabel info;
    private JSpinner amountSpinner;
    private SpinnerNumberModel amountSpinnerModel;
    private JPanel center, down;
    private  int produktId ;
    private double productPrice;
    private CartInfo cart;
    public BuyPanel(dataBaseConnection dataBase, int produktId , double productPrice , CartInfo cart) {
        this.dataBase = dataBase;
        this.productPrice = productPrice;
        this.produktId = produktId;
        this.cart = cart;
        windowMethods.window = new JFrame("Kupowanie");
        windowMethods.settings();
        windowMethods.window.setSize(400, 250);
        windowMethods.window.setLocation(800, 80);
        addComponents();
        windowMethods.window.setVisible(true);
    }
    //TODO ustawić poprawnie maksymalną wartość spinnera, dodawanie zmniejsza stan w magazynie
    private void addComponents() {
        prepButtons();
        prepLabels();
        amountSpinnerModel = new SpinnerNumberModel(1 , 1, 100 , 1);
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
