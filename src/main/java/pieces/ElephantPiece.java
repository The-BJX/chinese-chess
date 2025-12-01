package pieces;

import Core.Board;
import data.Side;
import data.PieceType;
import data.Position;

import java.util.List;

public class ElephantPiece extends Piece{
    public ElephantPiece(Side side){
        super(side, PieceType.ELEPHANT);
    }

    public List<Position> getUnfilteredLegalMoves(Board board, Position currentPosition) {
        List<Position> legalMoves = new java.util.ArrayList<>();
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getCol();

        // Possible elephant moves (2 points diagonally)
        int[][] moves = {
                {-2, -2}, {-2, 2},
                {2, -2}, {2, 2}
        };

        for (int[] move : moves) {
            int newRow = currentRow + move[0];
            int newCol = currentCol + move[1];
            Position newPosition = new Position(newRow, newCol);

            // Check board boundaries
            if (newPosition.inBoardBounds()) {
                // Check for river crossing
                if ((this.side == Side.RED && newRow >= 5) || (this.side == Side.BLACK && newRow <= 4)) {
                    Piece targetPiece = board.getPieceAt(newPosition);
                    if (targetPiece == null || targetPiece.side != this.side) {
                        legalMoves.add(newPosition);
                    }
                }
            }
        }

        return legalMoves;
    }

}
