package eu.isas.peptideshaker.gui.tabpanels;

import com.compomics.util.Util;
import com.compomics.util.examples.BareBonesBrowserLaunch;
import com.compomics.util.experiment.biology.PTM;
import com.compomics.util.experiment.biology.PTMFactory;
import com.compomics.util.experiment.biology.Peptide;
import com.compomics.util.experiment.identification.SequenceFactory;
import com.compomics.util.experiment.identification.matches.ModificationMatch;
import com.compomics.util.experiment.identification.matches.PeptideMatch;
import com.compomics.util.experiment.identification.matches.ProteinMatch;
import com.compomics.util.experiment.identification.matches.SpectrumMatch;
import com.compomics.util.gui.GuiUtilities;
import com.compomics.util.gui.waiting.waitinghandlers.ProgressDialogX;
import com.compomics.util.pdbfinder.FindPdbForUniprotAccessions;
import com.compomics.util.pdbfinder.pdb.PdbBlock;
import com.compomics.util.pdbfinder.pdb.PdbParameter;
import eu.isas.peptideshaker.export.OutputGenerator;
import eu.isas.peptideshaker.gui.ExportGraphicsDialog;
import eu.isas.peptideshaker.gui.HelpDialog;
import eu.isas.peptideshaker.gui.PeptideShakerGUI;
import eu.isas.peptideshaker.gui.ProteinInferenceDialog;
import eu.isas.peptideshaker.gui.ProteinInferencePeptideLevelDialog;
import eu.isas.peptideshaker.gui.tablemodels.ProteinTableModel;
import eu.isas.peptideshaker.myparameters.PSParameter;
import eu.isas.peptideshaker.preferences.SpectrumCountingPreferences.SpectralCountingMethod;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import no.uib.jsparklines.data.XYDataPoint;
import no.uib.jsparklines.extra.HtmlLinksRenderer;
import no.uib.jsparklines.extra.TrueFalseIconRenderer;
import no.uib.jsparklines.renderers.JSparklinesBarChartTableCellRenderer;
import no.uib.jsparklines.renderers.JSparklinesIntegerColorTableCellRenderer;
import no.uib.jsparklines.renderers.JSparklinesIntervalChartTableCellRenderer;
import no.uib.jsparklines.renderers.JSparklinesTwoValueBarChartTableCellRenderer;
import org.jfree.chart.plot.PlotOrientation;
import org.jmol.adapter.smarter.SmarterJmolAdapter;
import org.jmol.api.JmolAdapter;
import org.jmol.api.JmolViewer;

/**
 * The Protein Structures tab.
 *
 * @author Harald Barsnes
 */
public class ProteinStructurePanel extends javax.swing.JPanel {

    /**
     * Peptide keys that can be mapped to the current pdb file.
     */
    private ArrayList<String> peptidePdbArray;

    /**
     * Indexes for the three main data tables.
     */
    private enum TableIndex {

        PROTEIN_TABLE, PEPTIDE_TABLE, PDB_MATCHES, PDB_CHAINS
    };
    /**
     * If true, labels are shown for the modifications in the 3D structure.
     */
    private boolean showModificationLabels = true;
    /**
     * If true, the 3D model will be spinning.
     */
    private boolean spinModel = true;
    /**
     * If true the ribbon model is used.
     */
    private boolean ribbonModel = true;
    /**
     * If true the backbone model is used.
     */
    private boolean backboneModel = false;
    /**
     * The currently displayed PDB file.
     */
    private String currentlyDisplayedPdbFile;
    /**
     * A simple progress dialog.
     */
    private ProgressDialogX progressDialog;
    /**
     * The UniProt to PDB finder.
     */
    private FindPdbForUniprotAccessions uniProtPdb;
    /**
     * The PeptideShaker main frame.
     */
    private PeptideShakerGUI peptideShakerGUI;
    /**
     * The Jmol panel.
     */
    private JmolPanel jmolPanel;
    /**
     * The protein table column header tooltips.
     */
    private ArrayList<String> proteinTableToolTips;
    /**
     * The peptide table column header tooltips.
     */
    private ArrayList<String> peptideTableToolTips;
    /**
     * The pdb files table column header tooltips.
     */
    private ArrayList<String> pdbTableToolTips;
    /**
     * The pdb chains table column header tooltips.
     */
    private ArrayList<String> pdbChainsTableToolTips;
    /**
     * A mapping of the peptide table entries.
     */
    private HashMap<Integer, String> peptideTableMap = new HashMap<Integer, String>();
    /**
     * If true Jmol is currently displaying a structure.
     */
    private boolean jmolStructureShown = false;
    /**
     * The current PDB chains.
     */
    private PdbBlock[] chains;
    /**
     * The amino acid sequence of the current chain.
     */
    private String chainSequence;
    /**
     * The current protein sequence.
     */
    private String proteinSequence;
    /**
     * The sequence factory
     */
    private SequenceFactory sequenceFactory = SequenceFactory.getInstance();
    /**
     * A list of proteins in the protein table.
     */
    private ArrayList<String> proteinKeys = new ArrayList<String>();

    /**
     * Creates a new ProteinPanel.
     *
     * @param peptideShakerGUI the PeptideShaker main frame
     */
    public ProteinStructurePanel(PeptideShakerGUI peptideShakerGUI) {
        initComponents();
        this.peptideShakerGUI = peptideShakerGUI;

        jmolPanel = new JmolPanel();
        pdbPanel.add(jmolPanel);

        setUpTableHeaderToolTips();
        setTableProperties();

        proteinScrollPane.getViewport().setOpaque(false);
        peptideScrollPane.getViewport().setOpaque(false);
        pdbJScrollPane.getViewport().setOpaque(false);
        pdbChainsJScrollPane.getViewport().setOpaque(false);
    }

    /**
     * Set up the table header tooltips.
     */
    private void setUpTableHeaderToolTips() {
        proteinTableToolTips = new ArrayList<String>();
        proteinTableToolTips.add(null);
        proteinTableToolTips.add("Starred");
        proteinTableToolTips.add("Protein Inference Class");
        proteinTableToolTips.add("Protein Accession Number");
        proteinTableToolTips.add("Protein Description");
        proteinTableToolTips.add("Protein Seqeunce Coverage (%) (Observed / Possible)");
        proteinTableToolTips.add("Number of Peptides (Validated / Total)");
        proteinTableToolTips.add("Number of Spectra (Validated / Total)");
        proteinTableToolTips.add("MS2 Quantification");
        proteinTableToolTips.add("Protein Molecular Weight (kDa)");

        if (peptideShakerGUI.getDisplayPreferences().showScores()) {
            proteinTableToolTips.add("Protein Score");
        } else {
            proteinTableToolTips.add("Protein Confidence");
        }

        proteinTableToolTips.add("Validated");

        peptideTableToolTips = new ArrayList<String>();
        peptideTableToolTips.add(null);
        peptideTableToolTips.add("Starred");
        peptideTableToolTips.add("Protein Inference");
        peptideTableToolTips.add("Peptide Sequence");
        peptideTableToolTips.add("Peptide Start Index");
        peptideTableToolTips.add("In PDB Sequence");
        peptideTableToolTips.add("Validated");

        pdbTableToolTips = new ArrayList<String>();
        pdbTableToolTips.add(null);
        pdbTableToolTips.add("PDB Accession Number");
        pdbTableToolTips.add("PDB Title");
        pdbTableToolTips.add("Type of Structure");
        pdbTableToolTips.add("Number of Chains");

        pdbChainsTableToolTips = new ArrayList<String>();
        pdbChainsTableToolTips.add(null);
        pdbChainsTableToolTips.add("Chain Label");
        pdbChainsTableToolTips.add("Protein-PDB Alignment");
        pdbChainsTableToolTips.add("Protein Coverage for PDB Sequence");

        // correct the color for the upper right corner
        JPanel proteinCorner = new JPanel();
        proteinCorner.setBackground(proteinTable.getTableHeader().getBackground());
        proteinScrollPane.setCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER, proteinCorner);
        JPanel peptideCorner = new JPanel();
        peptideCorner.setBackground(peptideTable.getTableHeader().getBackground());
        peptideScrollPane.setCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER, peptideCorner);
        JPanel pdbMatchesCorner = new JPanel();
        pdbMatchesCorner.setBackground(pdbMatchesJTable.getTableHeader().getBackground());
        pdbJScrollPane.setCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER, pdbMatchesCorner);
        JPanel pdbChainsCorner = new JPanel();
        pdbChainsCorner.setBackground(pdbChainsJTable.getTableHeader().getBackground());
        pdbChainsJScrollPane.setCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER, pdbChainsCorner);
    }

    /**
     * Set up the properties of the tables.
     */
    private void setTableProperties() {
        setProteinTableProperties();
        setPeptideTableProperties();
        setPdbTablesProperties();
    }

    /**
     * Set up the properties of the protein table.
     */
    private void setProteinTableProperties() {

        proteinTable.getColumn(" ").setMaxWidth(50);
        proteinTable.getColumn(" ").setMinWidth(50);

        // the validated column
        proteinTable.getColumn("").setMaxWidth(30);
        proteinTable.getColumn("").setMinWidth(30);

        // the selected columns
        proteinTable.getColumn("  ").setMaxWidth(30);
        proteinTable.getColumn("  ").setMinWidth(30);

        // the protein inference column
        proteinTable.getColumn("PI").setMaxWidth(37);
        proteinTable.getColumn("PI").setMinWidth(37);

        try {
            proteinTable.getColumn("Confidence").setMaxWidth(90);
            proteinTable.getColumn("Confidence").setMinWidth(90);
        } catch (IllegalArgumentException w) {
            proteinTable.getColumn("Score").setMaxWidth(90);
            proteinTable.getColumn("Score").setMinWidth(90);
        }

        proteinTable.getTableHeader().setReorderingAllowed(false);

        proteinTable.setAutoCreateRowSorter(true);

        // make sure that the user is made aware that the tool is doing something during the sorting of the protein table
        proteinTable.getRowSorter().addRowSorterListener(new RowSorterListener() {

            @Override
            public void sorterChanged(RowSorterEvent e) {

                if (e.getType() == RowSorterEvent.Type.SORT_ORDER_CHANGED) {
                    peptideShakerGUI.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
                    proteinTable.getTableHeader().setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));

                    // change the peptide shaker icon to a "waiting version"
                    peptideShakerGUI.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker-orange.gif")));
                } else if (e.getType() == RowSorterEvent.Type.SORTED) {
                    peptideShakerGUI.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                    proteinTable.getTableHeader().setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

                    // change the peptide shaker icon to a "waiting version"
                    peptideShakerGUI.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker.gif")));
                }
            }
        });

        // set up the protein inference color map
        HashMap<Integer, Color> proteinInferenceColorMap = new HashMap<Integer, Color>();
        proteinInferenceColorMap.put(PSParameter.NOT_GROUP, peptideShakerGUI.getSparklineColor()); // NOT_GROUP
        proteinInferenceColorMap.put(PSParameter.ISOFORMS, Color.YELLOW); // ISOFORMS
        proteinInferenceColorMap.put(PSParameter.ISOFORMS_UNRELATED, Color.ORANGE); // ISOFORMS_UNRELATED
        proteinInferenceColorMap.put(PSParameter.UNRELATED, Color.RED); // UNRELATED

        // set up the protein inference tooltip map
        HashMap<Integer, String> proteinInferenceTooltipMap = new HashMap<Integer, String>();
        proteinInferenceTooltipMap.put(PSParameter.NOT_GROUP, "Single Protein");
        proteinInferenceTooltipMap.put(PSParameter.ISOFORMS, "Isoforms");
        proteinInferenceTooltipMap.put(PSParameter.ISOFORMS_UNRELATED, "Unrelated Isoforms");
        proteinInferenceTooltipMap.put(PSParameter.UNRELATED, "Unrelated Proteins");

        proteinTable.getColumn("Accession").setCellRenderer(new HtmlLinksRenderer(peptideShakerGUI.getSelectedRowHtmlTagFontColor(), peptideShakerGUI.getNotSelectedRowHtmlTagFontColor()));
        proteinTable.getColumn("PI").setCellRenderer(new JSparklinesIntegerColorTableCellRenderer(peptideShakerGUI.getSparklineColor(), proteinInferenceColorMap, proteinInferenceTooltipMap));
        proteinTable.getColumn("#Peptides").setCellRenderer(new JSparklinesTwoValueBarChartTableCellRenderer(PlotOrientation.HORIZONTAL, 100.0,
                peptideShakerGUI.getSparklineColor(), peptideShakerGUI.getSparklineColorNonValidated(), false));
        ((JSparklinesTwoValueBarChartTableCellRenderer) proteinTable.getColumn("#Peptides").getCellRenderer()).showNumberAndChart(true, peptideShakerGUI.getLabelWidth(), new DecimalFormat("0"));
        proteinTable.getColumn("#Spectra").setCellRenderer(new JSparklinesTwoValueBarChartTableCellRenderer(PlotOrientation.HORIZONTAL, 100.0,
                peptideShakerGUI.getSparklineColor(), peptideShakerGUI.getSparklineColorNonValidated(), false));
        ((JSparklinesTwoValueBarChartTableCellRenderer) proteinTable.getColumn("#Spectra").getCellRenderer()).showNumberAndChart(true, peptideShakerGUI.getLabelWidth(), new DecimalFormat("0"));
        proteinTable.getColumn("MS2 Quant.").setCellRenderer(new JSparklinesBarChartTableCellRenderer(PlotOrientation.HORIZONTAL, 10.0, peptideShakerGUI.getSparklineColor()));
        ((JSparklinesBarChartTableCellRenderer) proteinTable.getColumn("MS2 Quant.").getCellRenderer()).showNumberAndChart(true, peptideShakerGUI.getLabelWidth());
        proteinTable.getColumn("MW").setCellRenderer(new JSparklinesBarChartTableCellRenderer(PlotOrientation.HORIZONTAL, 10.0, peptideShakerGUI.getSparklineColor()));
        ((JSparklinesBarChartTableCellRenderer) proteinTable.getColumn("MW").getCellRenderer()).showNumberAndChart(true, peptideShakerGUI.getLabelWidth());

        try {
            proteinTable.getColumn("Confidence").setCellRenderer(new JSparklinesBarChartTableCellRenderer(PlotOrientation.HORIZONTAL, 100.0, peptideShakerGUI.getSparklineColor()));
            ((JSparklinesBarChartTableCellRenderer) proteinTable.getColumn("Confidence").getCellRenderer()).showNumberAndChart(
                    true, peptideShakerGUI.getLabelWidth() - 20, peptideShakerGUI.getScoreAndConfidenceDecimalFormat());
        } catch (IllegalArgumentException e) {
            proteinTable.getColumn("Score").setCellRenderer(new JSparklinesBarChartTableCellRenderer(PlotOrientation.HORIZONTAL, 100.0, peptideShakerGUI.getSparklineColor()));
            ((JSparklinesBarChartTableCellRenderer) proteinTable.getColumn("Score").getCellRenderer()).showNumberAndChart(
                    true, peptideShakerGUI.getLabelWidth() - 20, peptideShakerGUI.getScoreAndConfidenceDecimalFormat());
        }

        proteinTable.getColumn("Coverage").setCellRenderer(new JSparklinesTwoValueBarChartTableCellRenderer(PlotOrientation.HORIZONTAL, 100.0,
                peptideShakerGUI.getSparklineColor(), peptideShakerGUI.getUtilitiesUserPreferences().getSparklineColorNotFound(), true));
        ((JSparklinesTwoValueBarChartTableCellRenderer) proteinTable.getColumn("Coverage").getCellRenderer()).showNumberAndChart(true, peptideShakerGUI.getLabelWidth(), new DecimalFormat("0.00"));
        proteinTable.getColumn("").setCellRenderer(new TrueFalseIconRenderer(
                new ImageIcon(this.getClass().getResource("/icons/accept.png")),
                new ImageIcon(this.getClass().getResource("/icons/Error_3.png")),
                "Validated", "Not Validated"));
        proteinTable.getColumn("  ").setCellRenderer(new TrueFalseIconRenderer(
                new ImageIcon(this.getClass().getResource("/icons/star_yellow.png")),
                new ImageIcon(this.getClass().getResource("/icons/star_grey.png")),
                new ImageIcon(this.getClass().getResource("/icons/star_grey.png")),
                "Starred", null, null));

        // set the preferred size of the accession column
        Integer width = peptideShakerGUI.getPreferredAccessionColumnWidth(proteinTable, proteinTable.getColumn("Accession").getModelIndex(), 6);
        if (width != null) {
            proteinTable.getColumn("Accession").setMinWidth(width);
            proteinTable.getColumn("Accession").setMaxWidth(width);
        } else {
            proteinTable.getColumn("Accession").setMinWidth(15);
            proteinTable.getColumn("Accession").setMaxWidth(Integer.MAX_VALUE);
        }
    }

    /**
     * Set up the properties of the peptide table.
     */
    private void setPeptideTableProperties() {

        peptideTable.getColumn(" ").setMaxWidth(50);
        peptideTable.getColumn(" ").setMinWidth(50);

        peptideTable.getColumn("PDB").setMinWidth(50);
        peptideTable.getColumn("PDB").setMaxWidth(50);
        peptideTable.getColumn("Start").setMinWidth(50);

        // the validated column
        peptideTable.getColumn("").setMaxWidth(30);
        peptideTable.getColumn("").setMinWidth(30);

        // the selected columns
        peptideTable.getColumn("  ").setMaxWidth(30);
        peptideTable.getColumn("  ").setMinWidth(30);

        // the protein inference column
        peptideTable.getColumn("PI").setMaxWidth(37);
        peptideTable.getColumn("PI").setMinWidth(37);

        peptideTable.getTableHeader().setReorderingAllowed(false);

        peptideTable.setAutoCreateRowSorter(true);

        // make sure that the user is made aware that the tool is doing something during the sorting of the peptide table
        peptideTable.getRowSorter().addRowSorterListener(new RowSorterListener() {

            @Override
            public void sorterChanged(RowSorterEvent e) {

                if (e.getType() == RowSorterEvent.Type.SORT_ORDER_CHANGED) {
                    peptideShakerGUI.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
                    peptideTable.getTableHeader().setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));

                    // change the peptide shaker icon to a "waiting version"
                    peptideShakerGUI.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker-orange.gif")));
                } else if (e.getType() == RowSorterEvent.Type.SORTED) {
                    peptideShakerGUI.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                    peptideTable.getTableHeader().setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

                    // change the peptide shaker icon to a "waiting version"
                    peptideShakerGUI.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker.gif")));
                }
            }
        });

        // set up the peptide inference color map
        HashMap<Integer, Color> peptideInferenceColorMap = new HashMap<Integer, Color>();
        peptideInferenceColorMap.put(PSParameter.NOT_GROUP, peptideShakerGUI.getSparklineColor());
        peptideInferenceColorMap.put(PSParameter.ISOFORMS, Color.YELLOW);
        peptideInferenceColorMap.put(PSParameter.ISOFORMS_UNRELATED, Color.ORANGE);
        peptideInferenceColorMap.put(PSParameter.UNRELATED, Color.RED);

        // set up the peptide inference tooltip map
        HashMap<Integer, String> peptideInferenceTooltipMap = new HashMap<Integer, String>();
        peptideInferenceTooltipMap.put(0, "Unique to Protein/Protein Group");
        peptideInferenceTooltipMap.put(1, "Maps to 2 Proteins/Protein Groups");
        peptideInferenceTooltipMap.put(2, "Maps to 3-5 Proteins/Protein Groups");
        peptideInferenceTooltipMap.put(3, "Maps to >5 Proteins/Protein Groups");

        peptideTable.getColumn("PI").setCellRenderer(new JSparklinesIntegerColorTableCellRenderer(peptideShakerGUI.getSparklineColor(), peptideInferenceColorMap, peptideInferenceTooltipMap));
        peptideTable.getColumn("Start").setCellRenderer(new JSparklinesIntervalChartTableCellRenderer(PlotOrientation.HORIZONTAL, 100d, 100d, peptideShakerGUI.getSparklineColor()));
        peptideTable.getColumn("PDB").setCellRenderer(new TrueFalseIconRenderer(
                new ImageIcon(this.getClass().getResource("/icons/pdb.png")),
                null,
                "Mapped to PDB Structure", null));
        peptideTable.getColumn("").setCellRenderer(new TrueFalseIconRenderer(
                new ImageIcon(this.getClass().getResource("/icons/accept.png")),
                new ImageIcon(this.getClass().getResource("/icons/Error_3.png")),
                "Validated", "Not Validated"));
        peptideTable.getColumn("  ").setCellRenderer(new TrueFalseIconRenderer(
                new ImageIcon(this.getClass().getResource("/icons/star_yellow.png")),
                new ImageIcon(this.getClass().getResource("/icons/star_grey.png")),
                new ImageIcon(this.getClass().getResource("/icons/star_grey.png")),
                "Starred", null, null));
    }

    /**
     * Set up the properties of the pdb and pdb chains tables.
     */
    private void setPdbTablesProperties() {

        pdbMatchesJTable.getColumn(" ").setMaxWidth(50);
        pdbChainsJTable.getColumn(" ").setMaxWidth(50);
        pdbMatchesJTable.getColumn("PDB").setMaxWidth(50);
        pdbChainsJTable.getColumn("Chain").setMaxWidth(50);
        pdbMatchesJTable.getColumn(" ").setMinWidth(50);
        pdbChainsJTable.getColumn(" ").setMinWidth(50);
        pdbMatchesJTable.getColumn("PDB").setMinWidth(50);
        pdbChainsJTable.getColumn("Chain").setMinWidth(50);

        pdbMatchesJTable.getColumn("Chains").setMinWidth(100);
        pdbMatchesJTable.getColumn("Chains").setMaxWidth(100);

        pdbMatchesJTable.getTableHeader().setReorderingAllowed(false);
        pdbChainsJTable.getTableHeader().setReorderingAllowed(false);

        pdbChainsJTable.setAutoCreateRowSorter(true);
        pdbMatchesJTable.setAutoCreateRowSorter(true);

        pdbMatchesJTable.getColumn("PDB").setCellRenderer(new HtmlLinksRenderer(peptideShakerGUI.getSelectedRowHtmlTagFontColor(), peptideShakerGUI.getNotSelectedRowHtmlTagFontColor()));
        pdbMatchesJTable.getColumn("Chains").setCellRenderer(new JSparklinesBarChartTableCellRenderer(PlotOrientation.HORIZONTAL, 10.0, peptideShakerGUI.getSparklineColor()));
        ((JSparklinesBarChartTableCellRenderer) pdbMatchesJTable.getColumn("Chains").getCellRenderer()).showNumberAndChart(true, peptideShakerGUI.getLabelWidth());

        pdbChainsJTable.getColumn("Coverage").setCellRenderer(new JSparklinesBarChartTableCellRenderer(PlotOrientation.HORIZONTAL, 100.0, peptideShakerGUI.getSparklineColor()));
        ((JSparklinesBarChartTableCellRenderer) pdbChainsJTable.getColumn("Coverage").getCellRenderer()).showNumberAndChart(true, peptideShakerGUI.getLabelWidth());

        pdbChainsJTable.getColumn("PDB-Protein").setCellRenderer(new JSparklinesIntervalChartTableCellRenderer(PlotOrientation.HORIZONTAL, 100.0, 10.0, peptideShakerGUI.getSparklineColor()));
        ((JSparklinesIntervalChartTableCellRenderer) pdbChainsJTable.getColumn("PDB-Protein").getCellRenderer()).showReferenceLine(true, 0.02, Color.BLACK);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pdbStructureJPanel = new javax.swing.JPanel();
        pdbStructureLayeredPane = new javax.swing.JLayeredPane();
        pdbOuterPanel = new javax.swing.JPanel();
        pdbLayeredPane = new javax.swing.JLayeredPane();
        pdbPanel = new javax.swing.JPanel();
        labelsJButton = new javax.swing.JButton();
        ribbonJButton = new javax.swing.JButton();
        backboneJButton = new javax.swing.JButton();
        playJButton = new javax.swing.JButton();
        pdbStructureHelpJButton = new javax.swing.JButton();
        exportPdbStructureJButton = new javax.swing.JButton();
        contextMenuPdbStructureBackgroundPanel = new javax.swing.JPanel();
        proteinsJPanel = new javax.swing.JPanel();
        proteinsLayeredPane = new javax.swing.JLayeredPane();
        proteinsPanel = new javax.swing.JPanel();
        proteinScrollPane = new javax.swing.JScrollPane();
        proteinTable = new JTable() {
            protected JTableHeader createDefaultTableHeader() {
                return new JTableHeader(columnModel) {
                    public String getToolTipText(MouseEvent e) {
                        java.awt.Point p = e.getPoint();
                        int index = columnModel.getColumnIndexAtX(p.x);
                        int realIndex = columnModel.getColumn(index).getModelIndex();
                        String tip = (String) proteinTableToolTips.get(realIndex);
                        return tip;
                    }
                };
            }
        };
        proteinsHelpJButton = new javax.swing.JButton();
        exportProteinsJButton = new javax.swing.JButton();
        contextMenuProteinsBackgroundPanel = new javax.swing.JPanel();
        peptidesJPanel = new javax.swing.JPanel();
        peptidesLayeredPane = new javax.swing.JLayeredPane();
        peptidesPanel = new javax.swing.JPanel();
        peptideScrollPane = new javax.swing.JScrollPane();
        peptideTable = new JTable() {
            protected JTableHeader createDefaultTableHeader() {
                return new JTableHeader(columnModel) {
                    public String getToolTipText(MouseEvent e) {
                        java.awt.Point p = e.getPoint();
                        int index = columnModel.getColumnIndexAtX(p.x);
                        int realIndex = columnModel.getColumn(index).getModelIndex();
                        String tip = (String) peptideTableToolTips.get(realIndex);
                        return tip;
                    }
                };
            }
        };
        peptidesHelpJButton = new javax.swing.JButton();
        exportPeptidesJButton = new javax.swing.JButton();
        contextMenuPeptidesBackgroundPanel = new javax.swing.JPanel();
        pdbMatchesJPanel = new javax.swing.JPanel();
        pdbMatchesLayeredPane = new javax.swing.JLayeredPane();
        pdbMatchesPanel = new javax.swing.JPanel();
        pdbJScrollPane = new javax.swing.JScrollPane();
        pdbMatchesJTable = new JTable() {
            protected JTableHeader createDefaultTableHeader() {
                return new JTableHeader(columnModel) {
                    public String getToolTipText(MouseEvent e) {
                        java.awt.Point p = e.getPoint();
                        int index = columnModel.getColumnIndexAtX(p.x);
                        int realIndex = columnModel.getColumn(index).getModelIndex();
                        String tip = (String) pdbTableToolTips.get(realIndex);
                        return tip;
                    }
                };
            }
        };
        pdbMatchesHelpJButton = new javax.swing.JButton();
        exportPdbMatchesJButton = new javax.swing.JButton();
        contextMenuPdbMatchesBackgroundPanel = new javax.swing.JPanel();
        pdbChainsJPanel = new javax.swing.JPanel();
        pdbChainsLayeredPane = new javax.swing.JLayeredPane();
        pdbChainsPanel = new javax.swing.JPanel();
        pdbChainsJScrollPane = new javax.swing.JScrollPane();
        pdbChainsJTable = new JTable() {
            protected JTableHeader createDefaultTableHeader() {
                return new JTableHeader(columnModel) {
                    public String getToolTipText(MouseEvent e) {
                        java.awt.Point p = e.getPoint();
                        int index = columnModel.getColumnIndexAtX(p.x);
                        int realIndex = columnModel.getColumn(index).getModelIndex();
                        String tip = (String) pdbChainsTableToolTips.get(realIndex);
                        return tip;
                    }
                };
            }
        };
        pdbChainHelpJButton = new javax.swing.JButton();
        exportPdbChainsJButton = new javax.swing.JButton();
        contextMenuPdbChainsBackgroundPanel = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        pdbStructureJPanel.setOpaque(false);

        pdbOuterPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("PDB Structure"));
        pdbOuterPanel.setOpaque(false);

        pdbLayeredPane.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                pdbLayeredPaneComponentResized(evt);
            }
        });

        pdbPanel.setLayout(new javax.swing.BoxLayout(pdbPanel, javax.swing.BoxLayout.LINE_AXIS));
        pdbPanel.setBounds(0, 0, 435, 440);
        pdbLayeredPane.add(pdbPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        labelsJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/labels_selected.png"))); // NOI18N
        labelsJButton.setToolTipText("Hide Modification Labels");
        labelsJButton.setBorder(null);
        labelsJButton.setBorderPainted(false);
        labelsJButton.setContentAreaFilled(false);
        labelsJButton.setFocusable(false);
        labelsJButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        labelsJButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        labelsJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                labelsJButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                labelsJButtonMouseExited(evt);
            }
        });
        labelsJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                labelsJButtonActionPerformed(evt);
            }
        });
        labelsJButton.setBounds(0, 0, 25, 25);
        pdbLayeredPane.add(labelsJButton, javax.swing.JLayeredPane.POPUP_LAYER);

        ribbonJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/ribbon_selected.png"))); // NOI18N
        ribbonJButton.setToolTipText("Ribbon Model");
        ribbonJButton.setBorder(null);
        ribbonJButton.setBorderPainted(false);
        ribbonJButton.setContentAreaFilled(false);
        ribbonJButton.setFocusable(false);
        ribbonJButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ribbonJButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ribbonJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ribbonJButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ribbonJButtonMouseExited(evt);
            }
        });
        ribbonJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ribbonJButtonActionPerformed(evt);
            }
        });
        ribbonJButton.setBounds(0, 0, 25, 25);
        pdbLayeredPane.add(ribbonJButton, javax.swing.JLayeredPane.POPUP_LAYER);

        backboneJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/backbone.png"))); // NOI18N
        backboneJButton.setToolTipText("Backbone Model");
        backboneJButton.setBorder(null);
        backboneJButton.setBorderPainted(false);
        backboneJButton.setContentAreaFilled(false);
        backboneJButton.setFocusable(false);
        backboneJButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        backboneJButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        backboneJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backboneJButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backboneJButtonMouseExited(evt);
            }
        });
        backboneJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backboneJButtonActionPerformed(evt);
            }
        });
        backboneJButton.setBounds(0, 0, 25, 25);
        pdbLayeredPane.add(backboneJButton, javax.swing.JLayeredPane.POPUP_LAYER);

        playJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/pause.png"))); // NOI18N
        playJButton.setToolTipText("Stop Rotation");
        playJButton.setBorder(null);
        playJButton.setBorderPainted(false);
        playJButton.setContentAreaFilled(false);
        playJButton.setFocusable(false);
        playJButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        playJButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        playJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                playJButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                playJButtonMouseExited(evt);
            }
        });
        playJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playJButtonActionPerformed(evt);
            }
        });
        playJButton.setBounds(0, 0, 21, 21);
        pdbLayeredPane.add(playJButton, javax.swing.JLayeredPane.POPUP_LAYER);

        javax.swing.GroupLayout pdbOuterPanelLayout = new javax.swing.GroupLayout(pdbOuterPanel);
        pdbOuterPanel.setLayout(pdbOuterPanelLayout);
        pdbOuterPanelLayout.setHorizontalGroup(
            pdbOuterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 448, Short.MAX_VALUE)
            .addGroup(pdbOuterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pdbOuterPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pdbLayeredPane, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        pdbOuterPanelLayout.setVerticalGroup(
            pdbOuterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 463, Short.MAX_VALUE)
            .addGroup(pdbOuterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pdbOuterPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pdbLayeredPane, javax.swing.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        pdbOuterPanel.setBounds(0, 0, 460, 490);
        pdbStructureLayeredPane.add(pdbOuterPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        pdbStructureHelpJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/help_no_frame_grey.png"))); // NOI18N
        pdbStructureHelpJButton.setToolTipText("Help");
        pdbStructureHelpJButton.setBorder(null);
        pdbStructureHelpJButton.setBorderPainted(false);
        pdbStructureHelpJButton.setContentAreaFilled(false);
        pdbStructureHelpJButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/help_no_frame.png"))); // NOI18N
        pdbStructureHelpJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pdbStructureHelpJButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pdbStructureHelpJButtonMouseExited(evt);
            }
        });
        pdbStructureHelpJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pdbStructureHelpJButtonActionPerformed(evt);
            }
        });
        pdbStructureHelpJButton.setBounds(440, 0, 10, 19);
        pdbStructureLayeredPane.add(pdbStructureHelpJButton, javax.swing.JLayeredPane.POPUP_LAYER);

        exportPdbStructureJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/export_no_frame_grey.png"))); // NOI18N
        exportPdbStructureJButton.setToolTipText("Export");
        exportPdbStructureJButton.setBorder(null);
        exportPdbStructureJButton.setBorderPainted(false);
        exportPdbStructureJButton.setContentAreaFilled(false);
        exportPdbStructureJButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/export_no_frame_grey.png"))); // NOI18N
        exportPdbStructureJButton.setEnabled(false);
        exportPdbStructureJButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/export_no_frame.png"))); // NOI18N
        exportPdbStructureJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exportPdbStructureJButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exportPdbStructureJButtonMouseExited(evt);
            }
        });
        exportPdbStructureJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportPdbStructureJButtonActionPerformed(evt);
            }
        });
        exportPdbStructureJButton.setBounds(430, 0, 10, 19);
        pdbStructureLayeredPane.add(exportPdbStructureJButton, javax.swing.JLayeredPane.POPUP_LAYER);

        contextMenuPdbStructureBackgroundPanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout contextMenuPdbStructureBackgroundPanelLayout = new javax.swing.GroupLayout(contextMenuPdbStructureBackgroundPanel);
        contextMenuPdbStructureBackgroundPanel.setLayout(contextMenuPdbStructureBackgroundPanelLayout);
        contextMenuPdbStructureBackgroundPanelLayout.setHorizontalGroup(
            contextMenuPdbStructureBackgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        contextMenuPdbStructureBackgroundPanelLayout.setVerticalGroup(
            contextMenuPdbStructureBackgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 19, Short.MAX_VALUE)
        );

        contextMenuPdbStructureBackgroundPanel.setBounds(420, 0, 30, 19);
        pdbStructureLayeredPane.add(contextMenuPdbStructureBackgroundPanel, javax.swing.JLayeredPane.POPUP_LAYER);

        javax.swing.GroupLayout pdbStructureJPanelLayout = new javax.swing.GroupLayout(pdbStructureJPanel);
        pdbStructureJPanel.setLayout(pdbStructureJPanelLayout);
        pdbStructureJPanelLayout.setHorizontalGroup(
            pdbStructureJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pdbStructureLayeredPane, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE)
        );
        pdbStructureJPanelLayout.setVerticalGroup(
            pdbStructureJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pdbStructureLayeredPane, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
        );

        proteinsJPanel.setOpaque(false);

        proteinsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Proteins"));
        proteinsPanel.setOpaque(false);

        proteinScrollPane.setOpaque(false);

        proteinTable.setModel(new ProteinTableModel());
        proteinTable.setOpaque(false);
        proteinTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        proteinTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                proteinTableMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                proteinTableMouseReleased(evt);
            }
        });
        proteinTable.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                proteinTableMouseMoved(evt);
            }
        });
        proteinTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                proteinTableKeyReleased(evt);
            }
        });
        proteinScrollPane.setViewportView(proteinTable);

        javax.swing.GroupLayout proteinsPanelLayout = new javax.swing.GroupLayout(proteinsPanel);
        proteinsPanel.setLayout(proteinsPanelLayout);
        proteinsPanelLayout.setHorizontalGroup(
            proteinsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 938, Short.MAX_VALUE)
            .addGroup(proteinsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(proteinsPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(proteinScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 918, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        proteinsPanelLayout.setVerticalGroup(
            proteinsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 243, Short.MAX_VALUE)
            .addGroup(proteinsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(proteinsPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(proteinScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        proteinsPanel.setBounds(0, 0, 950, 270);
        proteinsLayeredPane.add(proteinsPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        proteinsHelpJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/help_no_frame_grey.png"))); // NOI18N
        proteinsHelpJButton.setToolTipText("Help");
        proteinsHelpJButton.setBorder(null);
        proteinsHelpJButton.setBorderPainted(false);
        proteinsHelpJButton.setContentAreaFilled(false);
        proteinsHelpJButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/help_no_frame.png"))); // NOI18N
        proteinsHelpJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                proteinsHelpJButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                proteinsHelpJButtonMouseExited(evt);
            }
        });
        proteinsHelpJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                proteinsHelpJButtonActionPerformed(evt);
            }
        });
        proteinsHelpJButton.setBounds(930, 0, 10, 19);
        proteinsLayeredPane.add(proteinsHelpJButton, javax.swing.JLayeredPane.POPUP_LAYER);

        exportProteinsJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/export_no_frame_grey.png"))); // NOI18N
        exportProteinsJButton.setToolTipText("Copy to File");
        exportProteinsJButton.setBorder(null);
        exportProteinsJButton.setBorderPainted(false);
        exportProteinsJButton.setContentAreaFilled(false);
        exportProteinsJButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/export_no_frame_grey.png"))); // NOI18N
        exportProteinsJButton.setEnabled(false);
        exportProteinsJButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/export_no_frame.png"))); // NOI18N
        exportProteinsJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exportProteinsJButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exportProteinsJButtonMouseExited(evt);
            }
        });
        exportProteinsJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportProteinsJButtonActionPerformed(evt);
            }
        });
        exportProteinsJButton.setBounds(920, 0, 10, 19);
        proteinsLayeredPane.add(exportProteinsJButton, javax.swing.JLayeredPane.POPUP_LAYER);

        contextMenuProteinsBackgroundPanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout contextMenuProteinsBackgroundPanelLayout = new javax.swing.GroupLayout(contextMenuProteinsBackgroundPanel);
        contextMenuProteinsBackgroundPanel.setLayout(contextMenuProteinsBackgroundPanelLayout);
        contextMenuProteinsBackgroundPanelLayout.setHorizontalGroup(
            contextMenuProteinsBackgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        contextMenuProteinsBackgroundPanelLayout.setVerticalGroup(
            contextMenuProteinsBackgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 19, Short.MAX_VALUE)
        );

        contextMenuProteinsBackgroundPanel.setBounds(920, 0, 30, 19);
        proteinsLayeredPane.add(contextMenuProteinsBackgroundPanel, javax.swing.JLayeredPane.POPUP_LAYER);

        javax.swing.GroupLayout proteinsJPanelLayout = new javax.swing.GroupLayout(proteinsJPanel);
        proteinsJPanel.setLayout(proteinsJPanelLayout);
        proteinsJPanelLayout.setHorizontalGroup(
            proteinsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(proteinsLayeredPane, javax.swing.GroupLayout.DEFAULT_SIZE, 957, Short.MAX_VALUE)
        );
        proteinsJPanelLayout.setVerticalGroup(
            proteinsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(proteinsLayeredPane, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
        );

        peptidesJPanel.setOpaque(false);

        peptidesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Peptides"));
        peptidesPanel.setOpaque(false);

        peptideScrollPane.setOpaque(false);

        peptideTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                " ", "  ", "PI", "Sequence", "Start", "PDB", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Boolean.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Boolean.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        peptideTable.setOpaque(false);
        peptideTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        peptideTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                peptideTableMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                peptideTableMouseReleased(evt);
            }
        });
        peptideTable.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                peptideTableMouseMoved(evt);
            }
        });
        peptideTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                peptideTableKeyReleased(evt);
            }
        });
        peptideScrollPane.setViewportView(peptideTable);

        javax.swing.GroupLayout peptidesPanelLayout = new javax.swing.GroupLayout(peptidesPanel);
        peptidesPanel.setLayout(peptidesPanelLayout);
        peptidesPanelLayout.setHorizontalGroup(
            peptidesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 468, Short.MAX_VALUE)
            .addGroup(peptidesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(peptidesPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(peptideScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        peptidesPanelLayout.setVerticalGroup(
            peptidesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 133, Short.MAX_VALUE)
            .addGroup(peptidesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(peptidesPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(peptideScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        peptidesPanel.setBounds(0, 0, 480, 160);
        peptidesLayeredPane.add(peptidesPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        peptidesHelpJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/help_no_frame_grey.png"))); // NOI18N
        peptidesHelpJButton.setToolTipText("Help");
        peptidesHelpJButton.setBorder(null);
        peptidesHelpJButton.setBorderPainted(false);
        peptidesHelpJButton.setContentAreaFilled(false);
        peptidesHelpJButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/help_no_frame.png"))); // NOI18N
        peptidesHelpJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                peptidesHelpJButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                peptidesHelpJButtonMouseExited(evt);
            }
        });
        peptidesHelpJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                peptidesHelpJButtonActionPerformed(evt);
            }
        });
        peptidesHelpJButton.setBounds(460, 0, 10, 19);
        peptidesLayeredPane.add(peptidesHelpJButton, javax.swing.JLayeredPane.POPUP_LAYER);

        exportPeptidesJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/export_no_frame_grey.png"))); // NOI18N
        exportPeptidesJButton.setToolTipText("Copy to File");
        exportPeptidesJButton.setBorder(null);
        exportPeptidesJButton.setBorderPainted(false);
        exportPeptidesJButton.setContentAreaFilled(false);
        exportPeptidesJButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/export_no_frame_grey.png"))); // NOI18N
        exportPeptidesJButton.setEnabled(false);
        exportPeptidesJButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/export_no_frame.png"))); // NOI18N
        exportPeptidesJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exportPeptidesJButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exportPeptidesJButtonMouseExited(evt);
            }
        });
        exportPeptidesJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportPeptidesJButtonActionPerformed(evt);
            }
        });
        exportPeptidesJButton.setBounds(450, 0, 10, 19);
        peptidesLayeredPane.add(exportPeptidesJButton, javax.swing.JLayeredPane.POPUP_LAYER);

        contextMenuPeptidesBackgroundPanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout contextMenuPeptidesBackgroundPanelLayout = new javax.swing.GroupLayout(contextMenuPeptidesBackgroundPanel);
        contextMenuPeptidesBackgroundPanel.setLayout(contextMenuPeptidesBackgroundPanelLayout);
        contextMenuPeptidesBackgroundPanelLayout.setHorizontalGroup(
            contextMenuPeptidesBackgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        contextMenuPeptidesBackgroundPanelLayout.setVerticalGroup(
            contextMenuPeptidesBackgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 19, Short.MAX_VALUE)
        );

        contextMenuPeptidesBackgroundPanel.setBounds(440, 0, 30, 19);
        peptidesLayeredPane.add(contextMenuPeptidesBackgroundPanel, javax.swing.JLayeredPane.POPUP_LAYER);

        javax.swing.GroupLayout peptidesJPanelLayout = new javax.swing.GroupLayout(peptidesJPanel);
        peptidesJPanel.setLayout(peptidesJPanelLayout);
        peptidesJPanelLayout.setHorizontalGroup(
            peptidesJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(peptidesLayeredPane, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
        );
        peptidesJPanelLayout.setVerticalGroup(
            peptidesJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(peptidesLayeredPane, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
        );

        pdbMatchesJPanel.setOpaque(false);

        pdbMatchesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("PDB Matches"));
        pdbMatchesPanel.setOpaque(false);

        pdbJScrollPane.setOpaque(false);

        pdbMatchesJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                " ", "PDB", "Title", "Type", "Chains"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        pdbMatchesJTable.setOpaque(false);
        pdbMatchesJTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        pdbMatchesJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pdbMatchesJTableMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pdbMatchesJTableMouseReleased(evt);
            }
        });
        pdbMatchesJTable.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                pdbMatchesJTableMouseMoved(evt);
            }
        });
        pdbMatchesJTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pdbMatchesJTableKeyReleased(evt);
            }
        });
        pdbJScrollPane.setViewportView(pdbMatchesJTable);

        javax.swing.GroupLayout pdbMatchesPanelLayout = new javax.swing.GroupLayout(pdbMatchesPanel);
        pdbMatchesPanel.setLayout(pdbMatchesPanelLayout);
        pdbMatchesPanelLayout.setHorizontalGroup(
            pdbMatchesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 468, Short.MAX_VALUE)
            .addGroup(pdbMatchesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pdbMatchesPanelLayout.createSequentialGroup()
                    .addGap(8, 8, 8)
                    .addComponent(pdbJScrollPane)
                    .addGap(8, 8, 8)))
        );
        pdbMatchesPanelLayout.setVerticalGroup(
            pdbMatchesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 123, Short.MAX_VALUE)
            .addGroup(pdbMatchesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pdbMatchesPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pdbJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        pdbMatchesPanel.setBounds(0, 0, 480, 150);
        pdbMatchesLayeredPane.add(pdbMatchesPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        pdbMatchesHelpJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/help_no_frame_grey.png"))); // NOI18N
        pdbMatchesHelpJButton.setToolTipText("Help");
        pdbMatchesHelpJButton.setBorder(null);
        pdbMatchesHelpJButton.setBorderPainted(false);
        pdbMatchesHelpJButton.setContentAreaFilled(false);
        pdbMatchesHelpJButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/help_no_frame.png"))); // NOI18N
        pdbMatchesHelpJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pdbMatchesHelpJButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pdbMatchesHelpJButtonMouseExited(evt);
            }
        });
        pdbMatchesHelpJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pdbMatchesHelpJButtonActionPerformed(evt);
            }
        });
        pdbMatchesHelpJButton.setBounds(460, 0, 10, 19);
        pdbMatchesLayeredPane.add(pdbMatchesHelpJButton, javax.swing.JLayeredPane.POPUP_LAYER);

        exportPdbMatchesJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/export_no_frame_grey.png"))); // NOI18N
        exportPdbMatchesJButton.setToolTipText("Copy to File");
        exportPdbMatchesJButton.setBorder(null);
        exportPdbMatchesJButton.setBorderPainted(false);
        exportPdbMatchesJButton.setContentAreaFilled(false);
        exportPdbMatchesJButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/export_no_frame_grey.png"))); // NOI18N
        exportPdbMatchesJButton.setEnabled(false);
        exportPdbMatchesJButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/export_no_frame.png"))); // NOI18N
        exportPdbMatchesJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exportPdbMatchesJButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exportPdbMatchesJButtonMouseExited(evt);
            }
        });
        exportPdbMatchesJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportPdbMatchesJButtonActionPerformed(evt);
            }
        });
        exportPdbMatchesJButton.setBounds(450, 0, 10, 19);
        pdbMatchesLayeredPane.add(exportPdbMatchesJButton, javax.swing.JLayeredPane.POPUP_LAYER);

        contextMenuPdbMatchesBackgroundPanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout contextMenuPdbMatchesBackgroundPanelLayout = new javax.swing.GroupLayout(contextMenuPdbMatchesBackgroundPanel);
        contextMenuPdbMatchesBackgroundPanel.setLayout(contextMenuPdbMatchesBackgroundPanelLayout);
        contextMenuPdbMatchesBackgroundPanelLayout.setHorizontalGroup(
            contextMenuPdbMatchesBackgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        contextMenuPdbMatchesBackgroundPanelLayout.setVerticalGroup(
            contextMenuPdbMatchesBackgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 19, Short.MAX_VALUE)
        );

        contextMenuPdbMatchesBackgroundPanel.setBounds(440, 0, 30, 19);
        pdbMatchesLayeredPane.add(contextMenuPdbMatchesBackgroundPanel, javax.swing.JLayeredPane.POPUP_LAYER);

        javax.swing.GroupLayout pdbMatchesJPanelLayout = new javax.swing.GroupLayout(pdbMatchesJPanel);
        pdbMatchesJPanel.setLayout(pdbMatchesJPanelLayout);
        pdbMatchesJPanelLayout.setHorizontalGroup(
            pdbMatchesJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pdbMatchesLayeredPane, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
        );
        pdbMatchesJPanelLayout.setVerticalGroup(
            pdbMatchesJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pdbMatchesLayeredPane, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
        );

        pdbChainsJPanel.setOpaque(false);

        pdbChainsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("PDB Chains"));
        pdbChainsPanel.setOpaque(false);

        pdbChainsJScrollPane.setOpaque(false);

        pdbChainsJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                " ", "Chain", "PDB-Protein", "Coverage"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Object.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        pdbChainsJTable.setOpaque(false);
        pdbChainsJTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        pdbChainsJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pdbChainsJTableMouseReleased(evt);
            }
        });
        pdbChainsJTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pdbChainsJTableKeyReleased(evt);
            }
        });
        pdbChainsJScrollPane.setViewportView(pdbChainsJTable);

        javax.swing.GroupLayout pdbChainsPanelLayout = new javax.swing.GroupLayout(pdbChainsPanel);
        pdbChainsPanel.setLayout(pdbChainsPanelLayout);
        pdbChainsPanelLayout.setHorizontalGroup(
            pdbChainsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 468, Short.MAX_VALUE)
            .addGroup(pdbChainsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pdbChainsPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pdbChainsJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        pdbChainsPanelLayout.setVerticalGroup(
            pdbChainsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 133, Short.MAX_VALUE)
            .addGroup(pdbChainsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pdbChainsPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pdbChainsJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        pdbChainsPanel.setBounds(0, 0, 480, 160);
        pdbChainsLayeredPane.add(pdbChainsPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        pdbChainHelpJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/help_no_frame_grey.png"))); // NOI18N
        pdbChainHelpJButton.setToolTipText("Help");
        pdbChainHelpJButton.setBorder(null);
        pdbChainHelpJButton.setBorderPainted(false);
        pdbChainHelpJButton.setContentAreaFilled(false);
        pdbChainHelpJButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/help_no_frame.png"))); // NOI18N
        pdbChainHelpJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pdbChainHelpJButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pdbChainHelpJButtonMouseExited(evt);
            }
        });
        pdbChainHelpJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pdbChainHelpJButtonActionPerformed(evt);
            }
        });
        pdbChainHelpJButton.setBounds(460, 0, 10, 19);
        pdbChainsLayeredPane.add(pdbChainHelpJButton, javax.swing.JLayeredPane.POPUP_LAYER);

        exportPdbChainsJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/export_no_frame_grey.png"))); // NOI18N
        exportPdbChainsJButton.setToolTipText("Copy to File");
        exportPdbChainsJButton.setBorder(null);
        exportPdbChainsJButton.setBorderPainted(false);
        exportPdbChainsJButton.setContentAreaFilled(false);
        exportPdbChainsJButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/export_no_frame_grey.png"))); // NOI18N
        exportPdbChainsJButton.setEnabled(false);
        exportPdbChainsJButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/export_no_frame.png"))); // NOI18N
        exportPdbChainsJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exportPdbChainsJButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exportPdbChainsJButtonMouseExited(evt);
            }
        });
        exportPdbChainsJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportPdbChainsJButtonActionPerformed(evt);
            }
        });
        exportPdbChainsJButton.setBounds(450, 0, 10, 19);
        pdbChainsLayeredPane.add(exportPdbChainsJButton, javax.swing.JLayeredPane.POPUP_LAYER);

        contextMenuPdbChainsBackgroundPanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout contextMenuPdbChainsBackgroundPanelLayout = new javax.swing.GroupLayout(contextMenuPdbChainsBackgroundPanel);
        contextMenuPdbChainsBackgroundPanel.setLayout(contextMenuPdbChainsBackgroundPanelLayout);
        contextMenuPdbChainsBackgroundPanelLayout.setHorizontalGroup(
            contextMenuPdbChainsBackgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        contextMenuPdbChainsBackgroundPanelLayout.setVerticalGroup(
            contextMenuPdbChainsBackgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 19, Short.MAX_VALUE)
        );

        contextMenuPdbChainsBackgroundPanel.setBounds(440, 0, 30, 19);
        pdbChainsLayeredPane.add(contextMenuPdbChainsBackgroundPanel, javax.swing.JLayeredPane.POPUP_LAYER);

        javax.swing.GroupLayout pdbChainsJPanelLayout = new javax.swing.GroupLayout(pdbChainsJPanel);
        pdbChainsJPanel.setLayout(pdbChainsJPanelLayout);
        pdbChainsJPanelLayout.setHorizontalGroup(
            pdbChainsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pdbChainsLayeredPane, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
        );
        pdbChainsJPanelLayout.setVerticalGroup(
            pdbChainsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pdbChainsLayeredPane, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pdbMatchesJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pdbChainsJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(peptidesJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pdbStructureJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(proteinsJPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(proteinsJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pdbMatchesJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pdbChainsJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(peptidesJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(pdbStructureJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pdbStructureJPanel.getAccessibleContext().setAccessibleName("Protein Details");
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Makes sure the cursor changes back to the default cursor when leaving the
     * protein accession number column.
     *
     * @param evt
     */
    private void proteinTableMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_proteinTableMouseExited
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_proteinTableMouseExited

    /**
     * Changes the cursor into a hand cursor if the table cell contains an html
     * link.
     *
     * @param evt
     */
    private void proteinTableMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_proteinTableMouseMoved
        int row = proteinTable.rowAtPoint(evt.getPoint());
        int column = proteinTable.columnAtPoint(evt.getPoint());

        proteinTable.setToolTipText(null);

        if (column == proteinTable.getColumn("Accession").getModelIndex() && proteinTable.getValueAt(row, column) != null) {

            String tempValue = (String) proteinTable.getValueAt(row, column);

            if (tempValue.lastIndexOf("<html>") != -1) {
                this.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            } else {
                this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            }
        } else if (column == proteinTable.getColumn("PI").getModelIndex() && proteinTable.getValueAt(row, column) != null) {
            this.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        } else if (column == proteinTable.getColumn("Description").getModelIndex() && proteinTable.getValueAt(row, column) != null) {
            if (GuiUtilities.getPreferredWidthOfCell(proteinTable, row, column) > proteinTable.getColumn("Description").getWidth()) {
                proteinTable.setToolTipText("" + proteinTable.getValueAt(row, column));
            }
            this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        } else {
            this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_proteinTableMouseMoved

    /**
     * Update the protein selection and the corresponding tables.
     *
     * @param evt
     */
    private void proteinTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_proteinTableKeyReleased
        proteinTableMouseReleased(null);
    }//GEN-LAST:event_proteinTableKeyReleased

    /**
     * Updates the PDB structure.
     *
     * @param evt
     */
    private void peptideTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_peptideTableKeyReleased
        if (evt == null || evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_DOWN
                || evt.getKeyCode() == KeyEvent.VK_PAGE_UP || evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
            peptideTableMouseReleased(null);
        }
    }//GEN-LAST:event_peptideTableKeyReleased

    /**
     * Update the PDB structure shown in the Jmol panel.
     *
     * @param evt
     */
    private void pdbMatchesJTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pdbMatchesJTableKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_DOWN
                || evt.getKeyCode() == KeyEvent.VK_PAGE_UP || evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
            pdbMatchesJTableMouseReleased(null);
        }
    }//GEN-LAST:event_pdbMatchesJTableKeyReleased

    /**
     * Update the protein selection and the corresponding tables.
     *
     * @param evt
     */
    private void proteinTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_proteinTableMouseReleased

        if (evt != null) {
            peptideShakerGUI.setSelectedItems(peptideShakerGUI.getSelectedProteinKey(), PeptideShakerGUI.NO_SELECTION, PeptideShakerGUI.NO_SELECTION);
        }

        int row = proteinTable.getSelectedRow();
        int proteinIndex = proteinTable.convertRowIndexToModel(row);
        int column = proteinTable.getSelectedColumn();

        if (evt == null || (evt.getButton() == MouseEvent.BUTTON1 && (proteinIndex != -1 && column != -1))) {

            if (proteinIndex != -1) {

                this.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));

                try {
                    // find and store the protein sequence for later use
                    String proteinKey = proteinKeys.get(proteinIndex);
                    ProteinMatch proteinMatch = peptideShakerGUI.getIdentification().getProteinMatch(proteinKey);
                    String proteinAccession = proteinMatch.getMainMatch();

                    try {
                        proteinSequence = sequenceFactory.getProtein(proteinAccession).getSequence();
                    } catch (Exception e) {
                        peptideShakerGUI.catchException(e);
                        proteinSequence = "";
                    }

                    // update the pdb file table
                    updatePdbTable(proteinKeys.get(proteinIndex));

                    // empty the jmol panel
                    if (jmolStructureShown) {
                        jmolPanel = new JmolPanel();
                        pdbPanel.removeAll();
                        pdbPanel.add(jmolPanel);
                        pdbPanel.revalidate();
                        pdbPanel.repaint();
                        jmolStructureShown = false;
                        currentlyDisplayedPdbFile = null;

                        ((TitledBorder) pdbOuterPanel.getBorder()).setTitle(PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING + "PDB Structure" + PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING);
                        pdbOuterPanel.repaint();
                    }

                    // update the peptide selection
                    updatedPeptideSelection(proteinIndex);


                    this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

                    // open protein link in web browser
                    if (column == proteinTable.getColumn("Accession").getModelIndex() && evt != null && evt.getButton() == MouseEvent.BUTTON1
                            && ((String) proteinTable.getValueAt(row, column)).lastIndexOf("<html>") != -1) {

                        String link = (String) proteinTable.getValueAt(row, column);
                        link = link.substring(link.indexOf("\"") + 1);
                        link = link.substring(0, link.indexOf("\""));

                        this.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
                        BareBonesBrowserLaunch.openURL(link);
                        this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                    }

                    // open the protein inference dialog
                    if (column == proteinTable.getColumn("PI").getModelIndex() && evt != null && evt.getButton() == MouseEvent.BUTTON1) {
                        new ProteinInferenceDialog(peptideShakerGUI, proteinKey, peptideShakerGUI.getIdentification());
                    }

                    if (column == proteinTable.getColumn("  ").getModelIndex()) {
                        String key = proteinKeys.get(proteinIndex);
                        if ((Boolean) proteinTable.getValueAt(row, column)) {
                            peptideShakerGUI.getStarHider().starProtein(key);
                        } else {
                            peptideShakerGUI.getStarHider().unStarProtein(key);
                        }
                    }
                } catch (Exception e) {
                    peptideShakerGUI.catchException(e);
                    this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                }
            }
        }
    }//GEN-LAST:event_proteinTableMouseReleased

    /**
     * Updates the PDB structure.
     *
     * @param evt
     */
    private void peptideTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_peptideTableMouseReleased

        setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));

        if (evt != null) {
            peptideShakerGUI.setSelectedItems(peptideShakerGUI.getSelectedProteinKey(), PeptideShakerGUI.NO_SELECTION, PeptideShakerGUI.NO_SELECTION);
        }

        try {
            int row = peptideTable.getSelectedRow();
            int proteinIndex = proteinTable.convertRowIndexToModel(row);
            int column = peptideTable.getSelectedColumn();

            if (row != -1) {
                if (pdbMatchesJTable.getSelectedRow() != -1) {
                    updatePeptideToPdbMapping();
                }

                // remember the selection
                newItemSelection();

                if (column == peptideTable.getColumn("  ").getModelIndex()) {
                    String key = peptideTableMap.get(getPeptideIndex(row));
                    if ((Boolean) peptideTable.getValueAt(row, column)) {
                        peptideShakerGUI.getStarHider().starPeptide(key);
                    } else {
                        peptideShakerGUI.getStarHider().unStarPeptide(key);
                    }
                }

                // open the protein inference at the petide level dialog
                if (evt != null && column == peptideTable.getColumn("PI").getModelIndex()) {
                    String proteinKey = proteinKeys.get(proteinIndex);
                    String peptideKey = peptideTableMap.get(getPeptideIndex(row));
                    new ProteinInferencePeptideLevelDialog(peptideShakerGUI, true, peptideKey, proteinKey);
                }
            }
        } catch (Exception e) {
            peptideShakerGUI.catchException(e);
        }
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_peptideTableMouseReleased

    /**
     * Update the PDB structure shown in the Jmol panel.
     *
     * @param evt
     */
    private void pdbMatchesJTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pdbMatchesJTableMouseReleased

        setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));

        boolean loadStructure = true;

        if (pdbMatchesJTable.getSelectedRow() != -1 && currentlyDisplayedPdbFile != null) {

            String tempPdbFile = (String) pdbMatchesJTable.getValueAt(pdbMatchesJTable.getSelectedRow(), pdbMatchesJTable.getColumn("PDB").getModelIndex());

            if (currentlyDisplayedPdbFile.equalsIgnoreCase(tempPdbFile)) {
                loadStructure = false;
            }
        }

        if (loadStructure) {

            // just a trick to make sure that the users cannot select another row until the selection has been updated
            this.setEnabled(false);

            DefaultTableModel dm = (DefaultTableModel) pdbChainsJTable.getModel();
            dm.getDataVector().removeAllElements();
            dm.fireTableDataChanged();

            // clear the peptide to pdb mappings in the peptide table
            for (int i = 0; i < peptideTable.getRowCount(); i++) {
                peptideTable.setValueAt(false, i, peptideTable.getColumn("PDB").getModelIndex());
            }

            // select the peptide in the table again
            int peptideRow = 0;
            String peptideKey = peptideShakerGUI.getSelectedPeptideKey();

            if (!peptideKey.equals(PeptideShakerGUI.NO_SELECTION)) {
                peptideRow = getPeptideRow(peptideKey);
            }

            if (peptideTable.getRowCount() > 0) {
                peptideTable.setRowSelectionInterval(peptideRow, peptideRow);
                peptideTable.scrollRectToVisible(peptideTable.getCellRect(peptideRow, peptideRow, false));
            }

            // empty the jmol panel
            if (jmolStructureShown) {
                jmolPanel = new JmolPanel();
                pdbPanel.removeAll();
                pdbPanel.add(jmolPanel);
                pdbPanel.revalidate();
                pdbPanel.repaint();
                jmolStructureShown = false;
                currentlyDisplayedPdbFile = null;

                ((TitledBorder) pdbOuterPanel.getBorder()).setTitle(PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING + "PDB Structure" + PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING);
                pdbOuterPanel.repaint();
            }

            // get the protein length
            int proteinSequenceLength = proteinSequence.length();

            if (pdbMatchesJTable.getSelectedRow() != -1) {

                currentlyDisplayedPdbFile = (String) pdbMatchesJTable.getValueAt(pdbMatchesJTable.getSelectedRow(), pdbMatchesJTable.getColumn("PDB").getModelIndex());

                // open protein link in web browser
                if (pdbMatchesJTable.getSelectedColumn() == pdbMatchesJTable.getColumn("PDB").getModelIndex() && evt.getButton() == MouseEvent.BUTTON1
                        && ((String) pdbMatchesJTable.getValueAt(pdbMatchesJTable.getSelectedRow(), pdbMatchesJTable.getSelectedColumn())).lastIndexOf("<html>") != -1) {

                    String temp = currentlyDisplayedPdbFile.substring(currentlyDisplayedPdbFile.indexOf("\"") + 1);
                    currentlyDisplayedPdbFile = temp.substring(0, temp.indexOf("\""));

                    this.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
                    BareBonesBrowserLaunch.openURL(currentlyDisplayedPdbFile);
                    this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                }

                // get the pdb file
                int selectedPdbTableIndex = (Integer) pdbMatchesJTable.getValueAt(pdbMatchesJTable.getSelectedRow(), 0);
                PdbParameter lParam = uniProtPdb.getPdbs().get(selectedPdbTableIndex - 1);
                chains = lParam.getBlocks();

                // add the chain information to the table
                for (int j = 0; j < chains.length; j++) {

                    XYDataPoint temp = new XYDataPoint(chains[j].getStart_protein(), chains[j].getEnd_protein());

                    ((DefaultTableModel) pdbChainsJTable.getModel()).addRow(new Object[]{
                                (j + 1),
                                chains[j].getBlock(),
                                temp,
                                (((double) chains[j].getEnd_protein() - chains[j].getStart_protein()) / proteinSequenceLength) * 100
                            });
                }

                ((JSparklinesIntervalChartTableCellRenderer) pdbChainsJTable.getColumn("PDB-Protein").getCellRenderer()).setMaxValue(proteinSequenceLength);

                if (pdbChainsJTable.getRowCount() > 0) {
                    ((TitledBorder) pdbChainsPanel.getBorder()).setTitle(PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING + "PDB Chains (" + pdbChainsJTable.getRowCount() + ")"
                            + PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING);
                } else {
                    ((TitledBorder) pdbChainsPanel.getBorder()).setTitle(PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING + "PDB Chains"
                            + PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING);
                }

                pdbChainsPanel.repaint();

                if (pdbChainsJTable.getRowCount() > 0) {
                    pdbChainsJTable.setRowSelectionInterval(0, 0);
                    pdbChainsJTable.scrollRectToVisible(pdbChainsJTable.getCellRect(0, 0, false));
                    pdbChainsJTableMouseReleased(null);
                }
            } else {
                ((TitledBorder) pdbChainsPanel.getBorder()).setTitle(PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING + "PDB Chains"
                        + PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING);
                pdbChainsPanel.repaint();
            }

            // give the power back to the user ;)
            this.setEnabled(true);
        } else {

            // open protein link in web browser
            if (pdbMatchesJTable.getSelectedColumn() == pdbMatchesJTable.getColumn("PDB").getModelIndex() && evt.getButton() == MouseEvent.BUTTON1
                    && ((String) pdbMatchesJTable.getValueAt(pdbMatchesJTable.getSelectedRow(), pdbMatchesJTable.getSelectedColumn())).lastIndexOf("<html>") != -1) {

                String temp = currentlyDisplayedPdbFile.substring(currentlyDisplayedPdbFile.indexOf("\"") + 1);
                currentlyDisplayedPdbFile = temp.substring(0, temp.indexOf("\""));

                this.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
                BareBonesBrowserLaunch.openURL(currentlyDisplayedPdbFile);
                this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            }
        }

        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_pdbMatchesJTableMouseReleased

    /**
     * Update the PDB structure with the currnet chain selection.
     *
     * @param evt
     */
    private void pdbChainsJTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pdbChainsJTableMouseReleased

        setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));

        if (jmolStructureShown) {
            updatePeptideToPdbMapping();
        } else {

            progressDialog = new ProgressDialogX(peptideShakerGUI,
                    Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker.gif")),
                    Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker-orange.gif")),
                    true);
            progressDialog.setIndeterminate(true);
            progressDialog.setTitle("Loading PDB Structure. Please Wait...");

            new Thread(new Runnable() {

                public void run() {
                    try {
                        progressDialog.setVisible(true);
                    } catch (IndexOutOfBoundsException e) {
                        // ignore
                    }
                }
            }, "PdbChaingThread").start();

            new Thread("StructureThread") {

                @Override
                public void run() {

                    progressDialog.setIndeterminate(true);

                    int selectedPdbIndex = (Integer) pdbMatchesJTable.getValueAt(pdbMatchesJTable.getSelectedRow(), 0);
                    PdbParameter lParam = uniProtPdb.getPdbs().get(selectedPdbIndex - 1);

                    String link = "http://www.rcsb.org/pdb/files/" + lParam.getPdbaccession() + ".pdb";

                    jmolPanel.getViewer().openFile(link);
                    if (ribbonModel) {
                        jmolPanel.getViewer().evalString("select all; ribbon only;");
                    } else if (backboneModel) {
                        jmolPanel.getViewer().evalString("select all; backbone only; backbone 100;");
                    }

                    if (!progressDialog.isRunCanceled()) {
                        spinModel(spinModel);
                        jmolStructureShown = true;

                        ((TitledBorder) pdbOuterPanel.getBorder()).setTitle(PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING + "PDB Structure (" + lParam.getPdbaccession() + ")"
                                + PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING);
                        pdbOuterPanel.repaint();
                    }

                    if (!progressDialog.isRunCanceled()) {
                        progressDialog.setTitle("Mapping Peptides. Please Wait...");

                        // get the chains
                        chains = lParam.getBlocks();
                        int selectedChainIndex = (Integer) pdbChainsJTable.getValueAt(pdbChainsJTable.getSelectedRow(), 0);
                        chainSequence = chains[selectedChainIndex - 1].getBlockSequence(lParam.getPdbaccession());

                        // update the peptide to pdb mappings
                        updatePeptideToPdbMapping();
                    }

                    progressDialog.setRunFinished();
                    setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                }
            }.start();
        }

        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_pdbChainsJTableMouseReleased

    /**
     * Update the PDB structure with the currently selected chain.
     *
     * @param evt
     */
    private void pdbChainsJTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pdbChainsJTableKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_DOWN
                || evt.getKeyCode() == KeyEvent.VK_PAGE_UP || evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
            updatePeptideToPdbMapping();
        }
    }//GEN-LAST:event_pdbChainsJTableKeyReleased

    /**
     * Changes the cursor into a hand cursor if the table cell contains an html
     * link.
     *
     * @param evt
     */
    private void pdbMatchesJTableMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pdbMatchesJTableMouseMoved
        int row = pdbMatchesJTable.rowAtPoint(evt.getPoint());
        int column = pdbMatchesJTable.columnAtPoint(evt.getPoint());

        if (column == pdbMatchesJTable.getColumn("PDB").getModelIndex() && pdbMatchesJTable.getValueAt(row, column) != null) {

            String tempValue = (String) pdbMatchesJTable.getValueAt(row, column);

            if (tempValue.lastIndexOf("<html>") != -1) {
                this.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            } else {
                this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            }
        } else {
            this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_pdbMatchesJTableMouseMoved

    /**
     * Changes the cursor back to the default cursor a hand.
     *
     * @param evt
     */
    private void pdbMatchesJTableMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pdbMatchesJTableMouseExited
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_pdbMatchesJTableMouseExited

    /**
     * Resizes the components in the PDB Structure layered pane if the layred
     * pane is resized.
     *
     * @param evt
     */
    private void pdbLayeredPaneComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_pdbLayeredPaneComponentResized

        int componentIndex = 0;

        // move the icons
        pdbLayeredPane.getComponent(componentIndex++).setBounds(
                pdbLayeredPane.getWidth() - pdbLayeredPane.getComponent(0).getWidth() * componentIndex - 10,
                pdbLayeredPane.getComponent(0).getHeight() / 10 - 2,
                pdbLayeredPane.getComponent(0).getWidth(),
                pdbLayeredPane.getComponent(0).getHeight());

        pdbLayeredPane.getComponent(componentIndex++).setBounds(
                pdbLayeredPane.getWidth() - pdbLayeredPane.getComponent(0).getWidth() * componentIndex - 15,
                pdbLayeredPane.getComponent(0).getHeight() / 10 - 2,
                pdbLayeredPane.getComponent(0).getWidth(),
                pdbLayeredPane.getComponent(0).getHeight());

        pdbLayeredPane.getComponent(componentIndex++).setBounds(
                pdbLayeredPane.getWidth() - pdbLayeredPane.getComponent(0).getWidth() * componentIndex - 10,
                pdbLayeredPane.getComponent(0).getHeight() / 10 - 2,
                pdbLayeredPane.getComponent(0).getWidth(),
                pdbLayeredPane.getComponent(0).getHeight());

        pdbLayeredPane.getComponent(componentIndex++).setBounds(
                pdbLayeredPane.getWidth() - pdbLayeredPane.getComponent(0).getWidth() * componentIndex - 15,
                pdbLayeredPane.getComponent(0).getHeight() / 10,
                pdbLayeredPane.getComponent(0).getWidth(),
                pdbLayeredPane.getComponent(0).getHeight());

        // resize the plot area
        pdbLayeredPane.getComponent(componentIndex++).setBounds(0, 0, pdbLayeredPane.getWidth(), pdbLayeredPane.getHeight());
        pdbLayeredPane.revalidate();
        pdbLayeredPane.repaint();
    }//GEN-LAST:event_pdbLayeredPaneComponentResized

    /**
     * Changes the cursor back to the default cursor.
     *
     * @param evt
     */
    private void peptideTableMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_peptideTableMouseExited
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_peptideTableMouseExited

    /**
     * Changes the cursor into a hand cursor if the table cell contains an HTML
     * link. Or shows a tooltip with modification details is over the sequence
     * column.
     *
     * @param evt
     */
    private void peptideTableMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_peptideTableMouseMoved
        int row = peptideTable.rowAtPoint(evt.getPoint());
        int column = peptideTable.columnAtPoint(evt.getPoint());

        if (peptideTable.getValueAt(row, column) != null) {
            if (column == peptideTable.getColumn("PI").getModelIndex()) {
                this.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                peptideTable.setToolTipText(null);
            } else if (column == peptideTable.getColumn("Sequence").getModelIndex()) {

                this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

                // check if we ought to show a tooltip with mod details
                String sequence = (String) peptideTable.getValueAt(row, column);

                if (sequence.indexOf("<span") != -1) {
                    try {
                        String peptideKey = peptideTableMap.get(getPeptideIndex(row));
                        Peptide peptide = peptideShakerGUI.getIdentification().getPeptideMatch(peptideKey).getTheoreticPeptide();
                        String tooltip = peptideShakerGUI.getIdentificationFeaturesGenerator().getPeptideModificationTooltipAsHtml(peptide);
                        peptideTable.setToolTipText(tooltip);
                    } catch (Exception e) {
                        peptideShakerGUI.catchException(e);
                        e.printStackTrace();
                    }
                } else {
                    peptideTable.setToolTipText(null);
                }
            } else {
                this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                peptideTable.setToolTipText(null);
            }
        } else {
            this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            peptideTable.setToolTipText(null);
        }
    }//GEN-LAST:event_peptideTableMouseMoved

    /**
     * Change the cursor to a hand cursor.
     *
     * @param evt
     */
    private void playJButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_playJButtonMouseEntered
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_playJButtonMouseEntered

    /**
     * Changes the cursor back to the default cursor.
     *
     * @param evt
     */
    private void playJButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_playJButtonMouseExited
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_playJButtonMouseExited

    /**
     * Start/stop the rotation of the structure.
     *
     * @param evt
     */
    private void playJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playJButtonActionPerformed

        spinModel = !spinModel;
        spinModel(spinModel);

        if (spinModel) {
            playJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/pause.png")));
            playJButton.setToolTipText("Stop Rotation");
        } else {
            playJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/play.png")));
            playJButton.setToolTipText("Start Rotation");
        }
    }//GEN-LAST:event_playJButtonActionPerformed

    /**
     * Change the cursor to a hand cursor.
     *
     * @param evt
     */
    private void ribbonJButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ribbonJButtonMouseEntered
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_ribbonJButtonMouseEntered

    /**
     * Changes the cursor back to the default cursor.
     *
     * @param evt
     */
    private void ribbonJButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ribbonJButtonMouseExited
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_ribbonJButtonMouseExited

    /**
     * Change the model type to ribbon.
     *
     * @param evt
     */
    private void ribbonJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ribbonJButtonActionPerformed
        ribbonModel = true;
        backboneModel = false;
        updateModelType();

        if (backboneModel) {
            backboneJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/backbone_selected.png")));
            ribbonJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/ribbon.png")));
        } else {
            backboneJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/backbone.png")));
            ribbonJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/ribbon_selected.png")));
        }
    }//GEN-LAST:event_ribbonJButtonActionPerformed

    /**
     * Change the cursor to a hand cursor.
     *
     * @param evt
     */
    private void backboneJButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backboneJButtonMouseEntered
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_backboneJButtonMouseEntered

    /**
     * Changes the cursor back to the default cursor.
     *
     * @param evt
     */
    private void backboneJButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backboneJButtonMouseExited
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_backboneJButtonMouseExited

    /**
     * Change the model type to backbone.
     *
     * @param evt
     */
    private void backboneJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backboneJButtonActionPerformed
        ribbonModel = false;
        backboneModel = true;
        updateModelType();

        if (backboneModel) {
            backboneJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/backbone_selected.png")));
            ribbonJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/ribbon.png")));
        } else {
            backboneJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/backbone.png")));
            ribbonJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/ribbon_selected.png")));
        }
    }//GEN-LAST:event_backboneJButtonActionPerformed

    /**
     * Change the cursor to a hand cursor.
     *
     * @param evt
     */
    private void labelsJButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelsJButtonMouseEntered
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_labelsJButtonMouseEntered

    /**
     * Changes the cursor back to the default cursor.
     *
     * @param evt
     */
    private void labelsJButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelsJButtonMouseExited
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_labelsJButtonMouseExited

    /**
     * Set if the modification labels are to be shown or not.
     *
     * @param evt
     */
    private void labelsJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_labelsJButtonActionPerformed
        showModificationLabels = !showModificationLabels;

        if (showModificationLabels) {
            labelsJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/labels_selected.png")));
            labelsJButton.setToolTipText("Hide Modification Labels");
        } else {
            labelsJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/labels.png")));
            labelsJButton.setToolTipText("Show Modification Labels");
        }

        if (pdbMatchesJTable.getSelectedRow() != -1 && peptideTable.getSelectedRow() != -1) {
            updatePeptideToPdbMapping();
        }
    }//GEN-LAST:event_labelsJButtonActionPerformed

    /**
     * Change the cursor to a hand cursor.
     *
     * @param evt
     */
    private void exportProteinsJButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exportProteinsJButtonMouseEntered
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_exportProteinsJButtonMouseEntered

    private void exportProteinsJButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exportProteinsJButtonMouseExited
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_exportProteinsJButtonMouseExited

    /**
     * Export the table contents.
     *
     * @param evt
     */
    private void exportProteinsJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportProteinsJButtonActionPerformed
        copyTableContentToClipboardOrFile(TableIndex.PROTEIN_TABLE);
    }//GEN-LAST:event_exportProteinsJButtonActionPerformed

    /**
     * Change the cursor to a hand cursor.
     *
     * @param evt
     */
    private void proteinsHelpJButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_proteinsHelpJButtonMouseEntered
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_proteinsHelpJButtonMouseEntered

    /**
     * Change the cursor back to the default cursor.
     *
     * @param evt
     */
    private void proteinsHelpJButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_proteinsHelpJButtonMouseExited
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_proteinsHelpJButtonMouseExited

    /**
     * Open the help dialog.
     *
     * @param evt
     */
    private void proteinsHelpJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_proteinsHelpJButtonActionPerformed
        setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        new HelpDialog(peptideShakerGUI, getClass().getResource("/helpFiles/PDB.html"));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_proteinsHelpJButtonActionPerformed

    /**
     * Update the layered panes.
     *
     * @param evt
     */
    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized

        // resize the layered panels
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                // move the icons
                proteinsLayeredPane.getComponent(0).setBounds(
                        proteinsLayeredPane.getWidth() - proteinsLayeredPane.getComponent(0).getWidth() - 10,
                        -3,
                        proteinsLayeredPane.getComponent(0).getWidth(),
                        proteinsLayeredPane.getComponent(0).getHeight());

                proteinsLayeredPane.getComponent(1).setBounds(
                        proteinsLayeredPane.getWidth() - proteinsLayeredPane.getComponent(1).getWidth() - 20,
                        -3,
                        proteinsLayeredPane.getComponent(1).getWidth(),
                        proteinsLayeredPane.getComponent(1).getHeight());

                proteinsLayeredPane.getComponent(2).setBounds(
                        proteinsLayeredPane.getWidth() - proteinsLayeredPane.getComponent(2).getWidth() - 5,
                        -3,
                        proteinsLayeredPane.getComponent(2).getWidth(),
                        proteinsLayeredPane.getComponent(2).getHeight());

                // resize the plot area
                proteinsLayeredPane.getComponent(3).setBounds(0, 0, proteinsLayeredPane.getWidth(), proteinsLayeredPane.getHeight());
                proteinsLayeredPane.revalidate();
                proteinsLayeredPane.repaint();


                // move the icons
                peptidesLayeredPane.getComponent(0).setBounds(
                        peptidesLayeredPane.getWidth() - peptidesLayeredPane.getComponent(0).getWidth() - 10,
                        -3,
                        peptidesLayeredPane.getComponent(0).getWidth(),
                        peptidesLayeredPane.getComponent(0).getHeight());

                peptidesLayeredPane.getComponent(1).setBounds(
                        peptidesLayeredPane.getWidth() - peptidesLayeredPane.getComponent(1).getWidth() - 20,
                        -3,
                        peptidesLayeredPane.getComponent(1).getWidth(),
                        peptidesLayeredPane.getComponent(1).getHeight());

                peptidesLayeredPane.getComponent(2).setBounds(
                        peptidesLayeredPane.getWidth() - peptidesLayeredPane.getComponent(2).getWidth() - 5,
                        -3,
                        peptidesLayeredPane.getComponent(2).getWidth(),
                        peptidesLayeredPane.getComponent(2).getHeight());

                // resize the plot area
                peptidesLayeredPane.getComponent(3).setBounds(0, 0, peptidesLayeredPane.getWidth(), peptidesLayeredPane.getHeight());
                peptidesLayeredPane.revalidate();
                peptidesLayeredPane.repaint();


                // move the icons
                pdbMatchesLayeredPane.getComponent(0).setBounds(
                        pdbMatchesLayeredPane.getWidth() - pdbMatchesLayeredPane.getComponent(0).getWidth() - 10,
                        -3,
                        pdbMatchesLayeredPane.getComponent(0).getWidth(),
                        pdbMatchesLayeredPane.getComponent(0).getHeight());

                pdbMatchesLayeredPane.getComponent(1).setBounds(
                        pdbMatchesLayeredPane.getWidth() - pdbMatchesLayeredPane.getComponent(1).getWidth() - 20,
                        -3,
                        pdbMatchesLayeredPane.getComponent(1).getWidth(),
                        pdbMatchesLayeredPane.getComponent(1).getHeight());

                pdbMatchesLayeredPane.getComponent(2).setBounds(
                        pdbMatchesLayeredPane.getWidth() - pdbMatchesLayeredPane.getComponent(2).getWidth() - 5,
                        -3,
                        pdbMatchesLayeredPane.getComponent(2).getWidth(),
                        pdbMatchesLayeredPane.getComponent(2).getHeight());

                // resize the plot area
                pdbMatchesLayeredPane.getComponent(3).setBounds(0, 0, pdbMatchesLayeredPane.getWidth(), pdbMatchesLayeredPane.getHeight());
                pdbMatchesLayeredPane.revalidate();
                pdbMatchesLayeredPane.repaint();


                // move the icons
                pdbChainsLayeredPane.getComponent(0).setBounds(
                        pdbChainsLayeredPane.getWidth() - pdbChainsLayeredPane.getComponent(0).getWidth() - 10,
                        -3,
                        pdbChainsLayeredPane.getComponent(0).getWidth(),
                        pdbChainsLayeredPane.getComponent(0).getHeight());

                pdbChainsLayeredPane.getComponent(1).setBounds(
                        pdbChainsLayeredPane.getWidth() - pdbChainsLayeredPane.getComponent(1).getWidth() - 20,
                        -3,
                        pdbChainsLayeredPane.getComponent(1).getWidth(),
                        pdbChainsLayeredPane.getComponent(1).getHeight());

                pdbChainsLayeredPane.getComponent(2).setBounds(
                        pdbChainsLayeredPane.getWidth() - pdbChainsLayeredPane.getComponent(2).getWidth() - 5,
                        -3,
                        pdbChainsLayeredPane.getComponent(2).getWidth(),
                        pdbChainsLayeredPane.getComponent(2).getHeight());

                // resize the plot area
                pdbChainsLayeredPane.getComponent(3).setBounds(0, 0, pdbChainsLayeredPane.getWidth(), pdbChainsLayeredPane.getHeight());
                pdbChainsLayeredPane.revalidate();
                pdbChainsLayeredPane.repaint();


                // move the icons
                pdbStructureLayeredPane.getComponent(0).setBounds(
                        pdbStructureLayeredPane.getWidth() - pdbStructureLayeredPane.getComponent(0).getWidth() - 10,
                        -3,
                        pdbStructureLayeredPane.getComponent(0).getWidth(),
                        pdbStructureLayeredPane.getComponent(0).getHeight());

                pdbStructureLayeredPane.getComponent(1).setBounds(
                        pdbStructureLayeredPane.getWidth() - pdbStructureLayeredPane.getComponent(1).getWidth() - 20,
                        -3,
                        pdbStructureLayeredPane.getComponent(1).getWidth(),
                        pdbStructureLayeredPane.getComponent(1).getHeight());

                pdbStructureLayeredPane.getComponent(2).setBounds(
                        pdbStructureLayeredPane.getWidth() - pdbStructureLayeredPane.getComponent(2).getWidth() - 5,
                        -3,
                        pdbStructureLayeredPane.getComponent(2).getWidth(),
                        pdbStructureLayeredPane.getComponent(2).getHeight());

                // resize the plot area
                pdbStructureLayeredPane.getComponent(3).setBounds(0, 0, pdbStructureLayeredPane.getWidth(), pdbStructureLayeredPane.getHeight());
                pdbStructureLayeredPane.revalidate();
                pdbStructureLayeredPane.repaint();
            }
        });
    }//GEN-LAST:event_formComponentResized

    /**
     * Change the cursor to a hand cursor.
     *
     * @param evt
     */
    private void peptidesHelpJButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_peptidesHelpJButtonMouseEntered
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_peptidesHelpJButtonMouseEntered

    /**
     * Change the cursor back to the default cursor.
     *
     * @param evt
     */
    private void peptidesHelpJButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_peptidesHelpJButtonMouseExited
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_peptidesHelpJButtonMouseExited

    /**
     * Open the help dialog.
     *
     * @param evt
     */
    private void peptidesHelpJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_peptidesHelpJButtonActionPerformed
        setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        new HelpDialog(peptideShakerGUI, getClass().getResource("/helpFiles/PDB.html"));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_peptidesHelpJButtonActionPerformed

    /**
     * Change the cursor to a hand cursor.
     *
     * @param evt
     */
    private void exportPeptidesJButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exportPeptidesJButtonMouseEntered
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_exportPeptidesJButtonMouseEntered

    /**
     * Change the cursor back to the default cursor.
     *
     * @param evt
     */
    private void exportPeptidesJButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exportPeptidesJButtonMouseExited
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_exportPeptidesJButtonMouseExited

    /**
     * Export the table contents.
     *
     * @param evt
     */
    private void exportPeptidesJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportPeptidesJButtonActionPerformed
        copyTableContentToClipboardOrFile(TableIndex.PEPTIDE_TABLE);
    }//GEN-LAST:event_exportPeptidesJButtonActionPerformed

    /**
     * Change the cursor to a hand cursor.
     *
     * @param evt
     */
    private void pdbMatchesHelpJButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pdbMatchesHelpJButtonMouseEntered
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_pdbMatchesHelpJButtonMouseEntered

    /**
     * Change the cursor back to the default cursor.
     *
     * @param evt
     */
    private void pdbMatchesHelpJButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pdbMatchesHelpJButtonMouseExited
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_pdbMatchesHelpJButtonMouseExited

    /**
     * Open the help dialog.
     *
     * @param evt
     */
    private void pdbMatchesHelpJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pdbMatchesHelpJButtonActionPerformed
        setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        new HelpDialog(peptideShakerGUI, getClass().getResource("/helpFiles/PDB.html"));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_pdbMatchesHelpJButtonActionPerformed

    /**
     * Change the cursor to a hand cursor.
     *
     * @param evt
     */
    private void exportPdbMatchesJButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exportPdbMatchesJButtonMouseEntered
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_exportPdbMatchesJButtonMouseEntered

    /**
     * Change the cursor back to the default cursor.
     *
     * @param evt
     */
    private void exportPdbMatchesJButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exportPdbMatchesJButtonMouseExited
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_exportPdbMatchesJButtonMouseExited

    /**
     * Export the table contents.
     *
     * @param evt
     */
    private void exportPdbMatchesJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportPdbMatchesJButtonActionPerformed
        copyTableContentToClipboardOrFile(TableIndex.PDB_MATCHES);
    }//GEN-LAST:event_exportPdbMatchesJButtonActionPerformed

    /**
     * Change the cursor to a hand cursor.
     *
     * @param evt
     */
    private void pdbChainHelpJButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pdbChainHelpJButtonMouseEntered
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_pdbChainHelpJButtonMouseEntered

    /**
     * Change the cursor back to the default cursor.
     *
     * @param evt
     */
    private void pdbChainHelpJButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pdbChainHelpJButtonMouseExited
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_pdbChainHelpJButtonMouseExited

    /**
     * Open the help dialog.
     *
     * @param evt
     */
    private void pdbChainHelpJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pdbChainHelpJButtonActionPerformed
        setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        new HelpDialog(peptideShakerGUI, getClass().getResource("/helpFiles/PDB.html"));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_pdbChainHelpJButtonActionPerformed

    /**
     * Change the cursor to a hand cursor.
     *
     * @param evt
     */
    private void exportPdbChainsJButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exportPdbChainsJButtonMouseEntered
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_exportPdbChainsJButtonMouseEntered

    /**
     * Change the cursor back to the default cursor.
     *
     * @param evt
     */
    private void exportPdbChainsJButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exportPdbChainsJButtonMouseExited
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_exportPdbChainsJButtonMouseExited

    /**
     * Export the table contents.
     *
     * @param evt
     */
    private void exportPdbChainsJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportPdbChainsJButtonActionPerformed
        copyTableContentToClipboardOrFile(TableIndex.PDB_CHAINS);
    }//GEN-LAST:event_exportPdbChainsJButtonActionPerformed

    /**
     * Change the cursor to a hand cursor.
     *
     * @param evt
     */
    private void pdbStructureHelpJButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pdbStructureHelpJButtonMouseEntered
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_pdbStructureHelpJButtonMouseEntered

    /**
     * Change the cursor back to the default cursor.
     *
     * @param evt
     */
    private void pdbStructureHelpJButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pdbStructureHelpJButtonMouseExited
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_pdbStructureHelpJButtonMouseExited

    /**
     * Open the help dialog.
     *
     * @param evt
     */
    private void pdbStructureHelpJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pdbStructureHelpJButtonActionPerformed
        setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        new HelpDialog(peptideShakerGUI, getClass().getResource("/helpFiles/PDB.html"));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_pdbStructureHelpJButtonActionPerformed

    /**
     * Change the cursor to a hand cursor.
     *
     * @param evt
     */
    private void exportPdbStructureJButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exportPdbStructureJButtonMouseEntered
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_exportPdbStructureJButtonMouseEntered

    /**
     * Change the cursor back to the default cursor.
     *
     * @param evt
     */
    private void exportPdbStructureJButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exportPdbStructureJButtonMouseExited
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_exportPdbStructureJButtonMouseExited

    /**
     * Export the pdb structure.
     *
     * @param evt
     */
    private void exportPdbStructureJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportPdbStructureJButtonActionPerformed
        new ExportGraphicsDialog(peptideShakerGUI, true, pdbPanel);

        // @TODO: use Jmol's export options...
    }//GEN-LAST:event_exportPdbStructureJButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backboneJButton;
    private javax.swing.JPanel contextMenuPdbChainsBackgroundPanel;
    private javax.swing.JPanel contextMenuPdbMatchesBackgroundPanel;
    private javax.swing.JPanel contextMenuPdbStructureBackgroundPanel;
    private javax.swing.JPanel contextMenuPeptidesBackgroundPanel;
    private javax.swing.JPanel contextMenuProteinsBackgroundPanel;
    private javax.swing.JButton exportPdbChainsJButton;
    private javax.swing.JButton exportPdbMatchesJButton;
    private javax.swing.JButton exportPdbStructureJButton;
    private javax.swing.JButton exportPeptidesJButton;
    private javax.swing.JButton exportProteinsJButton;
    private javax.swing.JButton labelsJButton;
    private javax.swing.JButton pdbChainHelpJButton;
    private javax.swing.JPanel pdbChainsJPanel;
    private javax.swing.JScrollPane pdbChainsJScrollPane;
    private javax.swing.JTable pdbChainsJTable;
    private javax.swing.JLayeredPane pdbChainsLayeredPane;
    private javax.swing.JPanel pdbChainsPanel;
    private javax.swing.JScrollPane pdbJScrollPane;
    private javax.swing.JLayeredPane pdbLayeredPane;
    private javax.swing.JButton pdbMatchesHelpJButton;
    private javax.swing.JPanel pdbMatchesJPanel;
    private javax.swing.JTable pdbMatchesJTable;
    private javax.swing.JLayeredPane pdbMatchesLayeredPane;
    private javax.swing.JPanel pdbMatchesPanel;
    private javax.swing.JPanel pdbOuterPanel;
    private javax.swing.JPanel pdbPanel;
    private javax.swing.JButton pdbStructureHelpJButton;
    private javax.swing.JPanel pdbStructureJPanel;
    private javax.swing.JLayeredPane pdbStructureLayeredPane;
    private javax.swing.JScrollPane peptideScrollPane;
    private javax.swing.JTable peptideTable;
    private javax.swing.JButton peptidesHelpJButton;
    private javax.swing.JPanel peptidesJPanel;
    private javax.swing.JLayeredPane peptidesLayeredPane;
    private javax.swing.JPanel peptidesPanel;
    private javax.swing.JButton playJButton;
    private javax.swing.JScrollPane proteinScrollPane;
    private javax.swing.JTable proteinTable;
    private javax.swing.JButton proteinsHelpJButton;
    private javax.swing.JPanel proteinsJPanel;
    private javax.swing.JLayeredPane proteinsLayeredPane;
    private javax.swing.JPanel proteinsPanel;
    private javax.swing.JButton ribbonJButton;
    // End of variables declaration//GEN-END:variables

    /**
     * Returns a list of keys of the displayed proteins
     *
     * @return a list of keys of the displayed proteins
     */
    public ArrayList<String> getDisplayedProteins() {
        return proteinKeys;
    }

    /**
     * Returns a list of keys of the displayed peptides
     *
     * @return a list of keys of the displayed peptides
     */
    public ArrayList<String> getDisplayedPeptides() {
        return new ArrayList<String>(peptideTableMap.values());
    }

    /**
     * Updates the peptide selection according to the currently selected
     * protein.
     *
     * @param proteinIndex the row index of the protein
     */
    private void updatedPeptideSelection(int proteinIndex) {

        if (proteinIndex != -1) {

            this.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));

            try {

                DefaultTableModel dm = (DefaultTableModel) peptideTable.getModel();
                dm.getDataVector().removeAllElements();
                dm.fireTableDataChanged();

                String proteinMatchKey = proteinKeys.get(proteinIndex);
                ProteinMatch proteinMatch = peptideShakerGUI.getIdentification().getProteinMatch(proteinMatchKey);
                peptideTableMap = new HashMap<Integer, String>();

                PSParameter probabilities = new PSParameter();
                PeptideMatch currentMatch;

                int index = 0;
                
                int nValidatedPeptides = peptideShakerGUI.getIdentificationFeaturesGenerator().getNValidatedPeptides(proteinMatchKey);

                ArrayList<String> peptideKeys = peptideShakerGUI.getIdentificationFeaturesGenerator().getSortedPeptideKeys(proteinMatchKey);

                for (String peptideKey : peptideKeys) {
                    currentMatch = peptideShakerGUI.getIdentification().getPeptideMatch(peptideKey);
                    probabilities = (PSParameter) peptideShakerGUI.getIdentification().getPeptideMatchParameter(peptideKey, probabilities);

                    if (!probabilities.isHidden()) {

                        ArrayList<String> otherProteins = new ArrayList<String>();
                        List<String> proteinProteins = Arrays.asList(ProteinMatch.getAccessions(proteinMatchKey));
                        for (String accession : currentMatch.getTheoreticPeptide().getParentProteins()) {
                            if (!proteinProteins.contains(accession)) {
                                otherProteins.add(accession);
                            }
                        }

                        // find and add the peptide start and end indexes
                        int peptideStart = 0;
                        String peptideSequence = currentMatch.getTheoreticPeptide().getSequence();

                        try {
                            String proteinAccession = proteinMatch.getMainMatch();
                            String tempProteinSequence = sequenceFactory.getProtein(proteinAccession).getSequence();
                            peptideStart = tempProteinSequence.lastIndexOf(peptideSequence) + 1;
                        } catch (Exception e) {
                            peptideShakerGUI.catchException(e);
                            e.printStackTrace();
                        }
                        int proteinInferenceType = probabilities.getGroupClass();
                        
                        // @TODO: should be replaced by a table model!!!

                        ((DefaultTableModel) peptideTable.getModel()).addRow(new Object[]{
                                    index + 1,
                                    probabilities.isStarred(),
                                    proteinInferenceType,
                                    peptideShakerGUI.getIdentificationFeaturesGenerator().getColoredPeptideSequence(peptideKey, true),
                                    peptideStart,
                                    false,
                                    probabilities.isValidated()
                                });

                        peptideTableMap.put(index + 1, currentMatch.getKey());
                        index++;
                    }
                }

                ((DefaultTableModel) peptideTable.getModel()).fireTableDataChanged();

                ((TitledBorder) peptidesPanel.getBorder()).setTitle(PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING + "Peptides (" + nValidatedPeptides + "/"
                        + peptideTable.getRowCount() + ")" + PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING);
                peptidesPanel.repaint();

                String tempSequence = sequenceFactory.getProtein(proteinMatch.getMainMatch()).getSequence();
                peptideTable.getColumn("Start").setCellRenderer(new JSparklinesIntervalChartTableCellRenderer(
                        PlotOrientation.HORIZONTAL, (double) tempSequence.length(),
                        ((double) tempSequence.length()) / 50, peptideShakerGUI.getSparklineColor()));
                ((JSparklinesIntervalChartTableCellRenderer) peptideTable.getColumn("Start").getCellRenderer()).showReferenceLine(true, 0.02, Color.BLACK);
                ((JSparklinesIntervalChartTableCellRenderer) peptideTable.getColumn("Start").getCellRenderer()).showNumberAndChart(true, peptideShakerGUI.getLabelWidth() - 10);

                // select the peptide in the table
                if (peptideTable.getRowCount() > 0) {
                    int peptideRow = 0;
                    String peptideKey = peptideShakerGUI.getSelectedPeptideKey();
                    if (!peptideKey.equals(PeptideShakerGUI.NO_SELECTION)) {
                        peptideRow = getPeptideRow(peptideKey);
                    }

                    if (peptideRow != -1) {
                        peptideTable.setRowSelectionInterval(peptideRow, peptideRow);
                        peptideTable.scrollRectToVisible(peptideTable.getCellRect(peptideRow, 0, false));
                        peptideTableKeyReleased(null);
                    }
                }
            } catch (Exception e) {
                peptideShakerGUI.catchException(e);
            }

            this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     * Displays the results in the result tables.
     */
    public void displayResults() {

        progressDialog = new ProgressDialogX(peptideShakerGUI,
                Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker.gif")),
                Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker-orange.gif")),
                true);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle("Updating Data. Please Wait...");

        new Thread(new Runnable() {

            public void run() {
                try {
                    progressDialog.setVisible(true);
                } catch (IndexOutOfBoundsException e) {
                    // ignore
                }
            }
        }, "ProgressDialog").start();

        new Thread("DisplayThread") {

            @Override
            public void run() {

                try {

                    peptideShakerGUI.getIdentificationFeaturesGenerator().setProteinKeys(peptideShakerGUI.getMetrics().getProteinKeys());
                    proteinKeys = peptideShakerGUI.getIdentificationFeaturesGenerator().getProcessedProteinKeys(progressDialog);

                    // update the table model
                    if (proteinTable.getModel() instanceof ProteinTableModel) {
                        ((ProteinTableModel) proteinTable.getModel()).updateDataModel(peptideShakerGUI);
                    } else {
                        ProteinTableModel proteinTableModel = new ProteinTableModel(peptideShakerGUI);
                        proteinTable.setModel(proteinTableModel);
                    }

                    setTableProperties();

                    // update spectrum counting column header tooltip
                    if (peptideShakerGUI.getSpectrumCountingPreferences().getSelectedMethod() == SpectralCountingMethod.EMPAI) {
                        proteinTableToolTips.set(proteinTable.getColumn("MS2 Quant.").getModelIndex(), "Protein MS2 Quantification - emPAI");
                    } else if (peptideShakerGUI.getSpectrumCountingPreferences().getSelectedMethod() == SpectralCountingMethod.NSAF) {
                        proteinTableToolTips.set(proteinTable.getColumn("MS2 Quant.").getModelIndex(), "Protein MS2 Quantification - NSAF");
                    } else {
                        proteinTableToolTips.set(proteinTable.getColumn("MS2 Quant.").getModelIndex(), "Protein MS2 Quantification");
                    }

                    if (peptideShakerGUI.getDisplayPreferences().showScores()) {
                        proteinTableToolTips.set(proteinTable.getColumnCount() - 2, "Protein Score");
                    } else {
                        proteinTableToolTips.set(proteinTable.getColumnCount() - 2, "Protein Confidence");
                    }

                    ((TitledBorder) proteinsPanel.getBorder()).setTitle(PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING + "Proteins ("
                            + peptideShakerGUI.getIdentificationFeaturesGenerator().getNValidatedProteins() + "/" + proteinTable.getRowCount() + ")"
                            + PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING);
                    proteinsPanel.repaint();

                    updateProteinTableCellRenderers();

                    peptideShakerGUI.setUpdated(PeptideShakerGUI.STRUCTURES_TAB_INDEX, true);

                    progressDialog.setIndeterminate(true);
                    progressDialog.setTitle("Preparing 3D Structure Tab. Please Wait...");

                    peptideShakerGUI.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                    progressDialog.setRunFinished();

                    // invoke later to give time for components to update
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            DefaultTableModel dm = (DefaultTableModel) proteinTable.getModel();
                            dm.fireTableDataChanged();
                            updateSelection();
                            proteinTable.requestFocus();
                        }
                    });

                } catch (Exception e) {
                    progressDialog.setRunFinished();
                    peptideShakerGUI.catchException(e);
                }
            }
        }.start();
    }

    /**
     * Returns the index of the peptide at the given row in the peptide table.
     *
     * @param row the row of interest
     * @return the index of the corresponding peptide
     */
    private Integer getPeptideIndex(int row) {
        if (row != -1) {
            return (Integer) peptideTable.getValueAt(row, 0);
        } else {
            return -1;
        }
    }

    /**
     * Update the PDB table according to the selected protein in the protein
     * table.
     *
     * @param proteinKey the current protein key
     */
    private void updatePdbTable(String aProteinKey) {

        final String proteinKey = aProteinKey;

        progressDialog = new ProgressDialogX(peptideShakerGUI,
                Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker.gif")),
                Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker-orange.gif")),
                true);
        progressDialog.setIndeterminate(true);

        new Thread(new Runnable() {

            public void run() {
                progressDialog.setTitle("Getting PDB Data. Please Wait...");
                try {
                    progressDialog.setVisible(true);
                } catch (IndexOutOfBoundsException e) {
                    // ignore
                }
            }
        }, "ProgressDialog").start();

        new Thread("ExtractThread") {

            @Override
            public void run() {

                try {
                    // get the accession number of the main match
                    ProteinMatch proteinMatch = peptideShakerGUI.getIdentification().getProteinMatch(proteinKey);
                    String tempAccession = proteinMatch.getMainMatch();

                    // find the pdb matches
                    uniProtPdb = new FindPdbForUniprotAccessions(tempAccession);

                    // delete the previous matches
                    DefaultTableModel dm = (DefaultTableModel) pdbMatchesJTable.getModel();
                    dm.getDataVector().removeAllElements();
                    dm.fireTableDataChanged();

                    dm = (DefaultTableModel) pdbChainsJTable.getModel();
                    dm.getDataVector().removeAllElements();
                    dm.fireTableDataChanged();

                    // clear the peptide to pdb mappings in the peptide table
                    for (int i = 0; i < peptideTable.getRowCount() && !progressDialog.isRunCanceled(); i++) {
                        peptideTable.setValueAt(false, i, peptideTable.getColumn("PDB").getModelIndex());
                    }

                    int maxNumberOfChains = 1;

                    // add the new matches to the pdb table
                    for (int i = 0; i < uniProtPdb.getPdbs().size() && !progressDialog.isRunCanceled(); i++) {
                        PdbParameter lParam = uniProtPdb.getPdbs().get(i);

                        ((DefaultTableModel) pdbMatchesJTable.getModel()).addRow(new Object[]{
                                    i + 1,
                                    addPdbDatabaseLink(lParam.getPdbaccession()),
                                    lParam.getTitle(),
                                    lParam.getExperiment_type(),
                                    lParam.getBlocks().length});

                        if (lParam.getBlocks().length > maxNumberOfChains) {
                            maxNumberOfChains = lParam.getBlocks().length;
                        }
                    }

                    if (!progressDialog.isRunCanceled()) {

                        ((JSparklinesBarChartTableCellRenderer) pdbMatchesJTable.getColumn("Chains").getCellRenderer()).setMaxValue(maxNumberOfChains);

                        if (!uniProtPdb.urlWasRead()) {
                            ((TitledBorder) pdbMatchesPanel.getBorder()).setTitle(PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING + "PDB Matches - Not Available Without Internet Connection!"
                                    + PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING);
                        } else {
                            ((TitledBorder) pdbMatchesPanel.getBorder()).setTitle(PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING + "PDB Matches (" + pdbMatchesJTable.getRowCount() + ")"
                                    + PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING);
                        }

                        pdbMatchesPanel.repaint();

                        ((TitledBorder) pdbChainsPanel.getBorder()).setTitle(PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING + "PDB Chains"
                                + PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING);
                        pdbChainsPanel.repaint();
                    }

                    progressDialog.setRunFinished();

                } catch (Exception e) {
                    progressDialog.setRunFinished();
                    peptideShakerGUI.catchException(e);
                }
            }
        }.start();
    }

    /**
     * Updates the model type if the jmol structure is currently visible.
     */
    public void updateModelType() {
        if (jmolStructureShown) {
            if (ribbonModel) {
                jmolPanel.getViewer().evalString("select all; ribbon; backbone off");
            } else if (backboneModel) {
                jmolPanel.getViewer().evalString("select all; backbone 100; ribbon off");
            }
        }
    }

    /**
     * A simple class for displaying a Jmol viewer in a JPanel.
     */
    public class JmolPanel extends JPanel {

        /**
         * The JmolViewer.
         */
        private JmolViewer viewer;
        /**
         * The current size of the JPanel.
         */
        private final Dimension currentSize = new Dimension();
        /**
         * The current rectangle of the JPanel.
         */
        private final Rectangle rectClip = new Rectangle();

        /**
         * Create a new JmolPanel.
         */
        JmolPanel() {
            JmolAdapter adapter = new SmarterJmolAdapter();
            viewer = JmolViewer.allocateViewer(this, adapter);
        }

        /**
         * Returns the JmolViewer.
         *
         * @return the JmolViewer
         */
        public JmolViewer getViewer() {
            return viewer;
        }

        /**
         * Executes the given command line on the Jmol instance.
         *
         * @param rasmolScript the command line to execute
         */
        public void executeCmd(String rasmolScript) {
            viewer.evalString(rasmolScript);
        }

        @Override
        public void paint(Graphics g) {
            getSize(currentSize);
            g.getClipBounds(rectClip);
            viewer.renderScreenImage(g, currentSize, rectClip);
        }
    }

    /**
     * Displays or hide sparklines in the tables.
     *
     * @param showSparkLines boolean indicating whether sparklines shall be
     * displayed or hidden
     */
    public void showSparkLines(boolean showSparkLines) {
        ((JSparklinesTwoValueBarChartTableCellRenderer) proteinTable.getColumn("Coverage").getCellRenderer()).showNumbers(!showSparkLines);
        ((JSparklinesBarChartTableCellRenderer) proteinTable.getColumn("MS2 Quant.").getCellRenderer()).showNumbers(!showSparkLines);
        ((JSparklinesBarChartTableCellRenderer) proteinTable.getColumn("MW").getCellRenderer()).showNumbers(!showSparkLines);
        ((JSparklinesTwoValueBarChartTableCellRenderer) proteinTable.getColumn("#Peptides").getCellRenderer()).showNumbers(!showSparkLines);
        ((JSparklinesTwoValueBarChartTableCellRenderer) proteinTable.getColumn("#Spectra").getCellRenderer()).showNumbers(!showSparkLines);

        try {
            ((JSparklinesBarChartTableCellRenderer) proteinTable.getColumn("Confidence").getCellRenderer()).showNumbers(!showSparkLines);
        } catch (IllegalArgumentException e) {
            ((JSparklinesBarChartTableCellRenderer) proteinTable.getColumn("Score").getCellRenderer()).showNumbers(!showSparkLines);
        }

        ((JSparklinesBarChartTableCellRenderer) pdbMatchesJTable.getColumn("Chains").getCellRenderer()).showNumbers(!showSparkLines);

        ((JSparklinesBarChartTableCellRenderer) pdbChainsJTable.getColumn("Coverage").getCellRenderer()).showNumbers(!showSparkLines);
        ((JSparklinesIntervalChartTableCellRenderer) pdbChainsJTable.getColumn("PDB-Protein").getCellRenderer()).showNumbers(!showSparkLines);

        ((JSparklinesIntervalChartTableCellRenderer) peptideTable.getColumn("Start").getCellRenderer()).showNumbers(!showSparkLines);

        proteinTable.revalidate();
        proteinTable.repaint();

        peptideTable.revalidate();
        peptideTable.repaint();

        pdbMatchesJTable.revalidate();
        pdbMatchesJTable.repaint();
    }

    /**
     * Transforms the PDB accesion number into an HTML link to the PDB. Note
     * that this is a complete HTML with HTML and a href tags, where the main
     * use is to include it in the PDB tables.
     *
     * @param protein the PDB accession number to get the link for
     * @return the transformed accession number
     */
    private String addPdbDatabaseLink(String pdbAccession) {

        return "<html><a href=\"" + getPDBAccesionLink(pdbAccession)
                + "\"><font color=\"" + peptideShakerGUI.getNotSelectedRowHtmlTagFontColor() + "\">"
                + pdbAccession + "</font></a></html>";
    }

    /**
     * Returns the PDB accession number as a web link to the given structure at
     * http://www.rcsb.org.
     *
     * @param pdbAccession the PDB accession number
     * @return the PDB accession web link
     */
    public String getPDBAccesionLink(String pdbAccession) {
        return "http://www.rcsb.org/pdb/explore/explore.do?structureId=" + pdbAccession;
    }

    /**
     * Update the peptide to PDB mappings.
     */
    private void updatePeptideToPdbMapping() {

        setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));

        // clear the old mappings
        for (int i = 0; i < peptideTable.getRowCount() && !progressDialog.isRunCanceled(); i++) {
            peptideTable.setValueAt(false, i, peptideTable.getColumn("PDB").getModelIndex());
        }

        jmolPanel.getViewer().evalString("select all; color grey");

        // update the peptide selection
        int selectedChainIndex = (Integer) pdbChainsJTable.getValueAt(pdbChainsJTable.getSelectedRow(), 0);
        String currentChain = chains[selectedChainIndex - 1].getBlock();

        peptidePdbArray = new ArrayList<String>();

        // iterate the peptide table and highlight the covered areas
        for (int i = 0; i < peptideTable.getRowCount() && !progressDialog.isRunCanceled(); i++) {
            String peptideKey = peptideTableMap.get(getPeptideIndex(i));
            String peptideSequence = Peptide.getSequence(peptideKey);
            String tempSequence = proteinSequence;

            while (tempSequence.lastIndexOf(peptideSequence) >= 0 && !progressDialog.isRunCanceled()) {
                int peptideTempStart = tempSequence.lastIndexOf(peptideSequence) + 1;
                int peptideTempEnd = peptideTempStart + peptideSequence.length() - 1;

                jmolPanel.getViewer().evalString(
                        "select resno >=" + (peptideTempStart - chains[selectedChainIndex - 1].getDifference())
                        + " and resno <=" + (peptideTempEnd - chains[selectedChainIndex - 1].getDifference())
                        + " and chain = " + currentChain + "; color green");

                tempSequence = proteinSequence.substring(0, peptideTempEnd - 1);

                if (chainSequence.indexOf(peptideSequence) != -1) {
                    peptideTable.setValueAt(true, i, peptideTable.getColumn("PDB").getModelIndex());
                    peptidePdbArray.add(peptideKey);
                }
            }
        }


        // highlight the selected peptide
        String peptideKey = peptideTableMap.get(getPeptideIndex(peptideTable.getSelectedRow()));
        String peptideSequence = Peptide.getSequence(peptideKey);
        String tempSequence = proteinSequence;
        PTMFactory pmtFactory = PTMFactory.getInstance();

        while (tempSequence.lastIndexOf(peptideSequence) >= 0 && !progressDialog.isRunCanceled()) {

            int peptideTempStart = tempSequence.lastIndexOf(peptideSequence) + 1;
            int peptideTempEnd = peptideTempStart + peptideSequence.length() - 1;

            jmolPanel.getViewer().evalString(
                    "select resno >=" + (peptideTempStart - chains[selectedChainIndex - 1].getDifference())
                    + " and resno <=" + (peptideTempEnd - chains[selectedChainIndex - 1].getDifference())
                    + " and chain = " + currentChain + "; color blue");

            tempSequence = proteinSequence.substring(0, peptideTempEnd - 1);
        }


        // remove old labels
        jmolPanel.getViewer().evalString("select all; label off");


        // annotate the modified covered residues
        for (int i = 0; i < peptideTable.getRowCount() && !progressDialog.isRunCanceled(); i++) {
            peptideKey = peptideTableMap.get(getPeptideIndex(i));
            peptideSequence = Peptide.getSequence(peptideKey);
            tempSequence = proteinSequence;

            ArrayList<ModificationMatch> modifications = new ArrayList<ModificationMatch>();

            try {
                modifications = peptideShakerGUI.getIdentification().getPeptideMatch(peptideKey).getTheoreticPeptide().getModificationMatches();
            } catch (Exception e) {
                peptideShakerGUI.catchException(e);
                e.printStackTrace();
            }

            while (tempSequence.lastIndexOf(peptideSequence) >= 0 && !progressDialog.isRunCanceled()) {

                int peptideTempStart = tempSequence.lastIndexOf(peptideSequence) + 1;
                int peptideTempEnd = peptideTempStart + peptideSequence.length() - 1;

                int peptideIndex = 0;

                for (int j = peptideTempStart; j < peptideTempEnd && !progressDialog.isRunCanceled(); j++) {
                    for (int k = 0; k < modifications.size() && !progressDialog.isRunCanceled(); k++) {
                        PTM ptm = pmtFactory.getPTM(modifications.get(k).getTheoreticPtm());
                        if (ptm.getType() == PTM.MODAA && modifications.get(k).isVariable()) {
                            if (modifications.get(k).getModificationSite() == (peptideIndex + 1)) {

                                Color ptmColor = peptideShakerGUI.getSearchParameters().getModificationProfile().getPtmColors().get(
                                        modifications.get(k).getTheoreticPtm());

                                jmolPanel.getViewer().evalString(
                                        "select resno =" + (j - chains[selectedChainIndex - 1].getDifference())
                                        + " and chain = " + currentChain + "; color ["
                                        + ptmColor.getRed() + "," + ptmColor.getGreen() + "," + ptmColor.getBlue() + "]");

                                if (showModificationLabels) {
                                    jmolPanel.getViewer().evalString(
                                            "select resno =" + (j - chains[selectedChainIndex - 1].getDifference())
                                            + " and chain = " + currentChain + " and *.ca; color ["
                                            + ptmColor.getRed() + "," + ptmColor.getGreen() + "," + ptmColor.getBlue() + "];"
                                            + "label " + modifications.get(k).getTheoreticPtm());
                                }
                            }
                        }
                    }

                    peptideIndex++;
                }

                tempSequence = proteinSequence.substring(0, peptideTempEnd - 1);
            }
        }

        // resort the peptide table, required if sorted on the pdb column and the structure is changed
        peptideTable.getRowSorter().allRowsChanged();

        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }

    /**
     * Update the main match for the given row in the protein table.
     *
     * @param mainMatch the protein match to use
     * @param proteinInferenceType the protein inference group type
     */
    public void updateMainMatch(String mainMatch, int proteinInferenceType) {
        if (proteinTable.getRowCount() > 0) {
            DefaultTableModel dm = (DefaultTableModel) proteinTable.getModel();
            dm.fireTableDataChanged();
            updateSelection();
        }
    }

    /**
     * Turns the spinning of the model on or off.
     *
     * @param spin if true the spinning is turned on.
     */
    public void spinModel(boolean spin) {

        if (spin) {
            jmolPanel.getViewer().evalString("set spin y 20; spin");
        } else {
            jmolPanel.getViewer().evalString("spin off");
        }
    }

    /**
     * Returns the protein table.
     *
     * @return the protein table
     */
    public JTable getProteinTable() {
        return proteinTable;
    }

    /**
     * Returns the peptide table.
     *
     * @return the peptide table
     */
    public JTable getPeptideTable() {
        return peptideTable;
    }

    /**
     * Hides or displays the score columns in the protein and peptide tables.
     */
    public void updateScores() {
        ((DefaultTableModel) proteinTable.getModel()).fireTableStructureChanged();
        setTableProperties();

        if (peptideShakerGUI.getSelectedTab() == PeptideShakerGUI.STRUCTURES_TAB_INDEX) {
            this.updateSelection();
        }

        if (peptideShakerGUI.getDisplayPreferences().showScores()) {
            proteinTableToolTips.set(proteinTable.getColumnCount() - 2, "Protein Score");
        } else {
            proteinTableToolTips.set(proteinTable.getColumnCount() - 2, "Protein Confidence");
        }

        updateProteinTableCellRenderers();
    }

    /**
     * Update the PTM color coding.
     */
    public void updatePtmColors() {

        this.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));

        HashMap<String, Color> ptmColors = peptideShakerGUI.getSearchParameters().getModificationProfile().getPtmColors();

        try {
            // update the peptide table
            for (int i = 0; i < peptideTable.getRowCount(); i++) {
                String peptideKey = peptideTableMap.get(getPeptideIndex(i));
                String modifiedSequence = peptideShakerGUI.getIdentification().getPeptideMatch(peptideKey).getTheoreticPeptide().getModifiedSequenceAsHtml(ptmColors, true);
                peptideTable.setValueAt(modifiedSequence, i, peptideTable.getColumn("Sequence").getModelIndex());
            }
        } catch (Exception e) {
            peptideShakerGUI.catchException(e);
            e.printStackTrace();
        }

        if (peptideTable.getRowCount() > 0) {
            // update the 3D structure
            peptideTableMouseReleased(null);
        }

        this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }

    /**
     * Update the protein inference type for the currently selected peptide.
     *
     * @param proteinInferenceType
     */
    public void updatePeptideProteinInference(int proteinInferenceType) {
        peptideTable.setValueAt(proteinInferenceType, peptideTable.getSelectedRow(), peptideTable.getColumn("PI").getModelIndex());
    }

    /**
     * Export the table contents to the clipboard.
     *
     * @param index
     */
    private void copyTableContentToClipboardOrFile(TableIndex index) {

        final TableIndex tableIndex = index;

        if (tableIndex == TableIndex.PROTEIN_TABLE
                || tableIndex == TableIndex.PEPTIDE_TABLE
                || tableIndex == TableIndex.PDB_MATCHES
                || tableIndex == TableIndex.PDB_CHAINS) {

            try {
                OutputGenerator outputGenerator = new OutputGenerator(peptideShakerGUI);

                if (tableIndex == TableIndex.PROTEIN_TABLE) {
                    ArrayList<String> selectedProteins = getDisplayedProteins();
                    outputGenerator.getProteinsOutput(
                            null, selectedProteins, true, false, true, true, true,
                            true, true, true, true, false, true,
                            true, true, true, true, true, false, true, false);
                } else if (tableIndex == TableIndex.PEPTIDE_TABLE) {
                    ArrayList<String> selectedPeptides = getDisplayedPeptides();
                    String proteinKey = proteinKeys.get(proteinTable.convertRowIndexToModel(proteinTable.getSelectedRow()));
                    outputGenerator.getPeptidesOutput(
                            null, selectedPeptides, peptidePdbArray, true, false, true, true, true, true,
                            true, true, true, true, true, true, true, true, false, false, false, proteinKey);
                } else if (tableIndex == TableIndex.PDB_MATCHES || tableIndex == TableIndex.PDB_CHAINS) {

                    // get the file to send the output to
                    File selectedFile = peptideShakerGUI.getUserSelectedFile(".txt", "Tab separated text file (.txt)", "Export...", false);

                    if (selectedFile != null) {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile));

                        if (tableIndex == TableIndex.PDB_CHAINS) {

                            writer.write("\tChain\tPDB-Start\tPDB-End\tCoverage" + System.getProperty("line.separator"));

                            for (int i = 0; i < pdbChainsJTable.getRowCount(); i++) {
                                writer.write(pdbChainsJTable.getValueAt(i, 0) + "\t");
                                writer.write(pdbChainsJTable.getValueAt(i, 1) + "\t");
                                XYDataPoint pdbCoverage = (XYDataPoint) pdbChainsJTable.getValueAt(i, 2);
                                writer.write(pdbCoverage.getX() + "\t" + pdbCoverage.getY() + "\t");
                                writer.write(pdbChainsJTable.getValueAt(i, 3) + System.getProperty("line.separator"));
                            }

                            writer.close();
                            JOptionPane.showMessageDialog(peptideShakerGUI, "Data copied to file:\n" + selectedFile.getPath(), "Data Exported.", JOptionPane.INFORMATION_MESSAGE);
                        } else if (tableIndex == TableIndex.PDB_MATCHES) {
                            Util.tableToFile(pdbMatchesJTable, "\t", null, true, writer);
                            writer.close();
                            JOptionPane.showMessageDialog(peptideShakerGUI, "Data copied to file:\n" + selectedFile.getPath(), "Data Exported.", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            } catch (Exception e) {
                progressDialog.dispose();
                JOptionPane.showMessageDialog(peptideShakerGUI, "An error occurred while generating the output.", "Output Error.", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    /**
     * Update the selected protein and peptide.
     */
    public void updateSelection() {

        int proteinRow = 0;
        String proteinKey = peptideShakerGUI.getSelectedProteinKey();
        String peptideKey = peptideShakerGUI.getSelectedPeptideKey();
        String psmKey = peptideShakerGUI.getSelectedPsmKey();

        if (proteinKey.equals(PeptideShakerGUI.NO_SELECTION)
                && peptideKey.equals(PeptideShakerGUI.NO_SELECTION)
                && !psmKey.equals(PeptideShakerGUI.NO_SELECTION)) {
            if (peptideShakerGUI.getIdentification().matchExists(psmKey)) {
                try {
                    SpectrumMatch spectrumMatch = peptideShakerGUI.getIdentification().getSpectrumMatch(psmKey);
                    peptideKey = spectrumMatch.getBestAssumption().getPeptide().getKey();
                } catch (Exception e) {
                    peptideShakerGUI.catchException(e);
                    return;
                }
            } else {
                peptideShakerGUI.resetSelectedItems();
            }
        }

        if (proteinKey.equals(PeptideShakerGUI.NO_SELECTION)
                && !peptideKey.equals(PeptideShakerGUI.NO_SELECTION)) {
            for (String possibleKey : peptideShakerGUI.getIdentification().getProteinIdentification()) {
                try {
                    ProteinMatch proteinMatch = peptideShakerGUI.getIdentification().getProteinMatch(possibleKey);
                    if (proteinMatch.getPeptideMatches().contains(peptideKey)) {
                        proteinKey = possibleKey;
                        peptideShakerGUI.setSelectedItems(proteinKey, peptideKey, psmKey);
                        break;
                    }
                } catch (Exception e) {
                    peptideShakerGUI.catchException(e);
                    return;
                }
            }
        }

        if (!proteinKey.equals(PeptideShakerGUI.NO_SELECTION)) {
            proteinRow = getProteinRow(proteinKey);
        }

        if (proteinKeys.isEmpty()) {
            // For the silly people like me who happen to hide all proteins
            clearData();
            return;
        }

        if (proteinRow == -1) {
            peptideShakerGUI.resetSelectedItems();
        } else if (proteinTable.getSelectedRow() != proteinRow) {
            proteinTable.setRowSelectionInterval(proteinRow, proteinRow);
            proteinTable.scrollRectToVisible(proteinTable.getCellRect(proteinRow, 0, false));
            proteinTableMouseReleased(null);
        }

        // invoke later to give time for components to update
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                int peptideRow = 0;
                String peptideKey = peptideShakerGUI.getSelectedPeptideKey();
                if (!peptideKey.equals(PeptideShakerGUI.NO_SELECTION)) {
                    peptideRow = getPeptideRow(peptideKey);
                }

                if (peptideTable.getSelectedRow() != peptideRow && peptideRow != -1) {
                    peptideTable.setRowSelectionInterval(peptideRow, peptideRow);
                    peptideTable.scrollRectToVisible(peptideTable.getCellRect(peptideRow, 0, false));
                    peptideTableMouseReleased(null);
                }
            }
        });
    }

    /**
     * Provides to the PeptideShakerGUI instance the currently selected protein,
     * peptide and psm.
     */
    public void newItemSelection() {

        String proteinKey = PeptideShakerGUI.NO_SELECTION;
        String peptideKey = PeptideShakerGUI.NO_SELECTION;
        String psmKey = peptideShakerGUI.getSelectedPsmKey();

        if (proteinTable.getSelectedRow() != -1) {
            proteinKey = proteinKeys.get(proteinTable.convertRowIndexToModel(proteinTable.getSelectedRow()));
        }
        if (peptideTable.getSelectedRow() != -1) {
            peptideKey = peptideTableMap.get(getPeptideIndex(peptideTable.getSelectedRow()));
        }

        if (!proteinKey.equalsIgnoreCase(peptideShakerGUI.getSelectedProteinKey()) ||
                !peptideKey.equalsIgnoreCase(peptideShakerGUI.getSelectedPeptideKey())) {
            psmKey = PeptideShakerGUI.NO_SELECTION;
        }

        peptideShakerGUI.setSelectedItems(proteinKey, peptideKey, psmKey);
    }

    /**
     * Returns the row of a desired protein.
     *
     * @param proteinKey the key of the protein
     * @return the row of the desired protein
     */
    private int getProteinRow(String proteinKey) {
        int modelIndex = proteinKeys.indexOf(proteinKey);
        if (modelIndex >= 0) {
            return proteinTable.convertRowIndexToView(modelIndex);
        } else {
            return -1;
        }
    }

    /**
     * Returns the row of a desired peptide.
     *
     * @param proteinKey the key of the peptide
     * @return the row of the desired peptide
     */
    private int getPeptideRow(String peptideKey) {
        int index = -1;
        for (int key : peptideTableMap.keySet()) {
            if (peptideTableMap.get(key).equals(peptideKey)) {
                index = key;
                break;
            }
        }
        for (int row = 0; row < peptideTable.getRowCount(); row++) {
            if ((Integer) peptideTable.getValueAt(row, 0) == index) {
                return row;
            }
        }
        return -1;
    }

    /**
     * Clear all the data.
     */
    public void clearData() {

        DefaultTableModel dm = (DefaultTableModel) proteinTable.getModel();
        dm.getDataVector().removeAllElements();
        dm.fireTableDataChanged();

        dm = (DefaultTableModel) peptideTable.getModel();
        dm.getDataVector().removeAllElements();
        dm.fireTableDataChanged();

        dm = (DefaultTableModel) pdbMatchesJTable.getModel();
        dm.getDataVector().removeAllElements();
        dm.fireTableDataChanged();

        dm = (DefaultTableModel) pdbChainsJTable.getModel();
        dm.getDataVector().removeAllElements();
        dm.fireTableDataChanged();

        peptideTableMap = new HashMap<Integer, String>();

        peptidePdbArray = new ArrayList<String>();
        currentlyDisplayedPdbFile = null;

        // empty the jmol panel
        if (jmolStructureShown) {
            jmolPanel = new JmolPanel();
            pdbPanel.removeAll();
            pdbPanel.add(jmolPanel);
            pdbPanel.revalidate();
            pdbPanel.repaint();
            jmolStructureShown = false;
            currentlyDisplayedPdbFile = null;

            ((TitledBorder) pdbOuterPanel.getBorder()).setTitle(PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING + "PDB Structure" + PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING);
            pdbOuterPanel.repaint();
        }

        ((TitledBorder) proteinsPanel.getBorder()).setTitle(PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING + "Proteins" + PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING);
        ((TitledBorder) peptidesPanel.getBorder()).setTitle(PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING + "Peptides" + PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING);
        ((TitledBorder) pdbMatchesPanel.getBorder()).setTitle(PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING + "Peptide-Spectrum Matches" + PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING);
        ((TitledBorder) pdbChainsPanel.getBorder()).setTitle(PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING + "Spectrum & Fragment Ions" + PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING);
    }

    /**
     * Update the protein table cell renderers.
     */
    private void updateProteinTableCellRenderers() {

        if (peptideShakerGUI.getIdentification() != null) {

            ((JSparklinesTwoValueBarChartTableCellRenderer) proteinTable.getColumn("#Peptides").getCellRenderer()).setMaxValue(peptideShakerGUI.getMetrics().getMaxNPeptides());
            ((JSparklinesTwoValueBarChartTableCellRenderer) proteinTable.getColumn("#Spectra").getCellRenderer()).setMaxValue(peptideShakerGUI.getMetrics().getMaxNSpectra());
            ((JSparklinesBarChartTableCellRenderer) proteinTable.getColumn("MS2 Quant.").getCellRenderer()).setMaxValue(peptideShakerGUI.getMetrics().getMaxSpectrumCounting());
            ((JSparklinesBarChartTableCellRenderer) proteinTable.getColumn("MW").getCellRenderer()).setMaxValue(peptideShakerGUI.getMetrics().getMaxMW());

            try {
                ((JSparklinesBarChartTableCellRenderer) proteinTable.getColumn("Confidence").getCellRenderer()).setMaxValue(100.0);
            } catch (IllegalArgumentException e) {
                ((JSparklinesBarChartTableCellRenderer) proteinTable.getColumn("Score").getCellRenderer()).setMaxValue(100.0);
            }

            showSparkLines(peptideShakerGUI.showSparklines());
        }
    }
}
