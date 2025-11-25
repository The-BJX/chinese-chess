package pieces;

import Core.Board;
import data.Side;
import data.PieceType;
import data.Position;

import java.util.List;

public abstract class Piece {
    public final Side side;
    public final PieceType pieceType;

    private Position currentPosition;

    public boolean isSelected=false;

    public Piece(Side side, PieceType pieceType){
        this.side = side;
        this.pieceType=pieceType;
    }

    public List<Position> getLegalMoves(Board board, Position currentPosition){
        List<Position> legalMoves=null;
        if(this.pieceType==PieceType.GENERAL){
            GeneralPiece generalPiece=new GeneralPiece(this.side);
            legalMoves = generalPiece.getLegalMoves(board,currentPosition);
        }

        //TODO Add other piece types here in the future

        //TODO If this move will cause check, remove it from legal moves

        return null;
    }

    public void setCurrentPosition(Position position){
        this.currentPosition=position;
    }

    public void setRow(int row){
        this.currentPosition.row=row;
    }

    public void setCol(int col){
        this.currentPosition.col=col;
    }

    public Position getCurrentPosition(){
        return currentPosition;
    }



}
