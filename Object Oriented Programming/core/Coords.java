package xxl;

import java.io.Serializable;

public class Coords implements Comparable<Coords>, Serializable {
    private int _row;
    private int _col;

    public Coords(int row, int col){
        _row = row;
        _col = col;
    }

    public int getCol(){
        return _col;
    }

    public void setCol(int col){
        _col = col;
    }

    public int getRow(){
        return _row;
    }

    public void setRow(int row){
        _row = row;
    }

    @Override
    public String toString(){
        return _row + ";" + _col;
    }

    @Override
    public int compareTo(Coords other) {
        if (this._row == other._row) {
            return Integer.compare(this._col, other._col);
        }
        return Integer.compare(this._row, other._row);
    }
}
