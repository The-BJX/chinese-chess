package pieces;

import Core.Board;
import data.Color;
import data.PieceType;
import data.Position;

import java.util.List;
import java.util.ArrayList;

public class GeneralPiece extends Piece{

    public GeneralPiece(Color color) {
        super(color, PieceType.GENERAL);
    }

    private boolean isInPalace(Position generalPosition) {
        int row = generalPosition.getRow();
        int col = generalPosition.getCol();
        if (col < 3 || col > 5) return false;
        // Red Palace: Rows 7, 8, 9
        if (color == Color.RED && row >= 7 && row <= 9) return true;
        // Black Palace: Rows 0, 1, 2
        if (color == Color.BLACK && row >= 0 && row <= 2) return true;
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
                if (targetPiece == null || targetPiece.color != this.color) {
                    legalMoves.add(tempPosition);
                }
            }
        }

        //TODO 将军沿着棋盘斜线走

        final Position redPalaceCenter=new Position(8,4);
        final Position blackPalaceCenter=new Position(1,4);


        if(color==Color.RED&&currentPosition.equals(redPalaceCenter)||color==Color.BLACK&&currentPosition.equals(blackPalaceCenter)){
            int[][] diagonalDirections={{1,1},{1,-1},{-1,1},{-1,-1}};
            for(int[] dir:diagonalDirections){
                int tempRow=currentRow+dir[0];
                int tempCol=currentCol+dir[1];
                Position tempPosition=new Position(tempRow,tempCol);
                Piece targetPiece=board.getPieceAt(tempPosition);
                if(targetPiece==null||targetPiece.color!=this.color){
                    legalMoves.add(tempPosition);
                }
            }
        }

        if(color==Color.RED){
            for(Position pos:legalMoves){
                if(redPalaceCenter.equals(pos)){
                    return legalMoves;
                }
            }
            if(board.getPieceAt(redPalaceCenter).color==Color.BLACK||board.getPieceAt(redPalaceCenter)==null){
                legalMoves.add(redPalaceCenter);
                return legalMoves;
            }
        }
        if(color==Color.BLACK){
            for(Position pos:legalMoves){
                if(blackPalaceCenter.equals(pos)){
                    return legalMoves;
                }
            }
            if(board.getPieceAt(blackPalaceCenter)==null||board.getPieceAt(blackPalaceCenter).color==Color.RED){
                legalMoves.add(blackPalaceCenter);
                return legalMoves;
            }
        }







        //TODO The situation where the two generals face each other directly without any pieces in between






        return legalMoves;
    }





}
