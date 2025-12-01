package pieces;

import Core.Board;
import data.Side;
import data.PieceType;
import data.Position;

import java.util.List;

public class ChariotPiece extends Piece{
    public ChariotPiece(Side side){
        super(side, PieceType.CHARIOT);
    }

    public List<Position> getUnfilteredLegalMoves(Board board, Position currentPosition) {
        List<Position> legalMoves = new java.util.ArrayList<>();
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getCol();

        // Directions: up, down, left, right
        int[][] directions = {
                {-1, 0}, {1, 0},
                {0, -1}, {0, 1}
        };

        for (int[] direction : directions) {
            int newRow = currentRow + direction[0];
            int newCol = currentCol + direction[1];

            while (newRow >= 0 && newRow < Board.ROWS && newCol >= 0 && newCol < Board.COLS) {
                Position newPosition = new Position(newRow, newCol);
                Piece targetPiece = board.getPieceAt(newPosition);

                if (targetPiece == null) {
                    legalMoves.add(newPosition);
                }
                else {
                    if (targetPiece.side != this.side) {
                        legalMoves.add(newPosition);
                    }
                    break; // Stop searching in this direction
                }

                newRow += direction[0];
                newCol += direction[1];
            }
        }

        return legalMoves;
    }

}