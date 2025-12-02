package pieces;

import Core.Board;
import data.Side;
import data.PieceType;
import data.Position;
import Game.Game;

import java.util.ArrayList;
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
        List<Position> unfiltered = getUnfilteredLegalMoves(board, currentPosition);
        if (unfiltered == null) return null;

        List<Position> legalMoves = new ArrayList<>();

        // For each unfiltered move, simulate it on the board (apply then undo)
        for (Position toPos : new ArrayList<>(unfiltered)){
            // Save pieces at from and to
            Piece movingPiece = board.getPieceAt(currentPosition);
            Piece capturedPiece = board.getPieceAt(toPos);

            // Apply move
            board.movePiece(currentPosition,toPos);
            //board.setPieceAt(toPos, movingPiece);
            //board.setPieceAt(currentPosition, null);

            // After applying, check whether own general is threatened by opponent using unfiltered moves
            Position ownGeneralPos = board.getGeneralPosition(this.side);
            boolean isInCheck = false;

            List<Position> threatened = board.getThreatenedPositions(this.side);
            isInCheck = Game.positionInList(ownGeneralPos,threatened);



            // Undo move
            board.setPieceAt(currentPosition, movingPiece);
            board.setPieceAt(toPos, capturedPiece);

            if (!isInCheck) {
                legalMoves.add(toPos);
            }
        }

        return legalMoves;
    }

    public abstract List<Position> getUnfilteredLegalMoves(Board board, Position currentPosition);

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
