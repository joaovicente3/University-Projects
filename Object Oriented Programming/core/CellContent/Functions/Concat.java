package xxl.CellContent.Functions;

import xxl.Coords;
import xxl.Spreadsheet;

public class Concat extends IntervalFuncs{
    private String _intervalString;
    private Spreadsheet _spreadsheet;

    public Concat(Spreadsheet spreadsheet, String intervalString){
        _spreadsheet = spreadsheet;
        _intervalString = intervalString;
    }

    public String toString(){
        if(createConcat() == null){
            return "#VALUE=CONCAT(" + _intervalString + ")";
        }
        else{
            return createConcat() + "=CONCAT(" + _intervalString + ")";
        }
    }

    public String asString(){
        return toString();
    }

    public Integer value(){
        return null;
    }

    public String createConcat(){
        String[] indexes = _intervalString.split(":");
        Coords c1 = _spreadsheet.getCoordsFromString(indexes[0]);
        Coords c2 = _spreadsheet.getCoordsFromString(indexes[1]);
        String concat = "'";
        if(c1.compareTo(c2) == 0){
            if(_spreadsheet.checkCoords(c1) && _spreadsheet.cellHasContent(c1) && //
                _spreadsheet.getCellValue(c1) == null && _spreadsheet.getCellString(c1) != ""){
                concat += _spreadsheet.getCellString(c1).split("'")[1];
                return concat;
            }
        }
        else if(c1.getRow() == c2.getRow()){
            if(c1.getCol() < c2.getCol()){
                for(int i = c1.getCol(); i <= c2.getCol(); i++){
                    Coords coords = new Coords(c1.getRow(), i);
                    if(_spreadsheet.checkCoords(coords) && _spreadsheet.cellHasContent(coords) && //
                        _spreadsheet.getCellValue(coords) == null && _spreadsheet.getCellString(coords) != ""){
                        concat += _spreadsheet.getCellString(coords).split("'")[1];
                    }
                }
                return concat;
            }
            else{
                for(int i = c2.getCol(); i <= c1.getCol(); i++){
                    Coords coords = new Coords(c1.getRow(), i);
                    if(_spreadsheet.checkCoords(coords) && _spreadsheet.cellHasContent(coords) && //
                        _spreadsheet.getCellValue(coords) == null && _spreadsheet.getCellString(coords) != ""){
                        concat += _spreadsheet.getCellString(coords).split("'")[1];
                    }  
                }
                return concat;
            }
        }
        else if(c1.getCol() == c2.getCol()){
            if(c1.getRow() < c2.getRow()){
                for(int i = c1.getRow(); i <= c2.getRow(); i++){
                    Coords coords = new Coords(i, c1.getCol());
                    if(_spreadsheet.checkCoords(coords) && _spreadsheet.cellHasContent(coords) && //
                        _spreadsheet.getCellValue(coords) == null && _spreadsheet.getCellString(coords) != ""){
                        concat += _spreadsheet.getCellString(coords).split("'")[1];
                    }             
                }
                return concat;
            }
            else{
                for(int i = c2.getRow(); i <= c1.getRow();i++){
                    Coords coords = new Coords(i, c1.getCol());
                    if(_spreadsheet.checkCoords(coords) && _spreadsheet.cellHasContent(coords) && //
                        _spreadsheet.getCellValue(coords) == null && _spreadsheet.getCellString(coords) != ""){
                        concat += _spreadsheet.getCellString(coords).split("'")[1];
                    }
                }
                return concat;
            }
        }
        return null;  
    }

    public String stringValue(){
        return createConcat();
    }
}

