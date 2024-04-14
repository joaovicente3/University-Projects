package xxl.app.edit;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;

/**
 * Class for searching functions.
 */
class DoShow extends Command<Spreadsheet> {

    DoShow(Spreadsheet receiver) {
        super(Label.SHOW, receiver);
    }

    @Override
    protected final void execute() throws CommandException {
        String input = Form.requestString(Prompt.address());
        String visualiser = _receiver.visualiseSpreadsheet(input); 
        if(visualiser == null){
            throw new InvalidCellRangeException(input);
        }
        else{_display.popup(visualiser);}
    }
}
