import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GridPanel extends JPanel {

    private final int rows;
    private final int cols;
    private final int cellSize;
    private final int startXLabel; // Starting label for x-axis
    private final int startYLabel; // Starting label for y-axis
    private final List<Point> blackCells; // List of cells to be filled black

    // Constructor to initialize rows, cols, cell size, start labels, and black cells
    public GridPanel(int cols, int rows, int cellSize, int startXLabel, int startYLabel, List<Point> blackCells) {
        this.rows = rows;
        this.cols = cols;
        this.cellSize = cellSize;
        this.startXLabel = startXLabel; // Initialize starting label for x-axis
        this.startYLabel = startYLabel; // Initialize starting label for y-axis
        this.blackCells = blackCells; // Initialize the list of black cells
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

                // Check if the current cell should be filled black
                if (blackCells.contains(new Point(row, col))) {
                    g2d.setColor(Color.BLACK); // Change the color to black
                    g2d.fillRect(x, y, cellSize, cellSize);
                }
                // Draw cell borders
                g2d.setColor(Color.BLACK);
                g2d.drawRect(x, y, cellSize, cellSize);
            }
        }

        // Label the rows and columns
        g2d.setColor(Color.BLACK);
        for (int i = 1; i < rows; i++) {
            // Row labels starting from startYLabel
            g2d.drawString(String.valueOf(startYLabel + i - 1), 5, (i + 1) * cellSize - 15);
        }
        for (int i = 1; i < cols; i++) {
            // Column labels starting from startXLabel
            g2d.drawString(String.valueOf(startXLabel + i - 1), (i + 1) * cellSize - 15, 20);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(cols * cellSize, rows * cellSize);
    }
}
