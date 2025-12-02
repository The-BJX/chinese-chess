package Game;
//This file is now for test only

import Core.Board;
import pieces.Piece;
import data.Position;

import java.util.ArrayList;
import java.util.List;

public class Game{
    private Board board= new Board();
    public void printBoard(){
        board.printBoard();
    }
    public Board getBoard(){
        return board;
    }

    public static boolean positionInList(Position pos, List<Position> posList){
        for(Position p: posList){
            if(p.equals(pos)){
                return true;
            }
        }
        return false;
    }

    public void touchPosition(Position position){
        if(board.getSelectedPosition()==null){
            //No piece is selected yet
            Piece tempPiece=board.getPieceAt(position);
            if(tempPiece==null){
                //No piece at this position
                System.out.println("No piece at this position!");
                return;
            }
            if(tempPiece.side==board.getCurrentTurn()){
                //Select this piece
                board.setSelectedPosition(position);
            }
        }
        else{
            //A piece is already selected
            Position selectedPosition=board.getSelectedPosition();
            Piece selectedPiece=board.getPieceAt(selectedPosition);
            if(selectedPosition.equals(position)){
                //Deselect the piece
                board.setSelectedPosition(null);
                selectedPiece.isSelected=false;
            }
            else{
                //Try to move the piece to the new position
                List<Position> legalMoves=selectedPiece.getLegalMoves(board,selectedPosition);
                if(positionInList(position,legalMoves)){
                    //Move is legal
                    board.movePiece(selectedPosition,position);
                    //Deselect the piece after moving
                    board.setSelectedPosition(null);
                    selectedPiece.isSelected=false;
                    //Switch turn
                    board.switchTurn();
                    System.out.println("Move successful!");

                    int status= board.judgeGameOver();
                    if(status==1){
                        System.out.println("Red wins!");
                    }
                    else if(status==2){
                        System.out.println("Black wins!");
                    }
                    else if(status==0){
                        System.out.println("Draw!");
                    }

                }
                else{
                    //Deselect the piece if the move is illegal
                    board.setSelectedPosition(null);
                    selectedPiece.isSelected=false;
                    System.out.println("Illegal move!");
                }
            }
        }

    }

}