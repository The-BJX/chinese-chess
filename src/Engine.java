import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class Engine {

    // --- 1. Enumerations ---

    public enum Color {
        RED, BLACK;

        public Color opposite() {
            return this == RED ? BLACK : RED;
        }

        @Override
        public String toString() {
            return this == RED ? "R" : "B";
        }
    }
    //PieceType enum表示棋子类型
    public enum PieceType {
        GENERAL, ADVISOR, ELEPHANT, CHARIOT, HORSE, CANNON, SOLDIER;

        //GENERAL帥 将
        //ADVISOR仕 士
        //ELEPHANT相 象
        //CHARIOT车
        //HORSE马
        //CANNON炮
        //SOLDIER兵 卒
    }

    // --- 2. Position (Immutable Value Object) ---

    /** Represents a single square (row, col) on the 10x9 board. */
    public static class Position {
        public final int row;
        public final int col;

        public Position(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return row == position.row && col == position.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }

        @Override
        public String toString() {
            return "(" + row + ", " + col + ")";
        }
    }

    // --- 3. Move (Immutable Value Object) ---

    /** Represents a transition from a start Position to an end Position. */
    public static class Move {
        public final Position start;
        public final Position end;
        public final Piece piece;

        public Move(Position start, Position end, Piece piece) {
            this.start = start;
            this.end = end;
            this.piece = piece;
        }

        @Override
        public String toString() {
            return piece.getShortCode() + " from " + start + " to " + end;
        }
    }

    // --- 4. Abstract Piece Class (The Contract) ---

    public static abstract class Piece {
        public final Color color;
        public final PieceType type;

        public Piece(Color color, PieceType type) {
            this.color = color;
            this.type = type;
        }

        /**
         * Calculates all potential moves for this piece, ignoring the "check" rule.
         * This is the piece's pattern only.
         */


        /** Returns a short code for board printing (e.g., 'RC' for Red Chariot) */
        public String getShortCode() {
            String typeCode = type.name().substring(0, 1);
            return color.toString() + typeCode;
        }
    }

    // --- 5. Concrete Piece Implementations ---

    /** Implements the movement logic for the CHRARIOT (Rook). */
    public static class ChariotPiece extends Piece {
        public ChariotPiece(Color color) {
            super(color, PieceType.CHARIOT);
        }

    }

    /** Implements the movement logic and PALACE constraint for the GENERAL (King). */
    public static class GeneralPiece extends Piece {
        public GeneralPiece(Color color) {
            super(color, PieceType.GENERAL);
        }
    }

    // --- 6. Board (The State Container) ---

    public static class Board {
        public static final int ROWS = 10;
        public static final int COLS = 9;
        private final Piece[][] grid = new Piece[ROWS][COLS];
        private Color currentTurn = Color.RED;

        public Board() {
            initializeBoard();
        }

        /** Initial setup of a Xiangqi game. */
        private void initializeBoard() {
            // Place Generals
            grid[0][4] = new GeneralPiece(Color.BLACK);
            grid[9][4] = new GeneralPiece(Color.RED);

            // Place Chariots (Rooks) - Example pieces for demonstration
            grid[0][0] = new ChariotPiece(Color.BLACK);
            grid[0][8] = new ChariotPiece(Color.BLACK);
            grid[9][0] = new ChariotPiece(Color.RED);
            grid[9][8] = new ChariotPiece(Color.RED);

            // Initialize the rest of the board with nulls (empty) by default.
            // Other pieces (Horse, Cannon, etc.) would be placed here.
        }

        /** Moves a piece from start to end, assuming the move is legal. */


        public void printBoard() {
            System.out.println("\n  -----------------------------------");
            System.out.println("  | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 |");
            System.out.println("  -----------------------------------");
            for (int r = 0; r < ROWS; r++) {
                System.out.printf("%d |", r);
                for (int c = 0; c < COLS; c++) {
                    Piece p = grid[r][c];
                    String cell = (p != null) ? p.getShortCode() : "  ";
                    System.out.print(" " + cell + " |");
                }
                if (r == 4) {
                    System.out.print(" <--- The River");
                }
                System.out.println();
                System.out.println("  -----------------------------------");
            }
            System.out.println("Current Turn: " + currentTurn);
        }
    }

    // --- 7. Main Engine Class ---

    public static void main(String[] args) {

    }
}
