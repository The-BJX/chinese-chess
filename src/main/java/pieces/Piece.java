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


    public List<Position> getLegalMoves(Board board, Position currentPosition) throws Exception {
        List<Position> unfiltered = getUnfilteredLegalMoves(board, currentPosition);
        if (unfiltered == null) return null;

        List<Position> legalMoves = new ArrayList<>();

        //create a simulate board in order to protect the game board
        Board simBoard= new Board();

        // For each unfiltered move, simulate it on the simBoard (apply then undo)
        for (Position toPos : new ArrayList<>(unfiltered)){

            //initialize simBoard
            for(int i=0;i<10;i++){
                for(int j=0;j<9;j++){
                    Position p=new Position(i,j);
                    simBoard.setPieceAt(p,board.getPieceAt(p));
                }
            }

            // Save pieces at from and to
            Piece movingPiece = simBoard.getPieceAt(currentPosition);
            Piece capturedPiece = simBoard.getPieceAt(toPos);

            // Apply move
            simBoard.movePiece(currentPosition,toPos);
            //board.setPieceAt(toPos, movingPiece);
            //board.setPieceAt(currentPosition, null);

            // After applying, check whether own general is threatened by opponent using unfiltered moves
            Position ownGeneralPos = simBoard.getGeneralPosition(this.side);
            boolean isInCheck = false;

            List<Position> threatened = simBoard.getThreatenedPositions(this.side);
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
