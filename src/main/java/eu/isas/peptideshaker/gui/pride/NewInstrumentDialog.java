
package eu.isas.peptideshaker.gui.pride;

import com.compomics.util.Util;
import com.compomics.util.pride.CvTerm;
import com.compomics.util.pride.prideobjects.Instrument;
import java.awt.Color;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import no.uib.olsdialog.OLSDialog;
import no.uib.olsdialog.OLSInputable;

/**
 * A dialog for annotating instruments.
 * 
 * @author Harald Barsnes
 */
public class NewInstrumentDialog extends javax.swing.JDialog implements OLSInputable {

    /**
     * The PRIDE Export Dialog.
     */
    private PrideExportDialog prideExportDialog;
    /**
     * The table column header tooltips.
     */
    private Vector columnToolTips;
    /**
     * The last valid input for contact name
     */
    private String lastNameInput = "";
    
    /**
     * Create a new NewInstrumentDialog.
     * 
     * @param prideExportDialog
     * @param modal 
     */
    public NewInstrumentDialog(PrideExportDialog prideExportDialog, boolean modal) {
        super(prideExportDialog, modal);
        this.prideExportDialog = prideExportDialog;
        initComponents();
        
        setUpTable();
        
        setLocationRelativeTo(prideExportDialog);
        setVisible(true);
    }
    
    /**
     * Creates a new NewInstrumentDialog.
     *
     * @param prideExportDialog
     * @param modal
     * @param instrument  
     */
    public NewInstrumentDialog(PrideExportDialog prideExportDialog, boolean modal, Instrument instrument) {
        super(prideExportDialog, modal);
        this.prideExportDialog = prideExportDialog;

        initComponents();
        setTitle("Edit Instrument");
        
        nameJTextField.setText(instrument.getName());
        
        for (int i=0; i<instrument.getCvTerms().size(); i++) {
            ((DefaultTableModel) analyzerCvTermsJTable.getModel()).addRow(new Object[] {
                (i+1),
                instrument.getCvTerms().get(i).getOntology(),
                instrument.getCvTerms().get(i).getAccession(),
                instrument.getCvTerms().get(i).getName(),
                instrument.getCvTerms().get(i).getValue()
            });
        }
        
        instrumentSourceJTextField.setText(instrument.getSource().getName() + " [" + instrument.getSource().getAccession() + "]");
        instrumentDetectorJTextField.setText(instrument.getDetector().getName() + " [" + instrument.getDetector().getAccession() + "]");
        
        setUpTable();
        validateInput();

        setLocationRelativeTo(prideExportDialog);
        setVisible(true);
    }
    
    /**
     * Set up the table properties.
     */
    private void setUpTable() {
        analyzerCvScrollPane.getViewport().setOpaque(false);
        analyzerCvTermsJTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        analyzerCvTermsJTable.getTableHeader().setReorderingAllowed(false);
        analyzerCvTermsJTable.getColumn(" ").setMaxWidth(40);
        analyzerCvTermsJTable.getColumn(" ").setMinWidth(40);
        
        columnToolTips = new Vector();
        columnToolTips.add(null);
        columnToolTips.add(null);
        columnToolTips.add(null);
        columnToolTips.add(null);
        columnToolTips.add(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popupJMenu = new javax.swing.JPopupMenu();
        editJMenuItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        moveUpJMenuItem = new javax.swing.JMenuItem();
        moveDownJMenuItem = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        deleteSelectedRowJMenuItem = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        sourceLabel = new javax.swing.JLabel();
        instrumentSourceJTextField = new javax.swing.JTextField();
        detectorLabel = new javax.swing.JLabel();
        instrumentDetectorJButton = new javax.swing.JButton();
        instrumentSourceJButton = new javax.swing.JButton();
        instrumentDetectorJTextField = new javax.swing.JTextField();
        analyzerLabel = new javax.swing.JLabel();
        nameLabel = new javax.swing.JLabel();
        analyzerCvScrollPane = new javax.swing.JScrollPane();
        analyzerCvTermsJTable = new JTable() {
            protected JTableHeader createDefaultTableHeader() {
                return new JTableHeader(columnModel) {
                    public String getToolTipText(MouseEvent e) {
                        java.awt.Point p = e.getPoint();
                        int index = columnModel.getColumnIndexAtX(p.x);
                        int realIndex = columnModel.getColumn(index).getModelIndex();
                        String tip = (String) columnToolTips.get(realIndex);
                        return tip;
                    }
                };
            }
        };
        addAnalyzerJButton = new javax.swing.JButton();
        nameJTextField = new javax.swing.JTextField();
        okJButton = new javax.swing.JButton();

        editJMenuItem.setMnemonic('E');
        editJMenuItem.setText("Edit");
        editJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editJMenuItemActionPerformed(evt);
            }
        });
        popupJMenu.add(editJMenuItem);
        popupJMenu.add(jSeparator3);

        moveUpJMenuItem.setMnemonic('U');
        moveUpJMenuItem.setText("Move Up");
        moveUpJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveUpJMenuItemActionPerformed(evt);
            }
        });
        popupJMenu.add(moveUpJMenuItem);

        moveDownJMenuItem.setMnemonic('D');
        moveDownJMenuItem.setText("Move Down");
        moveDownJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveDownJMenuItemActionPerformed(evt);
            }
        });
        popupJMenu.add(moveDownJMenuItem);
        popupJMenu.add(jSeparator4);

        deleteSelectedRowJMenuItem.setText("Delete");
        deleteSelectedRowJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteSelectedRowJMenuItemActionPerformed(evt);
            }
        });
        popupJMenu.add(deleteSelectedRowJMenuItem);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New Instrument");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Instrument"));

        sourceLabel.setForeground(new java.awt.Color(255, 0, 0));
        sourceLabel.setText("Source*");

        instrumentSourceJTextField.setEditable(false);
        instrumentSourceJTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        instrumentSourceJTextField.setMargin(new java.awt.Insets(2, 4, 2, 2));

        detectorLabel.setForeground(new java.awt.Color(255, 0, 0));
        detectorLabel.setText("Detector*");

        instrumentDetectorJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/ols_transparent.GIF"))); // NOI18N
        instrumentDetectorJButton.setToolTipText("Ontology Lookup Service");
        instrumentDetectorJButton.setPreferredSize(new java.awt.Dimension(61, 23));
        instrumentDetectorJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                instrumentDetectorJButtonActionPerformed(evt);
            }
        });

        instrumentSourceJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/ols_transparent.GIF"))); // NOI18N
        instrumentSourceJButton.setToolTipText("Ontology Lookup Service");
        instrumentSourceJButton.setPreferredSize(new java.awt.Dimension(61, 23));
        instrumentSourceJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                instrumentSourceJButtonActionPerformed(evt);
            }
        });

        instrumentDetectorJTextField.setEditable(false);
        instrumentDetectorJTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        instrumentDetectorJTextField.setMargin(new java.awt.Insets(2, 4, 2, 2));

        analyzerLabel.setForeground(new java.awt.Color(255, 0, 0));
        analyzerLabel.setText("Analyzers*");

        nameLabel.setForeground(new java.awt.Color(255, 0, 0));
        nameLabel.setText("Name*");

        analyzerCvTermsJTable.setFont(analyzerCvTermsJTable.getFont());
        analyzerCvTermsJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                " ", "Ontology", "Accession", "Name", "Value"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        analyzerCvTermsJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                analyzerCvTermsJTableMouseClicked(evt);
            }
        });
        analyzerCvTermsJTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                analyzerCvTermsJTableKeyReleased(evt);
            }
        });
        analyzerCvScrollPane.setViewportView(analyzerCvTermsJTable);

        addAnalyzerJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/ols_transparent.GIF"))); // NOI18N
        addAnalyzerJButton.setText("Add Analyzer");
        addAnalyzerJButton.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        addAnalyzerJButton.setPreferredSize(new java.awt.Dimension(159, 23));
        addAnalyzerJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAnalyzerJButtonActionPerformed(evt);
            }
        });

        nameJTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        nameJTextField.setMargin(new java.awt.Insets(2, 4, 2, 2));
        nameJTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nameJTextFieldKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(analyzerCvScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 541, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sourceLabel)
                            .addComponent(detectorLabel)
                            .addComponent(nameLabel)
                            .addComponent(analyzerLabel))
                        .addGap(4, 4, 4)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(instrumentDetectorJTextField)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(instrumentDetectorJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(nameJTextField, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(instrumentSourceJTextField))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(instrumentSourceJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(addAnalyzerJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(nameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(sourceLabel)
                    .addComponent(instrumentSourceJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(instrumentSourceJButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(detectorLabel)
                    .addComponent(instrumentDetectorJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(instrumentDetectorJButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(analyzerLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(analyzerCvScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addAnalyzerJButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        okJButton.setText("OK");
        okJButton.setEnabled(false);
        okJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okJButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(okJButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(okJButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Open the OLS Dialog to edit the detector.
     * 
     * @param evt 
     */
    private void instrumentDetectorJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_instrumentDetectorJButtonActionPerformed
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));

        String searchTerm = null;
        String ontology = "MS";

        if (instrumentDetectorJTextField.getText().length() > 0) {

            searchTerm = instrumentDetectorJTextField.getText();

            ontology = searchTerm.substring(searchTerm.lastIndexOf("[") + 1, searchTerm.lastIndexOf("]") - 1);
            ontology = PrideExportDialog.getOntologyFromCvTerm(ontology);

            searchTerm = instrumentDetectorJTextField.getText().substring(
                    0, instrumentDetectorJTextField.getText().lastIndexOf("[") - 1);
            searchTerm = searchTerm.replaceAll("-", " ");
            searchTerm = searchTerm.replaceAll(":", " ");
            searchTerm = searchTerm.replaceAll("\\(", " ");
            searchTerm = searchTerm.replaceAll("\\)", " ");
            searchTerm = searchTerm.replaceAll("&", " ");
            searchTerm = searchTerm.replaceAll("\\+", " ");
            searchTerm = searchTerm.replaceAll("\\[", " ");
            searchTerm = searchTerm.replaceAll("\\]", " ");
        }

        new OLSDialog(this, this, true, "instrumentDetector", ontology, searchTerm);
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_instrumentDetectorJButtonActionPerformed

    /**
     * Open the OLS Dialog to edit the source.
     * 
     * @param evt 
     */
    private void instrumentSourceJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_instrumentSourceJButtonActionPerformed
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));

        String searchTerm = null;
        String ontology = "MS";

        if (instrumentSourceJTextField.getText().length() > 0) {

            searchTerm = instrumentSourceJTextField.getText();

            ontology = searchTerm.substring(searchTerm.lastIndexOf("[") + 1, searchTerm.lastIndexOf("]") - 1);
            ontology = PrideExportDialog.getOntologyFromCvTerm(ontology);

            searchTerm = instrumentSourceJTextField.getText().substring(0, instrumentSourceJTextField.getText().lastIndexOf("[") - 1);
            searchTerm = searchTerm.replaceAll("-", " ");
            searchTerm = searchTerm.replaceAll(":", " ");
            searchTerm = searchTerm.replaceAll("\\(", " ");
            searchTerm = searchTerm.replaceAll("\\)", " ");
            searchTerm = searchTerm.replaceAll("&", " ");
            searchTerm = searchTerm.replaceAll("\\+", " ");
            searchTerm = searchTerm.replaceAll("\\[", " ");
            searchTerm = searchTerm.replaceAll("\\]", " ");
        }

        new OLSDialog(this, this, true, "instrumentSource", ontology, searchTerm);
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_instrumentSourceJButtonActionPerformed

    /**
     * Open the table popup menu.
     * 
     * @param evt 
     */
    private void analyzerCvTermsJTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_analyzerCvTermsJTableMouseClicked
        if (evt.getButton() == 3) {

            int row = analyzerCvTermsJTable.rowAtPoint(evt.getPoint());
            int column = analyzerCvTermsJTable.columnAtPoint(evt.getPoint());

            analyzerCvTermsJTable.changeSelection(row, column, false, false);

            this.moveUpJMenuItem.setEnabled(true);
            this.moveDownJMenuItem.setEnabled(true);

            if (row == analyzerCvTermsJTable.getRowCount() - 1) {
                this.moveDownJMenuItem.setEnabled(false);
            }

            if (row == 0) {
                this.moveUpJMenuItem.setEnabled(false);
            }

            popupJMenu.show(evt.getComponent(), evt.getX(), evt.getY());
        } else if (evt.getButton() == 1 && evt.getClickCount() == 2) {
            editJMenuItemActionPerformed(null);
        }
    }//GEN-LAST:event_analyzerCvTermsJTableMouseClicked

    /**
     * Delete the selected row.
     * 
     * @param evt 
     */
    private void analyzerCvTermsJTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_analyzerCvTermsJTableKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            deleteSelectedRowJMenuItemActionPerformed(null);
        }
    }//GEN-LAST:event_analyzerCvTermsJTableKeyReleased

    /**
     * Open the OLS Dialog to add an analyzer CV term.
     * 
     * @param evt 
     */
    private void addAnalyzerJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAnalyzerJButtonActionPerformed
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        new OLSDialog(prideExportDialog, this, true, "singleAnalyzer", "MS", null);
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_addAnalyzerJButtonActionPerformed

    /**
     * Edit the selected analyzer term.
     * 
     * @param evt 
     */
    private void editJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editJMenuItemActionPerformed
        int selectedRow = analyzerCvTermsJTable.getSelectedRow();

        String searchTerm = (String) analyzerCvTermsJTable.getValueAt(selectedRow, 3);
        String ontology = (String) analyzerCvTermsJTable.getValueAt(selectedRow, 1);
        ontology = PrideExportDialog.getOntologyFromCvTerm(ontology);

        this.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        new OLSDialog(prideExportDialog, this, true, "singleAnalyzer", ontology, selectedRow, searchTerm);
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_editJMenuItemActionPerformed

    /**
     * Move the selected row up.
     * 
     * @param evt 
     */
    private void moveUpJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveUpJMenuItemActionPerformed
        int selectedRow = analyzerCvTermsJTable.getSelectedRow();
        int selectedColumn = analyzerCvTermsJTable.getSelectedColumn();

        Object[] tempRow = new Object[]{
            analyzerCvTermsJTable.getValueAt(selectedRow - 1, 0),
            analyzerCvTermsJTable.getValueAt(selectedRow - 1, 1),
            analyzerCvTermsJTable.getValueAt(selectedRow - 1, 2)
        };

        ((DefaultTableModel) analyzerCvTermsJTable.getModel()).removeRow(selectedRow - 1);
        ((DefaultTableModel) analyzerCvTermsJTable.getModel()).insertRow(selectedRow, tempRow);

        analyzerCvTermsJTable.changeSelection(selectedRow - 1, selectedColumn, false, false);

        fixTableIndices();
    }//GEN-LAST:event_moveUpJMenuItemActionPerformed

    /**
     * More the selected row down.
     * 
     * @param evt 
     */
    private void moveDownJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveDownJMenuItemActionPerformed
        int selectedRow = analyzerCvTermsJTable.getSelectedRow();
        int selectedColumn = analyzerCvTermsJTable.getSelectedColumn();

        Object[] tempRow = new Object[]{
            analyzerCvTermsJTable.getValueAt(selectedRow + 1, 0),
            analyzerCvTermsJTable.getValueAt(selectedRow + 1, 1),
            analyzerCvTermsJTable.getValueAt(selectedRow + 1, 2)
        };

        ((DefaultTableModel) analyzerCvTermsJTable.getModel()).removeRow(selectedRow + 1);
        ((DefaultTableModel) analyzerCvTermsJTable.getModel()).insertRow(selectedRow, tempRow);

        analyzerCvTermsJTable.changeSelection(selectedRow + 1, selectedColumn, false, false);

        fixTableIndices();
    }//GEN-LAST:event_moveDownJMenuItemActionPerformed

    /**
     * Delete the selected row.
     * 
     * @param evt 
     */
    private void deleteSelectedRowJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteSelectedRowJMenuItemActionPerformed

        int selectedRow = analyzerCvTermsJTable.getSelectedRow();

        if (selectedRow != -1) {

            ((DefaultTableModel) analyzerCvTermsJTable.getModel()).removeRow(selectedRow);
            fixTableIndices();
            validateInput();
        }
    }//GEN-LAST:event_deleteSelectedRowJMenuItemActionPerformed

    /**
     * Save the instrument and close the dialog.
     * 
     * @param evt 
     */
    private void okJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okJButtonActionPerformed
        
        ArrayList<CvTerm> cvTerms = new ArrayList<CvTerm>();
        
        for (int i=0; i<analyzerCvTermsJTable.getRowCount(); i++) {
            cvTerms.add(new CvTerm(
                    (String) analyzerCvTermsJTable.getValueAt(i, 1), 
                    (String) analyzerCvTermsJTable.getValueAt(i, 2), 
                    (String) analyzerCvTermsJTable.getValueAt(i, 3), 
                    (String) analyzerCvTermsJTable.getValueAt(i, 4)));
        }
        
        String tempSource = instrumentSourceJTextField.getText();
        String termSource = tempSource.substring(0, tempSource.lastIndexOf("[") - 1);
        String accessionSource = tempSource.substring(tempSource.lastIndexOf("[") + 1, tempSource.lastIndexOf("]") - 1);
        String ontologySource = PrideExportDialog.getOntologyFromCvTerm(accessionSource);
        
        String tempDetector = instrumentDetectorJTextField.getText();
        String termDetector = tempDetector.substring(0, tempDetector.lastIndexOf("[") - 1);
        String accessionDetector = tempDetector.substring(tempDetector.lastIndexOf("[") + 1, tempDetector.lastIndexOf("]") - 1);
        String ontologyDetector = PrideExportDialog.getOntologyFromCvTerm(accessionDetector);
        
        prideExportDialog.setInstrument(new Instrument(nameJTextField.getText(), 
                new CvTerm(ontologySource, accessionSource, termSource, null), 
                new CvTerm(ontologyDetector, accessionDetector, termDetector, null), 
                cvTerms));
        dispose();
    }//GEN-LAST:event_okJButtonActionPerformed

    /**
     * Close the dialog without saving.
     * 
     * @param evt 
     */
    private void nameJTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameJTextFieldKeyReleased
        validateInput();
    }//GEN-LAST:event_nameJTextFieldKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addAnalyzerJButton;
    private javax.swing.JScrollPane analyzerCvScrollPane;
    private javax.swing.JTable analyzerCvTermsJTable;
    private javax.swing.JLabel analyzerLabel;
    private javax.swing.JMenuItem deleteSelectedRowJMenuItem;
    private javax.swing.JLabel detectorLabel;
    private javax.swing.JMenuItem editJMenuItem;
    private javax.swing.JButton instrumentDetectorJButton;
    private javax.swing.JTextField instrumentDetectorJTextField;
    private javax.swing.JButton instrumentSourceJButton;
    private javax.swing.JTextField instrumentSourceJTextField;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JMenuItem moveDownJMenuItem;
    private javax.swing.JMenuItem moveUpJMenuItem;
    private javax.swing.JTextField nameJTextField;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JButton okJButton;
    private javax.swing.JPopupMenu popupJMenu;
    private javax.swing.JLabel sourceLabel;
    // End of variables declaration//GEN-END:variables

    @Override
    public void insertOLSResult(String field, String selectedValue, String accession, String ontologyShort, String ontologyLong, int modifiedRow, String mappedTerm, Map<String, String> metadata) {
        
        if (field.equalsIgnoreCase("instrumentSource")) {
            setInstrumentSource(selectedValue, accession, ontologyShort);
        } else if (field.equalsIgnoreCase("instrumentDetector")) {
            setInstrumentDetector(selectedValue, accession, ontologyShort);
        } else {
            addAnalyzerDetails(selectedValue, accession, ontologyShort, modifiedRow);
        }     
    }

    @Override
    public Window getWindow() {
        return (Window) this;
    }
    
    /**
     * Insert a new instrument source.
     * 
     * @param name
     * @param accession
     * @param ontology
     */
    public void setInstrumentSource(String name, String accession, String ontology) {
        instrumentSourceJTextField.setText(name + " [" + accession + "]");
        instrumentSourceJTextField.setCaretPosition(0);
        validateInput();
    }

    /**
     * Insert a new instrument detector.
     * 
     * @param name
     * @param accession
     * @param ontology
     */
    public void setInstrumentDetector(String name, String accession, String ontology) {
        instrumentDetectorJTextField.setText(name + " [" + accession + "]");
        instrumentDetectorJTextField.setCaretPosition(0);
        validateInput();
    }
    
    /**
     * Fixes the indices so that they are in accending order starting from one.
     */
    private void fixTableIndices() {
        for (int row = 0; row < ((DefaultTableModel) analyzerCvTermsJTable.getModel()).getRowCount(); row++) {
            ((DefaultTableModel) analyzerCvTermsJTable.getModel()).setValueAt(new Integer(row + 1), row, 0);
        }
    }
    
    /**
     * Enables the OK button if a valid protocol set is selected.
     */
    private void validateInput() {
        
        String input = nameJTextField.getText();
        for (String forbiddenCharacter : Util.forbiddenCharacters) {
            if (input.contains(forbiddenCharacter)) {
                JOptionPane.showMessageDialog(null, "'" + forbiddenCharacter + "' is not allowed in instrument name.",
                    "Forbidden character", JOptionPane.ERROR_MESSAGE);
                nameJTextField.setText(lastNameInput);
                return;
            }
        }
        lastNameInput = input;
        
        if (analyzerCvTermsJTable.getRowCount() > 0 
                && nameJTextField.getText().length() > 0
                && instrumentSourceJTextField.getText().length() > 0
                && instrumentDetectorJTextField.getText().length() > 0) {
            okJButton.setEnabled(true);
        } else {
            okJButton.setEnabled(false);
        }
        
        // highlight the fields that have not been filled
        if (nameJTextField.getText().length() > 0) {
            nameLabel.setForeground(Color.BLACK);
        } else {
            nameLabel.setForeground(Color.RED);
        }
        
        if (instrumentSourceJTextField.getText().length() > 0) {
            sourceLabel.setForeground(Color.BLACK);
        } else {
            sourceLabel.setForeground(Color.RED);
        }
        
        if (instrumentDetectorJTextField.getText().length() > 0) {
            detectorLabel.setForeground(Color.BLACK);
        } else {
            detectorLabel.setForeground(Color.RED);
        }
        
        if (analyzerCvTermsJTable.getRowCount() > 0) {
            analyzerLabel.setForeground(Color.BLACK);
        } else {
            analyzerLabel.setForeground(Color.RED);
        }
    }
    
    /**
     * Add an analyzer cv term to the table.
     *
     * @param name
     * @param accession
     * @param ontology
     * @param modifiedRow the row to modify, use -1 if adding a new row
     */
    public void addAnalyzerDetails(String name, String accession, String ontology, int modifiedRow) {
        addAnalyzerDetails(name, accession, ontology, null, modifiedRow);
    }

    /**
     * Add an analyzer cv term to the table.
     *
     * @param name
     * @param accession
     * @param ontology
     * @param value
     * @param modifiedRow the row to modify, use -1 if adding a new row
     */
    public void addAnalyzerDetails(String name, String accession, String ontology, String value, int modifiedRow) {

        if (modifiedRow == -1) {

            ((DefaultTableModel) this.analyzerCvTermsJTable.getModel()).addRow(
                    new Object[]{
                        new Integer(analyzerCvTermsJTable.getRowCount() + 1),
                        ontology,
                        accession,
                        name,
                        value
                    });
        } else {
            analyzerCvTermsJTable.setValueAt(ontology, modifiedRow, 1);
            analyzerCvTermsJTable.setValueAt(accession, modifiedRow, 2);
            analyzerCvTermsJTable.setValueAt(name, modifiedRow, 3);
            analyzerCvTermsJTable.setValueAt(null, modifiedRow, 4);
        }
        
        validateInput();
    }
}
