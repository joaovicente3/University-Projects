package xxl;

import java.util.Map;
import java.util.TreeMap;

import java.io.Serializable;

public class StorageStruct implements Serializable{
    private Map<Coords, Cell> _registry = new TreeMap<Coords, Cell>();

    public Cell get(Coords s){
        return _registry.get(s);
    }

    public void put(Coords s, Cell c){
        _registry.put(s, c);
    }

    public void clear(){
        _registry.clear();
    }

    public Integer size(){
        return _registry.size();
    }
}
