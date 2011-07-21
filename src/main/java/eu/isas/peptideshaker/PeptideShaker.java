package eu.isas.peptideshaker;

import com.compomics.util.experiment.MsExperiment;
import com.compomics.util.experiment.ProteomicAnalysis;
import com.compomics.util.experiment.biology.PTM;
import com.compomics.util.experiment.biology.Peptide;
import com.compomics.util.experiment.biology.Protein;
import com.compomics.util.experiment.biology.Sample;
import com.compomics.util.experiment.identification.AdvocateFactory;
import com.compomics.util.experiment.identification.Identification;
import com.compomics.util.experiment.identification.IdentificationMethod;
import com.compomics.util.experiment.identification.PeptideAssumption;
import com.compomics.util.experiment.identification.SequenceDataBase;
import com.compomics.util.experiment.identification.identifications.Ms2Identification;
import com.compomics.util.experiment.identification.matches.ModificationMatch;
import com.compomics.util.experiment.identification.matches.PeptideMatch;
import com.compomics.util.experiment.identification.matches.ProteinMatch;
import com.compomics.util.experiment.identification.matches.SpectrumMatch;
import eu.isas.peptideshaker.scoring.InputMap;
import eu.isas.peptideshaker.scoring.PeptideSpecificMap;
import eu.isas.peptideshaker.scoring.ProteinMap;
import eu.isas.peptideshaker.scoring.PsmSpecificMap;
import eu.isas.peptideshaker.scoring.targetdecoy.TargetDecoyMap;
import eu.isas.peptideshaker.scoring.targetdecoy.TargetDecoyResults;
import eu.isas.peptideshaker.gui.WaitingDialog;
import eu.isas.peptideshaker.fileimport.IdFilter;
import eu.isas.peptideshaker.fileimport.FileImporter;
import eu.isas.peptideshaker.myparameters.PSMaps;
import eu.isas.peptideshaker.myparameters.PSParameter;
import eu.isas.peptideshaker.myparameters.PSPtmScores;
import eu.isas.peptideshaker.scoring.PtmScoring;
import eu.isas.peptideshaker.preferences.SearchParameters;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * This class will be responsible for the identification import and the associated calculations
 *
 * @author Marc Vaudel
 * @author Harald Barsnes
 */
public class PeptideShaker {

    /**
     * If set to true, detailed information is sent to the waiting dialog.
     */
    private boolean detailedReport = false;
    /**
     * The experiment conducted
     */
    private MsExperiment experiment;
    /**
     * The sample analyzed
     */
    private Sample sample;
    /**
     * The replicate number
     */
    private int replicateNumber;
    /**
     * The psm map
     */
    private PsmSpecificMap psmMap;
    /**
     * The peptide map
     */
    private PeptideSpecificMap peptideMap;
    /**
     * The protein map
     */
    private ProteinMap proteinMap;
    /**
     * The id importer will import and process the identifications
     */
    private FileImporter fileImporter = null;

    /**
     * Constructor without mass specification. Calculation will be done on new maps
     * which will be retrieved as compomics utilities parameters.
     *
     * @param experiment        The experiment conducted
     * @param sample            The sample analyzed
     * @param replicateNumber   The replicate number
     */
    public PeptideShaker(MsExperiment experiment, Sample sample, int replicateNumber) {
        this.experiment = experiment;
        this.sample = sample;
        this.replicateNumber = replicateNumber;
        psmMap = new PsmSpecificMap(experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber).getSpectrumCollection());
        peptideMap = new PeptideSpecificMap();
        proteinMap = new ProteinMap();
    }

    /**
     * Constructor with map specifications.
     *
     * @param experiment        The experiment conducted
     * @param sample            The sample analyzed
     * @param replicateNumber   The replicate number
     * @param psMaps            the peptide shaker maps
     */
    public PeptideShaker(MsExperiment experiment, Sample sample, int replicateNumber, PSMaps psMaps) {
        this.experiment = experiment;
        this.sample = sample;
        this.replicateNumber = replicateNumber;
        this.psmMap = psMaps.getPsmSpecificMap();
        this.peptideMap = psMaps.getPeptideSpecificMap();
        this.proteinMap = psMaps.getProteinMap();
    }

    /**
     * Method used to import identification from identification result files
     *
     * @param waitingDialog     A dialog to display the feedback
     * @param idFilter          The identification filter to use
     * @param idFiles           The files to import
     * @param spectrumFiles     The corresponding spectra (can be empty: spectra will not be loaded)
     * @param fastaFile         The database file in the fasta format
     * @param searchParameters  The search parameters
     */
    public void importFiles(WaitingDialog waitingDialog, IdFilter idFilter, ArrayList<File> idFiles, ArrayList<File> spectrumFiles, File fastaFile, SearchParameters searchParameters) {

        ProteomicAnalysis analysis = experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber);

        if (analysis.getIdentification(IdentificationMethod.MS2_IDENTIFICATION) == null) {
            analysis.addIdentificationResults(IdentificationMethod.MS2_IDENTIFICATION, new Ms2Identification());
            SequenceDataBase db = new SequenceDataBase();
            analysis.setSequenceDataBase(db);
            fileImporter = new FileImporter(this, waitingDialog, analysis, idFilter);
            fileImporter.importFiles(idFiles, spectrumFiles, fastaFile, searchParameters);
        } else {
            fileImporter = new FileImporter(this, waitingDialog, analysis, idFilter);
            fileImporter.importFiles(spectrumFiles);
        }
    }

    /**
     * This method processes the identifications and fills the peptide shaker maps
     *
     * @param inputMap          The input map
     * @param waitingDialog     A dialog to display the feedback
     */
    public void processIdentifications(InputMap inputMap, WaitingDialog waitingDialog) {

        waitingDialog.appendReport("Computing assumptions probabilities.");
        inputMap.estimateProbabilities();
        attachAssumptionsProbabilities(inputMap);
        waitingDialog.appendReport("Computing PSMs probabilities.");
        setFirstHit();
        fillPsmMap(inputMap);
        psmMap.cure();
        psmMap.estimateProbabilities();
        attachSpectrumProbabilities();
        waitingDialog.appendReport("Computing peptide probabilities.");
        buildPeptidesAndProteins();
        fillPeptideMaps();
        peptideMap.cure();
        peptideMap.estimateProbabilities();
        attachPeptideProbabilities();
        waitingDialog.appendReport("Scoring PTMs.");
        scorePSMPTMs();
        scorePeptidePTMs();
        waitingDialog.appendReport("Computing protein probabilities.");
        fillProteinMap();
        proteinMap.estimateProbabilities();
        attachProteinProbabilities();
        waitingDialog.appendReport("Trying to resolve protein inference issues.");

        try {
            cleanProteinGroups(waitingDialog);
        } catch (Exception e) {
            waitingDialog.appendReport("An error occured while trying to resolve protein inference issues.");
            e.printStackTrace();
        }
        proteinMap = new ProteinMap();
        fillProteinMap();
        proteinMap.estimateProbabilities();
        attachProteinProbabilities();

        waitingDialog.appendReport("Validating identifications at 1% FDR.");
        fdrValidation();

        String report = "Identification processing completed.\n\n";
        ArrayList<Integer> suspiciousInput = inputMap.suspiciousInput();
        ArrayList<String> suspiciousPsms = psmMap.suspiciousInput();
        ArrayList<String> suspiciousPeptides = peptideMap.suspiciousInput();
        boolean suspiciousProteins = proteinMap.suspicousInput();

        if (suspiciousInput.size() > 0
                || suspiciousPsms.size() > 0
                || suspiciousPeptides.size() > 0
                || suspiciousProteins) {

            // @TODO: display this in a separate dialog??
            if (detailedReport) {

                report += "The following identification classes retieved non robust statistical estimations, "
                        + "we advice to control the quality of the corresponding matches: \n";

                boolean firstLine = true;

                for (int searchEngine : suspiciousInput) {
                    if (firstLine) {
                        firstLine = false;
                    } else {
                        report += ", ";
                    }
                    report += AdvocateFactory.getInstance().getAdvocate(searchEngine).getName();
                }

                if (suspiciousInput.size() > 0) {
                    report += " identifications.\n";
                }

                firstLine = true;

                for (String fraction : suspiciousPsms) {
                    if (firstLine) {
                        firstLine = false;
                    } else {
                        report += ", ";
                    }
                    report += fraction;
                }

                report += " charged spectra.\n";

                firstLine = true;

                for (String fraction : suspiciousPeptides) {
                    if (firstLine) {
                        firstLine = false;
                    } else {
                        report += ", ";
                    }
                    report += fraction;
                }

                report += " modified peptides.\n";

                if (suspiciousProteins) {
                    report += "proteins. \n";
                }
            }
        }

        waitingDialog.appendReport(report);
        Identification identification = experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber).getIdentification(IdentificationMethod.MS2_IDENTIFICATION);
        identification.addUrParam(new PSMaps(proteinMap, psmMap, peptideMap));
        waitingDialog.setRunFinished();
    }

    /**
     * Makes a preliminary validation of hits. By default a 1% FDR is used for all maps
     */
    public void fdrValidation() {
        TargetDecoyMap currentMap = proteinMap.getTargetDecoyMap();
        TargetDecoyResults currentResults = currentMap.getTargetDecoyResults();
        currentResults.setClassicalEstimators(true);
        currentResults.setClassicalValidation(true);
        currentResults.setFdrLimit(1.0);
        currentMap.getTargetDecoySeries().getFDRResults(currentResults);

        for (String mapKey : peptideMap.getKeys()) {
            currentMap = peptideMap.getTargetDecoyMap(mapKey);
            currentResults = currentMap.getTargetDecoyResults();
            currentResults.setClassicalEstimators(true);
            currentResults.setClassicalValidation(true);
            currentResults.setFdrLimit(1.0);
            currentMap.getTargetDecoySeries().getFDRResults(currentResults);
        }

        for (int mapKey : psmMap.getKeys().keySet()) {
            currentMap = psmMap.getTargetDecoyMap(mapKey);
            currentResults = currentMap.getTargetDecoyResults();
            currentResults.setClassicalEstimators(true);
            currentResults.setClassicalValidation(true);
            currentResults.setFdrLimit(1.0);
            currentMap.getTargetDecoySeries().getFDRResults(currentResults);
        }

        validateIdentifications();
    }

    /**
     * Processes the identifications if a change occured in the psm map
     *
     * @throws Exception    Exception thrown whenever it is attempted to attach more
     *                      than one identification per search engine per spectrum
     */
    public void spectrumMapChanged() throws Exception {
        peptideMap = new PeptideSpecificMap();
        proteinMap = new ProteinMap();
        attachSpectrumProbabilities();
        fillPeptideMaps();
        peptideMap.cure();
        peptideMap.estimateProbabilities();
        attachPeptideProbabilities();
        fillProteinMap();
        proteinMap.estimateProbabilities();
        attachProteinProbabilities();
        cleanProteinGroups(null);
    }

    /**
     * Processes the identifications if a change occured in the peptide map
     * @throws Exception    Exception thrown whenever it is attempted to attach
     *                      more than one identification per search engine per spectrum
     */
    public void peptideMapChanged() throws Exception {
        proteinMap = new ProteinMap();
        attachPeptideProbabilities();
        fillProteinMap();
        proteinMap.estimateProbabilities();
        attachProteinProbabilities();
        cleanProteinGroups(null);
    }

    /**
     * Processes the identifications if a change occured in the protein map
     */
    public void proteinMapChanged() {
        attachProteinProbabilities();
    }

    /**
     * This method will flag validated identifications
     */
    public void validateIdentifications() {
        Identification identification = experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber).getIdentification(IdentificationMethod.MS2_IDENTIFICATION);
        PSParameter psParameter = new PSParameter();

        double proteinThreshold = proteinMap.getTargetDecoyMap().getTargetDecoyResults().getScoreLimit();
        for (ProteinMatch proteinMatch : identification.getProteinIdentification().values()) {
            psParameter = (PSParameter) proteinMatch.getUrParam(psParameter);
            if (psParameter.getProteinProbabilityScore() <= proteinThreshold) {
                psParameter.setValidated(true);
            } else {
                psParameter.setValidated(false);
            }
        }

        double peptideThreshold;
        for (PeptideMatch peptideMatch : identification.getPeptideIdentification().values()) {
            peptideThreshold = peptideMap.getTargetDecoyMap(peptideMap.getCorrectedKey(peptideMatch)).getTargetDecoyResults().getScoreLimit();
            psParameter = (PSParameter) peptideMatch.getUrParam(psParameter);
            if (psParameter.getPeptideProbabilityScore() <= peptideThreshold) {
                psParameter.setValidated(true);
            } else {
                psParameter.setValidated(false);
            }
        }

        double psmThreshold;
        for (SpectrumMatch spectrumMatch : identification.getSpectrumIdentification().values()) {
            psmThreshold = psmMap.getTargetDecoyMap(psmMap.getCorrectedKey(spectrumMatch)).getTargetDecoyResults().getScoreLimit();
            psParameter = (PSParameter) spectrumMatch.getUrParam(psParameter);
            if (psParameter.getPsmProbabilityScore() <= psmThreshold) {
                psParameter.setValidated(true);
            } else {
                psParameter.setValidated(false);
            }
        }
    }

    /**
     * When two different sequences result in the same score for a given search engine, this method will retain the peptide belonging to the protein leading to the most spectra.
     * This method is typically useful for Isoleucine/Leucine issues.
     */
    private void setFirstHit() {
        Identification identification = experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber).getIdentification(IdentificationMethod.MS2_IDENTIFICATION);
        ArrayList<String> conflictingPSMs = new ArrayList<String>();
        HashMap<String, Integer> spectrumCounting = new HashMap<String, Integer>();
        boolean conflict;
        String accession;
        for (SpectrumMatch spectrumMatch : identification.getSpectrumIdentification().values()) {
            conflict = false;
            for (int se : spectrumMatch.getAdvocates()) {
                for (PeptideAssumption peptideAssumption : spectrumMatch.getAllAssumptions(se).get(spectrumMatch.getFirstHit(se).getEValue())) {
                    if (!peptideAssumption.getPeptide().getSequence().equals(spectrumMatch.getFirstHit(se).getPeptide().getSequence())) {
                        conflict = true;
                    }
                    for (Protein protein : peptideAssumption.getPeptide().getParentProteins()) {
                        accession = protein.getAccession();
                        if (!spectrumCounting.containsKey(accession)) {
                            spectrumCounting.put(accession, 0);
                        }
                        spectrumCounting.put(accession, spectrumCounting.get(accession) + 1);
                    }
                }
            }
            if (conflict) {
                conflictingPSMs.add(spectrumMatch.getKey());
            }
        }
        SpectrumMatch conflictingPSM;
        PeptideAssumption bestAssumption;
        int maxCount;
        for (String conflictKey : conflictingPSMs) {
            conflictingPSM = identification.getSpectrumIdentification().get(conflictKey);
            maxCount = 0;
            for (int se : conflictingPSM.getAdvocates()) {
                bestAssumption = conflictingPSM.getFirstHit(se);
                for (Protein protein : bestAssumption.getPeptide().getParentProteins()) {
                    if (spectrumCounting.get(protein.getAccession()) > maxCount) {
                        maxCount = spectrumCounting.get(protein.getAccession());
                    }
                }
                for (PeptideAssumption peptideAssumption : conflictingPSM.getAllAssumptions(se).get(conflictingPSM.getFirstHit(se).getEValue())) {
                    if (!peptideAssumption.getPeptide().getSequence().equals(conflictingPSM.getFirstHit(se).getPeptide().getSequence())) {
                        for (Protein protein : peptideAssumption.getPeptide().getParentProteins()) {
                            if (spectrumCounting.get(protein.getAccession()) > maxCount) {
                                bestAssumption = peptideAssumption;
                                maxCount = spectrumCounting.get(protein.getAccession());
                            }
                        }
                        conflictingPSM.setFirstHit(se, bestAssumption);
                    }
                }
            }
        }
    }

    /**
     * Fills the psm specific map
     *
     * @param inputMap       The input map
     */
    private void fillPsmMap(InputMap inputMap) {
        Identification identification = experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber).getIdentification(IdentificationMethod.MS2_IDENTIFICATION);
        HashMap<String, Double> identifications;
        HashMap<Double, PeptideAssumption> peptideAssumptions;
        PSParameter psParameter;
        PeptideAssumption peptideAssumption;
        if (inputMap.isMultipleSearchEngines()) {
            for (SpectrumMatch spectrumMatch : identification.getSpectrumIdentification().values()) {
                psParameter = new PSParameter();
                identifications = new HashMap<String, Double>();
                peptideAssumptions = new HashMap<Double, PeptideAssumption>();
                String id;
                double p, pScore = 1;
                for (int searchEngine : spectrumMatch.getAdvocates()) {
                    peptideAssumption = spectrumMatch.getFirstHit(searchEngine);
                    psParameter = (PSParameter) peptideAssumption.getUrParam(psParameter);
                    p = psParameter.getSearchEngineProbability();
                    pScore = pScore * p;
                    id = peptideAssumption.getPeptide().getKey();
                    if (identifications.containsKey(id)) {
                        p = identifications.get(id) * p;
                        identifications.put(id, p);
                        peptideAssumptions.put(p, peptideAssumption);
                    } else {
                        identifications.put(id, p);
                        peptideAssumptions.put(p, peptideAssumption);
                    }
                }
                double pMin = Collections.min(identifications.values());
                psParameter.setSpectrumProbabilityScore(pScore);
                spectrumMatch.addUrParam(psParameter);
                spectrumMatch.setBestAssumption(peptideAssumptions.get(pMin));
                psmMap.addPoint(pScore, spectrumMatch);
            }
        } else {
            double eValue;
            for (SpectrumMatch spectrumMatch : identification.getSpectrumIdentification().values()) {
                psParameter = new PSParameter();
                for (int searchEngine : spectrumMatch.getAdvocates()) {
                    peptideAssumption = spectrumMatch.getFirstHit(searchEngine);
                    eValue = peptideAssumption.getEValue();
                    psParameter.setSpectrumProbabilityScore(eValue);
                    spectrumMatch.setBestAssumption(peptideAssumption);
                    psmMap.addPoint(eValue, spectrumMatch);
                }
                spectrumMatch.addUrParam(psParameter);
            }
        }
    }

    /**
     * Attaches the spectrum posterior error probabilities to the peptide assumptions
     */
    private void attachAssumptionsProbabilities(InputMap inputMap) {
        Identification identification = experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber).getIdentification(IdentificationMethod.MS2_IDENTIFICATION);
        PSParameter psParameter = new PSParameter();
        ArrayList<Double> eValues;
        double previousP, newP;
        for (SpectrumMatch spectrumMatch : identification.getSpectrumIdentification().values()) {
            for (int searchEngine : spectrumMatch.getAdvocates()) {
                eValues = new ArrayList<Double>(spectrumMatch.getAllAssumptions(searchEngine).keySet());
                Collections.sort(eValues);
                previousP = 0;
                for (double eValue : eValues) {
                    for (PeptideAssumption peptideAssumption : spectrumMatch.getAllAssumptions(searchEngine).get(eValue)) {
                        psParameter = new PSParameter();
                        newP = inputMap.getProbability(searchEngine, eValue);
                        if (newP > previousP) {
                            psParameter.setSearchEngineProbability(newP);
                            previousP = newP;
                        } else {
                            psParameter.setSearchEngineProbability(previousP);
                        }
                        peptideAssumption.addUrParam(psParameter);
                    }
                }
            }
        }
    }

    /**
     * Attaches the spectrum posterior error probabilities to the spectrum matches
     */
    private void attachSpectrumProbabilities() {
        Identification identification = experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber).getIdentification(IdentificationMethod.MS2_IDENTIFICATION);
        PSParameter psParameter = new PSParameter();
        for (SpectrumMatch spectrumMatch : identification.getSpectrumIdentification().values()) {
            psParameter = (PSParameter) spectrumMatch.getUrParam(psParameter);
            psParameter.setPsmProbability(psmMap.getProbability(spectrumMatch, psParameter.getPsmProbabilityScore()));
        }
    }

    /**
     * Build peptide and protein objects
     */
    private void buildPeptidesAndProteins() {
        Identification identification = experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber).getIdentification(IdentificationMethod.MS2_IDENTIFICATION);
        identification.buildPeptidesAndProteins();
    }

    /**
     * Attaches scores to possible PTM locations to spectrum matches 
     */
    public void scorePSMPTMs() {
        Identification identification = experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber).getIdentification(IdentificationMethod.MS2_IDENTIFICATION);
        for (SpectrumMatch spectrumMatch : identification.getSpectrumIdentification().values()) {
            scorePTMs(spectrumMatch);
        }
    }

    /**
     * Attaches scores to possible PTM locations to peptide matches
     */
    public void scorePeptidePTMs() {
        Identification identification = experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber).getIdentification(IdentificationMethod.MS2_IDENTIFICATION);
        for (PeptideMatch peptideMatch : identification.getPeptideIdentification().values()) {
            scorePTMs(peptideMatch);
        }
    }

    /**
     * Scores the PTMs for a peptide match
     */
    public void scorePTMs(PeptideMatch peptideMatch) {
        PSPtmScores psmScores, peptideScores = new PSPtmScores();
        PtmScoring spectrumScoring;
        ArrayList<String> variableModifications = new ArrayList<String>();
        for (ModificationMatch modificationMatch : peptideMatch.getTheoreticPeptide().getModificationMatches()) {
            if (modificationMatch.isVariable()
                    && modificationMatch.getTheoreticPtm().getType() == PTM.MODAA
                    && !variableModifications.contains(modificationMatch.getTheoreticPtm().getName())) {
                variableModifications.add(modificationMatch.getTheoreticPtm().getName());
            }
        }
        if (variableModifications.size() > 0) {
            for (SpectrumMatch spectrumMatch : peptideMatch.getSpectrumMatches().values()) {
                for (String modification : variableModifications) {
                    if (!peptideScores.containsPtm(modification)) {
                        peptideScores.addPtmScoring(modification, new PtmScoring(modification));
                    }
                    psmScores = (PSPtmScores) spectrumMatch.getUrParam(peptideScores);
                    if (psmScores != null) {
                        spectrumScoring = psmScores.getPtmScoring(modification);
                        if (spectrumScoring != null) {
                            peptideScores.getPtmScoring(modification).addAll(spectrumScoring);
                        }
                    }
                }
            }
            peptideMatch.addUrParam(peptideScores);
        }
    }

    /**
     * Scores PTM locations for a desired spectrumMatch
     * 
     * @param spectrumMatch The spectrum match of interest
     */
    public void scorePTMs(SpectrumMatch spectrumMatch) {

        // Estimate delta score
        PSParameter psParameter = new PSParameter();
        double p1, p2;
        String mainSequence, modificationName;
        ArrayList<String> modifications;
        HashMap<String, ArrayList<Integer>> modificationProfiles = new HashMap<String, ArrayList<Integer>>();
        PSPtmScores ptmScores;
        PtmScoring ptmScoring;
        ptmScores = new PSPtmScores();
        psParameter = (PSParameter) spectrumMatch.getBestAssumption().getUrParam(psParameter);
        p1 = psParameter.getSearchEngineConfidence();
        if (p1 < 1) {
            mainSequence = spectrumMatch.getBestAssumption().getPeptide().getSequence();
            p2 = 1;
            modifications = new ArrayList<String>();
            for (ModificationMatch modificationMatch : spectrumMatch.getBestAssumption().getPeptide().getModificationMatches()) {
                if (modificationMatch.isVariable()
                        && modificationMatch.getTheoreticPtm().getType() == PTM.MODAA) {
                    modificationName = modificationMatch.getTheoreticPtm().getName();
                    if (!modifications.contains(modificationName)) {
                        modifications.add(modificationName);
                        modificationProfiles.put(modificationName, new ArrayList<Integer>());
                    }
                    modificationProfiles.get(modificationName).add(modificationMatch.getModificationSite());
                }
            }
            if (modifications.size() > 0) {
                for (PeptideAssumption peptideAssumption : spectrumMatch.getAllAssumptions()) {
                    if (peptideAssumption.getRank() > 1 && peptideAssumption.getPeptide().getSequence().equals(mainSequence)) {
                        psParameter = (PSParameter) peptideAssumption.getUrParam(psParameter);
                        if (psParameter.getSearchEngineProbability() < p2) {
                            p2 = psParameter.getSearchEngineProbability();
                        }
                    }
                }
                for (String mod : modifications) {
                    ptmScoring = new PtmScoring(mod);
                    ptmScoring.addDeltaScore(modificationProfiles.get(mod), (p2 - p1) * 100);
                    ptmScores.addPtmScoring(mod, ptmScoring);
                }
                spectrumMatch.addUrParam(ptmScores);
            }
        }

        //@TODO: estimate A-score


    }

    /**
     * Fills the peptide specific map
     */
    private void fillPeptideMaps() {
        Identification identification = experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber).getIdentification(IdentificationMethod.MS2_IDENTIFICATION);
        double probaScore;
        PSParameter psParameter = new PSParameter();
        for (PeptideMatch peptideMatch : identification.getPeptideIdentification().values()) {
            probaScore = 1;
            for (SpectrumMatch spectrumMatch : peptideMatch.getSpectrumMatches().values()) {
                if (spectrumMatch.getBestAssumption().getPeptide().isSameAs(peptideMatch.getTheoreticPeptide())) {
                    psParameter = (PSParameter) spectrumMatch.getUrParam(psParameter);
                    probaScore = probaScore * psParameter.getPsmProbability();
                }
            }
            psParameter = new PSParameter();
            psParameter.setPeptideProbabilityScore(probaScore);
            peptideMatch.addUrParam(psParameter);
            peptideMap.addPoint(probaScore, peptideMatch);
        }
    }

    /**
     * Attaches the peptide posterior error probabilities to the peptide matches
     */
    private void attachPeptideProbabilities() {
        Identification identification = experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber).getIdentification(IdentificationMethod.MS2_IDENTIFICATION);
        PSParameter psParameter = new PSParameter();
        for (PeptideMatch peptideMatch : identification.getPeptideIdentification().values()) {
            psParameter = (PSParameter) peptideMatch.getUrParam(psParameter);
            psParameter.setPeptideProbability(peptideMap.getProbability(peptideMatch, psParameter.getPeptideProbabilityScore()));
        }
    }

    /**
     * Fills the protein map
     */
    private void fillProteinMap() {
        Identification identification = experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber).getIdentification(IdentificationMethod.MS2_IDENTIFICATION);
        double probaScore;
        PSParameter psParameter = new PSParameter();
        for (ProteinMatch proteinMatch : identification.getProteinIdentification().values()) {
            probaScore = 1;
            for (PeptideMatch peptideMatch : proteinMatch.getPeptideMatches().values()) {
                psParameter = (PSParameter) peptideMatch.getUrParam(psParameter);
                probaScore = probaScore * psParameter.getPeptideProbability();
            }
            psParameter = (PSParameter) proteinMatch.getUrParam(psParameter);
            if (psParameter == null) {
                psParameter = new PSParameter();
            }
            psParameter.setProteinProbabilityScore(probaScore);
            proteinMatch.addUrParam(psParameter);
            proteinMap.addPoint(probaScore, proteinMatch.isDecoy());
        }
    }

    /**
     * Attaches the protein posterior error probability to the protein matches
     */
    private void attachProteinProbabilities() {
        Identification identification = experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber).getIdentification(IdentificationMethod.MS2_IDENTIFICATION);
        PSParameter psParameter = new PSParameter();
        double proteinProbability;
        for (ProteinMatch proteinMatch : identification.getProteinIdentification().values()) {
            psParameter = (PSParameter) proteinMatch.getUrParam(psParameter);
            proteinProbability = proteinMap.getProbability(psParameter.getProteinProbabilityScore());
            psParameter.setProteinProbability(proteinProbability);
        }
    }

    /**
     * Solves protein inference issues when possible.
     * @throws Exception    exception thrown whenever it is attempted to attach two different spectrum matches to the same spectrum from the same search engine.
     */
    private void cleanProteinGroups(WaitingDialog waitingDialog) throws Exception {
        Identification identification = experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber).getIdentification(IdentificationMethod.MS2_IDENTIFICATION);
        PSParameter psParameter = new PSParameter();
        boolean better;
        ProteinMatch proteinShared;
        double sharedProteinProbabilityScore, uniqueProteinProbabilityScore;
        ArrayList<String> toRemove = new ArrayList<String>();
        for (String proteinSharedKey : identification.getProteinIdentification().keySet()) {
            proteinShared = identification.getProteinIdentification().get(proteinSharedKey);
            psParameter = (PSParameter) proteinShared.getUrParam(psParameter);
            sharedProteinProbabilityScore = psParameter.getProteinProbabilityScore();
            if (proteinShared.getNProteins() > 1 && sharedProteinProbabilityScore < 1) {
                better = false;
                for (ProteinMatch proteinUnique : identification.getProteinIdentification().values()) {
                    if (proteinShared.contains(proteinUnique)) {
                        psParameter = (PSParameter) proteinUnique.getUrParam(psParameter);
                        uniqueProteinProbabilityScore = psParameter.getProteinProbabilityScore();
                        for (PeptideMatch sharedPeptide : proteinShared.getPeptideMatches().values()) {
                            proteinUnique.addPeptideMatch(sharedPeptide);
                        }
                        if (uniqueProteinProbabilityScore <= sharedProteinProbabilityScore) {
                            better = true;
                        }
                    }
                }
                if (better) {
                    toRemove.add(proteinSharedKey);
                }
            }
        }
        for (String proteinKey : toRemove) {
            proteinShared = identification.getProteinIdentification().get(proteinKey);
            psParameter = (PSParameter) proteinShared.getUrParam(psParameter);
            proteinMap.removePoint(psParameter.getProteinProbabilityScore(), proteinShared.isDecoy());
            identification.getProteinIdentification().remove(proteinKey);
        }
        int nSolved = toRemove.size();
        int nLeft = 0;
        String mainKey = null;
        boolean similarityFound, allSimilar;
        ArrayList<String> primaryDescription, secondaryDescription, accessions;
        for (ProteinMatch proteinMatch : identification.getProteinIdentification().values()) {
            accessions = new ArrayList<String>(proteinMatch.getTheoreticProteinsAccessions());
            Collections.sort(accessions);
            for (String proteinKey : accessions) {
                mainKey = proteinKey;
                break;
            }
            if (proteinMatch.getNProteins() > 1) {
                similarityFound = false;
                allSimilar = false;
                psParameter = (PSParameter) proteinMatch.getUrParam(psParameter);
                ArrayList<String> primaryKeys = new ArrayList<String>(proteinMatch.getTheoreticProteinsAccessions());
                Collections.sort(primaryKeys);
                ArrayList<String> secondaryKeys = new ArrayList<String>(primaryKeys);
                for (int i = 0; i < primaryKeys.size() - 1; i++) {
                    primaryDescription = parseDescription(primaryKeys.get(i));
                    for (int j = i + 1; j < secondaryKeys.size(); j++) {
                        secondaryDescription = parseDescription(secondaryKeys.get(j));
                        if (getSimilarity(primaryDescription, secondaryDescription)) {
                            similarityFound = true;
                            mainKey = primaryKeys.get(i);
                            break;
                        }
                    }
                    if (similarityFound) {
                        break;
                    }
                }
                if (similarityFound) {
                    allSimilar = true;
                    for (String key : primaryKeys) {
                        if (!mainKey.equals(key)) {
                            primaryDescription = parseDescription(mainKey);
                            secondaryDescription = parseDescription(key);
                            if (!getSimilarity(primaryDescription, secondaryDescription)) {
                                allSimilar = false;
                                break;
                            }
                        }
                    }
                }
                if (!similarityFound) {
                    psParameter.setGroupClass(PSParameter.UNRELATED);
                    nLeft++;
                } else if (!allSimilar) {
                    psParameter.setGroupClass(PSParameter.ISOFORMS_UNRELATED);
                    nSolved++;
                } else {
                    psParameter.setGroupClass(PSParameter.ISOFORMS);
                    nSolved++;
                }
            }
            proteinMatch.setMainMatch(proteinMatch.getTheoreticProtein(mainKey));
        }
        if (waitingDialog != null) {
            waitingDialog.appendReport(nSolved + " conflicts resolved. " + nLeft + " protein groups remaining.");
        }
    }

    /**
     * Parses a protein description retaining only words longer than 3 characters
     * @param proteinAccession the accession of the inspected protein
     * @return description words longer than 3 characters
     */
    private ArrayList<String> parseDescription(String proteinAccession) {
        SequenceDataBase db = experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber).getSequenceDataBase();
        String description = db.getProteinHeader(proteinAccession).getDescription();
        ArrayList<String> result = new ArrayList<String>();
        for (String component : description.split(" ")) {
            if (component.length() > 3) {
                result.add(component);
            }
        }
        return result;
    }

    /**
     * Simplistic method comparing protein descriptions. Returns true if both descriptions are of same length and present more than half similar words.
     * @param primaryDescription    The parsed description of the first protein
     * @param secondaryDescription  The parsed description of the second protein
     * @return  a boolean indicating whether the descriptions are similar
     */
    private boolean getSimilarity(ArrayList<String> primaryDescription, ArrayList<String> secondaryDescription) {
        if (primaryDescription.size() == secondaryDescription.size()) {
            int nMatch = 0;
            for (int i = 0; i < primaryDescription.size(); i++) {
                if (primaryDescription.get(i).equals(secondaryDescription.get(i))) {
                    nMatch++;
                }
            }
            if (nMatch >= primaryDescription.size() / 2) {
                return true;
            }
        }
        return false;
    }
}
