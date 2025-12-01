package pieces;

import Core.Board;
import data.Side;
import data.PieceType;
import data.Position;

import java.util.List;

public class HorsePiece extends Piece{
    public HorsePiece(Side side){
        super(side, PieceType.HORSE);
    }

    public List<Position> getUnfilteredLegalMoves(Board board, Position currentPosition) {
        List<Position> legalMoves = new java.util.ArrayList<>();
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getCol();

        // Possible horse moves (L-shape)
        int[][] moves = {
                {-2, -1}, {-2, 1},
                {2, -1}, {2, 1},
                {-1, -2}, {-1, 2},
                {1, -2}, {1, 2}
        };

        for (int[] move : moves) {
            int newRow = currentRow + move[0];
            int newCol = currentCol + move[1];
            Position newPosition = new Position(newRow, newCol);

            // Check board boundaries
            if (newPosition.inBoardBounds()) {
                // Check for blocking pieces
                int blockRow = currentRow + (move[0] / 2);
                int blockCol = currentCol + (move[1] / 2);
                Position blockPosition = new Position(blockRow, blockCol);
                Piece blockingPiece = board.getPieceAt(blockPosition);

                if (blockingPiece == null) {
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