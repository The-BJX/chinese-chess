package Core;

import GameSave.MoveRecord;
import GameSave.ChineseChessDataSaver;

import chinese_chess.GraphicController;
import data.InitialPositions;
import data.Side;
import data.Position;
import pieces.GeneralPiece;
import pieces.Piece;

import java.util.List;


public class Board {
    public static final int ROWS =10;
    public static final int COLS = 9;
    private final Piece[][] grid=new Piece[ROWS][COLS];
    private Position hoverPosition;
    public Position getHoverPosition(){
        return hoverPosition;
    }
    public void setHoverPosition(Position pos){
        if(hoverPosition==null||hoverPosition!=pos) GraphicController.HoverChangedFlag=true;
        hoverPosition = pos;
    }
    public void deHover(){
        if(hoverPosition!=null) GraphicController.HoverChangedFlag=true;
        hoverPosition = null;
    }
    private Side currentTurn= Side.RED;
    // Track the currently selected position on this board (if any)
    private Position selectedPosition = null;


    public List<MoveRecord> moveHistory = new java.util.ArrayList<>();
    ChineseChessDataSaver dataSaver=new ChineseChessDataSaver();

    public Board(){
        initializeBoard();
    }

    public Side getCurrentTurn(){
        return currentTurn;
    }
    public void switchTurn(){
        if(currentTurn== Side.RED){
            currentTurn= Side.BLACK;
        }else{
            currentTurn= Side.RED;
        }
    }

    private void initializeBoard(){
        // Place generals at their initial positions
        setPieceAt(InitialPositions.blackGeneral, new GeneralPiece(Side.BLACK));
        setPieceAt(InitialPositions.redGeneral, new GeneralPiece(Side.RED));

        //Place soldiers at their initial positions
        for(Position pos: InitialPositions.blackSoldiers){
            setPieceAt(pos, new pieces.SoldierPiece(Side.BLACK));
        }
        for(Position pos: InitialPositions.redSoldiers){
            setPieceAt(pos, new pieces.SoldierPiece(Side.RED));
        }

        //Place cannons at their initial positions
        for(Position pos: InitialPositions.blackCannon){
            setPieceAt(pos, new pieces.CannonPiece(Side.BLACK));
        }
        for(Position pos: InitialPositions.redCannon){
            setPieceAt(pos, new pieces.CannonPiece(Side.RED));
        }

        //Place advisors at their initial positions
        for(Position pos: InitialPositions.blackAdvisor){
            setPieceAt(pos, new pieces.AdvisorPiece(Side.BLACK));
        }
        for(Position pos: InitialPositions.redAdvisor){
            setPieceAt(pos, new pieces.AdvisorPiece(Side.RED));
        }

        //Place elephants at their initial positions
        for(Position pos: InitialPositions.blackElephants){
            setPieceAt(pos, new pieces.ElephantPiece(Side.BLACK));
        }
        for(Position pos: InitialPositions.redElephants){
            setPieceAt(pos, new pieces.ElephantPiece(Side.RED));
        }

        //Place horses at their initial positions
        for(Position pos: InitialPositions.blackHorses){
            setPieceAt(pos, new pieces.HorsePiece(Side.BLACK));
        }
        for(Position pos: InitialPositions.redHorses){
            setPieceAt(pos, new pieces.HorsePiece(Side.RED));
        }

        //Place chariots at their initial positions
        for(Position pos: InitialPositions.blackChariots){
            setPieceAt(pos, new pieces.ChariotPiece(Side.BLACK));
        }
        for(Position pos: InitialPositions.redChariots){
            setPieceAt(pos, new pieces.ChariotPiece(Side.RED));
        }

    }

    public void loadBoardFromFile(String chinese_chess_save_dat) throws Exception {
        moveHistory=dataSaver.loadGameData(chinese_chess_save_dat);
        //Reinitialize the board
        for(int r=0; r<ROWS; r++){
            for(int c=0; c<COLS; c++){
                grid[r][c]=null;
            }
        }
        initializeBoard();
        //Replay the moves
        try{
            for(MoveRecord record: moveHistory){
                movePiece(record.fromPosition, record.toPosition);
                switchTurn();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void saveBoard(String filepath) throws Exception{
        dataSaver.saveGameData(moveHistory,filepath);
    }

    public Piece getPieceAt(Position position) {
        int r=position.getRow();
        int c=position.getCol();
        return grid[r][c];
    }

    public Position getGeneralPosition(Side side){
        for(int r=0; r<ROWS; r++){
            for(int c=0; c<COLS; c++){
                Piece piece=grid[r][c];
                if(piece!=null && piece.pieceType==data.PieceType.GENERAL && piece.side==side){
                    return new Position(r,c);
                }
            }
        }
        return null;
    }

    public List<Position> getThreatenedPositions(Side side){
        List<Position> threatenedPositions=new java.util.ArrayList<>();
        for(int r=0; r<ROWS; r++){
            for(int c=0; c<COLS; c++){
                Piece piece=grid[r][c];
                if(piece!=null && piece.side!=side){
                    Position piecePosition=new Position(r,c);
                    // Use unfiltered moves here to determine threats so we don't recurse through getLegalMoves
                    List<Position> pieceMoves=piece.getUnfilteredLegalMoves(this,piecePosition);
                    if(pieceMoves!=null){
                        threatenedPositions.addAll(pieceMoves);
                    }
                }
            }
        }

        //remove duplicates in threatenedPositions
        List<Position> uniqueThreatenedPositions=new java.util.ArrayList<>();
        for(Position pos: threatenedPositions){
            boolean isDuplicate=false;
            for(Position uniquePos: uniqueThreatenedPositions){
                if(pos.equals(uniquePos)){
                    isDuplicate=true;
                    break;
                }
            }
            if(!isDuplicate){
                uniqueThreatenedPositions.add(pos);
            }
        }
        return uniqueThreatenedPositions;
    }


    public boolean isGeneralInCheck(Side side){
        Position generalPosition=getGeneralPosition(side);
        List<Position> threatenedPositions=getThreatenedPositions(side);
        for(Position pos: threatenedPositions){
            if(pos.equals(generalPosition)){
                return true;
            }
        }
        return false;
    }

    public void movePiece(Position fromPosition, Position toPosition) throws Exception {
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

    public int judgeGameOver(){
        if(getThreatenedPositions(currentTurn)==null){
            if(isGeneralInCheck(currentTurn)){
                //Current player is checkmated
                return currentTurn== Side.RED? 2:1; //1 for Red wins, 2 for Black wins
            }
            else{
                //Stalemate
                return 0; //0 for draw
            }
        }
        return -1; //-1 for game not over
    }

    public void setPieceAt(Position position, Piece piece) {
        int r=position.getRow();
        int c=position.getCol();
        grid[r][c] = piece;
    }

    public void select(Position position) {
        Piece piece = getPieceAt(position);
        if (piece != null) {
            selectedPosition = position;
            piece.isSelected = true;
        }
    }

    public void deselect() {
        if (selectedPosition != null) {
            Piece piece = getPieceAt(selectedPosition);
            if (piece != null) {
                piece.isSelected = false;
            }
            selectedPosition = null;
        }
    }

    public Position getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(Position position) {
        this.selectedPosition = position;
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
