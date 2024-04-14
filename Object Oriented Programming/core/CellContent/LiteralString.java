package xxl.CellContent;

public class LiteralString extends Content{
    private String _value;
    
    public LiteralString(String s){
        _value = s;
    }

    public Integer value(){
        return null;
    }

    public String toString(){
        return _value;
    }

    public String asString(){
        return toString();
    }

    public String stringValue(){
        return _value;
    }
}

