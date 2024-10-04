import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GridPanel extends JPanel {

    private final int rows;
    private final int cols;
    private final int cellSize;
    private final int startXLabel;
    private final int startYLabel;
    private final List<Point> blackCells;

    // Constructor to initialize rows, cols, cell size, start labels, and black cells
    public GridPanel(int cols, int rows, int cellSize, int startXLabel, int startYLabel, List<Point> blackCells) {
        this.rows = rows;
        this.cols = cols;
        this.cellSize = cellSize;
        this.startXLabel = startXLabel;
        this.startYLabel = startYLabel;
        this.blackCells = blackCells;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw the grid
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = col * cellSize;
                int y = row * cellSize;
                if (blackCells.contains(new Point(row, col))) {
                    g2d.setColor(Color.BLACK);
                    g2d.fillRect(x, y, cellSize, cellSize);
                }
                g2d.setColor(Color.BLACK);
                g2d.drawRect(x, y, cellSize, cellSize);
            }
        }

        // Label the rows and columns
        g2d.setColor(Color.BLACK);
        for (int i = 1; i < rows; i++) {
            g2d.drawString(String.valueOf(startYLabel + i - 1), 5, (i + 1) * cellSize - 15);
        }
        for (int i = 1; i < cols; i++) {
            g2d.drawString(String.valueOf(startXLabel + i - 1), (i + 1) * cellSize - 15, 20);
        }

        // diagonal line
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(2));
        int startX = 30;
        int startY = 30;
        int endX = cols * cellSize;
        int endY = rows * cellSize;
        if (blackCells.getFirst().getX() > blackCells.getLast().getX()) {
            g2d.drawLine(endX, startY, startX, endY);
        } else {
            g2d.drawLine(startX, startY, endX, endY);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(cols * cellSize, rows * cellSize);
    }
}
