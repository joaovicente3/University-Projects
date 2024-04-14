package xxl.app.edit;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;
import xxl.exceptions.UnrecognizedEntryException;

/**
 * Cut command.
 */
class DoCut extends Command<Spreadsheet> {

    DoCut(Spreadsheet receiver) {
        super(Label.CUT, receiver);
    }

    @Override
    protected final void execute() throws CommandException {
        String range = Form.requestString(Prompt.address());
        try{
            _receiver.cut(range);
        } catch(UnrecognizedEntryException e){
            throw new InvalidCellRangeException(range);
        }
    }

}
