package views.customer.Product_panels;

import models.CartInfo;
import models.dataBaseConnection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BuyPanelProductTable extends BuyPanel {
    private Products_customer customer;
    public BuyPanelProductTable(dataBaseConnection dataBase, int produktId, double productPrice, CartInfo cart, Products_customer customer) {
        super(dataBase, produktId, productPrice, cart);
        this.customer=customer;
        if(buy != null)
        {
            buy.removeActionListener(buy.getActionListeners()[0]);
            buy.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //FOO
                    int amount = (int) amountSpinnerModel.getNumber();
                    dataBase.newCartItem(cart, produktId, productPrice *amount , amount);
                    customer.decreaseAmount(amount);
                    windowMethods.exit();
                }
            });
        }

    }
}
