package org.example.percentile_grids;

import org.jfree.chart.*;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
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

public class BMIChartPanel extends JPanel {
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

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }

    public BMIChartPanel() {
        setLayout(new BorderLayout());

        XYSeriesCollection dataset = new XYSeriesCollection();

        double[] x = {3, 3.5, 4, 4.5, 5, 5.5, 6, 6.5, 7, 7.5, 8, 8.5, 9, 9.5, 10, 10.5, 11, 11.5, 12, 12.5, 13, 13.5, 14, 14.5, 15, 15.5, 16, 16.5, 17, 17.5, 18};

        XYSeries[] centileSeries = new XYSeries[]{
                createSeries("-2 SD", x, new double[]{13.75, 13.60, 13.5, 13.35, 13.30, 13.20, 13.15, 13.14, 13.15, 13.20, 13.25, 13.30, 13.40, 13.5, 13.60, 13.75, 13.95, 14.05, 14.25, 14.5, 14.75, 14.95, 15.2, 15.5, 15.75, 15.99, 16.2, 16.45, 16.7, 17, 17.3}),
                createSeries("Underweight", x, new double[]{14.2, 14.1, 13.9, 13.85, 13.83, 13.82, 13.84, 13.85, 13.9, 13.95, 14, 14.1, 14.2, 14.3, 14.5, 14.70, 14.85, 15, 15.2, 15.4, 15.7, 15.99, 16.2, 16.5, 16.75, 17, 17.3, 17.6, 17.95, 18.2, 18.45}),
                createSeries("-1 SD", x, new double[]{14.6, 14.5, 14.4, 14.35, 14.30, 14.26, 14.25, 14.24, 14.26, 14.30, 14.40, 14.55, 14.65, 14.80, 15.01, 15.25, 15.4, 15.6, 15.84, 16.05, 16.3, 16.55, 16.8, 17.05, 17.35, 17.6, 18, 18.3, 18.6, 18.9, 19.2}),
                createSeries("50th Centile", x, new double[]{15.6, 15.55, 15.53, 15.51, 15.5, 15.52, 15.56, 15.67, 15.85, 16.02, 16.2, 16.35, 16.5, 16.75, 17, 17.25, 17.4, 17.65, 18, 18.3, 18.5, 18.9, 19.2, 19.5, 19.8, 20.2, 20.45, 20.8, 21.2, 21.4, 21.7}),
                createSeries("Overweight", x, new double[]{17, 17.03, 17.05, 17.15, 17.25, 17.4, 17.65, 17.8, 18, 18.35, 18.6, 19, 19.4, 19.7, 20.1, 20.4, 20.8, 21.2, 21.4, 21.65, 22, 22.25, 22.65, 22.76, 23.2, 23.5, 23.7, 24, 24.35, 24.65, 24.9}),
                createSeries("85th Centile", x, new double[]{17.1, 17.15, 17.2, 17.25, 17.45, 17.6, 17.8, 18, 18.2, 18.5, 18.9, 19.3, 19.7, 20.1, 20.3, 20.8, 21.2, 21.4, 21.8, 22, 22.3, 22.5, 22.9, 23.2, 23.6, 23.85, 24.1, 24.3, 24.6, 24.99, 25.3}),
                createSeries("95th Centile", x, new double[]{18.4, 18.45, 18.5, 18.9, 19.1, 19.4, 19.8, 20.2, 20.7, 21.1, 21.7, 22.1, 22.7, 23.3, 23.85, 24.2, 24.75, 25, 25.27, 25.55, 25.75, 26, 26.2, 26.4, 26.6, 26.8, 27.01, 27.25, 27.6, 27.9, 28.15}),
                createSeries("Obesity", x, new double[]{19.5, 19.55, 19.8, 20, 20.4, 20.85, 21.25, 21.75, 22.3, 23, 23.75, 24.4, 25.01, 25.85, 26.5, 27.05, 27.6, 28, 28.3, 28.5, 28.55, 28.57, 28.6, 28.7, 28.9, 29, 29.1, 29.4, 29.6, 29.8, 30.05}),
                createSeries("+2 SD", x, new double[]{19.55, 19.60, 19.85, 20.1, 20.5, 20.95, 21.3, 21.9, 22.4, 23.2, 24, 24.55, 25.25, 26, 26.75, 27.3, 27.8, 28.2, 28.5, 28.55, 28.60, 28.7, 28.7, 28.9, 29.05, 29.15, 29.2, 29.45, 29.7, 29.9, 30.2})
        };

        for (XYSeries series : centileSeries) dataset.addSeries(series);
        userSeries = new XYSeries("User point");
        dataset.addSeries(userSeries);

        JFreeChart chart = ChartFactory.createXYLineChart("BMI for age", "Age (years)", "BMI (kg/m^2)", dataset);
        chart.removeLegend();
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.getDomainAxis().setLowerBound(3);
        plot.getDomainAxis().setUpperBound(18);
        plot.getRangeAxis().setLowerBound(13);
        plot.getRangeAxis().setUpperBound(31);
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setTickUnit(new NumberTickUnit(1));


        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        Stroke[] strokes = {
                new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{2f, 4f}, 0),
                new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{2f, 4f}, 0),
                new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4f, 4f}, 0),
                new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{2f, 4f}, 0),
                new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3f, 6f}, 0),
                new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5f, 5f}, 0),
                new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{6f, 4f}, 0),
                new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{2f, 2f}, 0),
                new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{2f, 4f}, 0)
        };


        for (int i = 0; i < centileSeries.length; i++) {
            renderer.setSeriesStroke(i, strokes[i]);
            renderer.setSeriesPaint(i, Color.BLACK);
            renderer.setSeriesLinesVisible(i, true);
            renderer.setSeriesShapesVisible(i, false);
            String label = centileSeries[i].getKey().toString();
            double xVal = centileSeries[i].getX(centileSeries[i].getItemCount() - 1).doubleValue() - 0.3;
            double yVal = centileSeries[i].getY(centileSeries[i].getItemCount() - 1).doubleValue() + 0.1;

            if (label.equals("Obesity") || label.equals("Overweight")) {
                yVal -= 0.6;
            }

            XYTextAnnotation annotation = new XYTextAnnotation(label, xVal, yVal);
            annotation.setFont(new Font("SansSerif", Font.PLAIN, 9));
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
                File file = new File(desktopDir, "BMIChart .png");
                ChartUtils.saveChartAsPNG(file, chartPanel.getChart(), 1600, 900);
                JOptionPane.showMessageDialog(this, "Chart saved on desktop as BMIChart.png");
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
        JTextField bmiField = new JTextField(10);
        JButton addButton = new JButton("Add point");

        ageField.setFont(new Font("SansSerif", Font.PLAIN, 18));
        bmiField.setFont(new Font("SansSerif", Font.PLAIN, 18));

        pointCounterLabel = new JLabel("Points: 0");
        connectPointsCheckBox = new JCheckBox("Connect points");

        String[] columnNames = {"", "Age", "BMI", "Delete"};
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
        inputPanel.add(new JLabel("BMI:"));
        inputPanel.add(bmiField);
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
            String bmiText = bmiField.getText();

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

            double bmi;
            try {
                if (bmiText.isEmpty()) throw new NumberFormatException();
                bmi = Double.parseDouble(bmiText);
                if (bmi < 13 || bmi > 31) {
                    JOptionPane.showMessageDialog(this, "BMI must be between 13 and 31.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid bmi (numbers).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (int i = 0; i < userSeries.getItemCount(); i++) {
                if (userSeries.getX(i).doubleValue() == age && userSeries.getY(i).doubleValue() == bmi) {
                    JOptionPane.showMessageDialog(this, "This point already exists!", "Duplicate", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            if (singlePointMode) {
                userSeries.clear();
                tableModel.setRowCount(0);
            }
            userSeries.add(age, bmi);
            tableModel.addRow(new Object[]{"•", age, bmi, "x"});

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

        bmiField.addKeyListener(new KeyAdapter() {
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
