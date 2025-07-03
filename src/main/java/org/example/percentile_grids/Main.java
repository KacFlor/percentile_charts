package org.example.percentile_grids;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Failed to set Look and Feel", e);
            }

            JFrame frame = new JFrame("Charts");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

            JLabel titleLabel = new JLabel("Select a chart", SwingConstants.CENTER);
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));
            buttonPanel.add(titleLabel);

            buttonPanel.add(Box.createVerticalGlue());

            buttonPanel.add(createStyledButton("Weight for Age (Boys)", frame, new WeightToAgeBoysChartPanel(), buttonPanel));
            buttonPanel.add(Box.createVerticalStrut(15));
            buttonPanel.add(createStyledButton("Weight for Age (Girls)", frame, new WeightToAgeGirlsChartPanel(), buttonPanel));
            buttonPanel.add(Box.createVerticalStrut(15));
            buttonPanel.add(createStyledButton("Height for Age (Boys)", frame, new HeightToAgeBoysChartPanel(), buttonPanel));
            buttonPanel.add(Box.createVerticalStrut(15));
            buttonPanel.add(createStyledButton("Height for Age (Girls)", frame, new HeightToAgeGirlsChartPanel(), buttonPanel));
            buttonPanel.add(Box.createVerticalStrut(15));
            buttonPanel.add(createStyledButton("BMI", frame, new BMIChartPanel(), buttonPanel));

            buttonPanel.add(Box.createVerticalGlue());

            JLabel footerLabel = new JLabel("\n" + "© 2025 Medical Charts | version 1.0", SwingConstants.CENTER);
            footerLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
            footerLabel.setForeground(Color.GRAY);
            footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            footerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
            buttonPanel.add(footerLabel);

            frame.add(buttonPanel, BorderLayout.CENTER);

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = screenSize.width;
            int height = screenSize.height;
            frame.setSize(width, height);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);
        });
    }

    private static JButton createStyledButton(String text, JFrame frame, JPanel chartPanel, JPanel buttonPanel) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(250, 50));
        button.setMaximumSize(new Dimension(300, 60));

        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 128, 0), 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        button.addActionListener(e -> button.setBackground(new Color(0, 204, 0)));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(240, 255, 240));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
            }
        });

        button.addActionListener(e -> openChartPanel(frame, chartPanel, buttonPanel));

        return button;
    }

    private static void openChartPanel(JFrame frame, JPanel chartPanel, JPanel buttonPanel) {
        JPanel chartWrapper = new JPanel(new BorderLayout());
        chartWrapper.add(chartPanel, BorderLayout.CENTER);

        JButton backButton = createBackButton(frame, buttonPanel);

        JPanel buttonPanelWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanelWrapper.add(backButton);

        chartWrapper.add(buttonPanelWrapper, BorderLayout.SOUTH);

        frame.getContentPane().removeAll();
        frame.add(chartWrapper, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    private static JButton createBackButton(JFrame frame, JPanel buttonPanel) {
        JButton backButton = new JButton("Return to menu");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        backButton.setPreferredSize(new Dimension(250, 50));
        backButton.setMaximumSize(new Dimension(300, 60));
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(Color.BLACK);
        backButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 128, 0), 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        backButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.add(buttonPanel, BorderLayout.CENTER);
            frame.revalidate();
            frame.repaint();
        });
        return backButton;
    }
}
