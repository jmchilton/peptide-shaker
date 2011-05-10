/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SpectrumPanel.java
 *
 * Created on May 9, 2011, 4:04:56 PM
 */
package eu.isas.peptideshaker.gui.tabpanels;

import com.compomics.util.experiment.biology.Protein;
import com.compomics.util.experiment.identification.Advocate;
import com.compomics.util.experiment.identification.Identification;
import com.compomics.util.experiment.identification.PeptideAssumption;
import com.compomics.util.experiment.identification.matches.ModificationMatch;
import com.compomics.util.experiment.identification.matches.SpectrumMatch;
import com.compomics.util.experiment.massspectrometry.MSnSpectrum;
import com.compomics.util.experiment.massspectrometry.Precursor;
import com.compomics.util.experiment.massspectrometry.Spectrum;
import com.compomics.util.experiment.massspectrometry.SpectrumCollection;
import eu.isas.peptideshaker.gui.PeptideShakerGUI;
import eu.isas.peptideshaker.myparameters.PSParameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import uk.ac.ebi.jmzml.xml.io.MzMLUnmarshallerException;

/**
 *
 * @author vaudel
 */
public class SpectrumIdentificationPanel extends javax.swing.JPanel {

    /** Creates new form SpectrumPanel */
    public SpectrumIdentificationPanel(PeptideShakerGUI peptideShakerGUI) {
        this.peptideShakerGUI = peptideShakerGUI;
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        seTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        fileNamesCmb = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        spectrumTable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        psTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        mascotTable = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        omssaTable = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        xTandemTable = new javax.swing.JTable();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Search Engine Performance"));

        seTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Search Engine", "number of validated PSMs"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(seTable);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(617, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Spectrum Selection"));

        fileNamesCmb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "File name" }));
        fileNamesCmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileNamesCmbActionPerformed(evt);
            }
        });

        spectrumTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title", "Precursor m/z", "Precursor charge", "Precursor RT"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        spectrumTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                spectrumTableMouseReleased(evt);
            }
        });
        spectrumTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                spectrumTableKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(spectrumTable);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fileNamesCmb, 0, 260, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileNamesCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Spectrum"));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 777, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 471, Short.MAX_VALUE)
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Peptide to Spectrum Matches"));

        psTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Protein(s)", "Sequence", "Modifications", "Score", "Confidence", "delta p", "Validated"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(psTable);

        jLabel1.setText("Peptide-Shaker:");

        jLabel2.setText("Mascot:");

        jLabel3.setText("OMSSA:");

        jLabel4.setText("X!Tandem:");

        mascotTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Rank", "Protein(s)", "Peptide(s)", "Modification(s)", "e-Value", "confidence"
            }
        ));
        jScrollPane3.setViewportView(mascotTable);

        omssaTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Rank", "Protein(s)", "Peptide", "Modification(s)", "e-value", "confidence"
            }
        ));
        jScrollPane4.setViewportView(omssaTable);

        xTandemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Protein(s)", "Peptide", "Modification(s)", "e-value", "confidence"
            }
        ));
        jScrollPane5.setViewportView(xTandemTable);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1049, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(374, 374, 374))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jScrollPane4, 0, 0, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(278, 278, 278))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, 0, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void fileNamesCmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileNamesCmbActionPerformed
        fileSelectionChanged();
    }//GEN-LAST:event_fileNamesCmbActionPerformed

    private void spectrumTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_spectrumTableMouseReleased
        spectrumSelectionChanged();
    }//GEN-LAST:event_spectrumTableMouseReleased

    private void spectrumTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_spectrumTableKeyReleased
        spectrumSelectionChanged();
    }//GEN-LAST:event_spectrumTableKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox fileNamesCmb;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable mascotTable;
    private javax.swing.JTable omssaTable;
    private javax.swing.JTable psTable;
    private javax.swing.JTable seTable;
    private javax.swing.JTable spectrumTable;
    private javax.swing.JTable xTandemTable;
    // End of variables declaration//GEN-END:variables
    /**
     * The main GUI
     */
    private PeptideShakerGUI peptideShakerGUI;
    /**
     * The spectrum collection
     */
    private SpectrumCollection spectrumCollection;
    /**
     * The identification
     */
    private Identification identification;
    /**
     * The spectra indexed by their file name
     */
    private HashMap<String, ArrayList<String>> filesMap = new HashMap<String, ArrayList<String>>();

    /**
     * Displays the results on the panel
     */
    public void displayResults() {
        spectrumCollection = peptideShakerGUI.getSpectrumCollection();
        identification = peptideShakerGUI.getIdentification();
        int nMascot = 0;
        int nOMSSA = 0;
        int nXTandem = 0;
        PSParameter probabilities = new PSParameter();
        for (SpectrumMatch spectrumMatch : identification.getSpectrumIdentification().values()) {
            probabilities = (PSParameter) spectrumMatch.getUrParam(probabilities);
            if (probabilities.isValidated()) {
                if (spectrumMatch.getFirstHit(Advocate.MASCOT) != null) {
                    if (spectrumMatch.getFirstHit(Advocate.MASCOT).getPeptide().isSameAs(spectrumMatch.getBestAssumption().getPeptide())) {
                        nMascot++;
                    }
                }
                if (spectrumMatch.getFirstHit(Advocate.OMSSA) != null) {
                    if (spectrumMatch.getFirstHit(Advocate.OMSSA).getPeptide().isSameAs(spectrumMatch.getBestAssumption().getPeptide())) {
                        nOMSSA++;
                    }
                }
                if (spectrumMatch.getFirstHit(Advocate.XTANDEM) != null) {
                    if (spectrumMatch.getFirstHit(Advocate.XTANDEM).getPeptide().isSameAs(spectrumMatch.getBestAssumption().getPeptide())) {
                        nXTandem++;
                    }
                }
            }
        }
        ((DefaultTableModel) seTable.getModel()).addRow(new Object[]{
                    "Mascot",
                    nMascot
                });
        ((DefaultTableModel) seTable.getModel()).addRow(new Object[]{
                    "OMSSA",
                    nOMSSA
                });
        ((DefaultTableModel) seTable.getModel()).addRow(new Object[]{
                    "X!Tandem",
                    nXTandem
                });
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                seTable.revalidate();
                seTable.repaint();
            }
        });

        String fileName;
        for (String key : spectrumCollection.getAllKeys()) {
            fileName = Spectrum.getSpectrumFile(key);
            if (!filesMap.containsKey(fileName)) {
                filesMap.put(fileName, new ArrayList<String>());
            }
            filesMap.get(fileName).add(key);
        }
        String[] filesArray = new String[filesMap.keySet().size()];
        int cpt = 0;
        for (String tempName : filesMap.keySet()) {
            filesArray[cpt] = tempName;
            cpt++;
        }
        fileNamesCmb.setModel(new DefaultComboBoxModel(filesArray));

        fileSelectionChanged();
    }

    /**
     * Method called whenever the file selection changed
     */
    private void fileSelectionChanged() {
        try {
            while (spectrumTable.getRowCount() > 0) {
                ((DefaultTableModel) spectrumTable.getModel()).removeRow(0);
            }
            String fileSelected = (String) fileNamesCmb.getSelectedItem();
            MSnSpectrum spectrum;
            Precursor precursor;
            for (String spectrumKey : filesMap.get(fileSelected)) {
                spectrum = (MSnSpectrum) spectrumCollection.getSpectrum(spectrumKey);
                precursor = spectrum.getPrecursor();
                ((DefaultTableModel) spectrumTable.getModel()).addRow(new Object[]{
                            spectrum.getSpectrumTitle(),
                            precursor.getMz(),
                            precursor.getCharge().toString(),
                            precursor.getRt()
                        });
            }
            spectrumTable.setRowSelectionInterval(0, 0);
            spectrumSelectionChanged();
        } catch (MzMLUnmarshallerException e) {
            JOptionPane.showMessageDialog(this, "Error while importing mzML data.", "Peak Lists Error", JOptionPane.INFORMATION_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Method called whenever the spectrum selection changed
     */
    private void spectrumSelectionChanged() {
        String key = Spectrum.getSpectrumKey((String) fileNamesCmb.getSelectedItem(), (String) spectrumTable.getValueAt(spectrumTable.getSelectedRow(), 0));
        SpectrumMatch spectrumMatch = identification.getSpectrumIdentification().get(key);
        PSParameter probabilities = new PSParameter();
        probabilities = (PSParameter) spectrumMatch.getUrParam(probabilities);

        while (psTable.getRowCount() > 0) {
            ((DefaultTableModel) psTable.getModel()).removeRow(0);
        }
        while (omssaTable.getRowCount() > 0) {
            ((DefaultTableModel) psTable.getModel()).removeRow(0);
        }
        while (mascotTable.getRowCount() > 0) {
            ((DefaultTableModel) psTable.getModel()).removeRow(0);
        }
        while (xTandemTable.getRowCount() > 0) {
            ((DefaultTableModel) psTable.getModel()).removeRow(0);
        }

        // Fill peptide shaker table
        String proteins = "";
        for (Protein protein : spectrumMatch.getBestAssumption().getPeptide().getParentProteins()) {
            proteins += protein.getAccession() + " ";
        }
        String modifications = "";
        boolean firstline = true;
        for (ModificationMatch modificationMatch : spectrumMatch.getBestAssumption().getPeptide().getModificationMatches()) {
            if (!firstline) {
                modifications += ", ";
            } else {
                firstline = false;
            }
            modifications += modificationMatch.getTheoreticPtm().getName() + " (" + modificationMatch.getModificationSite() + ")";
        }
        ((DefaultTableModel) psTable.getModel()).addRow(new Object[]{
                    proteins,
                    spectrumMatch.getBestAssumption().getPeptide().getSequence(),
                    modifications,
                    probabilities.getPsmScore(),
                    probabilities.getPsmConfidence(),
                    0,
                    probabilities.isValidated()
                });

        // Fill Mascot table
        if (spectrumMatch.getAllAssumptions(Advocate.MASCOT) != null) {
            ArrayList<Double> eValues = new ArrayList<Double>(spectrumMatch.getAllAssumptions(Advocate.MASCOT).keySet());
            Collections.sort(eValues);
            PeptideAssumption currentAssumption;
            int rank = 0;
            for (double eValue : eValues) {
                currentAssumption = spectrumMatch.getAllAssumptions(Advocate.MASCOT).get(eValue);
                proteins = "";
                for (Protein protein : currentAssumption.getPeptide().getParentProteins()) {
                    proteins += protein.getAccession() + " ";
                }
                modifications = "";
                firstline = true;
                for (ModificationMatch modificationMatch : currentAssumption.getPeptide().getModificationMatches()) {
                    if (!firstline) {
                        modifications += ", ";
                    } else {
                        firstline = false;
                    }
                    modifications += modificationMatch.getTheoreticPtm().getName() + " (" + modificationMatch.getModificationSite() + ")";
                }
                ((DefaultTableModel) mascotTable.getModel()).addRow(new Object[]{
                            ++rank,
                            proteins,
                            currentAssumption.getPeptide().getSequence(),
                            modifications,
                            currentAssumption.getEValue(),
                            0
                        });
            }
        }

        // Fill OMSSA table

        if (spectrumMatch.getAllAssumptions(Advocate.OMSSA) != null) {
            ArrayList<Double> eValues = new ArrayList<Double>(spectrumMatch.getAllAssumptions(Advocate.OMSSA).keySet());
            Collections.sort(eValues);
            PeptideAssumption currentAssumption;
            int rank = 0;
            for (double eValue : eValues) {
                currentAssumption = spectrumMatch.getAllAssumptions(Advocate.OMSSA).get(eValue);
                proteins = "";
                for (Protein protein : currentAssumption.getPeptide().getParentProteins()) {
                    proteins += protein.getAccession() + " ";
                }
                modifications = "";
                firstline = true;
                for (ModificationMatch modificationMatch : currentAssumption.getPeptide().getModificationMatches()) {
                    if (!firstline) {
                        modifications += ", ";
                    } else {
                        firstline = false;
                    }
                    modifications += modificationMatch.getTheoreticPtm().getName() + " (" + modificationMatch.getModificationSite() + ")";
                }
                ((DefaultTableModel) omssaTable.getModel()).addRow(new Object[]{
                            ++rank,
                            proteins,
                            currentAssumption.getPeptide().getSequence(),
                            modifications,
                            currentAssumption.getEValue(),
                            0
                        });
            }
        }

        // Fill X!Tandem table

        if (spectrumMatch.getAllAssumptions(Advocate.XTANDEM) != null) {
            ArrayList<Double> eValues = new ArrayList<Double>(spectrumMatch.getAllAssumptions(Advocate.XTANDEM).keySet());
            Collections.sort(eValues);
            PeptideAssumption currentAssumption;
            int rank = 0;
            for (double eValue : eValues) {
                currentAssumption = spectrumMatch.getAllAssumptions(Advocate.XTANDEM).get(eValue);
                proteins = "";
                for (Protein protein : currentAssumption.getPeptide().getParentProteins()) {
                    proteins += protein.getAccession() + " ";
                }
                modifications = "";
                firstline = true;
                for (ModificationMatch modificationMatch : currentAssumption.getPeptide().getModificationMatches()) {
                    if (!firstline) {
                        modifications += ", ";
                    } else {
                        firstline = false;
                    }
                    modifications += modificationMatch.getTheoreticPtm().getName() + " (" + modificationMatch.getModificationSite() + ")";
                }
                ((DefaultTableModel) xTandemTable.getModel()).addRow(new Object[]{
                            ++rank,
                            proteins,
                            currentAssumption.getPeptide().getSequence(),
                            modifications,
                            currentAssumption.getEValue(),
                            0
                        });
            }
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                psTable.revalidate();
                psTable.repaint();
                mascotTable.revalidate();
                mascotTable.repaint();
                xTandemTable.revalidate();
                xTandemTable.repaint();
                omssaTable.revalidate();
                omssaTable.repaint();
            }
        });
    }
}
