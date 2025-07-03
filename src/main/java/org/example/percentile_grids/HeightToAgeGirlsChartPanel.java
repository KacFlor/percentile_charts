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

public class HeightToAgeGirlsChartPanel extends JPanel {
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

    public HeightToAgeGirlsChartPanel() {
        setLayout(new BorderLayout());

        XYSeriesCollection dataset = new XYSeriesCollection();

        double[] x = {3, 3.5, 4, 4.5, 5, 5.5, 6, 6.5, 7, 7.5, 8, 8.5, 9, 9.5, 10, 10.5, 11, 11.5, 12, 12.5, 13, 13.5, 14, 14.5, 15, 15.5, 16, 16.5, 17, 17.5, 18};

        XYSeries[] centileSeries = new XYSeries[]{
                createSeries("3rd Centile", x, new double[]{89, 92.4, 95.9, 99, 102, 104.7, 107.5, 110.3, 113, 115.9, 118.5, 121, 123.5, 126, 128.5, 131, 134, 137.2, 140.8, 144, 146.8, 149, 150.5, 151.6, 152.3, 152.9, 153.2, 153.5, 153.7, 153.9, 154}),
                createSeries("10th Centile", x, new double[]{91.5, 95, 98.4, 101.4, 104.9, 107.7, 110.5, 113.7, 116, 119, 122, 124.7, 127.5, 129.8, 132.1, 135, 138.5, 141.5, 145, 148.1, 150.3, 152.7, 154.1, 155.2, 156, 156.4, 156.9, 157, 157.1, 157.3, 157.5}),
                createSeries("25th Centile", x, new double[]{93.5, 97.2, 100.8, 104, 107.4, 110.4, 113.5, 116.5, 119.4, 122.4, 125.4, 128.1, 131, 133.7, 136.5, 139, 142.5, 145.5, 149.1, 152, 154.5, 156.5, 158, 159, 159.9, 160, 160.4, 160.8, 160.9, 161, 161.1}),
                createSeries("50th Centile", x, new double[]{96.5, 99.9, 103.5, 107, 110.7, 114, 117, 120, 123, 126, 129.4, 132.1, 135, 137.8, 141.8, 143.7, 147.5, 150.5, 154, 156.5, 159, 161, 162, 163, 163.9, 164, 164.2, 164.5, 164.8, 165, 165}),
                createSeries("75th Centile", x, new double[]{99, 102.7, 106.5, 110, 113.5, 117, 120, 123.5, 126.6, 129.7, 133, 136, 139, 142.4, 145, 148.7, 152.9, 155, 158.3, 161, 163, 164.9, 166.5, 167.1, 167.9, 168.1, 168.4, 168.7, 168.9, 169, 169}),
                createSeries("90th Centile", x, new double[]{101, 104.8, 108.7, 112.9, 116.5, 120, 123.5, 126.5, 129.8, 133, 136.7, 140, 143.1, 146, 149, 152.5, 156.1, 159.5, 162.5, 165, 167.2, 168.7, 170, 171, 171.5, 171.9, 172, 172.1, 172.2, 172.4, 172.5}),
                createSeries("97th Centile", x, new double[]{103.8, 107.8, 111.6, 115.2, 119.4, 122.8, 126.5, 130, 133.1, 136.7, 140, 143.5, 146.8, 150, 153.4, 157, 160.3, 163.7, 166.7, 169, 171, 172.5, 173.7, 174.5, 175, 175.5, 175.7, 175.9, 176, 176.1, 176.2})
        };

        for (XYSeries series : centileSeries) dataset.addSeries(series);
        userSeries = new XYSeries("User point");
        dataset.addSeries(userSeries);

        JFreeChart chart = ChartFactory.createXYLineChart("Height for age (Girls)", "Age (years)", "Height (cm)", dataset);
        chart.removeLegend();
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.getDomainAxis().setLowerBound(3);
        plot.getDomainAxis().setUpperBound(18);
        plot.getRangeAxis().setLowerBound(90);
        plot.getRangeAxis().setUpperBound(195);
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setTickUnit(new NumberTickUnit(5));

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
                File file = new File(desktopDir, "HeighToAgeGirlsChart.png");
                ChartUtils.saveChartAsPNG(file, chartPanel.getChart(), 1600, 900);
                JOptionPane.showMessageDialog(this, "Chart saved on desktop as HeighToAgeGirlsChart.png");
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
        JTextField heightField = new JTextField(10);
        JButton addButton = new JButton("Add point");

        ageField.setFont(new Font("SansSerif", Font.PLAIN, 18));
        heightField.setFont(new Font("SansSerif", Font.PLAIN, 18));

        pointCounterLabel = new JLabel("Points: 0");
        connectPointsCheckBox = new JCheckBox("Connect points");

        String[] columnNames = {"", "Age", "Height", "Delete"};
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
        inputPanel.add(new JLabel("Height:"));
        inputPanel.add(heightField);
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
            String heightText = heightField.getText();

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

            double height;
            try {
                if (heightText.isEmpty()) throw new NumberFormatException();
                height = Double.parseDouble(heightText);
                if (height < 90 || height > 195) {
                    JOptionPane.showMessageDialog(this, "Height must be between 90 and 195 cm.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid height (numbers).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (int i = 0; i < userSeries.getItemCount(); i++) {
                if (userSeries.getX(i).doubleValue() == age && userSeries.getY(i).doubleValue() == height) {
                    JOptionPane.showMessageDialog(this, "This point already exists!", "Duplicate", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            if (singlePointMode) {
                userSeries.clear();
                tableModel.setRowCount(0);
            }
            userSeries.add(age, height);
            tableModel.addRow(new Object[]{"•", age, height, "x"});

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

        heightField.addKeyListener(new KeyAdapter() {
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
