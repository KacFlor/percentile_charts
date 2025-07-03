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

public class WeightToAgeGirlsChartPanel extends JPanel {
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

    public WeightToAgeGirlsChartPanel() {
        setLayout(new BorderLayout());

        XYSeriesCollection dataset = new XYSeriesCollection();

        double[] x = {3, 3.5, 4, 4.5, 5, 5.5, 6, 6.5, 7, 7.5, 8, 8.5, 9, 9.5, 10, 10.5, 11, 11.5, 12, 12.5, 13, 13.5, 14, 14.5, 15, 15.5, 16, 16.5, 17, 17.5, 18};
        XYSeries[] centileSeries = new XYSeries[]{
                createSeries("3rd Centile", x, new double[]{11.95, 12.4, 13.1, 14, 14.6, 15.2, 16, 16.9, 17.8, 18.8, 19.9, 20.7, 21.8, 23, 24, 25.2, 26.5, 28.1, 30, 32, 34.3, 36.2, 38.2, 39.7, 41, 42, 42.8, 43.4, 43.9, 44.1, 44.4}),
                createSeries("10th Centile", x, new double[]{12.6, 13.2, 14, 14.9, 15.7, 16.5, 17.3, 18.1, 19.2, 20.1, 21.5, 22.6, 23.9, 25.4, 26.4, 27.8, 29.7, 31.2, 33.2, 35.6, 37.8, 39.9, 41.7, 43, 44.5, 45.2, 46, 46.6, 47, 47.4, 47.6}),
                createSeries("25th Centile", x, new double[]{13.5, 14.2, 15.1, 16, 17, 17.9, 18.9, 20, 21, 22, 23.7, 25, 26.5, 27.9, 29.4, 31, 33, 35, 37.2, 39.5, 42, 43.7, 45.6, 47, 48.1, 49.1, 49.7, 50.4, 50.8, 51.1, 51.2}),
                createSeries("50th Centile", x, new double[]{14.5, 15.5, 16.6, 17.5, 18.6, 19.8, 20.9, 22, 23.5, 24.9, 26.7, 28, 29.9, 31.6, 33.3, 35.5, 38, 40.2, 42.7, 45.1, 47.3, 49.5, 51, 52.4, 53.7, 54.2, 54.9, 55.5, 55.9, 56.1, 56.2}),
                createSeries("75th Centile", x, new double[]{15.9, 17, 18.1, 19.5, 20.6, 22, 23.5, 25, 26.9, 28.4, 30.4, 32, 34.5, 36.5, 38.9, 41.5, 44, 47, 49.5, 52, 54.4, 56.3, 57.8, 59, 60, 60.5, 61, 61.6, 62, 62.1, 62.2}),
                createSeries("90th Centile", x, new double[]{17.5, 19, 20.1, 21.9, 23.3, 25, 26.7, 28.4, 30.4, 32.5, 35, 37, 39.5, 42.4, 45.2, 48, 51.5, 54.5, 57.2, 60, 62, 64, 65.4, 66.1, 67.1, 67.8, 68.1, 68.4, 68.9, 69.1, 69.2}),
                createSeries("97th Centile", x, new double[]{19.5, 21, 22.8, 24.6, 26.5, 28.5, 30.5, 32.5, 35.2, 38, 40.5, 43.8, 47, 50, 53.2, 57, 60.4, 64, 67, 69.5, 71.7, 73.2, 74.5, 75.3, 76, 76.5, 76.9, 77.2, 77.5, 77.8, 77.9})
        };

        for (XYSeries series : centileSeries) dataset.addSeries(series);
        userSeries = new XYSeries("User point");
        dataset.addSeries(userSeries);

        JFreeChart chart = ChartFactory.createXYLineChart("Weight for age (Girls)", "Age (years)", "Weight (kg)", dataset);
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
                File file = new File(desktopDir, "WeightToAgeGirlsChart .png");
                ChartUtils.saveChartAsPNG(file, chartPanel.getChart(), 1600, 900);
                JOptionPane.showMessageDialog(this, "Chart saved on desktop as WeightToAgeGirlsChart.png");
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
