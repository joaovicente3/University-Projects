package xxl.app.edit;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;
import xxl.exceptions.UnrecognizedEntryException;

/**
 * Delete command.
 */
class DoDelete extends Command<Spreadsheet> {

    DoDelete(Spreadsheet receiver) {
        super(Label.DELETE, receiver);
    }

    @Override
    protected final void execute() throws CommandException {
        String index = Form.requestString(Prompt.address());
        try{
            _receiver.deleteContent(index);
        } catch(UnrecognizedEntryException e){
            throw new InvalidCellRangeException(index);
        }
    }

}
