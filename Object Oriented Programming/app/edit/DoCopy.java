package xxl.app.edit;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;
import xxl.exceptions.UnrecognizedEntryException;

/**
 * Copy command.
 */
class DoCopy extends Command<Spreadsheet> {

    DoCopy(Spreadsheet receiver) {
        super(Label.COPY, receiver);
    }

    @Override
    protected final void execute() throws CommandException {
        String range = Form.requestString(Prompt.address());
        try{
            _receiver.copy(range);
        } catch(UnrecognizedEntryException e){
            throw new InvalidCellRangeException(range);
        }
    }

}
