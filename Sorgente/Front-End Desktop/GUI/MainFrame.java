package GUI;

import control.AuthHandler;
import control.Controller;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;


public class MainFrame extends JFrame {
	
	public JTextField[] fields;

	public MainFrame (boolean superadmin) {
		
		Controller.setActiveFrame(this);
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(Controller.class.getResource("/icon/clapboard.png")));		
		setTitle("CinemateStats");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 906, 523);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
		
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel utentiLabel = new JLabel("Utenti:");
		utentiLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		utentiLabel.setBounds(36, 27, 55, 35);
		contentPane.add(utentiLabel);
		
		JLabel collegamentiLabel = new JLabel("Collegamenti tra utenti:");
		collegamentiLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		collegamentiLabel.setBounds(36, 237, 191, 35);
		contentPane.add(collegamentiLabel);
		
		JLabel ricercheLabel = new JLabel("Ricerche di film effettuate:");
		ricercheLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		ricercheLabel.setBounds(36, 307, 218, 35);
		contentPane.add(ricercheLabel);
		
		JLabel utentiConnessiLabel = new JLabel("Utenti connessi:");
		utentiConnessiLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		utentiConnessiLabel.setBounds(36, 97, 130, 35);
		contentPane.add(utentiConnessiLabel);

		JLabel avventuraLabel = new JLabel("Film di avventura nelle liste:");
		avventuraLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		avventuraLabel.setBounds(505, 27, 236, 35);
		contentPane.add(avventuraLabel);

		JLabel azioneLabel = new JLabel("Film d'azione nelle liste:");
		azioneLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		azioneLabel.setBounds(505, 97, 236, 35);
		contentPane.add(azioneLabel);

		JLabel commedieLabel = new JLabel("Commedie nelle liste:");
		commedieLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		commedieLabel.setBounds(505, 167, 236, 35);
		contentPane.add(commedieLabel);

		JLabel horrorLabel = new JLabel("Horror nelle liste: ");
		horrorLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		horrorLabel.setBounds(505, 307, 236, 35);
		contentPane.add(horrorLabel);

		JLabel thrillerLabel = new JLabel("Thriller nelle liste: ");
		thrillerLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		thrillerLabel.setBounds(505, 237, 236, 35);
		contentPane.add(thrillerLabel);
		
		JTextField utentiField = new JTextField();
		utentiField.setBorder(new MatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
		utentiField.setText("100");
		utentiField.setFont(new Font("Tahoma", Font.BOLD, 17));
		utentiField.setEditable(false);
		utentiField.setBounds(264, 31, 96, 27);
		contentPane.add(utentiField);
		utentiField.setColumns(10);
		
		JTextField utentiConnessiField = new JTextField();
		utentiConnessiField.setBorder(new MatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
		utentiConnessiField.setFont(new Font("Tahoma", Font.BOLD, 17));
		utentiConnessiField.setEditable(false);
		utentiConnessiField.setColumns(10);
		utentiConnessiField.setBounds(264, 102, 96, 27);
		contentPane.add(utentiConnessiField);
		
		JTextField collegamentiField = new JTextField();
		collegamentiField.setFont(new Font("Tahoma", Font.BOLD, 17));
		collegamentiField.setEditable(false);
		collegamentiField.setColumns(10);
		collegamentiField.setBorder(new MatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
		collegamentiField.setBounds(264, 241, 96, 27);
		contentPane.add(collegamentiField);
		
		JTextField ricercheField = new JTextField();
		ricercheField.setFont(new Font("Tahoma", Font.BOLD, 17));
		ricercheField.setEditable(false);
		ricercheField.setColumns(10);
		ricercheField.setBorder(new MatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
		ricercheField.setBounds(264, 311, 96, 27);
		contentPane.add(ricercheField);
		
		JTextField avventuraField = new JTextField();
		avventuraField.setFont(new Font("Tahoma", Font.BOLD, 17));
		avventuraField.setEditable(false);
		avventuraField.setColumns(10);
		avventuraField.setBorder(new MatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
		avventuraField.setBounds(760, 31, 96, 27);
		contentPane.add(avventuraField);
		
		JTextField azioneField = new JTextField();
		azioneField.setFont(new Font("Tahoma", Font.BOLD, 17));
		azioneField.setEditable(false);
		azioneField.setColumns(10);
		azioneField.setBorder(new MatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
		azioneField.setBounds(760, 102, 96, 27);
		contentPane.add(azioneField);
		
		JTextField commedieField = new JTextField();
		commedieField.setFont(new Font("Tahoma", Font.BOLD, 17));
		commedieField.setEditable(false);
		commedieField.setColumns(10);
		commedieField.setBorder(new MatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
		commedieField.setBounds(760, 171, 96, 27);
		contentPane.add(commedieField);
		
		JTextField thrillerField = new JTextField();
		thrillerField.setFont(new Font("Tahoma", Font.BOLD, 17));
		thrillerField.setEditable(false);
		thrillerField.setColumns(10);
		thrillerField.setBorder(new MatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
		thrillerField.setBounds(760, 241, 96, 27);
		contentPane.add(thrillerField);
		
		JTextField horrorField = new JTextField();
		horrorField.setFont(new Font("Tahoma", Font.BOLD, 17));
		horrorField.setEditable(false);
		horrorField.setColumns(10);
		horrorField.setBorder(new MatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
		horrorField.setBounds(760, 311, 96, 27);
		contentPane.add(horrorField);
		
		fields = new JTextField[] {utentiField, utentiConnessiField, collegamentiField, 
								   ricercheField, avventuraField, azioneField, 
								   commedieField, thrillerField, horrorField};
		
		Controller.updateStats(this);
		
		JButton aggiornaBtn = new JButton("Aggiorna Statistiche");
		aggiornaBtn.setForeground(Color.WHITE);
		aggiornaBtn.setBackground(Color.GRAY);
		aggiornaBtn.setFont(new Font("Tahoma", Font.PLAIN, 18));
		if (superadmin) {
			aggiornaBtn.setBounds(36, 421, 246, 35);
		}
		else {
			aggiornaBtn.setBounds(321, 418, 246, 35);
		}
		aggiornaBtn.setFocusable(false);
		aggiornaBtn.addActionListener(e -> Controller.updateStats(MainFrame.this));
		contentPane.add(aggiornaBtn);
		
		if (superadmin) {
			JButton nuoveCredenzialiBtn = new JButton("Genera nuove credenziali");
			nuoveCredenzialiBtn.setBackground(Color.GRAY);
			nuoveCredenzialiBtn.setFont(new Font("Tahoma", Font.PLAIN, 18));
			nuoveCredenzialiBtn.setBounds(610, 421, 246, 35);
			nuoveCredenzialiBtn.setForeground(Color.WHITE);
			nuoveCredenzialiBtn.setFocusable(false);
			contentPane.add(nuoveCredenzialiBtn);
			nuoveCredenzialiBtn.addActionListener(e -> {
				
				boolean writeOperation = AuthHandler.addNewCredentialsToFile();
				if (writeOperation) {
					JOptionPane.showMessageDialog(MainFrame.this,
												  "Sono state create delle nuove credenziali di accesso",
												  "Credenziali create",
												   JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(MainFrame.this,
												  "Problema nella scrittura delle nuove credenziali",
												  "Errore",
												   JOptionPane.ERROR_MESSAGE);
				}
			});
		}
		
	}
}
