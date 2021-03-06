package eu.isas.peptideshaker.recalibration;

import com.compomics.util.gui.waiting.waitinghandlers.ProgressDialogX;
import eu.isas.peptideshaker.gui.PeptideShakerGUI;
import java.io.IOException;
import java.util.HashMap;
import uk.ac.ebi.jmzml.xml.io.MzMLUnmarshallerException;

/**
 * This class computes statistics on the mass deviations of peak matching.
 *
 * @author Marc Vaudel
 */
public class DataSetErrors {

    /**
     * The GUI main instance.
     */
    private PeptideShakerGUI peptideShakerGUI;
    /**
     * The errors of every spectrum file.
     */
    private HashMap<String, FractionError> errors = new HashMap<String, FractionError>();

    /**
     * Constructor.
     *
     * @param peptideShakerGUI the main instance of the GUI
     */
    public DataSetErrors(PeptideShakerGUI peptideShakerGUI) {
        this.peptideShakerGUI = peptideShakerGUI;
    }

    /**
     * Returns the errors for the requested file.
     *
     * @param fileName the name of the spectrum file
     * @param progressDialog a dialog displaying progress to the user. Can be
     * null
     * @return the peak matching errors
     * @throws IOException
     * @throws MzMLUnmarshallerException  
     */
    public FractionError getFileErrors(String fileName, ProgressDialogX progressDialog) throws IOException, MzMLUnmarshallerException {
        if (!errors.containsKey(fileName)) {
            errors.put(fileName, new FractionError(peptideShakerGUI, fileName, progressDialog));
        }
        return errors.get(fileName);
    }
}
