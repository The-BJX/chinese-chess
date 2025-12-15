package Game;
//This file is now for test only

import Core.Board;
import GameDialogues.GameDialogue;
import GameSave.MoveRecord;
import chinese_chess.GraphicElements;
import data.GameStatus;
import pieces.Piece;
import data.Position;

import java.util.ArrayList;
import java.util.List;

public class Game{

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
    public boolean isViewingRecord;
    GraphicElements elements;
    public Game(String username, GraphicElements elements){
        board= new Board(username);
        gameStatus=GameStatus.ONGOING;
        board.AnnouncedGameResult(false);
        this.user=username;
        this.elements=elements;
    }

    public String user;
    public Board board;


    public static boolean positionInList(Position pos, List<Position> posList){
        for(Position p: posList){
            if(p.equals(pos)){
                return true;
            }
        }
        return false;
    }

    public void touchPosition(Position position) throws Exception {
        boolean isGuest = (user==null||user.equals(new String("")));
        if(board.isViewing()){
            System.out.println("Currently viewing past moves. Cannot make a move.");
            return;
        }

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
            if(getGameStatus()==GameStatus.ALTERING){
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
            else if(board.getPieceAt(position)!=null&&selectedPiece.side.equals(board.getPieceAt(position).side)&&getGameStatus()!=GameStatus.ALTERING){
                board.setSelectedPosition(position);
                selectedPiece.isSelected=false;
            }
            else{
                //Try to move the piece to the new position
                List<Position> legalMoves=selectedPiece.getLegalMoves(board,selectedPosition);
                if(positionInList(position,legalMoves)||getGameStatus()==GameStatus.ALTERING) {
                    //Move is legal
                    board.movePiece(selectedPosition, position, true, isGuest);
                    //Deselect the piece after moving
                    board.setSelectedPosition(null);
                    selectedPiece.isSelected = false;


                    //Switch turn
                    board.switchTurn();
                    System.out.println("Move successful!");
                    int status = board.judgeGameOver();
                    GameStatus prevStatus=null;
                    if(elements!=null){
                        prevStatus = elements.game.getGameStatus();
                    }
                    if (status == 1) {
                        System.out.println("Red wins!");
                        setGameStatus(GameStatus.RED_WIN);
                    } else if (status == 2) {
                        System.out.println("Black wins!");
                        setGameStatus(GameStatus.BLACK_WIN);
                    } else if (status == 3) {
                        System.out.println("Red wins! (Stale)");
                        setGameStatus(GameStatus.RED_WIN_STALE);
                    } else if (status == 4) {
                        System.out.println("Black wins! (Stale)");
                        setGameStatus(GameStatus.BLACK_WIN_STALE);
                    }

                    if(elements!=null&&elements.game.getGameStatus().equals(prevStatus)==false){
                        if(elements.game.getGameStatus().equals(GameStatus.RED_WIN)){
                            elements.Dialogue.startInfoDialogue(elements,"将死","红方胜利",elements.stage);
                        }
                        if(elements.game.getGameStatus().equals(GameStatus.BLACK_WIN)){
                            elements.Dialogue.startInfoDialogue(elements,"将死","黑方胜利",elements.stage);
                        }
                        if(elements.game.getGameStatus().equals(GameStatus.RED_WIN_STALE)){
                            elements.Dialogue.startInfoDialogue(elements,"困毙","红方胜利",elements.stage);
                        }
                        if(elements.game.getGameStatus().equals(GameStatus.BLACK_WIN_STALE)){
                            elements.Dialogue.startInfoDialogue(elements,"困毙","黑方胜利",elements.stage);
                        }
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