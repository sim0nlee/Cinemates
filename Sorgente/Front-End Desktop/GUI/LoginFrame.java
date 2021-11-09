package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import control.AuthHandler;
import control.Controller;

@SuppressWarnings("serial")
public class LoginFrame extends JFrame {

	public LoginFrame() {
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(Controller.class.getResource("/icon/clapboard.png")));		
		setTitle("CinemateStats - Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 333, 257);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
		
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JTextField adminNameTextField = new JTextField();
		adminNameTextField.setBounds(63, 49, 193, 19);
		adminNameTextField.setColumns(10);
		
		JPasswordField passwordField = new JPasswordField();
		passwordField.setBounds(63, 115, 193, 19);
		
		JLabel lblNomeAdmin = new JLabel("Nome admin:");
		lblNomeAdmin.setBounds(63, 20, 114, 19);
		lblNomeAdmin.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		JLabel lblPasswordAdmin = new JLabel("Password admin:");
		lblPasswordAdmin.setBounds(63, 86, 114, 19);
		lblPasswordAdmin.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		JButton accediButton = new JButton("Accedi");
		accediButton.setForeground(Color.WHITE);
		accediButton.setBackground(Color.GRAY);
		accediButton.setFocusable(false);
		accediButton.setBounds(102, 165, 114, 27);
		accediButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		accediButton.addActionListener(e -> {
			AuthHandler.login(this, adminNameTextField.getText(), passwordField.getText());
		});
		
		contentPane.setLayout(null);
		contentPane.add(lblPasswordAdmin);
		contentPane.add(lblNomeAdmin);
		contentPane.add(passwordField);
		contentPane.add(adminNameTextField);
		contentPane.add(accediButton);
	}
	
}
