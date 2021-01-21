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
import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrentCartPanel extends CartInfoPanel {
    private JButton details , buy , delete,paymentMethods;
    private JPanel bottomPanel;
    private JComboBox paymentNames;
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
        paymentMethods = new JButton("Zmień sposób płatności");
        paymentMethods.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                        
                        dataBase.setStmt();
                        int changes = dataBase.getStmt().executeUpdate(
                                "Update koszyk_zakupowy SET sposob_platnosci = '" + (String)paymentNames.getSelectedItem()  + "' WHERE nr_koszyka = "+ cart.getCartId()

                        );
                        System.out.println("Zmodyfikowano " + changes + " krotkę");
                        dataBase.getStmt().close();
                        paymentMethod.setText("Sposób płatności " + (String)paymentNames.getSelectedItem());





                } catch (SQLException throwables) {
                    System.out.println("Błąd przy usuwaniu produktu z obecnego koszyka");
                    throwables.printStackTrace();
                }
            }
        });
        delete = new JButton("Usuń z koszyka");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(table.getModel().getRowCount() > 0){
                        int id = Integer.parseInt((String) table.getModel().getValueAt(table.getSelectedRow(), 6 ));
                        int sztuk = Integer.parseInt((String) table.getModel().getValueAt(table.getSelectedRow(), 3 ));
                        double cena = Double.parseDouble((String) table.getModel().getValueAt(table.getSelectedRow(), 4 ));
                        int lp = Integer.parseInt((String) table.getModel().getValueAt(table.getSelectedRow(), 0 ));
                        dataBase.setStmt();
                        int changes = dataBase.getStmt().executeUpdate(
                                "Update produkt SET stan_magazyn = stan_magazyn + " + sztuk  + " WHERE id_produktu = "+ id

                        );
                        System.out.println("Zmodyfikowano " + changes + " krotkę");
                        changes = dataBase.getStmt().executeUpdate(
                                "Update koszyk_zakupowy SET wartosc_zakupow = wartosc_zakupow - " + cena  +
                                        " , calkowita_wartosc_zamowienia = calkowita_wartosc_zamowienia - "+cena
                                        +" WHERE nr_koszyka = "+ cart.getCartId()

                        );
                        System.out.println("Zmodyfikowano " + changes + " krotkę");
                        int deleted = dataBase.getStmt().executeUpdate(
                                "DELETE FROM element_koszyka WHERE \"L.p.\" = " + lp + " AND  koszyk_zakupowy_nr_koszyka = " + cart.getCartId()
                        );
                        dataBase.getStmt().close();
                        System.out.println("Usunięto "+deleted + " rekodów");
                        tableModel.removeRow(table.getSelectedRow());

                        dataBase.setStmt();

                        ResultSet rs ;

                        rs = dataBase.getStmt().executeQuery(
                                "Select  calkowita_wartosc_zamowienia  FROM koszyk_zakupowy WHERE nr_koszyka = " + cart.getCartId()
                        );
                        rs.next();

                        totalCost.setText("Łącznie do zapłaty " + rs.getString(1));

                        rs.close();
                        dataBase.getStmt().close();
                    }


                } catch (SQLException throwables) {
                    System.out.println("Błąd przy usuwaniu produktu z obecnego koszyka");
                    throwables.printStackTrace();
                }
            }
        });
        details = new JButton("Szczegóły");
        details.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(table.getModel().getRowCount() > 0){
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
                    }


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
                    if(cart.getLP() > 0){
                        dataBase.getConn().commit();
                        cart = new CartInfo(dataBase.newCart(user));
                        String message = "Dokonano pomyślnie transkcji";
                        JOptionPane.showMessageDialog(new JFrame(), message);
                        Customer_panel cp = new Customer_panel();
                        windowMethods.exit();
                        try {
                            cp.createFromBack(user , dataBase , cart);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }

                } catch (SQLException ex) {
                    System.out.println("Błąd przy finalizacji transakcji");
                    ex.printStackTrace();
                }


            }
        });
    }

    @Override
    protected void panels(){
        super.panels();
        down.add(details, BorderLayout.CENTER);
        bottomPanel = new JPanel();
        String[] pom = {"BLIK" , "Karta" , "PayPal"};
        paymentNames = new JComboBox(pom);
        paymentNames.setSelectedIndex(0);
        bottomPanel.add(paymentNames);
        bottomPanel.add(paymentMethods);
        bottomPanel.add(delete);
        down.add(bottomPanel, BorderLayout.SOUTH);
        down.add(buy, BorderLayout.EAST);
    }
}
