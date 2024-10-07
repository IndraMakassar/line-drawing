import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DigitalDifferentialAnalyzer extends JFrame {
    private JTextField X1;
    private JTextField X2;
    private JTextField Y1;
    private JTextField Y2;
    private JButton calculateButton;
    private JButton resetButton;
    private JPanel MainPanel;
    private JTable table1;
    private JScrollPane jScrollPane;
    private JRadioButton algoritmaDDARadioButton;
    private JRadioButton algoritmaBrassenhamRadioButton;
    private JRadioButton algoritmaDasarRadioButton;
    private JFrame frame;
    private DDATableModel tableModelDDA;
    private BasicTableModel tableModelBasic;
    ButtonGroup buttonGroup;

    public DigitalDifferentialAnalyzer() {
        setTitle("Digital Differential Analyzer");
        setContentPane(MainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        buttonGroup = new ButtonGroup();

        buttonGroup.add(algoritmaDDARadioButton);
        buttonGroup.add(algoritmaBrassenhamRadioButton);
        buttonGroup.add(algoritmaDasarRadioButton);
        algoritmaDasarRadioButton.setSelected(true);

        calculateButton.addActionListener(actionEvent -> calculate());
        resetButton.addActionListener(actionEvent -> reset());
    }

    private void reset() {
        X1.setText("");
        X2.setText("");
        Y1.setText("");
        Y2.setText("");
        tableModelBasic.clearData();
        tableModelDDA.clearData();
        frame.dispose();
    }

    private void calculate() {
        int x1 = 0, x2 = 0, y1 = 0, y2 = 0;
        List<Float> xValues = new ArrayList<>();
        List<Float> yValues = new ArrayList<>();
        try {
            x1 = Integer.parseInt(X1.getText());
            x2 = Integer.parseInt(X2.getText());
            y1 = Integer.parseInt(Y1.getText());
            y2 = Integer.parseInt(Y2.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number");
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return;
        }

        if (algoritmaDDARadioButton.isSelected()) {
            if (x2 > x1 || y2 > y1) {
                calculateDDA(x2, x1, y2, y1, xValues, yValues);
            } else {
                calculateDDA(x1, x2, y1, y2, xValues, yValues);
            }
        } else if (algoritmaBrassenhamRadioButton.isSelected()) {
            if (x2 > x1 || y2 > y1) {
                calculateDDA(x2, x1, y2, y1, xValues, yValues);
            } else {
                calculateDDA(x1, x2, y1, y2, xValues, yValues);
            }
        } else if (algoritmaDasarRadioButton.isSelected()) {
            calculateDasar(x1, x2, y1, y2, xValues, yValues);


        } else {

        }

        int cols = Math.abs(x1 - x2);
        int rows = Math.abs(y1 - y2);
        int xStartNumber = Math.min(x1, x2);
        int yStartNumber = Math.min(y1, y2);

        List<Point> blackCells = new ArrayList<>();

        for (int i = 0; i < xValues.size(); i++) {
            blackCells.add(new Point(Math.round(yValues.get(i) - yStartNumber + 1), Math.round(xValues.get(i)) - xStartNumber + 1));
        }

        frame = new JFrame("Grid");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new GridPanel(cols + 2, rows + 2, 30, xStartNumber, yStartNumber, blackCells));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void calculateDasar(int x1, int x2, int y1, int y2, List<Float> xValues, List<Float> yValues) {
        float m = (float) (y2 - y1) / (x2 - x1);
        float b = y1 - m * x1;

        xValues.add((float) x1);
        yValues.add((float) y1);

        for (int x = x1 + 1; x <= x2; x++) {
            float y = m * x + b;

            xValues.add((float) x);
            yValues.add(y);
        }

        tableModelBasic = new BasicTableModel();
        table1.setModel(tableModelBasic);

        tableModelBasic.setData(xValues, yValues, m, b);
    }

    private void calculateDDA(int x1, int x2, int y1, int y2, List<Float> xValues, List<Float> yValues) {
        int dx = x1 - x2;
        int dy = y1 - y2;
        float step = Math.max(Math.abs(dx), Math.abs(dy));
        float xIncrement = (float) dx / step;
        float yIncrement = (float) dy / step;
        float x = x2;
        float y = y2;

        for (int i = 0; i <= step; i++) {
            xValues.add(x);
            yValues.add(y);
            x += xIncrement;
            y += yIncrement;
        }
        tableModelDDA = new DDATableModel();
        table1.setModel(tableModelDDA);

        tableModelDDA.setData(xValues, yValues);
    }

    private void createUIComponents() {
        tableModelDDA = new DDATableModel();
        table1 = new JTable(tableModelDDA);
        jScrollPane = new JScrollPane(table1);
        JTable table2 = new JTable();
        table2.setRowHeight(30);
    }

    private static class BasicTableModel extends AbstractTableModel {
        private final String[] columnNames = {"x", "dx", "x", "y(b)", "m", "y"};
        private final List<List<String>> data = new ArrayList<>();

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return data.get(rowIndex).get(columnIndex);
        }

        public void setData(List<Float> xValues, List<Float> yValues, float m, float b) {
            data.clear();
            int size = xValues.size();

            // Populate data with values for each row
            for (int i = 0; i < size; i++) {
                List<String> row = new ArrayList<>();
                float x = xValues.get(i);
                float y = yValues.get(i);

                // Calculate dx (difference between x values)
                float dx = (i > 0) ? x - xValues.get(i - 1) : 0;

                // Add values to the row
                row.add(String.format("%.2f", x));        // x
                row.add(String.format("%.2f", dx));       // dx
                row.add(String.format("%.2f", x));        // x again
                row.add(String.format("%.2f", b));        // y-intercept (b)
                row.add(String.format("%.2f", m));        // slope (m)
                row.add(String.format("%.2f", y));        // y

                // Add row to the data
                data.add(row);
            }


            fireTableDataChanged();
        }

        public void clearData() {
            data.clear();
            fireTableDataChanged();
        }
    }

    private static class DDATableModel extends AbstractTableModel {
        private final String[] columnNames = {"K", "X", "Y", "round(x), round(y)"};
        private final List<List<String>> data = new ArrayList<>();

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return data.get(rowIndex).get(columnIndex);
        }

        public void setData(List<Float> xValues, List<Float> yValues) {
            data.clear();
            for (int i = 0; i < xValues.size(); i++) {
                float x = xValues.get(i);
                float y = yValues.get(i);
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(i));
                row.add(String.valueOf(x));
                row.add(String.valueOf(y));
                row.add(Math.round(x) + ", " + Math.round(y));
                data.add(row);
            }
            fireTableDataChanged();
        }

        public void clearData() {
            data.clear();
            fireTableDataChanged();
        }
    }
}


