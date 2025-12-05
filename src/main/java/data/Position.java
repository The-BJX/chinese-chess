package data;

import java.io.Serializable;

public class Position implements Serializable {
    public int row;
    public int col;

    public Position(int row, int col){
        this.row=row;
        this.col=col;
    }

    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }

    public boolean equals(Position other){
        return this.row==other.row && this.col==other.col;
    }

    public boolean inBoardBounds(){
        return row>=0 && row<10 && col>=0 && col<9;
    }

    @Override
    public String toString(){
        if(this==null)return new String("Nothing");
        return String.format("(%d,%d)",this.row,this.col);
    }
}
