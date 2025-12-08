package pieces;

import Core.Board;
import data.Side;
import data.PieceType;
import data.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdvisorPiece extends Piece{
    public AdvisorPiece(Side side){
        super(side, PieceType.ADVISOR);
    }

    public static final Position[] RED_ADVISOR_EDGES = {
            new Position(9,3),
            new Position(9,5),
            new Position(7,3),
            new Position(7,5)
    };
    public static final Position[] BLACK_ADVISOR_EDGES = {
            new Position(0,3),
            new Position(0,5),
            new Position(2,3),
            new Position(2,5)
    };
    public static final Position RED_ADVISOR_CENTER = new Position(8,4);
    public static final Position BLACK_ADVISOR_CENTER = new Position(1,4);

    public List<Position> getUnfilteredLegalMoves(Board board, Position currentPosition) {
        List<Position> legalMoves = new ArrayList<>();
        if(this.side== Side.RED){
            if(currentPosition.equals(RED_ADVISOR_CENTER)){
                for(Position pos: RED_ADVISOR_EDGES){
                    if(board.getPieceAt(pos)==null || board.getPieceAt(pos).side!=this.side){
                        legalMoves.add(pos);
                    }
                }
            }
            else{
                if(board.getPieceAt(RED_ADVISOR_CENTER)==null || board.getPieceAt(RED_ADVISOR_CENTER).side!=this.side){
                    legalMoves.add(RED_ADVISOR_CENTER);
                }
            }
        }
        else{
            if(currentPosition.equals(BLACK_ADVISOR_CENTER)){
                for(Position pos: BLACK_ADVISOR_EDGES){
                    if(board.getPieceAt(pos)==null || board.getPieceAt(pos).side!=this.side){
                        legalMoves.add(pos);
                    }
                }
            }
            else{
                if(board.getPieceAt(BLACK_ADVISOR_CENTER)==null || board.getPieceAt(BLACK_ADVISOR_CENTER).side!=this.side){
                    legalMoves.add(BLACK_ADVISOR_CENTER);
                }
            }
        }
        return legalMoves;
    }

}
