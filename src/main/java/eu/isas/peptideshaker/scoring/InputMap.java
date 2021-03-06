package eu.isas.peptideshaker.scoring;

import com.compomics.util.gui.waiting.WaitingHandler;
import eu.isas.peptideshaker.scoring.targetdecoy.TargetDecoyMap;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class contains basic information about the hits as imported from the
 * various search engine result files.
 *
 * @author Marc Vaudel
 */
public class InputMap {

    /**
     * Map of the hits as imported. One target/decoy map per search engine
     * (referenced by their compomics index)
     */
    private HashMap<Integer, TargetDecoyMap> inputMap = new HashMap<Integer, TargetDecoyMap>();

    /**
     * Returns true for multiple search engines investigations.
     *
     * @return true for multiple search engines investigations
     */
    public boolean isMultipleSearchEngines() {
        return inputMap.size() > 1;
    }

    /**
     * returns the first target/decoy map of the input map (used in case of
     * single search engines studies).
     *
     * @return the first target/decoy map of the input map
     */
    public TargetDecoyMap getFirstMap() {
        for (TargetDecoyMap firstMap : inputMap.values()) {
            return firstMap;
        }
        return null;
    }

    /**
     * Estimates the posterior error probability for each search engine
     *
     * @param waitingHandler the handler displaying feedback to the user
     */
    public void estimateProbabilities(WaitingHandler waitingHandler) {

        int max = getNEntries();
        waitingHandler.setSecondaryProgressDialogIndeterminate(false);
        waitingHandler.setMaxSecondaryProgressValue(max);

        for (TargetDecoyMap hitmap : inputMap.values()) {
            waitingHandler.increaseSecondaryProgressValue();
            hitmap.estimateProbabilities(waitingHandler);
            if (waitingHandler.isRunCanceled()) {
                return;
            }
        }

        waitingHandler.setSecondaryProgressDialogIndeterminate(true);
    }

    /**
     * returns the posterior error probability associated to the given e-value
     * for the given search-engine (indexed by its utilities index)
     *
     * @param searchEngine The search engine
     * @param eValue The e-value
     * @return the posterior error probability corresponding
     */
    public double getProbability(int searchEngine, double eValue) {
        return inputMap.get(searchEngine).getProbability(eValue);
    }

    /**
     * Returns a list of search engines indexed by utilities index presenting a
     * suspicious input
     *
     * @return a list of search engines presenting a suspicious input
     */
    public ArrayList<Integer> suspiciousInput() {
        ArrayList<Integer> result = new ArrayList<Integer>();
        if (inputMap.size() == 1) {
            return result;
        }
        for (int key : inputMap.keySet()) {
            if (inputMap.get(key).suspiciousInput()) {
                result.add(key);
            }
        }
        return result;
    }

    /**
     * constructor
     */
    public InputMap() {
    }

    /**
     * Adds an entry to the input map.
     *
     * @param searchEngine The search engine used referenced by its compomics
     * index
     * @param eValue The search engine e-value
     * @param isDecoy boolean indicating whether the hit was decoy or target
     * (resp. true/false)
     */
    public void addEntry(int searchEngine, double eValue, boolean isDecoy) {
        if (inputMap.get(searchEngine) == null) {
            inputMap.put(searchEngine, new TargetDecoyMap());
        }
        inputMap.get(searchEngine).put(eValue, isDecoy);
    }

    /**
     * Returns the number of entries
     *
     * @return the number of entries
     */
    public int getNEntries() {
        int result = 0;
        for (TargetDecoyMap targetDecoyMap : inputMap.values()) {
            result += targetDecoyMap.getMapSize();
        }
        return result;
    }
}
