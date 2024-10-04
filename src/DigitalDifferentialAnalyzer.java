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
    private JTable table2;
    private DDATableModel tableModel;

    public DigitalDifferentialAnalyzer() {
        setTitle("Digital Differential Analyzer");
        setContentPane(MainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);

        calculateButton.addActionListener(actionEvent -> calculate());
        resetButton.addActionListener(actionEvent -> reset());
    }

    private void reset() {
        X1.setText("");
        X2.setText("");
        Y1.setText("");
        Y2.setText("");
        tableModel.clearData();
    }

    private void calculate() {
        int x1 = 0, x2 = 0, y1 = 0, y2 = 0;
        int dx, dy;
        List<Float> xValues = new ArrayList<>();
        List<Float> yValues = new ArrayList<>();
        try {
            x1 = Integer.parseInt(X1.getText());
            x2 = Integer.parseInt(X2.getText());
            y1 = Integer.parseInt(Y1.getText());
            y2 = Integer.parseInt(Y2.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        if (x2 > x1 && y2 > y1) {
            calculateDDA(x2, x1, y2, y1, xValues, yValues);
        } else {
            calculateDDA(x1, x2, y1, y2, xValues, yValues);
        }
        tableModel.setData(xValues, yValues);

        int cols = Math.abs(x1 - x2);
        int rows = Math.abs(y1 - y2);
        int xStartNumber = Math.min(x1, x2);
        int yStartNumber = Math.min(y1, y2);

        List<Point> blackCells = new ArrayList<>();

        for (int i = 0; i < xValues.size(); i++) {
            blackCells.add(new Point(Math.round(yValues.get(i) - yStartNumber + 1), Math.round(xValues.get(i)) - xStartNumber + 1));
        }

        JFrame frame = new JFrame("Grid");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new GridPanel(cols + 2, rows + 2, 30, xStartNumber, yStartNumber, blackCells));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void calculateDDA(int x1, int x2, int y1, int y2, List<Float> xValues, List<Float> yValues) {
        int dx;
        int dy;
        dx = x1 - x2;
        dy = y1 - y2;
        float step = Math.max(dy, dx);
        float xIncrement = dx / step;
        float yIncrement = dy / step;

        for (int i = 0; i <= step; i++) {
            xValues.add(x2 + i * xIncrement);
            yValues.add(y2 + i * yIncrement);
        }
    }

    private void createUIComponents() {
        tableModel = new DDATableModel();
        table1 = new JTable(tableModel);
        jScrollPane = new JScrollPane(table1);
        table2 = new JTable();
        table2.setRowHeight(30);
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


