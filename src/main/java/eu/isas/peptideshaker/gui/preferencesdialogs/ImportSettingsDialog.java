package eu.isas.peptideshaker.gui.preferencesdialogs;

import com.compomics.util.gui.renderers.AlignedListCellRenderer;
import eu.isas.peptideshaker.fileimport.IdFilter;
import eu.isas.peptideshaker.gui.HelpDialog;
import eu.isas.peptideshaker.gui.NewDialog;
import eu.isas.peptideshaker.gui.PeptideShakerGUI;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

/**
 * The import settings dialog.
 * 
 * @author Marc Vaudel
 * @author Harald Barsnes
 */
public class ImportSettingsDialog extends javax.swing.JDialog {

    /**
     * PeptideShaker GUI
     */
    private PeptideShakerGUI peptideShakerGUI;
    /**
     * If true the user can edit the settings.
     */
    private boolean editable;
    /**
     * A reference to the NewDialog.
     */
    private NewDialog newDialog;

    /**
     * Creates a new ImportSettingsDialog.
     * 
     * @param peptideShakerGUI the PeptideShaker GUI
     * @param newDialog the NewDialog, can be null
     * @param editable boolean indicating whether the parameters can be editable
     */
    public ImportSettingsDialog(PeptideShakerGUI peptideShakerGUI, NewDialog newDialog, boolean editable) {
        super(peptideShakerGUI, true);
        this.peptideShakerGUI = peptideShakerGUI;
        this.newDialog = newDialog;
        this.editable = editable;

        initComponents();

        unitCmb.setRenderer(new AlignedListCellRenderer(SwingConstants.CENTER));

        IdFilter idFilter = peptideShakerGUI.getIdFilter();
        omssaEvalueTxt.setText(idFilter.getOmssaMaxEvalue() + "");
        xtandemEvalueTxt.setText(idFilter.getXtandemMaxEvalue() + "");
        mascotEvalueTxt.setText(idFilter.getMascotMaxEvalue() + "");
        nAAminTxt.setText(idFilter.getMinPepLength() + "");
        nAAmaxTxt.setText(idFilter.getMaxPepLength() + "");
        precDevTxt.setText(idFilter.getMaxMzDeviation() + "");
        ptmsCheck.setSelected(idFilter.removeUnknownPTMs());

        if (idFilter.isIsPpm()) {
            unitCmb.setSelectedIndex(0);
        } else {
            unitCmb.setSelectedIndex(1);
        }

        omssaEvalueTxt.setEditable(editable);
        xtandemEvalueTxt.setEditable(editable);
        mascotEvalueTxt.setEditable(editable);
        nAAminTxt.setEditable(editable);
        nAAmaxTxt.setEditable(editable);
        precDevTxt.setEditable(editable);
        unitCmb.setEnabled(editable);
        cancelButton.setEnabled(editable);

        setLocationRelativeTo(peptideShakerGUI);
        setVisible(true);
    }

    /**
     * Indicates whether the input is correct.
     * 
     * @return a boolean indicating whether the input is correct
     */
    private boolean validateInput() {
        try {
            new Double(omssaEvalueTxt.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please verify the input for OMSSA maximal e-value.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            new Double(mascotEvalueTxt.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please verify the input for Mascot maximal e-value.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            new Double(xtandemEvalueTxt.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please verify the input for X!Tandem maximal e-value.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            new Integer(nAAminTxt.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please verify the input for the minimal peptide length.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            new Integer(nAAmaxTxt.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please verify the input for the maximal peptide length.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            new Double(precDevTxt.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please verify the input for the precursor maximal deviation.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        nAAmaxTxt = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        nAAminTxt = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        unitCmb = new javax.swing.JComboBox();
        precDevTxt = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        xtandemEvalueTxt = new javax.swing.JTextField();
        omssaEvalueTxt = new javax.swing.JTextField();
        mascotEvalueTxt = new javax.swing.JTextField();
        ptmsCheck = new javax.swing.JCheckBox();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        helpJButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Import Filters");
        setBackground(new java.awt.Color(230, 230, 230));

        jPanel2.setBackground(new java.awt.Color(230, 230, 230));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Filters"));
        jPanel1.setOpaque(false);

        jLabel1.setText("OMSSA Max e-value:");

        jLabel2.setText("X!Tandem Max e-value:");

        jLabel3.setText("Mascot Max e-value:");

        nAAmaxTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        nAAmaxTxt.setText("jTextField1");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("-");

        nAAminTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        nAAminTxt.setText("jTextField2");

        jLabel5.setText("Peptide Length:");

        unitCmb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ppm", "Da" }));

        precDevTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        precDevTxt.setText("jTextField3");

        jLabel6.setText("Precursor Accuracy:");

        xtandemEvalueTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        xtandemEvalueTxt.setText("jTextField4");

        omssaEvalueTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        omssaEvalueTxt.setText("jTextField5");

        mascotEvalueTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        mascotEvalueTxt.setText("jTextField6");

        ptmsCheck.setText("Exclude Unknown PTMs");
        ptmsCheck.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ptmsCheck.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        ptmsCheck.setIconTextGap(10);
        ptmsCheck.setOpaque(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(precDevTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                            .addComponent(nAAminTxt))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nAAmaxTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(unitCmb, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addComponent(jLabel2)
                                .addComponent(jLabel3))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(omssaEvalueTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                                .addComponent(mascotEvalueTxt)
                                .addComponent(xtandemEvalueTxt))))
                    .addComponent(ptmsCheck, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(omssaEvalueTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(xtandemEvalueTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(mascotEvalueTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nAAmaxTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nAAminTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(unitCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(precDevTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ptmsCheck)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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

        helpJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/help.GIF"))); // NOI18N
        helpJButton.setToolTipText("Help");
        helpJButton.setBorder(null);
        helpJButton.setBorderPainted(false);
        helpJButton.setContentAreaFilled(false);
        helpJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                helpJButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                helpJButtonMouseExited(evt);
            }
        });
        helpJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpJButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(helpJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(okButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(helpJButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(okButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Closes the dialog.
     * 
     * @param evt 
     */
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Update the settings.
     * 
     * @param evt 
     */
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        if (editable) {
            if (validateInput()) {
                peptideShakerGUI.setIdFilter(new IdFilter(
                        new Integer(nAAminTxt.getText()),
                        new Integer(nAAmaxTxt.getText()),
                        new Double(mascotEvalueTxt.getText()),
                        new Double(omssaEvalueTxt.getText()),
                        new Double(xtandemEvalueTxt.getText()),
                        new Double(precDevTxt.getText()),
                        unitCmb.getSelectedIndex() == 0,
                        ptmsCheck.isSelected()));
                
                if (newDialog != null) {
                    newDialog.updateFilterSettingsField("User Edit");
                }
                
                dispose();
            }
        } else {
            dispose();
        }
    }//GEN-LAST:event_okButtonActionPerformed

    /**
     * Change the cursor to a hand cursor.
     * 
     * @param evt 
     */
    private void helpJButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_helpJButtonMouseEntered
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_helpJButtonMouseEntered

    /**
     * Change the cursor back to the default cursor.
     * 
     * @param evt 
     */
    private void helpJButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_helpJButtonMouseExited
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_helpJButtonMouseExited

    /**
     * Open the help dialog.
     * 
     * @param evt 
     */
    private void helpJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpJButtonActionPerformed
        setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        new HelpDialog(peptideShakerGUI, getClass().getResource("/helpFiles/FilterSettings.html"));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_helpJButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton helpJButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField mascotEvalueTxt;
    private javax.swing.JTextField nAAmaxTxt;
    private javax.swing.JTextField nAAminTxt;
    private javax.swing.JButton okButton;
    private javax.swing.JTextField omssaEvalueTxt;
    private javax.swing.JTextField precDevTxt;
    private javax.swing.JCheckBox ptmsCheck;
    private javax.swing.JComboBox unitCmb;
    private javax.swing.JTextField xtandemEvalueTxt;
    // End of variables declaration//GEN-END:variables
}
