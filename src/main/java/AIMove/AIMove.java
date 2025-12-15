package AIMove;

import Core.Board;
import GameSave.MoveRecord;
import data.Position;
import data.Side;
import data.PieceType;
import pieces.Piece;

import java.util.List;

/**
 * Simple AI that searches to a fixed depth using negamax with alpha-beta.
 * This implementation copies the board for each simulated move (non-destructive).
 */

// this algorithm partly come from internet
public class AIMove {
    private final int maxDepth;
    private static final int MATE_SCORE = 1000000;
    private MoveRecord curSuggestedMove;
    public MoveRecord getCurSuggestedMove() {
        return curSuggestedMove;
    }
    public void setCurSuggestedMove(MoveRecord curSuggestedMove) {
        this.curSuggestedMove = curSuggestedMove;
    }
    public AIMove(int maxDepth){
        this.maxDepth = Math.max(1, maxDepth);
    }



    public MoveRecord findBestMove(Board board, Side side) throws Exception {
        MoveRecord bestMove = null;
        int bestScore = Integer.MIN_VALUE;

        // Iterate all pieces of the side and their legal moves
        for (int r = 0; r < Board.ROWS; r++){
            for (int c = 0; c < Board.COLS; c++){
                Position from = new Position(r, c);
                Piece p = board.getPieceAt(from);
                if (p == null || p.side != side) continue;

                List<Position> moves = p.getLegalMoves(board, from);
                if (moves == null || moves.isEmpty()) continue;

                for (Position to : moves){
                    // simulate on a copied board
                    Board sim = copyBoard(board);
                    sim.movePiece(from, to, false, true);
                    sim.switchTurn();

                    int score = -negamax(sim, maxDepth - 1, Integer.MIN_VALUE/2, Integer.MAX_VALUE/2, opposite(side));

                    if (score > bestScore){
                        bestScore = score;
                        bestMove = new MoveRecord(from, to);
                    }
                }
            }
        }

        return bestMove;
    }

    private int negamax(Board board, int depth, int alpha, int beta, Side side) throws Exception{
        List<Position> moves = board.getAllLegalMoves(side);

        if ((moves == null || moves.isEmpty()) || depth == 0){
            if (moves == null || moves.isEmpty()){
                // no legal moves: mate or stalemate
                if (board.isGeneralInCheck(side)){
                    return -MATE_SCORE - depth; // losing side
                }else{
                    return 0; // stalemate
                }
            }
            return evaluate(board, side);
        }

        int max = Integer.MIN_VALUE;

        // We need actual from->to moves; board.getAllLegalMoves only returns target squares, so iterate over pieces
        for (int r = 0; r < Board.ROWS; r++){
            for (int c = 0; c < Board.COLS; c++){
                Position from = new Position(r, c);
                Piece p = board.getPieceAt(from);
                if (p == null || p.side != side) continue;
                List<Position> legal = p.getLegalMoves(board, from);
                if (legal == null) continue;
                for (Position to : legal){
                    Board sim = copyBoard(board);
                    sim.movePiece(from, to, false,true);
                    sim.switchTurn();
                    int score = -negamax(sim, depth - 1, -beta, -alpha, opposite(side));
                    if (score > max) max = score;
                    if (score > alpha) alpha = score;
                    if (alpha >= beta) return alpha; // cutoff
                }
            }
        }

        return max;
    }

    private Board copyBoard(Board board){
        Board sim = new Board("sim");
        for (int r = 0; r < Board.ROWS; r++){
            for (int c = 0; c < Board.COLS; c++){
                Position p = new Position(r, c);
                sim.setPieceAt(p, board.getPieceAt(p));
            }
        }
        // Keep the same current turn
        while (sim.getCurrentTurn() != board.getCurrentTurn()) sim.switchTurn();
        return sim;
    }

    private Side opposite(Side s){
        return s == Side.RED ? Side.BLACK : Side.RED;
    }

    private int evaluate(Board board, Side side){
        int score = 0;
        for (int r = 0; r < Board.ROWS; r++){
            for (int c = 0; c < Board.COLS; c++){
                Position pos = new Position(r, c);
                Piece p = board.getPieceAt(pos);
                if (p == null) continue;
                int val = pieceValue(p.pieceType);
                score += (p.side == side) ? val : -val;
            }
        }
        return score;
    }

    private int pieceValue(PieceType t){
        switch (t){
            case GENERAL: return 100000;
            case CHARIOT: return 900;
            case CANNON: return 450;
            case HORSE: return 400;
            case ELEPHANT: return 250;
            case ADVISOR: return 250;
            case SOLDIER: return 200;
            default: return 0;
        }
    }
}

