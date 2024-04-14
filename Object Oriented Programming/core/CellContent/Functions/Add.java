package xxl.CellContent.Functions;

import xxl.Coords;
import xxl.Spreadsheet;
import xxl.CellContent.Content;

public class Add extends BinaryFuncs{

    private Spreadsheet _spreaadsheet;
    private String _func;
    private Integer _i1;
    private Integer _i2;

    public Add(String func, Spreadsheet spreadsheet){
        _func = func;
        _spreaadsheet = spreadsheet;
    }

    public Integer value(){
        String[] fields = _func.split(",");
        String[] s1 = fields[0].split(";"), s2 = fields[1].split(";");
        
        if(s1.length == 1){ //1st item is a literal number
            _i1 = Integer.parseInt(s1[0]);
        }
        else{ //1st item is a refrence
            Coords coords = _spreaadsheet.getCoordsFromString(fields[0]);
            if(_spreaadsheet.checkCoords(coords) && _spreaadsheet.cellHasContent(coords) && _spreaadsheet.getCellContent(coords) != null){
                _i1 = _spreaadsheet.getCellValue(coords);
            }
            else{_i1 = null;}
        }
        
        if(s2.length == 1){
            _i2 = Integer.parseInt(s2[0]);
        }
        else{ 
            Coords coords = _spreaadsheet.getCoordsFromString(fields[1]);
            if(_spreaadsheet.checkCoords(coords) && _spreaadsheet.cellHasContent(coords) && _spreaadsheet.getCellContent(coords) != null){
                _i2 = _spreaadsheet.getCellValue(coords);
            }
            else{_i2 = null;}
        }

        if(_i1 == null || _i2 == null){
            return null;
        }        
        return _i1 + _i2;
    }

    @Override
    public String toString(){
        if(value() == null){
            return "#VALUE=ADD(" + _func + ")";
        }
        return value() + "=ADD(" + _func + ")";
    } 

    public String asString(){
        return toString();
    }
}
