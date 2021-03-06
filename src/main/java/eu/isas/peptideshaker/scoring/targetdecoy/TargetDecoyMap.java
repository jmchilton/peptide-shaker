package eu.isas.peptideshaker.scoring.targetdecoy;

import com.compomics.util.gui.waiting.WaitingHandler;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * This map contains the information of a target/decoy strategy
 *
 * @author Marc Vaudel
 */
public class TargetDecoyMap implements Serializable {

    /**
     * Serial version UID for post-serialization compatibility.
     */
    static final long serialVersionUID = 7333389442377322662L;
    /**
     * The hit map containing the indexed target/decoy points.
     */
    private HashMap<Double, TargetDecoyPoint> hitMap = new HashMap<Double, TargetDecoyPoint>();
    /**
     * The scores imported in the map.
     */
    private ArrayList<Double> scores;
    /**
     * The maximal amount of target hits comprised between two subsequent decoy
     * hits.
     */
    private Integer nmax;
    /**
     * The window size for pep estimation.
     */
    private Integer windowSize;
    /**
     * The number of target hits found before the first decoy hit.
     */
    private Integer nTargetOnly;
    /**
     * the results computed on this map.
     */
    private TargetDecoyResults targetDecoyResults = new TargetDecoyResults();

    /**
     * Returns the posterior error probability estimated at the given score.
     *
     * @param score the given score
     * @return the estimated posterior error probability
     */
    public Double getProbability(double score) {
        TargetDecoyPoint point = hitMap.get(score);
        if (point != null) {
            return point.p;
        } else if (score >= scores.get(scores.size() - 1)) {
            return hitMap.get(scores.get(scores.size() - 1)).p;
        } else {
            int indexInf = 0;
            int indexSup = scores.size() - 1;
            int indexTemp;
            while (indexSup - indexInf > 1) {
                indexTemp = (indexSup - indexInf) / 2 + indexInf;
                if (scores.get(indexTemp) > score) {
                    indexSup = indexTemp;
                } else {
                    indexInf = indexTemp;
                }
            }
            return (hitMap.get(scores.get(indexSup)).p + hitMap.get(scores.get(indexInf)).p) / 2;
        }
    }

    /**
     * Returns the number of target hits found at the given score.
     *
     * @param score the given score
     * @return the number of target hits found at the given score
     */
    public int getNTarget(double score) {
        return hitMap.get(score).nTarget;
    }

    /**
     * Returns the number of decoy hits found at the given score.
     *
     * @param score the given score
     * @return the number of decoy hits found at the given score
     */
    public int getNDecoy(double score) {
        return hitMap.get(score).nDecoy;
    }

    /**
     * Puts a new point in the target/decoy map at the given score.
     *
     * @param score The given score
     * @param isDecoy boolean indicating whether the hit is decoy
     */
    public void put(double score, boolean isDecoy) {
        if (!hitMap.containsKey(score)) {
            hitMap.put(score, new TargetDecoyPoint());
        }
        if (isDecoy) {
            hitMap.get(score).nDecoy++;
        } else {
            hitMap.get(score).nTarget++;
        }
    }

    /**
     * Removes a point in the target/decoy map at the given score.
     *
     * @param score the given score
     * @param isDecoy boolean indicating whether the hit is decoy
     */
    public void remove(double score, boolean isDecoy) {
        if (!isDecoy) {
            hitMap.get(score).nTarget--;
        } else {
            hitMap.get(score).nDecoy--;
        }
        if (hitMap.get(score).nTarget == 0
                && hitMap.get(score).nDecoy == 0) {
            hitMap.remove(score);
        }
        scores = null;
        nmax = null;
        windowSize = null;
    }

    /**
     * Constructs a target/decoy map.
     */
    public TargetDecoyMap() {
    }

    /**
     * Estimates the metrics of the map: Nmax and NtargetOnly.
     */
    private void estimateNs() {
        if (scores == null) {
            estimateScores();
        }
        double scoreMax = scores.get(scores.size() - 1); // used to avoid side effects at p=1
        boolean onlyTarget = true;
        int nMax1 = 0;
        int targetCpt = 0;
        nTargetOnly = 0;

        for (double peptideP : scores) {
            TargetDecoyPoint point = hitMap.get(peptideP);
            if (onlyTarget) {
                nTargetOnly += point.nTarget;
                if (point.nDecoy > 0) {
                    onlyTarget = false;
                }
            } else {
                targetCpt += point.nTarget;
                if (point.nDecoy > 0) {
                    if (targetCpt > nMax1 && peptideP < scoreMax) {
                        nMax1 = targetCpt;
                    }
                    targetCpt = point.nTarget;
                }
            }
        }
        nmax = nMax1;
    }

    /**
     * Estimates the posterior error probabilities in this map.
     *
     * @param waitingHandler the handler displaying feedback to the user
     */
    public void estimateProbabilities(WaitingHandler waitingHandler) {

        waitingHandler.setWaitingText("Estimating Probabilities. Please Wait...");

        if (scores == null) {
            estimateScores();
        }
        if (nmax == null) {
            estimateNs();
        }
        if (windowSize == null) {
            windowSize = nmax;
        }

        // estimate p
        TargetDecoyPoint tempPoint, previousPoint = hitMap.get(scores.get(0));
        double nLimit = 0.5 * windowSize;
        double nTargetSup = 1.5 * previousPoint.nTarget;
        double nTargetInf = -0.5 * previousPoint.nTarget;
        double nDecoy = previousPoint.nDecoy;
        int cptInf = 0;
        int cptSup = 1;
        boolean oneReached = false;
        
        for (int cpt = 0; cpt < scores.size(); cpt++) {
            TargetDecoyPoint point = hitMap.get(scores.get(cpt));
            if (!oneReached) {
                double change = 0.5 * (previousPoint.nTarget + point.nTarget);
                nTargetInf += change;
                nTargetSup -= change;
                while (nTargetInf > nLimit) {
                    if (cptInf < cpt) {
                        tempPoint = hitMap.get(scores.get(cptInf));
                        double nTargetInfTemp = nTargetInf - tempPoint.nTarget;
                        if (nTargetInfTemp >= nLimit) {
                            nDecoy -= tempPoint.nDecoy;
                            nTargetInf = nTargetInfTemp;
                            cptInf++;
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                }
                while (nTargetSup < nLimit) {
                    if (cptSup < scores.size()) {
                        tempPoint = hitMap.get(scores.get(cptSup));
                        nTargetSup += tempPoint.nTarget;
                        nDecoy += tempPoint.nDecoy;
                        cptSup++;
                    } else {
                        break;
                    }
                }
                point.p = Math.min(nDecoy / (nTargetInf + nTargetSup), 1);
                if (point.p >= 0.98) {
                    oneReached = true;
                }
            } else {
                point.p = 1;
            }
            previousPoint = point;

            waitingHandler.increaseSecondaryProgressValue();
            if (waitingHandler.isRunCanceled()) {
                return;
            }
        }
    }

    /**
     * Returns the Nmax metric.
     *
     * @return the Nmax metric
     */
    public int getnMax() {
        if (nmax == null) {
            estimateNs();
        }
        return nmax;
    }

    /**
     * Returns the number of target hits before the first decoy hit.
     *
     * @return the number of target hits before the first decoy hit
     */
    public Integer getnTargetOnly() {
        return nTargetOnly;
    }

    /**
     * Sorts the scores implemented in this map.
     */
    private void estimateScores() {
        scores = new ArrayList<Double>(hitMap.keySet());
        Collections.sort(scores);
    }

    /**
     * Returns the sorted scores implemented in this map.
     *
     * @return the sorted scores implemented in this map.
     */
    public ArrayList<Double> getScores() {
        if (scores == null) {
            estimateScores();
        }
        return scores;
    }

    /**
     * Adds all the points from another target/decoy map.
     *
     * @param anOtherMap another target/decoy map
     */
    public void addAll(TargetDecoyMap anOtherMap) {
        for (double score : anOtherMap.getScores()) {
            for (int i = 0; i < anOtherMap.getNDecoy(score); i++) {
                put(score, true);
            }
            for (int i = 0; i < anOtherMap.getNTarget(score); i++) {
                put(score, false);
            }
        }
        scores = null;
        nmax = null;
        windowSize = null;
    }

    /**
     * Returns a boolean indicating if a suspicious input was detected.
     *
     * @return a boolean indicating if a suspicious input was detected
     */
    public boolean suspiciousInput() {
        if (nmax == null) {
            estimateNs();
        }
        if (nmax < 100
                || nTargetOnly < 100
                || nTargetOnly <= nmax) {
            return true;
        }
        return false;
    }

    /**
     * Returns the current target decoy results.
     *
     * @return the current target decoy results
     */
    public TargetDecoyResults getTargetDecoyResults() {
        return targetDecoyResults;
    }

    /**
     * Returns the target decoy series.
     *
     * @return the target decoy series
     */
    public TargetDecoySeries getTargetDecoySeries() {
        return new TargetDecoySeries(hitMap);
    }

    /**
     * Returns the window size used for pep estimation.
     *
     * @return the window size used for pep estimation
     */
    public int getWindowSize() {
        if (windowSize == null) {
            windowSize = getnMax();
        }
        return windowSize;
    }

    /**
     * Sets the window size used for pep estimation.
     *
     * @param windowSize the window size used for pep estimation
     */
    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;
    }

    /**
     * Returns the size of the map.
     *
     * @return the size of the map
     */
    public int getMapSize() {
        return hitMap.size();
    }
}
