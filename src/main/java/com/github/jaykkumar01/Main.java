package com.github.jaykkumar01;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PDFProjectSetupUI ui = new PDFProjectSetupUI();
            ui.setVisible(true);
            ui.start();
        });
    }
}
