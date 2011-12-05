package eu.isas.peptideshaker.gui;

import com.compomics.util.experiment.biology.Peptide;
import com.compomics.util.experiment.identification.matches.PeptideMatch;
import com.compomics.util.experiment.identification.matches.ProteinMatch;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * A Jump To panel for use in the menu bar in the main frame.
 * 
 * @author Marc Vaudel
 * @author Harald Barsnes
 */
public class JumpToPanel extends javax.swing.JPanel {

    /**
     * Instance of the main GUI class
     */
    private PeptideShakerGUI peptideShakerGUI;
    /**
     * Items matching the criterion
     */
    private ArrayList<String> possibilities = new ArrayList<String>();
    /**
     * currently selected item
     */
    private int currentSelection = 0;

    /**
     * Type of item selected
     */
    private enum Type {

        PROTEIN,
        PEPTIDE
    }
    /**
     * Type of each possible item
     */
    private ArrayList<Type> types = new ArrayList<Type>();

    /** 
     * Creates a new JumpToPanel
     * 
     * @param peptideShakerGUI the parent
     */
    public JumpToPanel(PeptideShakerGUI peptideShakerGUI) {
        initComponents();

        this.peptideShakerGUI = peptideShakerGUI;

        if (!peptideShakerGUI.getSelectedProteinKey().equals(PeptideShakerGUI.NO_SELECTION)) {
            inputTxt.setText(peptideShakerGUI.getSelectedProteinKey());
            possibilities.add(peptideShakerGUI.getSelectedProteinKey());
            types.add(Type.PROTEIN);
        } else if (!peptideShakerGUI.getSelectedPeptideKey().equals(PeptideShakerGUI.NO_SELECTION)) {
            inputTxt.setText(Peptide.getSequence(peptideShakerGUI.getSelectedPeptideKey()));
            possibilities.add(peptideShakerGUI.getSelectedPeptideKey());
            types.add(Type.PEPTIDE);
        } else {
            indexLabel.setText("");
            previousButton.setEnabled(false);
            nextButton.setEnabled(false);
        }
    }

    /**
     * Moce the focus to the Jump To text field and select all the content.
     */
    public void selectTextField() {
        inputTxt.requestFocus();
        inputTxt.selectAll();
    }

    /**
     * Updates the item selection in the selected tab
     */
    public void updateSelectionInTab() {
        if (types.get(currentSelection) == Type.PROTEIN) {
            peptideShakerGUI.setSelectedItems(possibilities.get(currentSelection), PeptideShakerGUI.NO_SELECTION, PeptideShakerGUI.NO_SELECTION);
            peptideShakerGUI.updateSelectionInCurrentTab();
        } else {
            peptideShakerGUI.setSelectedItems(PeptideShakerGUI.NO_SELECTION, possibilities.get(currentSelection), PeptideShakerGUI.NO_SELECTION);
            peptideShakerGUI.updateSelectionInCurrentTab();
        }
        indexLabel.setText("(" + (currentSelection + 1) + " of " + possibilities.size() + ")");
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        findJLabel = new javax.swing.JLabel();
        inputTxt = new javax.swing.JTextField();
        previousButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        indexLabel = new javax.swing.JLabel();

        setOpaque(false);

        findJLabel.setText("Find:");

        inputTxt.setForeground(new java.awt.Color(204, 204, 204));
        inputTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        inputTxt.setText("(accession or sequence)");
        inputTxt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        inputTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                inputTxtKeyReleased(evt);
            }
        });

        previousButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrowUp_grey.png"))); // NOI18N
        previousButton.setToolTipText("Previous");
        previousButton.setBorder(null);
        previousButton.setBorderPainted(false);
        previousButton.setContentAreaFilled(false);
        previousButton.setIconTextGap(0);
        previousButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrowUp.png"))); // NOI18N
        previousButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousButtonActionPerformed(evt);
            }
        });

        nextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrowDown_grey.png"))); // NOI18N
        nextButton.setToolTipText("Next");
        nextButton.setBorderPainted(false);
        nextButton.setContentAreaFilled(false);
        nextButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrowDown.png"))); // NOI18N
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        indexLabel.setFont(indexLabel.getFont().deriveFont((indexLabel.getFont().getStyle() | java.awt.Font.ITALIC)));
        indexLabel.setText(" ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(findJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(inputTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(previousButton, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(indexLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {nextButton, previousButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(findJLabel)
                    .addComponent(inputTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(previousButton, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(indexLabel))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Update the jump to filter.
     * 
     * @param evt 
     */
    private void inputTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputTxtKeyReleased

        if (peptideShakerGUI.getIdentification() != null)  {

            if (!inputTxt.getText().equalsIgnoreCase("(accession or sequence)")) {
                inputTxt.setForeground(Color.black);
            } else {
                inputTxt.setForeground(new Color(204, 204, 204));
            }

            if (evt.getKeyCode() == KeyEvent.VK_UP && previousButton.isEnabled()) {
                previousButtonActionPerformed(null);
            } else if (evt.getKeyCode() == KeyEvent.VK_DOWN & nextButton.isEnabled()) {
                nextButtonActionPerformed(null);
            } else {
                possibilities = new ArrayList<String>();
                types = new ArrayList<Type>();
                currentSelection = 0;
                String input = inputTxt.getText().trim().toLowerCase();

                if (!input.equals("")) {

                    this.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));

                    for (String proteinKey : peptideShakerGUI.getIdentification().getProteinIdentification()) {
                        if (!ProteinMatch.isDecoy(proteinKey) && proteinKey.toLowerCase().contains(input)) {
                            possibilities.add(proteinKey);
                            types.add(Type.PROTEIN);
                        }
                    }

                    ArrayList<String> secondaryCandidates = new ArrayList<String>();
                    PeptideMatch peptideMatch;

                    for (String peptideKey : peptideShakerGUI.getIdentification().getPeptideIdentification()) {
//                        if (peptideKey.toLowerCase().startsWith(input)) {
//                            // @TODO: should there be something here??
//                        } else 
                        if (peptideKey.toLowerCase().contains(input)) {
                            secondaryCandidates.add(peptideKey);
                        }
                    }

                    for (String secondaryCandidate : secondaryCandidates) {

                        peptideMatch = peptideShakerGUI.getIdentification().getPeptideMatch(secondaryCandidate);

                        for (String protein : peptideMatch.getTheoreticPeptide().getParentProteins()) {
                            if (!ProteinMatch.isDecoy(protein)) {
                                possibilities.add(secondaryCandidate);
                                types.add(Type.PEPTIDE);
                                break;
                            }
                        }
                    }

                    if (possibilities.size() > 0) {
                        
                        if (possibilities.size() > 1) {
                            previousButton.setEnabled(true);
                            nextButton.setEnabled(true);
                        } else { // possibilities.size() == 1
                            previousButton.setEnabled(false);
                            nextButton.setEnabled(false);
                        }
                        
                        updateSelectionInTab();
                    } else {
                        previousButton.setEnabled(false);
                        nextButton.setEnabled(false);

                        if (!inputTxt.getText().equalsIgnoreCase("(accession or sequence)")) {
                            indexLabel.setText("(no matches)");
                        } else {
                            indexLabel.setText("");
                        }
                    }

                    this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                } else {
                    indexLabel.setText("");
                    previousButton.setEnabled(false);
                    nextButton.setEnabled(false);
                }
            }
        }
    }//GEN-LAST:event_inputTxtKeyReleased

    /**
     * Display the previous match in the list.
     * 
     * @param evt 
     */
    private void previousButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousButtonActionPerformed
        if (currentSelection == 0) {
            currentSelection = possibilities.size() - 1;
        } else {
            currentSelection--;
        }
        updateSelectionInTab();
    }//GEN-LAST:event_previousButtonActionPerformed

    /**
     * Display the next match in the list.
     * 
     * @param evt 
     */
    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        if (currentSelection == possibilities.size() - 1) {
            currentSelection = 0;
        } else {
            currentSelection++;
        }
        updateSelectionInTab();
    }//GEN-LAST:event_nextButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel findJLabel;
    private javax.swing.JLabel indexLabel;
    private javax.swing.JTextField inputTxt;
    private javax.swing.JButton nextButton;
    private javax.swing.JButton previousButton;
    // End of variables declaration//GEN-END:variables

    /**
     * Override to set the input text field enabled or not.
     * 
     * @param enabled 
     */
    public void setEnabled(boolean enabled) {
        inputTxt.setEnabled(enabled);
    }
}
