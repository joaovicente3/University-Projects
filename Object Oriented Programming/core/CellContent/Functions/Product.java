package xxl.CellContent.Functions;

import xxl.Coords;
import xxl.Spreadsheet;

public class Product extends IntervalFuncs{
    
    private String _intervalString;
    private Spreadsheet _spreadsheet;
    
    public Product(Spreadsheet spreadsheet, String intervalString){
        _spreadsheet = spreadsheet;
        _intervalString = intervalString;
    }

    public String toString(){
        if(value() == null){
            return "#VALUE=PRODUCT(" + _intervalString + ")";
        }
        else{
            return value() + "=PRODUCT(" + _intervalString + ")";
        }
    }

    public String asString(){
        return toString();
    }

    public Integer value(){
        String[] indexes = _intervalString.split(":");
        Coords c1 = _spreadsheet.getCoordsFromString(indexes[0]);
        Coords c2 = _spreadsheet.getCoordsFromString(indexes[1]);
        Integer value = 1;
        if(c1.getRow() == c2.getRow()){
            if(c1.getCol() < c2.getCol()){
                for(int i = c1.getCol(); i <= c2.getCol(); i++){
                    Coords coords = new Coords(c1.getRow(), i);
                    if(_spreadsheet.checkCoords(coords) && _spreadsheet.getCellValue(coords) != null){
                        value *= _spreadsheet.getCellValue(coords);
                    }
                    else{return null;}
                }
                return value;
            }
            else{
                for(int i = c2.getCol(); i <= c1.getCol(); i++){
                    Coords coords = new Coords(c1.getRow(), i);
                    if(_spreadsheet.checkCoords(coords) && _spreadsheet.getCellValue(coords) != null){
                        value *= _spreadsheet.getCellValue(coords);
                    }
                    else{return null;}  
                }
                return value;
            }
        }
        else{
            if(c1.getRow() < c2.getRow()){
                for(int i = c1.getRow(); i <= c2.getRow(); i++){
                    Coords coords = new Coords(i, c1.getCol());
                    if(_spreadsheet.checkCoords(coords) && _spreadsheet.getCellValue(coords) != null){
                        value *= _spreadsheet.getCellValue(coords);
                    }
                    else{return null;}             
                }
                return value;
            }
            else{
                for(int i = c2.getRow(); i <= c1.getRow();i++){
                    Coords coords = new Coords(i, c1.getCol());
                    if(_spreadsheet.checkCoords(coords) && _spreadsheet.getCellValue(coords) != null){
                        value *= _spreadsheet.getCellValue(coords);
                    }
                    else{return null;}
                }
                return value;
            }
        }       
    }

    public String stringValue(){
        return null;
    }
    
}
