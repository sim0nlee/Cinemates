package GUI;

import control.AuthHandler;
import control.Controller;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CambiaCredenzialiFrame extends JFrame {

	private final JPasswordField nuovaPasswordField;
	private final JTextField nuovoNomeField;

	public CambiaCredenzialiFrame() {
		
		Controller.setActiveFrame(this);
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(Controller.class.getResource("/icon/clapboard.png")));		
		setTitle("CinemateStats - Creds");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 314, 274);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNuovaPassword = new JLabel("Nuova password");
		lblNuovaPassword.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNuovaPassword.setBounds(55, 87, 114, 19);
		contentPane.add(lblNuovaPassword);
		
		JLabel lblNuovoNome = new JLabel("Nuovo nome");
		lblNuovoNome.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNuovoNome.setBounds(55, 21, 114, 19);
		contentPane.add(lblNuovoNome);
		
		nuovaPasswordField = new JPasswordField();
		nuovaPasswordField.setBounds(55, 116, 193, 19);
		contentPane.add(nuovaPasswordField);
		
		nuovoNomeField = new JTextField();
		nuovoNomeField.setColumns(10);
		nuovoNomeField.setBounds(55, 50, 193, 19);
		contentPane.add(nuovoNomeField);
		
		JButton btnConferma = new JButton("Conferma");
		btnConferma.setForeground(Color.WHITE);
		btnConferma.setBackground(Color.GRAY);
		btnConferma.addActionListener(e -> {

			String newName = nuovoNomeField.getText();
			String newPassword = nuovaPasswordField.getText();
			if (!newName.isBlank() && !newPassword.isBlank()) {
				
				boolean updateOperation = AuthHandler.updateSuperadminCredentialsOnFile(newName, newPassword);
				
				if (updateOperation) {
					CambiaCredenzialiFrame.this.setVisible(false);
					MainFrame mainFrame = new MainFrame(true);
					mainFrame.setVisible(true);
					mainFrame.setTitle(mainFrame.getTitle() + " - Admin: " + newName);
				}
				else {
					JOptionPane.showMessageDialog(CambiaCredenzialiFrame.this,
												  "Problema nella scrittura delle credenziali",
												  "Errore",
												  JOptionPane.ERROR_MESSAGE);
				}
				
			}
			else {
				JOptionPane.showMessageDialog(CambiaCredenzialiFrame.this,
											  "Inserisci delle credenziali valide",
											  "Credenziali non valide",
											   JOptionPane.ERROR_MESSAGE);
			}

		});
		btnConferma.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnConferma.setFocusable(false);
		btnConferma.setBounds(94, 166, 114, 27);
		contentPane.add(btnConferma);
		
	}

}
