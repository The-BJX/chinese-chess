package pieces;

import Core.Board;
import data.Side;
import data.PieceType;
import data.Position;

import java.util.List;

public class SoldierPiece extends Piece{
    public SoldierPiece(Side side){
        super(side, PieceType.SOLDIER);
    }

    public boolean crossedRiver(Position position){
        if(this.side== Side.RED){
            return position.getRow()<=4;
        }
        else{
            return position.getRow()>=5;
        }
    }

    public List<Position> getUnfilteredLegalMoves(Board board, Position currentPosition) {
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getCol();
        List<Position> legalMoves = new java.util.ArrayList<>();

        // Forward move
        int forwardRow = (this.side == Side.RED) ? currentRow - 1 : currentRow + 1;
        if (forwardRow >= 0 && forwardRow < Board.ROWS) {
            Position forwardPosition = new Position(forwardRow, currentCol);
            Piece targetPiece = board.getPieceAt(forwardPosition);
            if (targetPiece == null || targetPiece.side != this.side) {
                legalMoves.add(forwardPosition);
            }
        }

        // Sideways moves after crossing the river
        if (crossedRiver(currentPosition)) {
            // Left move
            int leftCol = currentCol - 1;
            if (leftCol >= 0) {
                Position leftPosition = new Position(currentRow, leftCol);
                Piece targetPiece = board.getPieceAt(leftPosition);
                if (targetPiece == null || targetPiece.side != this.side) {
                    legalMoves.add(leftPosition);
                }
            }

            // Right move
            int rightCol = currentCol + 1;
            if (rightCol < Board.COLS) {
                Position rightPosition = new Position(currentRow, rightCol);
                Piece targetPiece = board.getPieceAt(rightPosition);
                if (targetPiece == null || targetPiece.side != this.side) {
                    legalMoves.add(rightPosition);
                }
            }
        }

        return legalMoves;
    }

}
