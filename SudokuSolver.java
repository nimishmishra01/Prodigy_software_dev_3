import java.awt.*;
import java.awt.event.*;

public class SudokuSolver extends Frame {
    private static final int SIZE = 9; // Size of the Sudoku grid
    private TextField[][] grid; // Text fields for the Sudoku grid

    public SudokuSolver() {
        // Set up the frame
        setTitle("Sudoku Solver");
        setSize(600, 600);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(2, 2, 2, 2); // Padding around cells

        // Initialize the grid
        grid = new TextField[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                grid[row][col] = new TextField();
                grid[row][col].setFont(new Font("Arial", Font.BOLD, 20));
                grid[row][col].setText("0");
                grid[row][col].setPreferredSize(new Dimension(50, 50));
                gbc.gridx = col;
                gbc.gridy = row;
                add(grid[row][col], gbc);
            }
        }

        // Add solve button
        Button solveButton = new Button("Solve");
        solveButton.setFont(new Font("Arial", Font.BOLD, 20));
        solveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[][] puzzle = getPuzzleFromGrid();
                if (solveSudoku(puzzle)) {
                    setGridFromPuzzle(puzzle);
                } else {
                    showMessage("No solution exists for this puzzle.");
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = SIZE;
        gbc.gridwidth = SIZE;
        add(solveButton, gbc);

        // Close window event
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });

        setVisible(true);
    }

    // Get puzzle from grid
    private int[][] getPuzzleFromGrid() {
        int[][] puzzle = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                String text = grid[row][col].getText();
                puzzle[row][col] = text.isEmpty() ? 0 : Integer.parseInt(text);
            }
        }
        return puzzle;
    }

    // Set grid from puzzle
    private void setGridFromPuzzle(int[][] puzzle) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                grid[row][col].setText(puzzle[row][col] == 0 ? "" : Integer.toString(puzzle[row][col]));
            }
        }
    }

    // Display a message
    private void showMessage(String message) {
        Dialog dialog = new Dialog(this, "Message", true);
        dialog.setLayout(new FlowLayout());
        dialog.add(new Label(message));
        Button okButton = new Button("OK");
        okButton.addActionListener(e -> dialog.setVisible(false));
        dialog.add(okButton);
        dialog.setSize(200, 100);
        dialog.setVisible(true);
    }

    // Sudoku solving using backtracking
    private boolean solveSudoku(int[][] puzzle) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (puzzle[row][col] == 0) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (isSafe(puzzle, row, col, num)) {
                            puzzle[row][col] = num;
                            if (solveSudoku(puzzle)) {
                                return true;
                            }
                            puzzle[row][col] = 0; // Undo assignment
                        }
                    }
                    return false; // Trigger backtracking
                }
            }
        }
        return true; // Solved
    }

    // Check if it's safe to place a number
    private boolean isSafe(int[][] puzzle, int row, int col, int num) {
        for (int x = 0; x < SIZE; x++) {
            if (puzzle[row][x] == num || puzzle[x][col] == num || puzzle[row - row % 3 + x / 3][col - col % 3 + x % 3] == num) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        new SudokuSolver();
    }
}
