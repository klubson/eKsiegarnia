package models;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Image extends JPanel {
    private BufferedImage image;
    private int divider = 1;

    public Image(String path)
    {
        super();
        File file = new File(path);
        try
        {
            image = ImageIO.read(file);
        } catch (IOException e)
        {
            System.err.println("Błąd odczytu obrazka");
            e.printStackTrace();
        }

        Dimension dimension = new Dimension(image.getWidth(), image.getHeight());
        setPreferredSize(dimension);
    }

    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(1.0/divider, 1.0/divider);
        g2d.drawImage(image, 0, 0, this);
    }
}
