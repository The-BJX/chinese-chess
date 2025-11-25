package pieces;

import Core.Board;
import data.Side;
import data.PieceType;
import data.Position;

import java.util.List;

public class GeneralPiece extends Piece{

    public GeneralPiece(Side side) {
        super(side, PieceType.GENERAL);
    }

    private boolean isInPalace(Position generalPosition) {
        int row = generalPosition.getRow();
        int col = generalPosition.getCol();
        if (col < 3 || col > 5) return false;
        // Red Palace: Rows 7, 8, 9
        if (side == Side.RED && row >= 7 && row <= 9) return true;
        // Black Palace: Rows 0, 1, 2
        if (side == Side.BLACK && row >= 0 && row <= 2) return true;
        return false;
    }


    public boolean isChecked(Position generalPosition, List<Position> opponentMoves) {
        int generalRow = generalPosition.getRow();
        int generalCol = generalPosition.getCol();
        for (Position pos : opponentMoves) {
            if (pos.row == generalRow && pos.col == generalCol) {
                return true;
            }
        }
        return false;
    }
    public List<Position> getLegalMoves(Board board, Position currentPosition) {
        int currentRow = currentPosition.row;
        int currentCol = currentPosition.col;
        List<Position> legalMoves = new java.util.ArrayList<>();
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}}; // Down, Up, Right, Left

        for (int[] dir : directions) {
            int tempRow = currentRow + dir[0];
            int tempCol = currentCol + dir[1];
            Position tempPosition = new Position(tempRow, tempCol);

            if (isInPalace(tempPosition)) {
                Piece targetPiece = board.getPieceAt(tempPosition);
                if (targetPiece == null || targetPiece.side != this.side) {
                    legalMoves.add(tempPosition);
                }
            }
        }


        final Position redPalaceCenter=new Position(8,4);
        final Position blackPalaceCenter=new Position(1,4);


        if(side == Side.RED&&currentPosition.equals(redPalaceCenter)|| side == Side.BLACK&&currentPosition.equals(blackPalaceCenter)){
            int[][] diagonalDirections={{1,1},{1,-1},{-1,1},{-1,-1}};
            for(int[] dir:diagonalDirections){
                int tempRow=currentRow+dir[0];
                int tempCol=currentCol+dir[1];
                Position tempPosition=new Position(tempRow,tempCol);
                Piece targetPiece=board.getPieceAt(tempPosition);
                if(targetPiece==null||targetPiece.side !=this.side){
                    legalMoves.add(tempPosition);
                }
            }
        }

        boolean canMoveDiagonally=true;
        if(side == Side.RED){
            for(Position pos:legalMoves){
                if(redPalaceCenter.equals(pos)){
                    canMoveDiagonally=false;
                }
            }
            if(canMoveDiagonally){
                if(board.getPieceAt(redPalaceCenter).side == Side.BLACK||board.getPieceAt(redPalaceCenter)==null){
                    legalMoves.add(redPalaceCenter);
                }
            }
        }
        else{
            for(Position pos:legalMoves){
                if(blackPalaceCenter.equals(pos)){
                    canMoveDiagonally=false;
                }
            }
            if(canMoveDiagonally){
                if(board.getPieceAt(blackPalaceCenter).side == Side.RED||board.getPieceAt(blackPalaceCenter)==null){
                    legalMoves.add(blackPalaceCenter);
                }
            }
        }


        //The situation where the two generals face each other directly without any pieces in between
        // scan upwards
        for (int r = currentRow - 1; r >= 0; r--) {
            Position p = new Position(r, currentCol);
            Piece tempPiece = board.getPieceAt(p);
            if (tempPiece == null) continue;
            // first non-empty piece encountered
            if (tempPiece.pieceType == PieceType.GENERAL && tempPiece.side != this.side) {
                legalMoves.add(p);
            }
            break;
        }
        // scan downwards
        for (int r = currentRow + 1; r < Board.ROWS; r++) {
            Position p = new Position(r, currentCol);
            Piece tempPiece = board.getPieceAt(p);
            if (tempPiece == null) continue;
            if (tempPiece.pieceType == PieceType.GENERAL && tempPiece.side != this.side) {
                legalMoves.add(p);
            }
            break;
        }


        return legalMoves;
    }


}
