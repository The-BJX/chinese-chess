package TestAlgorithm;
//this file is now for test only
import Game.Game;
import Core.Board;
import pieces.Piece;
import data.Position;
import java.util.List;

public class testAlgorithm {
    public static void main(String[] args) {
        System.out.println("Hello and welcome!");

        Game game = new Game();
        game.printBoard();

        Piece tempPiece=game.getBoard().getPieceAt(new Position(0,4));
        Board tempBoard=game.getBoard();
        List<Position> legalMoves= tempPiece.getLegalMoves(tempBoard, new Position(0,4));
        System.out.println("Legal moves for the piece at (0,4):");
        for(Position pos: legalMoves) {
            System.out.println("(" + pos.row + "," + pos.col + ")");
        }



    }
}