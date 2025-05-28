package com.github.jaykkumar01;

import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;

public class PDFProjectSetupUI extends JFrame implements ResourceFolderExtractor.CopyListener {

    private final JProgressBar progressBar = new JProgressBar(0, 100);
    private final JLabel statusLabel = new JLabel("Starting PDF Project setup...");
    private final JButton okButton = new JButton("OK");

    private final JTextArea messageArea = new JTextArea(
            "Sample InputData placed in Downloads/PDFProject\n" +
                    "PDF Project is placed in .epsilon/Projects\n" +
                    "Custom Method JAR is placed in .epsilon/lib/commands"
    );

    public PDFProjectSetupUI() {
        setTitle("PDF Project Setup");
        setSize(500, 210);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ignored) {}

        // Progress bar configuration
        progressBar.setUI(new FlatProgressBarUI());
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(400, 6));
        progressBar.setForeground(new Color(0, 200, 100));

        // Message area setup (initially hidden)
        messageArea.setEditable(false);
        messageArea.setFocusable(false);
        messageArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
        messageArea.setBackground(new Color(250, 250, 250));
        messageArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setVisible(false); // Initially hidden

        // OK button
        okButton.setVisible(false);
        okButton.addActionListener(e -> System.exit(0));

        // Layout
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.add(statusLabel, BorderLayout.NORTH);
        centerPanel.add(progressBar, BorderLayout.CENTER);
        centerPanel.add(messageArea, BorderLayout.SOUTH); // Positioned above OK button

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(okButton, BorderLayout.CENTER);

        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(southPanel, BorderLayout.SOUTH);

        add(panel);
    }

    public void start() {
        new Thread(() -> new ResourceFolderExtractor().extractAll(this)).start();
    }

    @Override
    public void onProgress(int current, int total) {
        SwingUtilities.invokeLater(() -> {
            int progress = (int) ((current / (double) total) * 100);
            progressBar.setValue(progress);
            statusLabel.setText("Copying... " + progress + "%");

            if (progress >= 100) {
                statusLabel.setText("Setup complete!");
                okButton.setVisible(true);
                messageArea.setVisible(true); // Show message area when done
            }
        });
    }

    @Override
    public void onLog(String message) {
        SwingUtilities.invokeLater(() -> statusLabel.setText("Copying: " + message));
    }

    // Flat progress bar (simple)
    private static class FlatProgressBarUI extends BasicProgressBarUI {
        @Override
        protected void paintDeterminate(Graphics g, JComponent c) {
            int width = progressBar.getWidth();
            int height = progressBar.getHeight();
            int filled = (int) (width * progressBar.getPercentComplete());

            g.setColor(new Color(240, 240, 240));
            g.fillRect(0, 0, width, height);
            g.setColor(progressBar.getForeground());
            g.fillRect(0, 0, filled, height);
        }

        @Override
        protected Dimension getPreferredInnerHorizontal() {
            return new Dimension(super.getPreferredInnerHorizontal().width, 6);
        }
    }
}
