package xxl;

import java.io.Serializable;
import java.util.List;

public class CutBuffer implements Serializable{
    private StorageStruct _cells;

    private String _status;

    public CutBuffer(){
        _cells = new StorageStruct(); 
    }

    public StorageStruct getCells(){
        return _cells;
    }

    public String getStatus(){
        return _status;
    }

    public void copy(List<Cell> cells, String status){
        _cells.clear();
        if(status.equals("singleCell")){
            _status = "singleCell";
            Coords coords = new Coords(1, 1);
            _cells.put(coords, cells.get(0));
        }
        else if(status.equals("horizontal")){
            _status = "horizontal";
            for(int i = 1; i <= cells.size(); i++){
                Coords coords = new Coords(1, i);
                _cells.put(coords, cells.get(i-1));
            }
        }
        else{
            for(int i = 1; i <= cells.size(); i++){
                _status = "vertical";
                Coords coords = new Coords(i, 1);
                _cells.put(coords, cells.get(i-1));
            }
        }
    }

    public String show(){
        String visualizer = "";
        if(_status.equals("singleCell")){
                Coords coords = new Coords(1, 1);
                if(_cells.get(coords) != null && _cells.get(coords).hasContent()){
                    Cell cell = _cells.get(coords);
                    return "1;1|" + cell.toString() + "\n";
                }
                else return "1;1|\n";
        }
        else if(_status.equals("horizontal")){
            for(int i = 1; i <= _cells.size(); i++){
                Coords coords = new Coords(1, i);
                if(_cells.get(coords) != null && _cells.get(coords).hasContent()){
                    Cell cell = _cells.get(coords);
                    visualizer += "1;" + i + "|" + cell.toString() + "\n";
                }
                else{ visualizer += "1;" + i +"|\n"; }
            }
            return visualizer;
        }
        else if(_status.equals("vertical")){
            for(int i = 1; i <= _cells.size(); i++){
                Coords coords = new Coords(i, 1);
                if(_cells.get(coords) != null && _cells.get(coords).hasContent()){
                    Cell cell = _cells.get(coords);
                    visualizer += i + ";1|" + cell.toString() + "\n";
                }
                else{ visualizer += i + ";1|" + "|\n"; }
            }
            return visualizer;
        }
        return "";
    }
}
