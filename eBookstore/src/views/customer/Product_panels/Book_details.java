package views.customer.Product_panels;

import models.dataBaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Book_details extends JFrame {
    private JFrame window;
    private JLabel name, price, year, storage, storage2, product_type, publisher, authors, cover_type, pages, size, series;
    private dataBaseConnection dataBase = new dataBaseConnection();
    private JButton back;
    private JPanel center, down, storage_panel;

    public void create(int product_id) throws SQLException {
        window = new JFrame("Szczegóły produktu");
        settings();
        add_components();
        setLabels(product_id);
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
                "SELECT k_Typ_okladki, k_Liczba_stron, k_format, Seria_tytul FROM Ksiazka WHERE ID_produktu = " + id
        );
        rs.next();
        cover_type.setText(cover_type.getText() + rs.getString(1));
        pages.setText(pages.getText() + Integer.toString(rs.getInt(2)));
        size.setText(size.getText() + rs.getString(3));
        series.setText(series.getText() + rs.getString(4));
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
        cover_type = new JLabel("Typ okładki: ");
        pages = new JLabel("Liczba stron: ");
        size = new JLabel("Format książki: ");
        series = new JLabel("Tytuł serii: ");
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
        center.add(cover_type);
        center.add(pages);
        center.add(size);
        center.add(series);

        down = new JPanel();
        down.setLayout(new BorderLayout());
        down.add(back, BorderLayout.WEST);
    }
    private void add_components(){
        panels();
        window.add(center, BorderLayout.CENTER);
        window.add(down, BorderLayout.SOUTH);
    }
    private void exit(){
        window.setVisible(false);
        window.dispose();
    }
}
