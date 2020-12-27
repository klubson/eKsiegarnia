package views.customer.cartsPanels;

import models.CartInfo;
import models.dataBaseConnection;
import views.customer.Customer_panel;
import views.customer.Product_panels.Book_details;
import views.customer.Product_panels.Game_details;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class CurrentCartPanel extends CartInfoPanel {
    private JButton details , buy;
    public CurrentCartPanel(String user, dataBaseConnection dataBase, CartInfo cart) {
        super(user, dataBase, cart);
        windowMethods.window.setTitle("Obecny koszyk");
    }

    @Override
    protected void buttons(){
        super.buttons();
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Customer_panel cp = new Customer_panel();
                windowMethods.exit();
                try {
                    cp.createFromBack(user , dataBase , cart);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        details = new JButton("Szczegóły");
        details.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    int id = Integer.parseInt((String) table.getModel().getValueAt(table.getSelectedRow(), 6 ));
                    String type = data.get(table.getSelectedRow()).get(5);
                    if(type.equals("książka")){
                        Book_details bd = new Book_details();
                        bd.create(id , dataBase , cart,user);
                    }
                    else if(type.equals("gra planszowa")){
                        Game_details gd = new Game_details();
                        gd.create(id , dataBase , cart,user);
                    }
                    windowMethods.exit();

                } catch (SQLException throwables) {
                    System.out.println("Błąd przy wyświetlaniu szczegółów o produkcie z obecnego koszyka");
                    throwables.printStackTrace();
                }
            }
        });

        buy = new JButton("Kup");
        buy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dataBase.getConn().commit();
                    cart = new CartInfo(dataBase.newCart(user));
                    String message = "Dokonano pomyślnie transkcji";
                    JOptionPane.showMessageDialog(new JFrame(), message);
                } catch (SQLException ex) {
                    System.out.println("Błąd przy finalizacji transakcji");
                    ex.printStackTrace();
                }

                Customer_panel cp = new Customer_panel();
                windowMethods.exit();
                try {
                    cp.createFromBack(user , dataBase , cart);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void panels(){
        super.panels();
        down.add(details, BorderLayout.CENTER);
        down.add(buy, BorderLayout.EAST);
    }
}
