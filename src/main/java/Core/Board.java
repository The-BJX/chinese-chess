package Core;

import data.InitialPositions;
import data.Side;
import data.Position;
import pieces.GeneralPiece;
import pieces.Piece;

import java.util.List;

public class Board {
    public static final int ROWS =10;
    public static final int COLS = 9;
    private final Piece[][] grid=new Piece[ROWS][COLS];
    private Side currentTurn= Side.RED;
    // Track the currently selected position on this board (if any)
    private Position selectedPosition = null;

    public Board(){
        initializeBoard();
    }

    public Side getCurrentTurn(){
        return currentTurn;
    }

    public void switchTurn(){
        if(currentTurn== Side.RED){
            currentTurn= Side.BLACK;
        }else{
            currentTurn= Side.RED;
        }
    }

    private void initializeBoard(){
        setPieceAt(InitialPositions.blackGeneral, new GeneralPiece(Side.BLACK));
        setPieceAt(InitialPositions.redGeneral, new GeneralPiece(Side.RED));
    }

    public Piece getPieceAt(Position position) {
        int r=position.getRow();
        int c=position.getCol();
        return grid[r][c];
    }

    public Position getGeneralPosition(Side side){
        for(int r=0; r<ROWS; r++){
            for(int c=0; c<COLS; c++){
                Piece piece=grid[r][c];
                if(piece!=null && piece.pieceType==data.PieceType.GENERAL && piece.side==side){
                    return new Position(r,c);
                }
            }
        }
        return null;
    }

    public List<Position> getThreatenedPositions(Side side){
        List<Position> threatenedPositions=new java.util.ArrayList<>();
        for(int r=0; r<ROWS; r++){
            for(int c=0; c<COLS; c++){
                Piece piece=grid[r][c];
                if(piece!=null && piece.side!=side){
                    Position piecePosition=new Position(r,c);
                    // Use unfiltered moves here to determine threats so we don't recurse through getLegalMoves
                    List<Position> pieceMoves=piece.getUnfilteredMoves(this,piecePosition);
                    if(pieceMoves!=null){
                        threatenedPositions.addAll(pieceMoves);
                    }
                }
            }
        }
        return threatenedPositions;
    }


    //TODO return isChecked boolean

    public void movePiece(Position fromPosition, Position toPosition) {
        int fromRow=fromPosition.getRow();
        int fromCol=fromPosition.getCol();
        if (grid[fromRow][fromCol]==null){
            System.out.println("No piece at the source position!");
            return;
        }
        Piece piece = getPieceAt(fromPosition);
        setPieceAt(toPosition, piece);
        setPieceAt(fromPosition, null);


        //TODO output data log should be added here
    }

    public void setPieceAt(Position position, Piece piece) {
        int r=position.getRow();
        int c=position.getCol();
        grid[r][c] = piece;
    }

    public void select(Position position) {
        Piece piece = getPieceAt(position);
        if (piece != null) {
            selectedPosition = position;
            piece.isSelected = true;
        }
    }

    public void deselect() {
        if (selectedPosition != null) {
            Piece piece = getPieceAt(selectedPosition);
            if (piece != null) {
                piece.isSelected = false;
            }
            selectedPosition = null;
        }
    }

    public Position getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(Position position) {
        this.selectedPosition = position;
    }

    //printBoard
    public void printBoard(){
        for(int r=0; r<ROWS; r++){
            for(int c=0; c<COLS; c++){
                if(grid[r][c]==null){
                    System.out.print(". ");
                }else{
                    System.out.print(grid[r][c].pieceType.toString().charAt(0)+" ");
                }
            }
            System.out.println();
        }
    }


}
