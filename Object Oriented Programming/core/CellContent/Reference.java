package xxl.CellContent;

import xxl.Spreadsheet;
import xxl.Coords;

public class Reference extends Content {
    private String _ref;
    private Spreadsheet _spreadsheet;

    public Reference(Spreadsheet spreadsheet, String ref){
        _spreadsheet = spreadsheet;
        _ref = ref;
    }

    public String toString(){
        Coords coords = _spreadsheet.getCoordsFromString(_ref);
        if(_spreadsheet.checkCoords(coords) && _spreadsheet.getCellContent(coords) != null){
            return asString() + "=" + _ref;
        }
        else return "#VALUE=" + _ref;
    }

    public String asString(){
        Coords coords = _spreadsheet.getCoordsFromString(_ref);
        if(_spreadsheet.checkCoords(coords) && _spreadsheet.getCellContent(coords) != null){
            return _spreadsheet.getCellContent(coords).asString();
        }
        else return "#VALUE";
    }

    public Integer value(){
        Coords coords = _spreadsheet.getCoordsFromString(_ref);
        if(_spreadsheet.checkCoords(coords) && _spreadsheet.getCellContent(coords) != null){
            return _spreadsheet.getCellContent(coords).value();
        }
        else return null;
    }

    public String stringValue(){
        Coords coords = _spreadsheet.getCoordsFromString(_ref);
        if(_spreadsheet.checkCoords(coords) && _spreadsheet.getCellContent(coords) != null){
            return _spreadsheet.getCellContent(coords).stringValue();
        }
        else return null;
    }
}
