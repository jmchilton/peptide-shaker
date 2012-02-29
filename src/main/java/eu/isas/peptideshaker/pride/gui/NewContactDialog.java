package eu.isas.peptideshaker.pride.gui;

import eu.isas.peptideshaker.pride.Contact;

/**
 * A dialog for creating new contacts and editing old ones.
 * 
 * @author Harald Barsnes
 */
public class NewContactDialog extends javax.swing.JDialog {

    /**
     * A reference to the PRIDE export dialog.
     */
    private PrideExportDialog prideExportDialog;

    /**
     * Creates a new NewContactDialog.
     * 
     * @param prideExportDialog
     * @param modal 
     */
    public NewContactDialog(PrideExportDialog prideExportDialog, boolean modal) {
        super(prideExportDialog, modal);
        this.prideExportDialog = prideExportDialog;
        initComponents();
        setLocationRelativeTo(prideExportDialog);
        setVisible(true);
    }
    
    /**
     * Creates a new NewContactDialog.
     * 
     * @param prideExportDialog
     * @param modal
     * @param contact 
     */
    public NewContactDialog(PrideExportDialog prideExportDialog, boolean modal, Contact contact) {
        super(prideExportDialog, modal);
        this.prideExportDialog = prideExportDialog;
        initComponents();
        
        nameJTextField.setText(contact.getName());
        eMailJTextField.setText(contact.getEMail());
        institutionJTextArea.setText(contact.getInstitution());
        validateInput();
        
        setTitle("Edit Contact");
        
        setLocationRelativeTo(prideExportDialog);
        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        eMailJTextField = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        institutionJTextArea = new javax.swing.JTextArea();
        nameJTextField = new javax.swing.JTextField();
        okJButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New Contact");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Contact"));

        jLabel6.setText("Institution");

        jLabel7.setText("Name");

        jLabel8.setText("E-mail");

        eMailJTextField.setFont(eMailJTextField.getFont());
        eMailJTextField.setMargin(new java.awt.Insets(2, 4, 2, 2));
        eMailJTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                eMailJTextFieldKeyReleased(evt);
            }
        });

        institutionJTextArea.setColumns(20);
        institutionJTextArea.setFont(institutionJTextArea.getFont());
        institutionJTextArea.setLineWrap(true);
        institutionJTextArea.setRows(3);
        institutionJTextArea.setWrapStyleWord(true);
        institutionJTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                institutionJTextAreaKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(institutionJTextArea);

        nameJTextField.setFont(nameJTextField.getFont());
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
                    .addComponent(jLabel8)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6))
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)
                    .addComponent(eMailJTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(nameJTextField))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(nameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(eMailJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel6)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(okJButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(okJButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @see #validateInput() 
     */
    private void eMailJTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_eMailJTextFieldKeyReleased
        validateInput();
    }//GEN-LAST:event_eMailJTextFieldKeyReleased

    /**
     * @see #validateInput() 
     */
    private void institutionJTextAreaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_institutionJTextAreaKeyReleased
        validateInput();
    }//GEN-LAST:event_institutionJTextAreaKeyReleased

    /**
     * @see #validateInput() 
     */
    private void nameJTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameJTextFieldKeyReleased
        validateInput();
    }//GEN-LAST:event_nameJTextFieldKeyReleased

    /**
     * Saves the contact and closes the dialog.
     * 
     * @param evt 
     */
    private void okJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okJButtonActionPerformed
        prideExportDialog.setContact(new Contact(nameJTextField.getText(), eMailJTextField.getText(), institutionJTextArea.getText()));
        dispose();
    }//GEN-LAST:event_okJButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField eMailJTextField;
    private javax.swing.JTextArea institutionJTextArea;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField nameJTextField;
    private javax.swing.JButton okJButton;
    // End of variables declaration//GEN-END:variables

    /**
     * Checks if all mandatory information is filled in. Enables or disables the
     * OK button.
     */
    public void validateInput() {
        if (nameJTextField.getText().length() > 0 
                && eMailJTextField.getText().length() > 0
                && institutionJTextArea.getText().length() > 0) {
            okJButton.setEnabled(true);
        } else {
            okJButton.setEnabled(false);
        }
    }
}
