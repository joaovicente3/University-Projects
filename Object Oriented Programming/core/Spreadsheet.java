package xxl;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import xxl.CellContent.Content;
import xxl.CellContent.LiteralInt;
import xxl.CellContent.LiteralString;
import xxl.CellContent.NoContent;
import xxl.CellContent.Reference;
import xxl.CellContent.Functions.*;
import xxl.exceptions.UnrecognizedEntryException;

/**
 * Class representing a spreadsheet.
 */
public class Spreadsheet implements Serializable {

    @Serial
    private static final long serialVersionUID = 202308312359L;

    private int _numRows;
    private int _numCols;

    private boolean _changed = false;

    private StorageStruct _cells;

    private CutBuffer _cutBuffer;

    private User[] _users;

    public Spreadsheet(int numRows, int numCols){
        _cells = new StorageStruct();
        _numRows = numRows;
        _numCols = numCols;
        _cutBuffer = new CutBuffer();
    }

    /**
     * Insert specified content in specified range.
     *
     * @param rangeSpecification
     * @param contentSpecification
     */
    public void insertContents(String rangeSpecification, String contentSpecification) throws UnrecognizedEntryException /* FIXME maybe add exceptions */ {
        if (contentSpecification.isEmpty()) {return;}

        changed();

        Coords coords = getCoordsFromString(rangeSpecification);

        if(contentSpecification.charAt(0) == '-' || Character.isDigit(contentSpecification.charAt(0))){ 
            insertLitInt(coords, contentSpecification);
            return;
        }
        if(contentSpecification.equals("NoContent")){
            insertNoContent(coords);
            return;
        }

        String[] fields;
        switch(contentSpecification.charAt(0)){
            case '=' -> {
                fields = contentSpecification.split("=");
                fields = fields[1].split("\\(");
                switch (fields[0]){
                    case "ADD" -> insertAdd(coords, fields[1]);
                    case "SUB" -> insertSub(coords, fields[1]);
                    case "MUL" -> insertMul(coords, fields[1]);
                    case "DIV" -> insertDiv(coords, fields[1]);
                    case "AVERAGE" -> insertAverage(coords, fields[1]);
                    case "PRODUCT" -> insertProduct(coords, fields[1]);
                    case "CONCAT" -> insertConcat(coords, fields[1]);
                    case "COALESCE" -> insertCoalesce(coords, fields[1]);
                    default -> insertRef(coords, fields[0]);
                }
            }
            case '\'' -> insertLitString(coords, contentSpecification);
            
            default -> throw new UnrecognizedEntryException(contentSpecification);
        }
    }

    public Coords getCoordsFromString(String s){
        String[] parts = s.split(";");
        int row = Integer.parseInt(parts[0]);
        int col = Integer.parseInt(parts[1]);
        return new Coords(row, col);
    }

    public void insertIntoCell(Coords coords, Content content){
        Cell c = new Cell(content);
        _cells.put(coords, c);
    }

    public void insertLitInt(Coords coords, String sContent){
        int value = Integer.parseInt(sContent);
        LiteralInt litInt = new LiteralInt(value);
        insertIntoCell(coords, litInt);
    }

    public void insertLitString(Coords coords, String sContent){
        LiteralString litString = new LiteralString(sContent);
        insertIntoCell(coords, litString);
    }

    public void insertAdd(Coords coords, String sContent){
        String content = sContent.split("\\)")[0];
        Add add = new Add(content, this);
        insertIntoCell(coords, add);
    }

    public void insertSub(Coords coords, String sContent){
        String content = sContent.split("\\)")[0];
        Sub sub = new Sub(content, this);
        insertIntoCell(coords, sub);
    }

    public void insertMul(Coords coords, String sContent){
        String content = sContent.split("\\)")[0];
        Mul mul = new Mul(content, this);
        insertIntoCell(coords, mul);
    }

    public void insertDiv(Coords coords, String sContent){
        String content = sContent.split("\\)")[0];
        Div div = new Div(content, this);
        insertIntoCell(coords, div);
    }

    public void insertRef(Coords coords, String sContent){
        Reference ref = new Reference(this, sContent);
        insertIntoCell(coords, ref);
    }

    public void insertNoContent(Coords coords){
        NoContent noContent = new NoContent();
        insertIntoCell(coords, noContent);
    }

    public void insertAverage(Coords coords, String sContent){
        String content = sContent.split("\\)")[0];
        Average average = new Average(this, content);
        insertIntoCell(coords, average);
    }

    public void insertProduct(Coords coords, String sContent){
        String content = sContent.split("\\)")[0];
        Product product = new Product(this, content);
        insertIntoCell(coords, product);
    }

    public void insertConcat(Coords coords, String sContent){
        String content = sContent.split("\\)")[0];
        Concat concat = new Concat(this, content);
        insertIntoCell(coords, concat);
    }

    public void insertCoalesce(Coords coords, String sContent){
        String content = sContent.split("\\)")[0];
        Coalesce coalesce = new Coalesce(this, content);
        insertIntoCell(coords, coalesce);
    }
 
    public void insertFromRequest(String rangeSpecification, String contentSpecification) throws UnrecognizedEntryException{
        try{
            String[] indexes = rangeSpecification.split(":");
            if(indexes.length == 1){
                insertContents(rangeSpecification, contentSpecification);
            }
            else{
                Coords c1 = getCoordsFromString(indexes[0]);
                Coords c2 = getCoordsFromString(indexes[1]);
                if(c1.getRow() == c2.getRow()){
                    if(c1.getCol() < c2.getCol()){
                        for(int i = c1.getCol(); i <= c2.getCol(); i++){
                            Coords coords = new Coords(c1.getRow(), i);
                            if(checkCoords(coords)){
                                insertContents(coords.toString(), contentSpecification);
                            }
                        }
                    }
                    else{
                        for(int i = c2.getCol(); i <= c1.getCol(); i++){
                            Coords coords = new Coords(c1.getRow(), i);
                            if(checkCoords(coords)){
                                insertContents(coords.toString(), contentSpecification);
                            }   
                        }
                    }
                }
                else{
                    if(c1.getRow() < c2.getRow()){
                        for(int i = c1.getRow(); i <= c2.getRow(); i++){
                            Coords coords = new Coords(i, c1.getCol());
                            if(checkCoords(coords)){
                                insertContents(coords.toString(), contentSpecification);
                            }              
                        }
                    }
                    else{
                        for(int i = c2.getRow(); i <= c1.getRow();i++){
                            Coords coords = new Coords(i, c1.getCol());
                            if(checkCoords(coords)){
                                insertContents(coords.toString(), contentSpecification);
                            }
                        }
                    }
                }
            }
        } catch(UnrecognizedEntryException e){
            throw new UnrecognizedEntryException(contentSpecification);
        }
    }

    public void deleteContent(String rangeSpecification) throws UnrecognizedEntryException{ 
        try{
            insertFromRequest(rangeSpecification, "NoContent");
            changed();
        } catch(UnrecognizedEntryException e){
            throw new UnrecognizedEntryException(rangeSpecification);
        }
    }


    /* ---------- DO SHOW---------- */

    public String visualiseSpreadsheet(String input){
        if(input == null){return null;}

        String _text = ""; //spreadsheet as text that will be shown
        String[] fields = input.split(":");
        if(fields.length == 1){ //single cell
            Coords coords = getCoordsFromString(fields[0]);
            if(checkCoords(coords)){
                return printCellString(coords);
            }
            else{return null;}
        }
        else if(fields.length == 2){
            Coords c1 = getCoordsFromString(fields[0]);
            Coords c2 = getCoordsFromString(fields[1]);
            if(c1.getRow() == c2.getRow()){
                if(c1.getCol() < c2.getCol()){
                    for(int i = c1.getCol(); i <= c2.getCol(); i++){
                        Coords coords = new Coords(c1.getRow(), i);
                        if(checkCoords(coords)){
                            _text +=  printCellString(coords);
                        }
                        else{return null;}  
                    }
                    return _text; 
                }
                else{
                    for(int i = c2.getCol(); i <= c1.getCol(); i++){
                        Coords coords = new Coords(c1.getRow(), i);
                        if(checkCoords(coords)){
                            _text +=  printCellString(coords);
                        }
                        else{return null;}    
                    }
                    return _text; 
                }
            }
            else if(c1.getCol() == c2.getCol()){
                if(c1.getRow() < c2.getRow()){
                    for(int i = c1.getRow(); i <= c2.getRow(); i++){
                        Coords coords = new Coords(i, c1.getCol());
                        if(checkCoords(coords)){
                            _text +=  printCellString(coords);
                        }
                        else{return null;}                
                    }
                    return _text; 
                }
                else{
                    for(int i = c2.getRow(); i <= c1.getRow();i++){
                        Coords coords = new Coords(i, c1.getCol());
                        if(checkCoords(coords)){
                            printCellString(coords);
                        }
                        else{return null;}    
                         
                    }
                    return _text; 
                }
            }
        }
        return null;
    }

    public String printCellString(Coords coords){
        return coords.toString() + "|" + getCellString(coords) + "\n";
    }

    public boolean checkCoords(Coords coords){
        if(coords.getRow() > _numRows || coords.getCol() > _numCols){return false;}
        else{return true;}
    }


    public Content getCellContent(Coords coords){
        if(checkCoords(coords) && cellHasContent(coords))    
            return _cells.get(coords).getContent();
        else{return null;}
    }

    public Integer getCellValue(Coords coords){
        if(checkCoords(coords) && cellHasContent(coords))    
            return _cells.get(coords).getContent().value();
        else{return null;}
    }

    public String getCellStringValue(Coords coords){
        if(checkCoords(coords) && cellHasContent(coords))    
            return _cells.get(coords).getContent().stringValue();
        else{return null;}
    }

    public String getCellString(Coords coords){
        if(checkCoords(coords) && cellHasContent(coords))    
            return _cells.get(coords).getContent().toString();
        else{return "";}
    }
    
    public boolean cellHasContent(Coords coords){
        return _cells.get(coords) != null;
    }

    
    /* ---------- DO SEARCH ---------- */

    public String showValues(String value){
        String txt = "";
        if(value.charAt(0) == '\''){
            for(int row = 1; row <= _numRows; row++){
                for(int col = 1; col <= _numCols; col++){
                    Coords coords = new Coords(row, col);
                    if(cellHasContent(coords) && getCellStringValue(coords) != null && getCellStringValue(coords).equals(value)){
                        txt += printCellString(coords);
                    }
                }
            } 
            return txt;
        }
        else{
            Integer integerValue = Integer.parseInt(value);
            for(int row = 1; row <= _numRows; row++){
                for(int col = 1; col <= _numCols; col++){
                    Coords coords = new Coords(row, col);
                    if(cellHasContent(coords) && getCellValue(coords) != null && getCellValue(coords) == integerValue){
                        txt += printCellString(coords);
                    }
                }
            }
            return txt; 
        }
    }

    public String showFunctions(String func){
        String txt = "";
        List<List<String>> list = new ArrayList<>();
        for(int row = 1; row <= _numRows; row++){
            for(int col = 1; col <= _numCols; col++){
                Coords coords = new Coords(row, col);
                if(checkCoords(coords) && cellHasContent(coords)){
                    String[] fields = getCellString(coords).split("=");
                    if(fields.length == 2){
                        String name = fields[1].split("\\(")[0];
                        if(name.contains(func)){
                            List<String> innerList = new ArrayList<>();
                            String[] parts = printCellString(coords).split("(?<==)");
                            innerList.add(parts[0]);
                            innerList.add(parts[1]);
                            list.add(innerList);
                        }
                    }
                }
            }
        }
        Collections.sort(list, (list1, list2) -> list1.get(1).compareTo(list2.get(1)));
        for (int i = 0; i < list.size(); i++) {
            List<String> innerList = list.get(i);
            txt += innerList.get(0) + innerList.get(1);
        }
        return txt;
    }

    /* ---------- CUT BUFFER ---------- */

    public void copy(String rangeSpecification) throws UnrecognizedEntryException{
        String[] indexes = rangeSpecification.split(":");
        List<Cell> cells = new ArrayList<>();
        if(indexes.length == 1){
            Coords c = getCoordsFromString(rangeSpecification);
            if(checkCoords(c)){
                cells.add(0,_cells.get(c));
            }
            else{
                throw new UnrecognizedEntryException(rangeSpecification);
            }
            _cutBuffer.copy(cells, "singleCell");
            return;
        }
        else{
            Coords c1 = getCoordsFromString(indexes[0]);
            Coords c2 = getCoordsFromString(indexes[1]);
            int index = 0;
            if(c1.getRow() == c2.getRow()){
                if(c1.getCol() < c2.getCol()){
                    for(int i = c1.getCol(); i <= c2.getCol(); i++){
                        Coords coords = new Coords(c1.getRow(), i);
                        if(checkCoords(coords)){
                            cells.add(index++, _cells.get(coords));
                        }
                        else{
                            throw new UnrecognizedEntryException(rangeSpecification);
                        }
                    }
                }
                else{
                    for(int i = c2.getCol(); i <= c1.getCol(); i++){
                        Coords coords = new Coords(c1.getRow(), i);
                        if(checkCoords(coords)){
                            cells.add(index++, _cells.get(coords));
                        }
                        else{
                            throw new UnrecognizedEntryException(rangeSpecification);
                        } 
                    }
                }
                _cutBuffer.copy(cells, "horizontal");
                return;
            }
            else if(c1.getCol() == c2.getCol()){
                if(c1.getRow() < c2.getRow()){
                    for(int i = c1.getRow(); i <= c2.getRow(); i++){
                        Coords coords = new Coords(i, c1.getCol());
                        if(checkCoords(coords)){
                            cells.add(index++, _cells.get(coords));
                        }
                        else{
                            throw new UnrecognizedEntryException(rangeSpecification);
                        }           
                    }
                }
                else{
                    for(int i = c2.getRow(); i <= c1.getRow();i++){
                        Coords coords = new Coords(i, c1.getCol());
                        if(checkCoords(coords)){
                            cells.add(index++, _cells.get(coords));
                        }
                        else{
                            throw new UnrecognizedEntryException(rangeSpecification);
                        }
                    }
                }
                _cutBuffer.copy(cells, "vertical");
                return;
            }
            else{
                throw new UnrecognizedEntryException(rangeSpecification);
            }
        }
    }

    public void paste(String rangeSpecification) throws UnrecognizedEntryException{
        changed();
        String status = _cutBuffer.getStatus();
        StorageStruct cutBufferCells = _cutBuffer.getCells();
        String[] indexes = rangeSpecification.split(":");
        if(indexes.length == 1){
            Coords c = getCoordsFromString(rangeSpecification);
            if(status.equals("singleCell")){
                _cells.put(c, cutBufferCells.get(new Coords(1, 1)));
            }
            else if(status.equals("horizontal")){
                for(int i = 1; i <= cutBufferCells.size(); i++){
                    Coords getAt = new Coords(1, i);
                    Coords addAt = new Coords(c.getRow(), c.getCol() + i - 1);
                    if(checkCoords(addAt)){
                        _cells.put(addAt, cutBufferCells.get(getAt));
                    }
                }
            }
            else if(status.equals("vertical")){
                for(int i = 1; i <= cutBufferCells.size(); i++){
                    Coords getAt = new Coords(i, 1);
                    Coords addAt = new Coords(c.getRow() + i - 1, c.getCol());
                    if(checkCoords(addAt)){
                        _cells.put(addAt, cutBufferCells.get(getAt));
                    }
                }
            }
        }
        else{
            Coords c1 = getCoordsFromString(indexes[0]);
            Coords c2 = getCoordsFromString(indexes[1]);
            if(c1.getRow() == c2.getRow() && status.equals("horizontal")){
                if(c1.getCol() < c2.getCol() && c2.getCol() - c1.getCol() + 1 == cutBufferCells.size()){
                    for(int i = c1.getCol(); i <= c2.getCol(); i++){
                        Coords coords = new Coords(c1.getRow(), i);
                        if(checkCoords(coords)){
                            Coords get = new Coords(1, i-c1.getCol()+1);
                            _cells.put(coords, cutBufferCells.get(get));
                        }
                        else{
                            throw new UnrecognizedEntryException(rangeSpecification);
                        }
                    }
                }
                else if(c1.getCol() - c2.getCol() + 1 == cutBufferCells.size()){
                    for(int i = c2.getCol(); i <= c1.getCol(); i++){
                        Coords coords = new Coords(c1.getRow(), i);
                        if(checkCoords(coords)){
                            Coords get = new Coords(1, i-c2.getCol()+1);
                            _cells.put(coords, cutBufferCells.get(get));
                        }
                        else{
                            throw new UnrecognizedEntryException(rangeSpecification);
                        } 
                    }
                }
            }
            else if(c1.getCol() == c2.getCol() && status.equals("vertical")){
                if(c1.getRow() < c2.getRow() && c2.getRow() - c1.getRow() + 1 == cutBufferCells.size()){
                    for(int i = c1.getRow(); i <= c2.getRow(); i++){
                        Coords coords = new Coords(i, c1.getCol());
                        if(checkCoords(coords)){
                            Coords get = new Coords(1, i-c1.getRow()+1);
                            _cells.put(coords, cutBufferCells.get(get));
                        }
                        else{
                            throw new UnrecognizedEntryException(rangeSpecification);
                        }           
                    }
                }
                else if(c1.getRow() - c2.getRow() + 1 == cutBufferCells.size()){
                    for(int i = c2.getRow(); i <= c1.getRow(); i++){
                        Coords coords = new Coords(i, c1.getCol());
                        if(checkCoords(coords)){
                           Coords get = new Coords(1, i-c2.getRow()+1);
                            _cells.put(coords, cutBufferCells.get(get));
                        }
                        else{
                            throw new UnrecognizedEntryException(rangeSpecification);
                        }
                    }
                }
            }
        }

    }

    public void cut(String rangeSpecification) throws UnrecognizedEntryException{
        try{
            copy(rangeSpecification);
            deleteContent(rangeSpecification);
        } catch(UnrecognizedEntryException e){
            throw new UnrecognizedEntryException(rangeSpecification);
        }
    }

    public String showCutBuffer(){
        return _cutBuffer.show();
    }


    /**
     * Set changed.
     */
    public void changed() {
        setChanged(true);
    }

    /**
     * @return changed
     */
    public boolean hasChanged() {
        return _changed;
    }

    /**
     * @param changed
     */
    public void setChanged(boolean changed) {
        _changed = changed;
    }
}

