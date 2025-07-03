package org.example.percentile_grids;

import org.jfree.chart.*;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.*;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.io.File;

public class WeightToAgeBoysChartPanel extends JPanel {
    private final XYSeries userSeries;
    private boolean singlePointMode = true;
    private final JLabel pointCounterLabel;
    private final JCheckBox connectPointsCheckBox;

    private XYSeries createSeries(String name, double[] xData, double[] yData) {
        XYSeries series = new XYSeries(name);
        for (int i = 0; i < xData.length; i++) {
            series.add(xData[i], yData[i]);
        }
        return series;
    }

    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("x");
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            return this;
        }
    }

    static class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private int currentRow = -1;
        private final XYSeries userSeries;
        private final DefaultTableModel tableModel;
        private final JLabel pointCounterLabel;
        private final JCheckBox connectPointsCheckBox;

        public ButtonEditor(JCheckBox checkBox,
                            XYSeries userSeries,
                            DefaultTableModel tableModel,
                            JLabel pointCounterLabel,
                            JCheckBox connectPointsCheckBox) {
            super(checkBox);
            this.userSeries = userSeries;
            this.tableModel = tableModel;
            this.pointCounterLabel = pointCounterLabel;
            this.connectPointsCheckBox = connectPointsCheckBox;

            button = new JButton("x");
            button.addActionListener(e -> {
                fireEditingStopped();
                SwingUtilities.invokeLater(this::deleteRow);
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.currentRow = row;
            return button;
        }

        private void deleteRow() {
            if (currentRow >= 0 && currentRow < userSeries.getItemCount()) {
                userSeries.remove(currentRow);
                tableModel.removeRow(currentRow);

                pointCounterLabel.setText("Points: " + userSeries.getItemCount());
                if (userSeries.getItemCount() == 0) {
                    pointCounterLabel.setVisible(false);
                    connectPointsCheckBox.setVisible(false);
                    connectPointsCheckBox.setSelected(false);
                }
            }
        }
    }

    public WeightToAgeBoysChartPanel() {
        setLayout(new BorderLayout());

        XYSeriesCollection dataset = new XYSeriesCollection();

        double[] x = {3, 3.5, 4, 4.5, 5, 5.5, 6, 6.5, 7, 7.5, 8, 8.5, 9, 9.5, 10, 10.5, 11, 11.5, 12, 12.5, 13, 13.5, 14, 14.5, 15, 15.5, 16, 16.5, 17, 17.5, 18};
        XYSeries[] centileSeries = new XYSeries[]{
                createSeries("3rd Centile", x, new double[]{12, 13, 14, 14.5, 15, 16, 17, 18, 18.5, 19, 20.5, 21.5, 22.5, 23.5, 24.5, 26, 27, 28, 30, 31.5, 33.5, 35, 38, 40, 43, 45, 47, 49.5, 51, 52.5, 54}),
                createSeries("10th Centile", x, new double[]{13, 13.75, 14.75, 15.5, 16, 17, 18, 19, 20.25, 21.25, 22.5, 23.5, 24.75, 25.75, 27, 28, 29.75, 31.25, 33, 35, 37, 39.75, 42, 44.75, 47, 49.25, 51.5, 53.5, 55.25, 57, 58.25}),
                createSeries("25th Centile", x, new double[]{13.75, 14.75, 15.75, 16.5, 17.25, 18.5, 19.5, 20.5, 22, 23, 24.75, 26, 27, 28.5, 30, 31.5, 33.1, 35, 37, 39.25, 41.5, 44.25, 46.75, 49.9, 52, 54.5, 56.5, 58.5, 60, 61.75, 63.5}),
                createSeries("50th Centile", x, new double[]{15, 16, 17, 18, 19, 20.25, 21.5, 22.9, 24.5, 26, 27.5, 29, 30.75, 32.5, 34.25, 36, 38, 40.25, 43, 45.25, 48, 51, 53.75, 56.25, 59, 61, 63, 65, 67, 68.5, 70}),
                createSeries("75th Centile", x, new double[]{16.5, 17.5, 18.75, 20, 21.25, 22.5, 24.5, 26, 27.75, 29.9, 31.5, 33.25, 35.5, 37.25, 39.9, 41.9, 44.75, 47, 50, 53, 55.75, 59, 62, 64.75, 67, 69.25, 71.25, 73.1, 74.9, 76.5, 78}),
                createSeries("90th Centile", x, new double[]{18, 19.25, 20.75, 22, 23.5, 25.5, 27.5, 29.5, 32, 34.25, 36.75, 38.75, 41.25, 44, 46.75, 49.75, 52.5, 55.1, 58.6, 61.9, 65, 68.5, 71, 73.75, 76, 78.25, 80, 82, 83.25, 84.9, 86}),
                createSeries("97th Centile", x, new double[]{20, 21.5, 23, 24.9, 26.75, 29.75, 32.1, 34.9, 37.5, 40.5, 43.5, 46.25, 49.5, 52.5, 56, 59, 62.75, 66, 69.75, 73.1, 76.5, 80, 82.75, 85, 87, 89, 90.5, 92.1, 93.5, 94.75, 96})
        };

        for (XYSeries series : centileSeries) dataset.addSeries(series);
        userSeries = new XYSeries("User point");
        dataset.addSeries(userSeries);

        JFreeChart chart = ChartFactory.createXYLineChart("Weight for age (Boys)", "Age (years)", "Weight (kg)", dataset);
        chart.removeLegend();
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.getDomainAxis().setLowerBound(3);
        plot.getDomainAxis().setUpperBound(18);
        plot.getRangeAxis().setLowerBound(10);
        plot.getRangeAxis().setUpperBound(100);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        Stroke[] strokes = {
                new BasicStroke(1.0f),
                new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{2f, 4f, 8f, 4f}, 0),
                new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{2f, 6f}, 0),
                new BasicStroke(2.0f),
                new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{2f, 6f}, 0),
                new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{2f, 4f, 8f, 4f}, 0),
                new BasicStroke(1.0f)
        };

        for (int i = 0; i < centileSeries.length; i++) {
            renderer.setSeriesStroke(i, strokes[i]);
            renderer.setSeriesPaint(i, Color.BLACK);
            renderer.setSeriesLinesVisible(i, true);
            renderer.setSeriesShapesVisible(i, false);
            XYTextAnnotation annotation = new XYTextAnnotation(
                    "c" + centileSeries[i].getKey().toString().replaceAll("[^0-9]", ""),
                    centileSeries[i].getX(centileSeries[i].getItemCount() - 1).doubleValue() - 0.1,
                    centileSeries[i].getY(centileSeries[i].getItemCount() - 1).doubleValue() + 1
            );
            annotation.setFont(new Font("SansSerif", Font.PLAIN, 12));
            plot.addAnnotation(annotation);
        }

        int userIndex = dataset.getSeriesCount() - 1;
        renderer.setSeriesLinesVisible(userIndex, false);
        renderer.setSeriesShapesVisible(userIndex, true);
        renderer.setSeriesShape(userIndex, new Ellipse2D.Double(-2, -2, 4, 4));
        renderer.setSeriesPaint(userIndex, Color.RED);

        plot.setRenderer(renderer);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setMouseWheelEnabled(false);
        chartPanel.setDomainZoomable(false);
        chartPanel.setRangeZoomable(false);
        chartPanel.setPreferredSize(new Dimension(1600, 900));
        add(chartPanel, BorderLayout.CENTER);
        JButton saveButton = new JButton("Save as PNG");
        saveButton.addActionListener(e -> {
            try {
                File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
                File file = new File(desktopDir, "WeightToAgeBoysChart.png");
                ChartUtils.saveChartAsPNG(file, chartPanel.getChart(), 1600, 900);
                JOptionPane.showMessageDialog(this, "Chart saved on desktop as WeightToAgeBoysChart.png");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        JPanel controlPanel = new JPanel(new BorderLayout());
        JPanel modePanel = new JPanel(new FlowLayout());
        JButton singlePointButton = new JButton("Single point");
        JButton multiPointButton = new JButton("Multi points");
        singlePointButton.setEnabled(false);

        modePanel.add(singlePointButton);
        modePanel.add(multiPointButton);

        controlPanel.add(modePanel, BorderLayout.NORTH);

        JTextField ageField = new JTextField(10);
        JTextField weightField = new JTextField(10);
        JButton addButton = new JButton("Add point");

        ageField.setFont(new Font("SansSerif", Font.PLAIN, 18));
        weightField.setFont(new Font("SansSerif", Font.PLAIN, 18));

        pointCounterLabel = new JLabel("Points: 0");
        connectPointsCheckBox = new JCheckBox("Connect points");

        String[] columnNames = {"", "Age", "Weight", "Delete"};
        DefaultTableModel tableModel = new DefaultTableModel(null, columnNames) {
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        JTable pointTable = new JTable(tableModel);
        pointTable.setRowHeight(25);

        pointTable.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        pointTable.getColumnModel().getColumn(3).setCellEditor(
                new ButtonEditor(
                        new JCheckBox(),
                        userSeries,
                        tableModel,
                        pointCounterLabel,
                        connectPointsCheckBox
                )
        );

        JScrollPane tableScrollPane = new JScrollPane(pointTable);
        tableScrollPane.setPreferredSize(new Dimension(250, 900));

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(saveButton);
        inputPanel.add(Box.createRigidArea(new Dimension(40, 0)));
        inputPanel.add(new JLabel("Age:"));
        inputPanel.add(ageField);
        inputPanel.add(new JLabel("Weight:"));
        inputPanel.add(weightField);
        inputPanel.add(addButton);

        JPanel multiExtras = new JPanel();
        connectPointsCheckBox.setEnabled(false);
        multiExtras.add(pointCounterLabel);
        multiExtras.add(connectPointsCheckBox);
        pointCounterLabel.setVisible(false);
        connectPointsCheckBox.setVisible(false);

        controlPanel.add(inputPanel, BorderLayout.CENTER);
        controlPanel.add(multiExtras, BorderLayout.SOUTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(tableScrollPane, BorderLayout.WEST);
        mainPanel.add(chartPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            String ageText = ageField.getText();
            String weightText = weightField.getText();

            double age;
            try {
                if (ageText.isEmpty()) throw new NumberFormatException();
                age = Double.parseDouble(ageText);
                if (age < 3 || age > 18) {
                    JOptionPane.showMessageDialog(this, "Age must be between 3 and 18 years old.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid age (numbers).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double weight;
            try {
                if (weightText.isEmpty()) throw new NumberFormatException();
                weight = Double.parseDouble(weightText);
                if (weight < 10 || weight > 100) {
                    JOptionPane.showMessageDialog(this, "Weight must be between 10 and 100 kg.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid weight (numbers).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (int i = 0; i < userSeries.getItemCount(); i++) {
                if (userSeries.getX(i).doubleValue() == age && userSeries.getY(i).doubleValue() == weight) {
                    JOptionPane.showMessageDialog(this, "This point already exists!", "Duplicate", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            if (singlePointMode) {
                userSeries.clear();
                tableModel.setRowCount(0);
            }
            userSeries.add(age, weight);
            tableModel.addRow(new Object[]{"•", age, weight, "x"});

            pointCounterLabel.setText("Points: " + userSeries.getItemCount());
            pointCounterLabel.setVisible(true);
            if (!singlePointMode) connectPointsCheckBox.setVisible(true);

        });

        ageField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isDigit(c) || c == '.' || c == KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                }
            }
        });

        weightField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isDigit(c) || c == '.' || c == KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                }
            }
        });

        singlePointButton.addActionListener(e -> {
            singlePointMode = true;
            singlePointButton.setEnabled(false);
            multiPointButton.setEnabled(true);
            userSeries.clear();
            tableModel.setRowCount(0);
            pointCounterLabel.setText("Points: 0");
            pointCounterLabel.setVisible(false);
            connectPointsCheckBox.setVisible(false);
            connectPointsCheckBox.setSelected(false);
        });

        multiPointButton.addActionListener(e -> {
            singlePointMode = false;
            singlePointButton.setEnabled(true);
            multiPointButton.setEnabled(false);
            connectPointsCheckBox.setEnabled(true);
            if (userSeries.getItemCount() > 0) {
                pointCounterLabel.setVisible(true);
                connectPointsCheckBox.setVisible(true);
            }
        });

        connectPointsCheckBox.addActionListener(e -> {
            XYPlot p = (XYPlot) chartPanel.getChart().getPlot();
            XYLineAndShapeRenderer r = (XYLineAndShapeRenderer) p.getRenderer();
            int userInde = dataset.getSeriesCount() - 1;
            r.setSeriesLinesVisible(userInde, connectPointsCheckBox.isSelected());
        });
    }
}
