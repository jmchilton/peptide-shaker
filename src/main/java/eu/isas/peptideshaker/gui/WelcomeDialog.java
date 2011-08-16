package eu.isas.peptideshaker.gui;

import eu.isas.peptideshaker.utils.BareBonesBrowserLaunch;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 * A simple welcome dialog with the option to open an existing project or 
 * create a new one.
 * 
 * @author Harald Barsnes
 */
public class WelcomeDialog extends javax.swing.JDialog {

    /**
     * The PeptideShaker parent frame.
     */
    private PeptideShakerGUI peptideShakerGUI;
    /**
     * A reference to the open dialog.
     */
    private OpenDialog openDialog;

    /**
     * Create a new WelcomeDialog.
     * 
     * @param peptideShakerGUI  the dialog parent
     * @param modal             modal or not modal
     */
    public WelcomeDialog(PeptideShakerGUI peptideShakerGUI, boolean modal) {
        super(peptideShakerGUI, modal);
        initComponents();

        openDialog = new OpenDialog(peptideShakerGUI, false);

        this.peptideShakerGUI = peptideShakerGUI;
        setLocationRelativeTo(peptideShakerGUI);
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

        backgroundPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        openJButton = new javax.swing.JButton();
        newJButton = new javax.swing.JButton();
        peptideShakerButton = new javax.swing.JButton();
        compomicsButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Welcome to PeptideShaker");
        setResizable(false);

        backgroundPanel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(jLabel1.getFont().deriveFont((jLabel1.getFont().getStyle() | java.awt.Font.ITALIC)));
        jLabel1.setText("Create a new PeptideShaker project");

        jLabel2.setFont(jLabel2.getFont().deriveFont((jLabel2.getFont().getStyle() | java.awt.Font.ITALIC)));
        jLabel2.setText("Open an existing PeptideShaker project");

        openJButton.setText("Open Project");
        openJButton.setFocusPainted(false);
        openJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openJButtonActionPerformed(evt);
            }
        });

        newJButton.setText("New Project");
        newJButton.setFocusPainted(false);
        newJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newJButtonActionPerformed(evt);
            }
        });

        peptideShakerButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/peptide-shaker.gif"))); // NOI18N
        peptideShakerButton.setToolTipText("PeptideShaker");
        peptideShakerButton.setBorder(null);
        peptideShakerButton.setBorderPainted(false);
        peptideShakerButton.setContentAreaFilled(false);
        peptideShakerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                peptideShakerButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                peptideShakerButtonMouseExited(evt);
            }
        });
        peptideShakerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                peptideShakerButtonActionPerformed(evt);
            }
        });

        compomicsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/compomics.png"))); // NOI18N
        compomicsButton.setToolTipText("CompOmics");
        compomicsButton.setBorder(null);
        compomicsButton.setBorderPainted(false);
        compomicsButton.setContentAreaFilled(false);
        compomicsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                compomicsButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                compomicsButtonMouseExited(evt);
            }
        });
        compomicsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compomicsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout backgroundPanelLayout = new javax.swing.GroupLayout(backgroundPanel);
        backgroundPanel.setLayout(backgroundPanelLayout);
        backgroundPanelLayout.setHorizontalGroup(
            backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundPanelLayout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(backgroundPanelLayout.createSequentialGroup()
                        .addComponent(openJButton)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2))
                    .addGroup(backgroundPanelLayout.createSequentialGroup()
                        .addComponent(newJButton)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)))
                .addGap(55, 55, 55)
                .addComponent(peptideShakerButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(compomicsButton)
                .addContainerGap(47, Short.MAX_VALUE))
        );

        backgroundPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {newJButton, openJButton});

        backgroundPanelLayout.setVerticalGroup(
            backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundPanelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(compomicsButton, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                    .addGroup(backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(peptideShakerButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(backgroundPanelLayout.createSequentialGroup()
                            .addGroup(backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(newJButton)
                                .addComponent(jLabel1))
                            .addGap(18, 18, 18)
                            .addGroup(backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(openJButton)
                                .addComponent(jLabel2)))))
                .addGap(27, 27, 27))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backgroundPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backgroundPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Open the dialog for creating a new project.
     * 
     * @param evt 
     */
    private void newJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newJButtonActionPerformed
        this.setVisible(false);
        openDialog.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_newJButtonActionPerformed
    /**
     * Open the dialog for opening an existing project.
     * 
     * @param evt 
     */
    private void openJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openJButtonActionPerformed
        this.setVisible(false);
        JFileChooser fileChooser = new JFileChooser(peptideShakerGUI.getLastSelectedFolder());
        fileChooser.setDialogTitle("Open PeptideShaker Project");

        FileFilter filter = new FileFilter() {

            @Override
            public boolean accept(File myFile) {
                return myFile.getName().toLowerCase().endsWith("cps")
                        || myFile.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Supported formats: Peptide Shaker (.cps)";
            }
        };

        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showDialog(this.getParent(), "Open");

        if (returnVal == JFileChooser.APPROVE_OPTION) {

            openDialog.setSearchParamatersFiles(new ArrayList<File>());
            File newFile = fileChooser.getSelectedFile();
            peptideShakerGUI.setLastSelectedFolder(newFile.getAbsolutePath());

            if (!newFile.getName().toLowerCase().endsWith("cps")) {
                JOptionPane.showMessageDialog(this, "Not a PeptideShaker file (.cps).",
                        "Wrong File.", JOptionPane.ERROR_MESSAGE);
            } else {

                // get the properties files
                for (File file : newFile.getParentFile().listFiles()) {
                    if (file.getName().toLowerCase().endsWith(".properties")) {
                        if (!openDialog.getSearchParametersFiles().contains(file)) {
                            openDialog.getSearchParametersFiles().add(file);
                        }
                    }
                }

                this.setVisible(false);
                openDialog.isPsFile(true);
                openDialog.importPeptideShakerFile(newFile);
                this.dispose();
            }
        }
    }//GEN-LAST:event_openJButtonActionPerformed

    /**
     * Change the cursor to a hand cursor.
     * 
     * @param evt 
     */
    private void peptideShakerButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_peptideShakerButtonMouseEntered
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_peptideShakerButtonMouseEntered

    /**
     * Change the cursor back to the default cursor.
     * 
     * @param evt 
     */
    private void peptideShakerButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_peptideShakerButtonMouseExited
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_peptideShakerButtonMouseExited

    /**
     * Open the PeptideShaker web page.
     * 
     * @param evt 
     */
    private void peptideShakerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_peptideShakerButtonActionPerformed
        setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        BareBonesBrowserLaunch.openURL("http://peptide-shaker.googlecode.com");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_peptideShakerButtonActionPerformed

    /**
     * Change the cursor to a hand cursor.
     * 
     * @param evt 
     */
    private void compomicsButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_compomicsButtonMouseEntered
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_compomicsButtonMouseEntered

    /**
     * Change the cursor back to the default cursor.
     * 
     * @param evt 
     */
    private void compomicsButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_compomicsButtonMouseExited
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_compomicsButtonMouseExited

    /**
     * Open the Compomics web page.
     * 
     * @param evt 
     */
    private void compomicsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compomicsButtonActionPerformed
        setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        BareBonesBrowserLaunch.openURL("http://compomics.wordpress.com");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_compomicsButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel backgroundPanel;
    private javax.swing.JButton compomicsButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton newJButton;
    private javax.swing.JButton openJButton;
    private javax.swing.JButton peptideShakerButton;
    // End of variables declaration//GEN-END:variables
}
