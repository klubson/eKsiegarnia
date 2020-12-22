package views.customer.cartsPanels;

import models.CartInfo;
import models.dataBaseConnection;
import views.customer.Customer_panel;
import views.customer.ShoppingHistoryPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class PreviousCartPanel extends CartInfoPanel {
    private CartInfo currentCart;
    public PreviousCartPanel(String user, dataBaseConnection dataBase, CartInfo cart, CartInfo currentCart) {
        super(user, dataBase, cart);
        this.currentCart = currentCart;
        windowMethods.window.setTitle("Koszyk zakupowy");
    }

    protected void buttons() {
        super.buttons();
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                windowMethods.exit();
                new ShoppingHistoryPanel(user, dataBase, currentCart);
            }
        });
    }
}
