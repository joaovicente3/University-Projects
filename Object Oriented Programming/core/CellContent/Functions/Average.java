package xxl.CellContent.Functions;

import xxl.Coords;
import xxl.Spreadsheet;

public class Average extends IntervalFuncs{
    private String _intervalString;
    private Spreadsheet _spreadsheet;

    public Average(Spreadsheet spreadsheet, String intervalString){
        _spreadsheet = spreadsheet;
        _intervalString = intervalString;
    }

    public String toString(){
        if(value() == null){
            return "#VALUE=AVERAGE(" + _intervalString + ")";
        }
        else{
            return value() + "=AVERAGE(" + _intervalString + ")";
        }
    }

    public String asString(){
        return toString();
    }

    public Integer value(){
        String[] indexes = _intervalString.split(":");
        Coords c1 = _spreadsheet.getCoordsFromString(indexes[0]);
        Coords c2 = _spreadsheet.getCoordsFromString(indexes[1]);
        Integer value = 0, count = 0;
        if(c1.getRow() == c2.getRow()){
            if(c1.getCol() < c2.getCol()){
                for(int i = c1.getCol(); i <= c2.getCol(); i++){
                    Coords coords = new Coords(c1.getRow(), i);
                    if(_spreadsheet.checkCoords(coords) && _spreadsheet.cellHasContent(coords) && _spreadsheet.getCellContent(coords).value() != null){
                        value += _spreadsheet.getCellValue(coords);
                        count++;
                    }
                    else{return null;}
                }
                return value / count;
            }
            else{
                for(int i = c2.getCol(); i <= c1.getCol(); i++){
                    Coords coords = new Coords(c1.getRow(), i);
                    if(_spreadsheet.checkCoords(coords) && _spreadsheet.cellHasContent(coords) && _spreadsheet.getCellContent(coords).value() != null){
                        value += _spreadsheet.getCellValue(coords);
                        count++;
                    }
                    else{return null;}  
                }
                return value / count;
            }
        }
        else if(c1.getCol() == c2.getCol()){
            if(c1.getRow() < c2.getRow()){
                for(int i = c1.getRow(); i <= c2.getRow(); i++){
                    Coords coords = new Coords(i, c1.getCol());
                    if(_spreadsheet.checkCoords(coords) && _spreadsheet.cellHasContent(coords) && _spreadsheet.getCellContent(coords).value() != null){
                        value += _spreadsheet.getCellValue(coords);
                        count++;
                    }
                    else{return null;}             
                }
                return value / count;
            }
            else{
                for(int i = c2.getRow(); i <= c1.getRow();i++){
                    Coords coords = new Coords(i, c1.getCol());
                    if(_spreadsheet.checkCoords(coords) && _spreadsheet.cellHasContent(coords) && _spreadsheet.getCellContent(coords).value() != null){
                        value += _spreadsheet.getCellValue(coords);
                        count++;
                    }
                    else{return null;}
                }
                return value / count;
            }
        }
        else{
            return null;
        }  
    }

    public String stringValue(){
        return null;
    }

}
