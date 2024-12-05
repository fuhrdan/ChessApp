import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessApp {
    private JFrame frame;
    private JButton[][] boardButtons;
    private String[][] boardState;
    private boolean isWhiteTurn = true; // Tracks player turns
    private int selectedRow = -1, selectedCol = -1;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChessApp::new);
    }

    public ChessApp() {
        initializeBoard();
        createAndShowGUI();
    }

    // Initialize the board state with pieces
    private void initializeBoard() {
        boardState = new String[8][8];
        String[] backRow = {"R", "N", "B", "Q", "K", "B", "N", "R"}; // Rook, Knight, Bishop, etc.

        // White pieces (bottom of the board)
        for (int i = 0; i < 8; i++) {
            boardState[7][i] = "W" + backRow[i]; // Example: WR = White Rook
            boardState[6][i] = "WP";           // White Pawns
        }

        // Black pieces (top of the board)
        for (int i = 0; i < 8; i++) {
            boardState[0][i] = "B" + backRow[i]; // Example: BR = Black Rook
            boardState[1][i] = "BP";           // Black Pawns
        }

        // Empty squares
        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                boardState[i][j] = "";
            }
        }
    }

    // Create and display the GUI
    private void createAndShowGUI() {
        frame = new JFrame("Chess - 2 Player");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(8, 8));

        boardButtons = new JButton[8][8];

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton button = new JButton();
                button.setFont(new Font("Arial", Font.PLAIN, 20));
                button.setFocusPainted(false);

                // Set initial piece
                updateButtonText(button, boardState[row][col]);

                // Set alternating colors for the board
                if ((row + col) % 2 == 0) {
                    button.setBackground(Color.WHITE);
                } else {
                    button.setBackground(Color.GRAY);
                }

                final int r = row, c = col;
                button.addActionListener(e -> handleButtonClick(r, c));
                boardButtons[row][col] = button;
                frame.add(button);
            }
        }

        frame.setSize(600, 600);
        frame.setVisible(true);
    }

    // Handle button clicks for piece selection and movement
    private void handleButtonClick(int row, int col) {
        if (selectedRow == -1 && selectedCol == -1) {
            // Select a piece
            if (!boardState[row][col].isEmpty() && isCorrectPlayerPiece(row, col)) {
                selectedRow = row;
                selectedCol = col;
                boardButtons[row][col].setBackground(Color.YELLOW); // Highlight selected piece
            }
        } else {
            // Move the piece
            if (isCorrectPlayerPiece(row, col)) {
                // If clicked another valid piece of the same player, switch selection
                resetSelection();
                handleButtonClick(row, col);
            } else {
                // Move logic: no validation for now, just swap
                boardState[row][col] = boardState[selectedRow][selectedCol];
                boardState[selectedRow][selectedCol] = "";

                updateButtonText(boardButtons[row][col], boardState[row][col]);
                updateButtonText(boardButtons[selectedRow][selectedCol], "");

                resetSelection();
                isWhiteTurn = !isWhiteTurn; // Switch turns
            }
        }
    }

    // Check if the piece belongs to the current player
    private boolean isCorrectPlayerPiece(int row, int col) {
        if (boardState[row][col].isEmpty()) return false;
        return (isWhiteTurn && boardState[row][col].startsWith("W")) ||
               (!isWhiteTurn && boardState[row][col].startsWith("B"));
    }

    // Update button text to show piece
    private void updateButtonText(JButton button, String piece) {
        if (piece.isEmpty()) {
            button.setText("");
        } else {
            switch (piece.charAt(1)) {
                case 'P': button.setText(piece.startsWith("W") ? "♙" : "♟"); break;
                case 'R': button.setText(piece.startsWith("W") ? "♖" : "♜"); break;
                case 'N': button.setText(piece.startsWith("W") ? "♘" : "♞"); break;
                case 'B': button.setText(piece.startsWith("W") ? "♗" : "♝"); break;
                case 'Q': button.setText(piece.startsWith("W") ? "♕" : "♛"); break;
                case 'K': button.setText(piece.startsWith("W") ? "♔" : "♚"); break;
            }
        }
    }

    // Reset selection after move or deselect
    private void resetSelection() {
        if (selectedRow != -1 && selectedCol != -1) {
            boardButtons[selectedRow][selectedCol].setBackground(
                (selectedRow + selectedCol) % 2 == 0 ? Color.WHITE : Color.GRAY);
        }
        selectedRow = -1;
        selectedCol = -1;
    }
}
