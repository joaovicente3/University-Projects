package xxl.CellContent;

import java.io.Serializable;

public abstract class Content implements Serializable {
    public abstract Integer value();
    public abstract String asString();
    public abstract String stringValue();
}
