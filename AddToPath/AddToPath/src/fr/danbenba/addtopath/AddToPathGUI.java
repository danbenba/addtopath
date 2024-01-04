package fr.danbenba.addtopath;

import javax.swing.*;
import javax.swing.border.AbstractBorder;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class AddToPathGUI {

	@SuppressWarnings("serial")
	static class RoundedBorder extends AbstractBorder {
	    private int radius;

	    RoundedBorder(int radius) {
	        this.radius = radius;
	    }

	    @Override
	    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
	        g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
	    }

	    @Override
	    public Insets getBorderInsets(Component c) {
	        return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
	    }

	    @Override
	    public Insets getBorderInsets(Component c, Insets insets) {
	        insets.left = insets.right = insets.bottom = this.radius + 1;
	        insets.top = this.radius + 2;
	        return insets;
	    }
	}
	
	private static String extractScriptFromResources() throws IOException {
	    String resourcePath = "/updatePath.bat"; // Chemin vers updatePath.bat dans les ressources
	    File tempFile = File.createTempFile("updatePath", ".bat");
	    tempFile.deleteOnExit();
	    try (InputStream in = AddToPathGUI.class.getResourceAsStream(resourcePath);
	         FileOutputStream out = new FileOutputStream(tempFile)) {
	        if (in == null) {
	            throw new IOException("Resource not found: " + resourcePath);
	        }
	        byte[] buffer = new byte[1024];
	        int bytesRead;
	        while ((bytesRead = in.read(buffer)) != -1) {
	            out.write(buffer, 0, bytesRead);
	        }
	    }
	    return tempFile.getAbsolutePath();
	}

	
	public static void main(String[] args) {
		try {
	        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
	        e.printStackTrace();
	    }
		
		try {
            // Set the System out to use UTF-8 encoding
            PrintStream out = new PrintStream(System.out, true, "UTF-8");
            
            // Print some UTF-8 encoded text
            out.println("AddToPath created by danbenba.\nVersion 1.0.0\n");
            out.println("UTF-8 initialized");

        } catch (UnsupportedEncodingException e) {
            // Handle the exception here
            System.err.println("UTF-8 encoding is not supported on this platform.");
            e.printStackTrace();
        }
		

	    JFrame frame = new JFrame("AddToPath - Version 1.0.0 Béta - by danbenba");
	    frame.setSize(590, 280); // Window size
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setLayout(null);

	    // Centering components horizontally
	    int frameCenterX = frame.getWidth() / 2;
	    int textFieldWidth = 260;
	    int buttonWidth = 100;
	    int checkboxWidth = 250;
	    int radioButtonWidth = 180; // Adjust as needed
	    int spaceBetweenComponents = 5; // Space between checkbox and radio buttons

	    JTextField textField = new JTextField();
	    textField.setBounds(frameCenterX - textFieldWidth / 2, 20, textFieldWidth, 25);
	    frame.add(textField);

	    JButton selectButton = new JButton("Select File");
	    selectButton.setBounds(frameCenterX - buttonWidth / 2, 55, buttonWidth, 25);
	    frame.add(selectButton);

	    JCheckBox copyCheckBox = new JCheckBox("Copy file to AppData (LocalAppdata)");
	    copyCheckBox.setBounds(frameCenterX - checkboxWidth / 2, 90, checkboxWidth, 25);
	    frame.add(copyCheckBox);

	    // Calculate the starting Y position for the radio buttons
	    int radioButtonStartY = copyCheckBox.getY() + copyCheckBox.getHeight() + spaceBetweenComponents;

	    // Radio buttons
	    ButtonGroup pathOptionsGroup = new ButtonGroup();
	    JRadioButton userPathRadioButton = new JRadioButton("Add to User PATH", true);
	    JRadioButton systemPathRadioButton = new JRadioButton("Add to System PATH");
	    JRadioButton bothPathsRadioButton = new JRadioButton("Add to Both PATHs");

	    // Set bounds for radio buttons with appropriate spacing and centered
	    int startX = frameCenterX - (3 * radioButtonWidth + 2 * spaceBetweenComponents) / 2;
	    userPathRadioButton.setBounds(startX, radioButtonStartY, radioButtonWidth, 25);
	    systemPathRadioButton.setBounds(startX + radioButtonWidth + spaceBetweenComponents, radioButtonStartY, radioButtonWidth, 25);
	    bothPathsRadioButton.setBounds(startX + 2 * (radioButtonWidth + spaceBetweenComponents), radioButtonStartY, radioButtonWidth, 25);

	    pathOptionsGroup.add(userPathRadioButton);
	    pathOptionsGroup.add(systemPathRadioButton);
	    pathOptionsGroup.add(bothPathsRadioButton);

	    frame.add(userPathRadioButton);
	    frame.add(systemPathRadioButton);
	    frame.add(bothPathsRadioButton);

	    JButton updateButton = new JButton("Update Path");
	    int updateButtonWidth = 100;
	    int updateButtonHeight = 25;
	    int updateButtonX = (frame.getWidth() - updateButtonWidth) / 2; // Centered on the frame
	    int updateButtonY = 150; // Positioned below the radio buttons
	    updateButton.setBounds(updateButtonX, updateButtonY, updateButtonWidth, updateButtonHeight);
	    frame.add(updateButton);

	    JLabel poweredByLabel = new JLabel("Version 1.0.0 Béta - Powered by danbenba", SwingConstants.CENTER);
	    int labelWidth = frame.getWidth();
	    int labelHeight = 40;
	    int labelX = 0;
	    int labelY = frame.getHeight() - labelHeight - 30;
	    poweredByLabel.setBounds(labelX, labelY, labelWidth, labelHeight);
	    frame.add(poweredByLabel);
        // Assurez-vous que cette ligne vient après avoir ajouté tous les composants
        frame.setVisible(true);
        
        selectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                if(fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION){
                    File file = fileChooser.getSelectedFile();
                    textField.setText(file.getAbsolutePath());
                }
            }
        });


        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String filePath = textField.getText();
                boolean copyFile = copyCheckBox.isSelected();

                if(copyFile){
                    try {
                        File targetDir = new File(System.getenv("LocalAppData") + "\\Files@AddToPath");
                        if(!targetDir.exists()) {
                            targetDir.mkdirs();
                        }
                        Files.copy(Paths.get(filePath), 
                                   Paths.get(targetDir.getAbsolutePath() + "\\" + new File(filePath).getName()),
                                   StandardCopyOption.REPLACE_EXISTING);
                        JOptionPane.showMessageDialog(frame, "Fichier copié avec succès.");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Erreur lors de la copie du fichier.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }

                try {
                    String scriptPath = extractScriptFromResources();
                    String filePathArg = "\"" + filePath + "\"";

                    if (userPathRadioButton.isSelected()) {
                        executeBatchScript(scriptPath, filePathArg, false);
                    } else if (systemPathRadioButton.isSelected()) {
                        executeBatchScript(scriptPath, filePathArg, true);
                    } else if (bothPathsRadioButton.isSelected()) {
                        executeBatchScript(scriptPath, filePathArg, false);
                        executeBatchScript(scriptPath, filePathArg, true);
                    }

                    JOptionPane.showMessageDialog(frame, "PATH mis à jour.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Erreur lors de la mise à jour du PATH : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        frame.setVisible(true);
    }
	private static void executeBatchScript(String scriptPath, String filePath, boolean isSystemPath) throws IOException {
	    ProcessBuilder pb;
	    if (isSystemPath) {
	        // Exécuter en tant qu'administrateur pour le PATH système
	        pb = new ProcessBuilder("cmd.exe", "/c", "admin" + scriptPath, filePath);
	    } else {
	        // Exécuter normalement pour le PATH utilisateur
	        pb = new ProcessBuilder("cmd.exe", "/c", scriptPath, filePath);
	    }
	    pb.redirectErrorStream(true); // Rediriger stderr vers stdout
	    Process p = pb.start();

	    // Lecture et affichage des messages d'erreur
	    try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            System.out.println(line);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    try {
	        int exitCode = p.waitFor();
	        if (exitCode != 0) {
	            throw new IOException("Le script a échoué avec le code de sortie : " + exitCode);
	        }
	    } catch (InterruptedException e) {
	        Thread.currentThread().interrupt();
	        throw new IOException("L'exécution du script a été interrompue", e);
	    }
	}
}
