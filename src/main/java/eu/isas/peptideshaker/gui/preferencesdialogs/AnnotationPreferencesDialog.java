package eu.isas.peptideshaker.gui.preferencesdialogs;

import com.compomics.util.experiment.biology.NeutralLoss;
import com.compomics.util.experiment.biology.ions.PeptideFragmentIon.PeptideFragmentIonType;
import eu.isas.peptideshaker.gui.HelpWindow;
import eu.isas.peptideshaker.gui.PeptideShakerGUI;
import eu.isas.peptideshaker.preferences.AnnotationPreferences;
import javax.swing.JOptionPane;

/**
 * A simple dialog for setting the spectrum annotation preferences.
 * 
 * @author Marc Vaudel
 * @author Harald Barsnes
 */
public class AnnotationPreferencesDialog extends javax.swing.JDialog {

    /**
     * The annotation preferences.
     */
    private AnnotationPreferences annotationPreferences;
    /**
     * The PeptideShakerGUI parent.
     */
    private PeptideShakerGUI peptideShakerGUI;

    /** 
     * Creates a new AnnotationPreferencesDialog.
     * 
     * @param peptideShakerGUI the PeptideShaker GUI parent
     */
    public AnnotationPreferencesDialog(PeptideShakerGUI peptideShakerGUI) {
        super(peptideShakerGUI, true);
        this.peptideShakerGUI = peptideShakerGUI;
        this.annotationPreferences = peptideShakerGUI.getAnnotationPreferences();
        initComponents();
        updateGUI();
        this.setLocationRelativeTo(peptideShakerGUI);
        setVisible(true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        annotationPreferencesHelpJButton = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        aBox = new javax.swing.JCheckBox();
        bBox = new javax.swing.JCheckBox();
        cBox = new javax.swing.JCheckBox();
        xBox = new javax.swing.JCheckBox();
        yBox = new javax.swing.JCheckBox();
        zBox = new javax.swing.JCheckBox();
        mhBox = new javax.swing.JCheckBox();
        immoniumBox = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        mzToleranceTxt = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        mostIntenseJCheckBox = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        h2oBox = new javax.swing.JCheckBox();
        nh3Box = new javax.swing.JCheckBox();
        h3po4Box = new javax.swing.JCheckBox();
        hpo3Box = new javax.swing.JCheckBox();
        ch4osBox = new javax.swing.JCheckBox();
        sequenceLossCheck = new javax.swing.JCheckBox();
        chargePanel = new javax.swing.JPanel();
        oneCharge = new javax.swing.JCheckBox();
        twoCharges = new javax.swing.JCheckBox();
        moreCharges = new javax.swing.JCheckBox();
        allSpectraCheck = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Spectrum Annotation");
        setResizable(false);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        annotationPreferencesHelpJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/help.GIF"))); // NOI18N
        annotationPreferencesHelpJButton.setToolTipText("Help");
        annotationPreferencesHelpJButton.setBorder(null);
        annotationPreferencesHelpJButton.setBorderPainted(false);
        annotationPreferencesHelpJButton.setContentAreaFilled(false);
        annotationPreferencesHelpJButton.setFocusable(false);
        annotationPreferencesHelpJButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        annotationPreferencesHelpJButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        annotationPreferencesHelpJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                annotationPreferencesHelpJButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                annotationPreferencesHelpJButtonMouseExited(evt);
            }
        });
        annotationPreferencesHelpJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                annotationPreferencesHelpJButtonActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Ion Type"));

        aBox.setText("a");

        bBox.setText("b");

        cBox.setText("c");

        xBox.setText("x");

        yBox.setText("y");

        zBox.setText("z");

        mhBox.setText("MH");

        immoniumBox.setText("Immonium Ions");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(aBox)
                    .addComponent(bBox)
                    .addComponent(cBox))
                .addGap(63, 63, 63)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(zBox)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(xBox)
                            .addComponent(yBox))
                        .addGap(71, 71, 71)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(immoniumBox)
                            .addComponent(mhBox))))
                .addContainerGap(225, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(aBox)
                    .addComponent(xBox)
                    .addComponent(mhBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bBox)
                    .addComponent(yBox)
                    .addComponent(immoniumBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cBox)
                    .addComponent(zBox)))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Peak Matching"));

        jLabel2.setText("m/z Accuracy:");

        mzToleranceTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        mzToleranceTxt.setText("0.5");

        jLabel3.setText("Da");

        mostIntenseJCheckBox.setText("Annotate Most Intense Peaks");
        mostIntenseJCheckBox.setIconTextGap(15);
        mostIntenseJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostIntenseJCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mostIntenseJCheckBox)
                .addGap(28, 28, 28)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(mzToleranceTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addContainerGap(123, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(mzToleranceTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(mostIntenseJCheckBox))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Standard Settings", jPanel4);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Neutral Losses"));

        h2oBox.setText("H2O");

        nh3Box.setText("NH3");

        h3po4Box.setText("H3PO4");

        hpo3Box.setText("HPO3");

        ch4osBox.setText("CH4OS");

        sequenceLossCheck.setText("Adapt neutral losses to the sequence and modifications");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(h2oBox)
                            .addComponent(nh3Box))
                        .addGap(51, 51, 51)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(h3po4Box)
                            .addComponent(hpo3Box))
                        .addGap(99, 99, 99)
                        .addComponent(ch4osBox))
                    .addComponent(sequenceLossCheck))
                .addContainerGap(203, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(h2oBox)
                    .addComponent(h3po4Box)
                    .addComponent(ch4osBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nh3Box)
                    .addComponent(hpo3Box))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sequenceLossCheck))
        );

        chargePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Fragment Ion Charge"));

        oneCharge.setText("1+");

        twoCharges.setText("2+");

        moreCharges.setText(">2");

        javax.swing.GroupLayout chargePanelLayout = new javax.swing.GroupLayout(chargePanel);
        chargePanel.setLayout(chargePanelLayout);
        chargePanelLayout.setHorizontalGroup(
            chargePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chargePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(oneCharge)
                .addGap(18, 18, 18)
                .addComponent(twoCharges)
                .addGap(18, 18, 18)
                .addComponent(moreCharges)
                .addContainerGap(365, Short.MAX_VALUE))
        );
        chargePanelLayout.setVerticalGroup(
            chargePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chargePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(chargePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(oneCharge)
                    .addComponent(twoCharges)
                    .addComponent(moreCharges))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        allSpectraCheck.setText("Always use these settings for this project");
        allSpectraCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allSpectraCheckActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(305, Short.MAX_VALUE)
                .addComponent(allSpectraCheck)
                .addContainerGap())
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(chargePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chargePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(allSpectraCheck)
                .addGap(46, 46, 46))
        );

        jTabbedPane1.addTab("Advanced Settings", jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 541, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(annotationPreferencesHelpJButton)
                .addGap(380, 380, 380)
                .addComponent(okButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(annotationPreferencesHelpJButton)
                        .addComponent(okButton))
                    .addComponent(cancelButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Close the dialog and update the spectrum annotations.
     * 
     * @param evt 
     */
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        if (validateInput()) {
            annotationPreferences.clearIonTypes();
            if (aBox.isSelected()) {
                annotationPreferences.addIonType(PeptideFragmentIonType.A_ION);
            }
            if (bBox.isSelected()) {
                annotationPreferences.addIonType(PeptideFragmentIonType.B_ION);
            }
            if (cBox.isSelected()) {
                annotationPreferences.addIonType(PeptideFragmentIonType.C_ION);
            }
            if (xBox.isSelected()) {
                annotationPreferences.addIonType(PeptideFragmentIonType.X_ION);
            }
            if (yBox.isSelected()) {
                annotationPreferences.addIonType(PeptideFragmentIonType.Y_ION);
            }
            if (zBox.isSelected()) {
                annotationPreferences.addIonType(PeptideFragmentIonType.Z_ION);
            }
            if (mhBox.isSelected()) {
                annotationPreferences.addIonType(PeptideFragmentIonType.PRECURSOR_ION);
            }
            if (immoniumBox.isSelected()) {
                annotationPreferences.addIonType(PeptideFragmentIonType.IMMONIUM);
            }
            annotationPreferences.annotateMostIntensePeaks(mostIntenseJCheckBox.isSelected());
            annotationPreferences.setMzTolerance(new Double(mzToleranceTxt.getText().trim()));

            annotationPreferences.clearNeutralLosses();
            if (h2oBox.isSelected()) {
                annotationPreferences.addNeutralLoss(NeutralLoss.H2O);
            }
            if (nh3Box.isSelected()) {
                annotationPreferences.addNeutralLoss(NeutralLoss.NH3);
            }
            if (h3po4Box.isSelected()) {
                annotationPreferences.addNeutralLoss(NeutralLoss.H3PO4);
            }
            if (hpo3Box.isSelected()) {
                annotationPreferences.addNeutralLoss(NeutralLoss.HPO3);
            }
            if (ch4osBox.isSelected()) {
                annotationPreferences.addNeutralLoss(NeutralLoss.CH4OS);
            }
            
            annotationPreferences.useDefaultAnnotation(!allSpectraCheck.isSelected());
            
            annotationPreferences.clearCharges();
            if (oneCharge.isSelected()) {
                annotationPreferences.addSelectedCharge(1);
            }
            if (twoCharges.isSelected()) {
                annotationPreferences.addSelectedCharge(2);
            }
            if (moreCharges.isSelected()) {
                int precursorCharge = annotationPreferences.getCurrentPrecursorCharge();
                if (precursorCharge > 2) {
                    for (int charge = 3; charge < precursorCharge ; charge ++) {
                        annotationPreferences.addSelectedCharge(charge);
                    }
                }
            }
            peptideShakerGUI.setAnnotationPreferences(annotationPreferences);
            peptideShakerGUI.updateAnnotations();
            peptideShakerGUI.setDataSaved(false);
            dispose();
        }
    }//GEN-LAST:event_okButtonActionPerformed

    /**
     * Closes the dialog without saving.
     * 
     * @param evt 
     */
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Updates the annotation preferences.
     * 
     * @param evt 
     */
    private void mostIntenseJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mostIntenseJCheckBoxActionPerformed
    }//GEN-LAST:event_mostIntenseJCheckBoxActionPerformed

    /**
     * Change the cursor to a hand cursor.
     * 
     * @param evt 
     */
    private void annotationPreferencesHelpJButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_annotationPreferencesHelpJButtonMouseEntered
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
}//GEN-LAST:event_annotationPreferencesHelpJButtonMouseEntered

    /**
     * Change the cursor back to the default cursor.
     * 
     * @param evt 
     */
    private void annotationPreferencesHelpJButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_annotationPreferencesHelpJButtonMouseExited
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
}//GEN-LAST:event_annotationPreferencesHelpJButtonMouseExited

    /**
     * Open the help dialog.
     * 
     * @param evt 
     */
    private void annotationPreferencesHelpJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_annotationPreferencesHelpJButtonActionPerformed
        setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        new HelpWindow(peptideShakerGUI, getClass().getResource("/helpFiles/AnnotationPreferences.html"));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
}//GEN-LAST:event_annotationPreferencesHelpJButtonActionPerformed

    private void allSpectraCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allSpectraCheckActionPerformed

    }//GEN-LAST:event_allSpectraCheckActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox aBox;
    private javax.swing.JCheckBox allSpectraCheck;
    private javax.swing.JButton annotationPreferencesHelpJButton;
    private javax.swing.JCheckBox bBox;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox cBox;
    private javax.swing.JButton cancelButton;
    private javax.swing.JCheckBox ch4osBox;
    private javax.swing.JPanel chargePanel;
    private javax.swing.JCheckBox h2oBox;
    private javax.swing.JCheckBox h3po4Box;
    private javax.swing.JCheckBox hpo3Box;
    private javax.swing.JCheckBox immoniumBox;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JCheckBox mhBox;
    private javax.swing.JCheckBox moreCharges;
    private javax.swing.JCheckBox mostIntenseJCheckBox;
    private javax.swing.JTextField mzToleranceTxt;
    private javax.swing.JCheckBox nh3Box;
    private javax.swing.JButton okButton;
    private javax.swing.JCheckBox oneCharge;
    private javax.swing.JCheckBox sequenceLossCheck;
    private javax.swing.JCheckBox twoCharges;
    private javax.swing.JCheckBox xBox;
    private javax.swing.JCheckBox yBox;
    private javax.swing.JCheckBox zBox;
    // End of variables declaration//GEN-END:variables

    /**
     * Refresh the selection,
     */
    private void updateGUI() {
        mostIntenseJCheckBox.setSelected(annotationPreferences.shallAnnotateMostIntensePeaks());
        mzToleranceTxt.setText(annotationPreferences.getMzTolerance() + "");
        aBox.setSelected(false);
        bBox.setSelected(false);
        cBox.setSelected(false);
        xBox.setSelected(false);
        yBox.setSelected(false);
        zBox.setSelected(false);
        mhBox.setSelected(false);
        immoniumBox.setSelected(false);
        for (PeptideFragmentIonType ionType : annotationPreferences.getIonTypes()) {
            if (ionType == PeptideFragmentIonType.A_ION) {
                aBox.setSelected(true);
            } else if (ionType == PeptideFragmentIonType.B_ION) {
                bBox.setSelected(true);
            } else if (ionType == PeptideFragmentIonType.C_ION) {
                cBox.setSelected(true);
            } else if (ionType == PeptideFragmentIonType.X_ION) {
                xBox.setSelected(true);
            } else if (ionType == PeptideFragmentIonType.Y_ION) {
                yBox.setSelected(true);
            } else if (ionType == PeptideFragmentIonType.Z_ION) {
                zBox.setSelected(true);
            } else if (ionType == PeptideFragmentIonType.PRECURSOR_ION) {
                mhBox.setSelected(true);
            } else if (ionType == PeptideFragmentIonType.IMMONIUM) {
                immoniumBox.setSelected(true);
            }
        }
        h2oBox.setSelected(false);
        nh3Box.setSelected(false);
        h3po4Box.setSelected(false);
        hpo3Box.setSelected(false);
        ch4osBox.setSelected(false);
        for (NeutralLoss neutralLoss : annotationPreferences.getNeutralLosses().keySet()) {
            if (neutralLoss.isSameAs(NeutralLoss.H2O)) {
                h2oBox.setSelected(true);
            } else if (neutralLoss.isSameAs(NeutralLoss.NH3)) {
                nh3Box.setSelected(true);
            } else if (neutralLoss.isSameAs(NeutralLoss.H3PO4)) {
                h3po4Box.setSelected(true);
            } else if (neutralLoss.isSameAs(NeutralLoss.HPO3)) {
                hpo3Box.setSelected(true);
            } else if (neutralLoss.isSameAs(NeutralLoss.CH4OS)) {
                ch4osBox.setSelected(true);
            }
        }
        oneCharge.setSelected(false);
        twoCharges.setSelected(false);
        moreCharges.setSelected(false);
        for (int charge : annotationPreferences.getValidatedCharges()) {
            if (charge == 1) {
                oneCharge.setSelected(true);
            } else if (charge == 2) {
                twoCharges.setSelected(true);
            } else if (charge > 2) {
                moreCharges.setSelected(true);
            }
        }
        sequenceLossCheck.setSelected(annotationPreferences.areNeutralLossesSequenceDependant());
        allSpectraCheck.setSelected(!annotationPreferences.useDefaultAnnotation());
    }

    /**
     * Validate the annotation accuracy.
     * 
     * @return true if validated
     */
    private boolean validateInput() {
        try {
            new Double(mzToleranceTxt.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please verify the input for max mass deviation.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
