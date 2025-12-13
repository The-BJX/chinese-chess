package TestAlgorithm;
//this file is now for test only
import Game.Game;
import GameSave.ChineseChessDataSaver;
import GameSave.MoveRecord;
import data.Side;
import pieces.Piece;
import data.Position;
import java.util.List;
import java.util.Scanner;
import AIMove.AIMove;

public class testAlgorithm {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello and welcome!");

        Game game = new Game("testa");
        game.printBoard();

        System.out.printf("Now it is %s's turn",game.getBoard().getCurrentTurn());
        if(game.getBoard().isGeneralInCheck(game.getBoard().getCurrentTurn())){
            System.out.println(" -- Check!");
        }


        AIMove bot=new AIMove(3);


        Scanner sc= new Scanner(System.in);
        while(true){
            String input=sc.nextLine();
            String[] parts=input.split(" ");
            if(parts.length!=2) {
                if (input.equals("regret")) {
                    game.getBoard().regretLastMove();
                    game.printBoard();
                    System.out.printf("Now it is %s's turn", game.getBoard().getCurrentTurn());
                    if (game.getBoard().isGeneralInCheck(game.getBoard().getCurrentTurn())) {
                        System.out.println(" -- Check!");
                    }
                    System.out.println();
                    continue;
                }

                if (input.equals("viewnext")) {
                    game.getBoard().viewNextMove();
                    game.printBoard();
                    System.out.printf("Now it is %s's turn", game.getBoard().getCurrentTurn());
                    if (game.getBoard().isGeneralInCheck(game.getBoard().getCurrentTurn())) {
                        System.out.println(" -- Check!");
                    }
                    System.out.println();
                    continue;
                }

                if (input.equals("viewprev")) {
                    game.getBoard().viewPreviousMove();
                    game.printBoard();
                    System.out.printf("Now it is %s's turn", game.getBoard().getCurrentTurn());
                    if (game.getBoard().isGeneralInCheck(game.getBoard().getCurrentTurn())) {
                        System.out.println(" -- Check!");
                    }
                    System.out.println();
                    continue;
                }

                if(input.equals("viewstep")){
                    System.out.println("Currently viewing step:"+game.getBoard().getCurrentViewingStep());
                }

                if(input.equals("viewinitial")){
                    game.getBoard().returnViewToInitial();
                    game.printBoard();
                    System.out.printf("Now it is %s's turn", game.getBoard().getCurrentTurn());
                    if (game.getBoard().isGeneralInCheck(game.getBoard().getCurrentTurn())) {
                        System.out.println(" -- Check!");
                    }
                    System.out.println();
                    continue;
                }

                if(input.equals("exitview")){
                    game.getBoard().returnToLatestMove();
                    game.printBoard();
                    System.out.printf("Now it is %s's turn", game.getBoard().getCurrentTurn());
                    if (game.getBoard().isGeneralInCheck(game.getBoard().getCurrentTurn())) {
                        System.out.println(" -- Check!");
                    }
                    System.out.println();
                    continue;
                }
                if(input.equals("import")){
                    System.out.println("Please enter the file path to import from:");
                    String filepath=sc.nextLine();
                    game.getBoard().loadBoardFromFile("testa",filepath);
                    game.printBoard();
                    System.out.printf("Now it is %s's turn",game.getBoard().getCurrentTurn());
                    if(game.getBoard().isGeneralInCheck(game.getBoard().getCurrentTurn())){
                        System.out.println(" -- Check!");
                    }
                    System.out.println();
                    continue;
                }


                if(input.equals("currentsidealllegalmoves")){
                    Side currentSide=game.getBoard().getCurrentTurn();
                    System.out.printf("All legal moves for %s:%n",currentSide);
                    for(int r=0;r<10;r++){
                        for(int c=0;c<9;c++){
                            Position pos=new Position(r,c);
                            Piece piece=game.getBoard().getPieceAt(pos);
                            if(piece!=null && piece.side.equals(currentSide)){
                                List<Position> legalMoves= piece.getLegalMoves(game.getBoard(), pos);
                                for(Position movePos: legalMoves){
                                    System.out.printf("Piece at (%d,%d) can move to (%d,%d)%n",r,c,movePos.row,movePos.col);
                                }
                            }
                        }
                    }
                    System.out.println();
                    continue;
                }


                if(input.equals("hint")){
                    System.out.printf("current best move is ");
                    MoveRecord bestmov= bot.findBestMove(game.getBoard(),game.getBoard().getCurrentTurn());
                    System.out.println(bestmov.fromPosition+"to"+bestmov.toPosition);
                }

                System.out.println("Invalid input. Please enter row and column separated by a space.");
                continue;
            }
            int row=Integer.parseInt(parts[0]);
            int col=Integer.parseInt(parts[1]);
            Position position=new Position(row,col);
            game.touchPosition(position);
            game.printBoard();
            if(game.getBoard().getSelectedPosition()==null){
                System.out.println("No piece selected yet.");
            }
            else{
                System.out.printf("Piece at (%d,%d) is selected.%n",game.getBoard().getSelectedPosition().row,game.getBoard().getSelectedPosition().col);
                System.out.println();
                System.out.printf("Legal moves for the selected piece:%n");
                Piece selectedPiece=game.getBoard().getPieceAt(game.getBoard().getSelectedPosition());
                List<Position> legalMoves= selectedPiece.getLegalMoves(game.getBoard(), game.getBoard().getSelectedPosition());
                for(Position pos: legalMoves) {
                    System.out.printf("(%d,%d)%n", pos.row, pos.col);
                }
            }
            System.out.printf("Now it is %s's turn",game.getBoard().getCurrentTurn());
            if(game.getBoard().isGeneralInCheck(game.getBoard().getCurrentTurn())){
                System.out.println(" -- Check!");
            }
            System.out.println();
        }


    }
}