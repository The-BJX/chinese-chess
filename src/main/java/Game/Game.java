package Game;
//This file is now for test only

import Core.Board;
import GameSave.MoveRecord;
import data.GameStatus;
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
    private GameStatus gameStatus;
    public void setGameStatus(GameStatus u){
        gameStatus=u;
    }
    public GameStatus getGameStatus(){
        return gameStatus;
    }
    public Game(){
        gameStatus=GameStatus.ONGOING;
    }


    public static boolean positionInList(Position pos, List<Position> posList){
        for(Position p: posList){
            if(p.equals(pos)){
                return true;
            }
        }
        return false;
    }

    public void touchPosition(Position position) throws Exception {
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
            else if(board.getPieceAt(position)!=null&&selectedPiece.side.equals(board.getPieceAt(position).side)){
                board.setSelectedPosition(position);
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

                    MoveRecord record=new MoveRecord(selectedPosition, position);
                    board.moveHistory.add(record);
                    board.saveBoard("chinese_chess_save.dat");

                    //Switch turn
                    board.switchTurn();
                    System.out.println("Move successful!");



                    int status= board.judgeGameOver();
                    if(status==1){
                        System.out.println("Red wins!");
                        setGameStatus(GameStatus.RED_WIN);
                    }
                    else if(status==2){
                        System.out.println("Black wins!");
                        setGameStatus(GameStatus.BLACK_WIN);
                    }
                    else if(status==3){
                        System.out.println("Red stalemate, Black Wins");//这里应该仔细判断到底是哪边困毙了
                    }
                    else if(status==4) {
                        System.out.println("Black stalemate, Red Wins");
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