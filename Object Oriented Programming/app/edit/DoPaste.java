package xxl.app.edit;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;
import xxl.exceptions.UnrecognizedEntryException;

/**
 * Paste command.
 */
class DoPaste extends Command<Spreadsheet> {

    DoPaste(Spreadsheet receiver) {
        super(Label.PASTE, receiver);
    }

    @Override
    protected final void execute() throws CommandException {
        String range = Form.requestString(Prompt.address());
        try{
            _receiver.paste(range);
        } catch(UnrecognizedEntryException e){
            throw new InvalidCellRangeException(range);
        }
    }

}
