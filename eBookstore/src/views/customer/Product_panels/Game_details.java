package views.customer.Product_panels;

import models.CartInfo;
import models.dataBaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Game_details extends JFrame {

    private JFrame window;
    private JLabel name, price, year, storage, storage2, product_type, publisher, authors, min_players, max_players, min_age, est_time;
    private dataBaseConnection dataBase = new dataBaseConnection();
    private JButton back , buy;
    private JPanel center, down, storage_panel;
    private  int produktId ;
    private double productPrice;
    private CartInfo cart;

    public void create(int product_id , dataBaseConnection dataBase , CartInfo cart) throws SQLException {
        this.dataBase = dataBase;
        this.cart = cart;
        window = new JFrame("Szczegóły produktu");
        settings();
        add_components();
        setLabels(product_id);
        produktId = product_id;
        window.setVisible(true);
    }
    private void settings(){
        window.setSize(400, 250);
        window.setLocation(800, 80);
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
    }
    private void setLabels(int id) throws SQLException {
        dataBase.setStmt();
        ResultSet rs = dataBase.getStmt().executeQuery(
                "SELECT Nazwa, Cena, Rok_wydania, Stan_magazyn, Wydawnictwo_ID_wydawnictwa, co " +
                        "FROM Produkt WHERE ID_produktu = " + id
        );
        rs.next();
        name.setText(name.getText() + rs.getString(1));
        price.setText(price.getText() + Float.toString(rs.getFloat(2)));
        productPrice = rs.getFloat(2);
        year.setText(year.getText() + rs.getString(3));
        int amount = rs.getInt(4);
        if(amount == 0){
            storage2.setText("Produkt niedostępny");
            storage2.setForeground(Color.RED);
        }
        else if(amount <= 6){
            storage2.setText("Ostatnie sztuki");
            storage2.setForeground(Color.ORANGE);
        }
        else{
            storage2.setText("Produkt dostępny");
            storage2.setForeground(Color.GREEN);
        }
        int publisher_id = rs.getInt(5);
        String product_tmp = rs.getString(6);
        if(product_tmp.equals("k")){
            product_type.setText(product_type.getText() + "książka");
        }
        else if(product_tmp.equals("g")){
            product_type.setText(product_type.getText() + "gra planszowa");
        }
        rs.close();
        rs = dataBase.getStmt().executeQuery(
                "SELECT Nazwa FROM Wydawnictwo WHERE ID_wydawnictwa = " + publisher_id
        );
        rs.next();
        publisher.setText(publisher.getText() + rs.getString(1));
        rs.close();
        rs = dataBase.getStmt().executeQuery(
                "SELECT a.Imie, a.Nazwisko FROM Autor a JOIN Autor_produktu b ON b.Autor_ID_autora = a.ID_autora WHERE Produkt_ID_produktu = " + id
        );
        String authors_tmp = authors.getText();
        while (rs.next()){
            authors_tmp += rs.getString(1) + " " + rs.getString(2) + ", ";
        }
        rs.close();
        if(authors_tmp.length() > 10) authors_tmp = authors_tmp.substring(0, authors_tmp.length() - 2);
        authors.setText(authors_tmp);
        rs = dataBase.getStmt().executeQuery(
                "SELECT g_Min_gracze, g_Max_gracze, g_Min_wiek, g_Czas_gry FROM Gra_planszowa WHERE ID_produktu = " + id
        );
        rs.next();
        min_players.setText(min_players.getText() + Integer.toString(rs.getInt(1)));
        max_players.setText(max_players.getText() + Integer.toString(rs.getInt(2)));
        min_age.setText(min_age.getText() + Integer.toString(rs.getInt(3)));
        est_time.setText(est_time.getText() + Integer.toString(rs.getInt(4)));
        rs.close();
        dataBase.getStmt().close();
    }
    private void labels(){
        name = new JLabel("Nazwa produktu: ");
        price = new JLabel("Cena: ");
        year = new JLabel("Rok wydania: ");
        storage = new JLabel("Dostępność: ");
        storage2 = new JLabel("");
        product_type = new JLabel("Typ produktu: ");
        publisher = new JLabel("Nazwa wydawnictwa: ");
        authors = new JLabel("Autorzy: ");
        min_players = new JLabel("Minimalna liczba graczy: ");
        max_players = new JLabel("Maksymalna liczba graczy: ");
        min_age = new JLabel("Zalecany minimalny wiek: ");
        est_time = new JLabel("Szacowany czas gry (minuty): ");
    }
    private void components(){
        labels();
        back = new JButton("Powrót");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });

        buy = new JButton("Dodaj do koszyka");
        buy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BuyPanel(dataBase,produktId,productPrice,cart  );
            }
        });
    }
    private void panels(){
        components();
        storage_panel = new JPanel();
        storage_panel.add(storage);
        storage_panel.add(storage2);

        center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
        center.add(name);
        center.add(price);
        center.add(year);
        center.add(storage_panel);
        center.add(product_type);
        center.add(publisher);
        center.add(authors);
        center.add(min_players);
        center.add(max_players);
        center.add(min_age);
        center.add(est_time);

        down = new JPanel();
        down.setLayout(new BorderLayout());
        down.add(back, BorderLayout.WEST);
    }
    private void add_components(){
        panels();
        window.add(center, BorderLayout.CENTER);
        window.add(down, BorderLayout.SOUTH);
        down.add(buy , BorderLayout.EAST);
    }
    private void exit(){
        window.setVisible(false);
        window.dispose();
    }
}
