package Core;

import GameDialogues.GameDialogue;
import GameSave.AutoSaver;
import GameSave.MoveRecord;
import GameSave.ChineseChessDataSaver;

import chinese_chess.GraphicController;
import data.GameStatus;
import data.InitialPositions;
import data.Side;
import data.Position;
import javafx.geometry.Pos;
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
    AutoSaver autoDataSaver=new AutoSaver();

    public int currentViewingStep=0;

    public final String username;


    public Board(String username){
        initializeBoard();
        this.username=username;
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

    public void initializeBoard(){
        //make sure curren turn is red
        this.currentTurn=Side.RED;

        //make sure no position selected
        selectedPosition = null;

        //make sure all the positions are empty
        for(int r=0; r<ROWS; r++){
            for(int c=0; c<COLS; c++){
                grid[r][c]=null;
            }
        }

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
    private boolean haveAnnouncedGameResult;
    public boolean haveIAnnouncedGameResult() {
        return haveAnnouncedGameResult;
    }
    public void AnnouncedGameResult(boolean haveAnnouncedGameResult) {
        this.haveAnnouncedGameResult = haveAnnouncedGameResult;
    }
    // save and load functions
    public void loadBoardFromFile(String username,String path) throws Exception {
        List<MoveRecord> tempMoveHistory;
        try {
            tempMoveHistory = dataSaver.loadGameData(username, path);
        }catch (Exception e){
            throw e;
        }
        if(tempMoveHistory==null){
            System.out.println("load failed");
            return;
        }

        moveHistory=dataSaver.loadGameData(username,path);


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
                movePiece(record.fromPosition, record.toPosition,false,true);
                switchTurn();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        currentViewingStep=moveHistory.size();
        return;
    }

    public void saveBoard(String username,String filepath) throws Exception{
        dataSaver.saveGameData(username,moveHistory,filepath);
    }

    public void autoSaveBoard(String username) throws Exception{
        autoDataSaver.autoSaveGameData(username,moveHistory);
    }

    public Piece getPieceAt(Position position) {
        int r=position.getRow();
        int c=position.getCol();
        if(r<0||r>=ROWS||c<0||c>=COLS){
            return null;
        }
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

    public List<Position> getAllLegalMoves(Side side) throws Exception {
        List<Position> allLegalMoves=new java.util.ArrayList<>();
        for(int r=0; r<ROWS; r++){
            for(int c=0; c<COLS; c++){
                Piece piece=grid[r][c];
                if(piece!=null && piece.side==side){
                    Position piecePosition=new Position(r,c);
                    List<Position> pieceLegalMoves=piece.getLegalMoves(this,piecePosition);
                    if(pieceLegalMoves!=null){
                        allLegalMoves.addAll(pieceLegalMoves);
                    }
                }
            }
        }
        if(allLegalMoves.size()==0){
            return null;
        }
        return allLegalMoves;
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

    // move piece and regret move

    public void movePiece(Position fromPosition, Position toPosition, boolean formal, boolean isGuest) throws Exception {
        int fromRow=fromPosition.getRow();
        int fromCol=fromPosition.getCol();
        if (grid[fromRow][fromCol]==null){
            System.out.println("No piece at the source position!");
            return;
        }
        Piece piece = getPieceAt(fromPosition);
        setPieceAt(toPosition, piece);
        setPieceAt(fromPosition, null);

        if(formal){
            MoveRecord record=new MoveRecord(fromPosition, toPosition);
            this.moveHistory.add(record);
            //this.saveBoard("chinese_chess_save.dat");

            if(false==isGuest)this.autoSaveBoard(username);
            currentViewingStep=moveHistory.size();
        }

    }

    public void forceMovePiece(Position fromPosition, Position toPosition){
        Piece piece = getPieceAt(fromPosition);
        setPieceAt(toPosition, piece);
        setPieceAt(fromPosition, null);
        MoveRecord record=new MoveRecord(fromPosition, toPosition);
        this.moveHistory.add(record);
    }

    public void regretLastMove(boolean isGuest) throws Exception {
        if(moveHistory.size()==0){
            System.out.println("No moves to regret!");
            return;
        }
        //MoveRecord lastMove=moveHistory.get(moveHistory.size()-1);

        moveHistory.remove(moveHistory.size()-1);
        //Move the piece back
        //movePiece(lastMove.toPosition, lastMove.fromPosition,false);
        //Switch turn back
        //switchTurn();
        //Save the board

        initializeBoard();
        //Replay the moves
        try{
            for(MoveRecord record: moveHistory){
                movePiece(record.fromPosition, record.toPosition,false,isGuest);
                switchTurn();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        currentViewingStep=moveHistory.size();

        //this.saveBoard("chinese_chess_save.dat");
        if(false==isGuest){
            this.autoSaveBoard(username);
        }
    }

    // view function

    public void viewPreviousMove(){
        if(currentViewingStep==0){
            System.out.println("Already at the beginning of the game!");
            return;
        }
        currentViewingStep--;

        initializeBoard();
        //Replay the moves
        try{
            for(int i=0;i<currentViewingStep;i++){
                MoveRecord record=moveHistory.get(i);
                movePiece(record.fromPosition, record.toPosition,false, true);
                switchTurn();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }


    }

    public void viewNextMove(){
        if(currentViewingStep==moveHistory.size()){
            System.out.println("Already at the latest move!");
            return;
        }
        currentViewingStep++;

        initializeBoard();
        //Replay the moves
        try{
            for(int i=0;i<currentViewingStep;i++){
                MoveRecord record=moveHistory.get(i);
                movePiece(record.fromPosition, record.toPosition,false, true);
                switchTurn();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    public void returnToLatestMove(){
        if(currentViewingStep==moveHistory.size()){
            System.out.println("Already at the latest move!");
            return;
        }
        currentViewingStep=moveHistory.size();

        initializeBoard();
        //Replay the moves
        try{
            for(int i=0;i<currentViewingStep;i++){
                MoveRecord record=moveHistory.get(i);
                movePiece(record.fromPosition, record.toPosition,false, true);
                switchTurn();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    public void returnViewToInitial(){
        if(currentViewingStep==0){
            System.out.println("Already initial");
            return;
        }

        currentViewingStep=0;

        initializeBoard();
    }

    public int getCurrentViewingStep(){
        return currentViewingStep;
    }

    public boolean isViewing(){
        return currentViewingStep!=moveHistory.size();
    }

    public Position previousStepFrom(){
        if(moveHistory==null||moveHistory.isEmpty()||currentViewingStep==0)return null;
        else return moveHistory.get(currentViewingStep-1).fromPosition;
    }
    public Position previousStepTo(){
        if(moveHistory==null||moveHistory.isEmpty()||currentViewingStep==0)return null;
        else return (moveHistory.get(currentViewingStep-1).toPosition);
    }

    //win lose judgement

    public int judgeGameOver() throws Exception {
        if(getAllLegalMoves(currentTurn)==null){
            if(isGeneralInCheck(currentTurn)){
                //Current player is checkmated
                return currentTurn== Side.RED? 2:1; //1 for Red wins, 2 for Black wins
            }
            else{
                //Stalemate
                return currentTurn== Side.RED? 4:3;
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
