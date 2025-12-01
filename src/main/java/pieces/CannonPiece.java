package pieces;

import Core.Board;
import data.Side;
import data.PieceType;
import data.Position;

import java.util.List;

public class CannonPiece extends Piece{
    public CannonPiece(Side side){
        super(side, PieceType.CANNON);
    }

    public List<Position> getUnfilteredLegalMoves(Board board, Position currentPosition) {
        List<Position> legalMoves = new java.util.ArrayList<>();
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getCol();

        // Directions: up, down, left, right
        int[][] directions = {
                {-1, 0}, // up
                {1, 0},  // down
                {0, -1}, // left
                {0, 1}   // right
        };

        for (int[] direction : directions) {
            int dRow = direction[0];
            int dCol = direction[1];
            boolean hasJumped = false;
            int r = currentRow + dRow;
            int c = currentCol + dCol;

            while (r >= 0 && r < Board.ROWS && c >= 0 && c < Board.COLS) {
                Position newPosition = new Position(r, c);
                Piece targetPiece = board.getPieceAt(newPosition);

                if (!hasJumped) {
                    if (targetPiece == null) {
                        legalMoves.add(newPosition);
                    }
                    else {
                        hasJumped = true; // Found the first piece to jump over
                    }
                }
                else {
                    if (targetPiece != null) {
                        if (targetPiece.side != this.side) {
                            legalMoves.add(newPosition); // Can capture
                        }
                        break; // Stop searching in this direction after capturing or hitting a piece
                    }
                }

                r += dRow;
                c += dCol;
            }
        }

        return legalMoves;
    }

}
