package xxl.CellContent;

public class LiteralInt extends Content {
    private int _value;

    public LiteralInt(int value){
        _value = value;
    }

    public Integer value(){
        return _value;
    }

    public String toString(){
        return String.valueOf(_value);
    }
    
    public String asString(){
        return toString();
    }

    public String stringValue(){
        return null;
    }
}
