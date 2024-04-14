package xxl;

import java.io.Serializable;

import xxl.CellContent.Content;

public class Cell implements Serializable{
    private Content _content;

    public Cell(Content c){
        _content = c;
    }

    public Content getContent() {
        return _content;
    }

    public void setContent(Content c){
        _content = c;
    }

    public boolean hasContent(){
        return _content != null;
    }

    public String toString(){
        return _content.toString();
    }
}


