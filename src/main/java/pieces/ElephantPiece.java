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

        List<Position> ret = new java.util.ArrayList<>();

        for(Position pos:legalMoves){
            int toRow=pos.getRow();
            int toCol=pos.getCol();
            int elephantEyeRow = (currentRow + toRow)/2;
            int elephantEyeCol = (currentCol + toCol)/2;
            Position elephantEyePos= new Position(elephantEyeRow, elephantEyeCol);
            Piece elephantEyePiece=board.getPieceAt(elephantEyePos);
            if(elephantEyePiece==null){
                ret.add(pos);
            }

        }

        return ret;
    }

}
