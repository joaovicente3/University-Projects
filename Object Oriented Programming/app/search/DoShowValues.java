package xxl.app.search;

import pt.tecnico.uilib.menus.Command;
import xxl.Spreadsheet;
import pt.tecnico.uilib.forms.Form;

/**
 * Command for searching content values.
 */
class DoShowValues extends Command<Spreadsheet> {

    DoShowValues(Spreadsheet receiver) {
        super(Label.SEARCH_VALUES, receiver);
    }

    @Override
    protected final void execute() {
        String txt = _receiver.showValues(Form.requestString(Prompt.searchValue()));
        _display.popup(txt);
    }

}
