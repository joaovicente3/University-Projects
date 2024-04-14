package xxl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;

import xxl.exceptions.ImportFileException;
import xxl.exceptions.MissingFileAssociationException;
import xxl.exceptions.UnavailableFileException;
import xxl.exceptions.UnrecognizedEntryException;

/**
 * Class representing a spreadsheet application.
 */
public class Calculator{

    /** The current spreadsheet. */
    private Spreadsheet _spreadsheet;

    private User[] user;

    private String _filename = "";

    public Spreadsheet getSpreadsheet(){
        return _spreadsheet;
    }


    /**
     * @param numLines number of lines of new spreasheet
     * @param numColumns number of columns of new spreasheet
     */
    public void createSpreadsheet(int numLines, int numColumns){
        _spreadsheet = new Spreadsheet(numLines, numColumns);
    }

    /**
     * Saves the serialized application's state into the file associated to the current network.
     *
     * @throws FileNotFoundException if for some reason the file cannot be created or opened. 
     * @throws MissingFileAssociationException if the current network does not have a file.
     * @throws IOException if there is some error while serializing the state of the network to disk.
     */
    public void save() throws FileNotFoundException, MissingFileAssociationException, IOException {
        if(_filename == null || _filename.isEmpty()){
            throw new MissingFileAssociationException();
        }
        try(ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(_filename)))){
            oos.writeObject(_spreadsheet);
            _spreadsheet.setChanged(false);
        }
    }

    /**
     * Saves the serialized application's state into the specified file. The current network is
     * associated to this file.
     *
     * @param filename the name of the file.
     * @throws FileNotFoundException if for some reason the file cannot be created or opened.
     * @throws MissingFileAssociationException if the current network does not have a file.
     * @throws IOException if there is some error while serializing the state of the network to disk.
     */
    public void saveAs(String filename) throws FileNotFoundException, MissingFileAssociationException, IOException {
        _filename = filename;
        save();    
    }

    /**
     * @param filename name of the file containing the serialized application's state
     *        to load.
     * @throws UnavailableFileException if the specified file does not exist or there is
     *         an error while processing this file.
     */
    public void load(String filename) throws UnavailableFileException {
        _filename = filename;
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
            _spreadsheet = (Spreadsheet) ois.readObject();
            _spreadsheet.setChanged(false);
        }
        catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
            throw new UnavailableFileException(filename);
        }
    }

    /**
     * Read text input file and create domain entities..
     *
     * @param filename name of the text input file
     * @throws ImportFileException
     */
    public void importFile(String filename) throws ImportFileException {
        if (filename == null || filename.equals(""))
            return;

        String line;
        int numCols = 0, numRows = 0;
        try (BufferedReader input = new BufferedReader(new FileReader(filename))){
            for(int i = 0; i < 2; i++){
                line = input.readLine();
                if (line.startsWith("linhas=")) {
                    numRows = Integer.parseInt(line.substring("linhas=".length()));
                } 
                else if (line.startsWith("colunas=")) {
                    numCols = Integer.parseInt(line.substring("colunas=".length()));
                } 
            }

            _spreadsheet = new Spreadsheet(numRows, numCols);
            _spreadsheet.changed();

            while ((line = input.readLine()) != null) {
                String[] fields = line.split("\\|");
                String index = fields[0];
                if(fields.length == 2){
                    String contentSpecification = fields[1];
	                _spreadsheet.insertContents(index, contentSpecification);
                }
            }

        } catch (IOException | UnrecognizedEntryException e) {
            throw new ImportFileException(filename);
        }
    }

   /**
   * @return filename
   */
  public String getFilename() {
    return _filename;
  }

  /**
   * @param filename
   */
  public void setFilename(String filename) {
    _filename = filename;
  }

  /**
   * @return changed?
   */
  public boolean changed() {
    if(_spreadsheet == null){
        return false;
    }
    return _spreadsheet.hasChanged();
  }

}
