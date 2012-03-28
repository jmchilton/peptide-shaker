package eu.isas.peptideshaker.scoring;

import eu.isas.peptideshaker.gui.interfaces.WaitingHandler;
import eu.isas.peptideshaker.scoring.targetdecoy.TargetDecoyMap;
import java.io.Serializable;

/**
 * This map will be used to score protein matches and solve protein inference
 * problems
 *
 * @author Marc Vaudel
 */
public class ProteinMap implements Serializable {

    /**
     * serial version UID for post-serialization compatibility.
     */
    static final long serialVersionUID = -2438674334416191482L;
    /**
     * The protein target/decoy map.
     */
    private TargetDecoyMap proteinMatchMap = new TargetDecoyMap();

    /**
     * Constructor.
     */
    public ProteinMap() {
    }

    /**
     * Estimate the posterior error probabilities.
     *
     * @param waitingHandler the handler displaying feedback to the user (can be
     * null)
     */
    public void estimateProbabilities(WaitingHandler waitingHandler) { //@TODO: replace this by a progress bar?
        if (waitingHandler != null) {
            waitingHandler.setSecondaryProgressDialogIntermediate(false);
            waitingHandler.setMaxSecondaryProgressValue(proteinMatchMap.getMapSize());
        }
        proteinMatchMap.estimateProbabilities(waitingHandler);
        if (waitingHandler != null) {
            waitingHandler.setSecondaryProgressDialogIntermediate(true);
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
     * Returns the posterior error probability of a peptide match at the given
     * score.
     *
     * @param score the score of the match
     * @return the posterior error probability
     */
    public double getProbability(double score) {
        return proteinMatchMap.getProbability(score);
    }

    /**
     * Returns a boolean indicating if a suspicious input was detected.
     *
     * @return a boolean indicating if a suspicious input was detected
     */
    public boolean suspicousInput() {
        return proteinMatchMap.suspiciousInput();
    }

    /**
     * Returns the target decoy map.
     *
     * @return the target decoy map
     */
    public TargetDecoyMap getTargetDecoyMap() {
        return proteinMatchMap;
    }
}
