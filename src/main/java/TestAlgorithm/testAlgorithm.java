package TestAlgorithm;
//this file is now for test only
import Game.Game;
import pieces.Piece;
import data.Position;
import java.util.List;
import java.util.Scanner;

public class testAlgorithm {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello and welcome!");

        Game game = new Game();
        game.printBoard();

        System.out.printf("Now it is %s's turn",game.getBoard().getCurrentTurn());
        if(game.getBoard().isGeneralInCheck(game.getBoard().getCurrentTurn())){
            System.out.println(" -- Check!");
        }


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