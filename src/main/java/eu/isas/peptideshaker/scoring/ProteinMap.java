package eu.isas.peptideshaker.scoring;

import eu.isas.peptideshaker.gui.WaitingDialog;
import eu.isas.peptideshaker.scoring.targetdecoy.TargetDecoyMap;
import java.io.Serializable;

/**
 * This map will be used to score protein matches and solve protein inference problems
 *
 * @author Marc
 */
public class ProteinMap implements Serializable {

    /**
     * serial version UID for post-serialization compatibility
     */
    static final long serialVersionUID = -2438674334416191482L;
    /**
     * The protein target/decoy map
     */
    private TargetDecoyMap proteinMatchMap = new TargetDecoyMap();

    /**
     * Constructor
     */
    public ProteinMap() {
        
    }

    /**
     * estimate the posterior error probabilities 
     * 
     * @param waitingDialog a WaitingDialog to diplay the progress (can be null)
     */
    public void estimateProbabilities(WaitingDialog waitingDialog) { //@TODO: replace this by a progress bar?
        if (waitingDialog != null) {
            waitingDialog.setSecondaryProgressDialogIntermediate(false);
            waitingDialog.setMaxSecondaryProgressValue(proteinMatchMap.getMapSize());
        }
        proteinMatchMap.estimateProbabilities(waitingDialog);
        if (waitingDialog != null) {
            waitingDialog.setSecondaryProgressDialogIntermediate(true);
        }
    }

    /**
     * Adds a point in the target/decoy map.
     *
     * @param probabilityScore The estimated protein probabilistic score
     * @param isDecoy a boolean indicating whether the protein is decoy
     */
    public void addPoint(double probabilityScore, boolean isDecoy) {
        proteinMatchMap.put(probabilityScore, isDecoy);
    }

    /**
     * Removes a point in the target/decoy map.
     * 
     * @param probabilityScore The estimated protein probabilistic score
     * @param isDecoy a boolean indicating whether the protein is decoy
     */
    public void removePoint(double probabilityScore, boolean isDecoy) {
        proteinMatchMap.remove(probabilityScore, isDecoy);
    }

    /**
     * Returns the posterior error probability of a peptide match at the given score
     *
     * @param score        the score of the match
     * @return the posterior error probability
     */
    public double getProbability(double score) {
        return proteinMatchMap.getProbability(score);
    }

    /**
     * Returns a boolean indicating if a suspicious input was detected
     * @return a boolean indicating if a suspicious input was detected
     */
    public boolean suspicousInput() {
        return proteinMatchMap.suspiciousInput();
    }

    /**
     * Returns the target decoy map
     * @return the target decoy map
     */
    public TargetDecoyMap getTargetDecoyMap() {
        return proteinMatchMap;
    }
}
