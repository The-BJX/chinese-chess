package Core;

import data.Color;
import data.Position;
import pieces.GeneralPiece;
import pieces.Piece;

public class Board {
    public static final int ROWS =10;
    public static final int COLS = 9;
    private final Piece[][] grid=new Piece[ROWS][COLS];
    private Color currentTurn=Color.RED;

    public Board(){
        initializeBoard();
    }

    private void initializeBoard(){
        setPieceAt(0,4, new GeneralPiece(Color.BLACK));
        setPieceAt(9,4, new GeneralPiece(Color.RED));
    }

    public Piece getPieceAt(Position position) {
        int r=position.getRow();
        int c=position.getCol();
        return grid[r][c];
    }

    public void movePiece(Position fromPosition, Position toPosition) {
        int fromRow=fromPosition.getRow();
        int fromCol=fromPosition.getCol();
        int toRow=toPosition.getRow();
        int toCol=toPosition.getCol();
        if (grid[fromRow][fromCol]==null){
            System.out.println("No piece at the source position!");
            return;
        }
        Piece piece = getPieceAt(fromPosition);
        setPieceAt(toRow, toCol, piece);
        setPieceAt(fromRow, fromCol, null);
    }


    public void setPieceAt(int r, int c, Piece piece) {
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
