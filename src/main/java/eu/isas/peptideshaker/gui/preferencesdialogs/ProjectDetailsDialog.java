package eu.isas.peptideshaker.gui.preferencesdialogs;

import eu.isas.peptideshaker.gui.PeptideShakerGUI;
import eu.isas.peptideshaker.preferences.ProjectDetails;
import eu.isas.peptideshaker.preferences.SearchParameters;
import java.io.File;

/**
 * This dialog displays the project properties.
 *
 * @author Marc Vaudel
 */
public class ProjectDetailsDialog extends javax.swing.JDialog {

    /**
     * Creates a dialog to display the project properties.
     *
     * @param parent
     */
    public ProjectDetailsDialog(PeptideShakerGUI parent) {
        super(parent, true);
        initComponents();

        ProjectDetails projectDetails = parent.getProjectDetails();
        SearchParameters searchParameters = parent.getSearchParameters();

        if (projectDetails != null) {

            String report = "<html><br>";
            report += "<b>Experiment</b>: " + parent.getExperiment().getReference() + "<br>";
            report += "<b>Sample:</b> " + parent.getSample().getReference() + "<br>";
            report += "<b>Replicate number:</b> " + parent.getReplicateNumber() + "<br><br>";

            report += "<b>Creation Date:</b> " + projectDetails.getCreationDate() + "<br><br>";

            report += "<b>Identification Files</b>:<br>";
            for (File idFile : projectDetails.getIdentificationFiles()) {
                report += idFile.getAbsolutePath() + "<br>";
            }

            report += "<br><b>Spectrum Files:</b><br>";
            for (String mgfFile : searchParameters.getSpectrumFiles()) {
                report += mgfFile + "<br>";
            }

            report += "<br><b>FASTA File:</b><br>";
            report += projectDetails.getDbFile() + "<br>";

            report += "<br><br><b>Report:</b><br>";
            report += "<pre>" + projectDetails.getReport() + "</pre>";

            report += "</html>";

            projectDetailsJEditorPane.setText(report);
            projectDetailsJEditorPane.setCaretPosition(0);

            setTitle("Project Properties - " + parent.getExperiment().getReference());
        } else {
            projectDetailsJEditorPane.setText("No project loaded.");
        }

        setLocationRelativeTo(parent);
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
        closeButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        projectDetailsJScrollPane = new javax.swing.JScrollPane();
        projectDetailsJEditorPane = new javax.swing.JEditorPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Details"));

        projectDetailsJEditorPane.setContentType("text/html");
        projectDetailsJEditorPane.setEditable(false);
        projectDetailsJScrollPane.setViewportView(projectDetailsJEditorPane);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(projectDetailsJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(projectDetailsJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(closeButton))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(closeButton)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Close the dialog.
     *
     * @param evt
     */
    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        dispose();
    }//GEN-LAST:event_closeButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JEditorPane projectDetailsJEditorPane;
    private javax.swing.JScrollPane projectDetailsJScrollPane;
    // End of variables declaration//GEN-END:variables
}
