package Core;

import data.InitialPositions;
import data.Side;
import data.Position;
import pieces.GeneralPiece;
import pieces.Piece;

public class Board {
    public static final int ROWS =10;
    public static final int COLS = 9;
    private final Piece[][] grid=new Piece[ROWS][COLS];
    private Side currentTurn= Side.RED;

    public Board(){
        initializeBoard();
    }

    public Side getCurrentTurn(){
        return currentTurn;
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
    }


    public void setPieceAt(Position position, Piece piece) {
        int r=position.getRow();
        int c=position.getCol();
        grid[r][c] = piece;
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
