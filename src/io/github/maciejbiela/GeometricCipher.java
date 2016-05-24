package io.github.maciejbiela;

import javax.swing.*;
import java.awt.*;

public class GeometricCipher {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            RootFrame frame = new RootFrame();
            frame.setSize(800, 600);
            frame.setTitle("\"Geometric\" Cipher Project");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
