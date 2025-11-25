package pieces;

import Core.Board;
import data.Color;
import data.PieceType;
import data.Position;

import java.util.List;

public abstract class Piece {
    public final Color color;
    public final PieceType pieceType;

    private Position currentPosition;

    public boolean isSelected=false;

    public Piece(Color color, PieceType pieceType){
        this.color=color;
        this.pieceType=pieceType;
    }

    public List<Position> getLegalMoves(Board board, Position currentPosition){
        List<Position> legalMoves=null;
        if(this.pieceType==PieceType.GENERAL){
            GeneralPiece generalPiece=new GeneralPiece(this.color);
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

    public void select(Board board){
        if(!isSelected) isSelected=true;
    }

    public void deselect(){
        if(isSelected) isSelected=false;
    }

    public boolean isSelected(){
        return isSelected;
    }


}
