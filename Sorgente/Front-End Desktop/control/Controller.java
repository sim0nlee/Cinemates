package control;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import GUI.CambiaCredenzialiFrame;
import GUI.LoginFrame;
import GUI.MainFrame;
import data.RTDBStatisticsAccess;
import data.AdminDAO;
import data.AdminFileAccess;
import data.StatisticsDAO;

public class Controller {
	
	private static StatisticsDAO statisticsAccess = new RTDBStatisticsAccess();
	private static final AdminDAO adminDataHandler = new AdminFileAccess();

	//Quando si eseguono test viene automaticamente rimpiazzato dal file di test
	public static String credentialsPath = "C://credenziali.txt";


	private static LoginFrame loginFrame;
	private static CambiaCredenzialiFrame cambiaCredenzialiFrame;
	
	private static JFrame activeFrame;
	


	public static void main(String[] args) {

		//SIMULAZIONE: IL FILE VIENE DELLE CREDENZIALI VIENE GENERATO AL LANCIO, SE INESISTENTE
		try {
			if (new File(credentialsPath).createNewFile()) {
				try (FileWriter fw = new FileWriter(credentialsPath)) {
					fw.write("admin password 0");
				}
			}
		} catch (IOException e) {
			System.out.println("Errore di creazione del file.");
			e.printStackTrace();
		}

		EventQueue.invokeLater(() -> {
			try {
				getLoginFrame().setVisible(true);
				setActiveFrame(loginFrame);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	


	public static void updateStats (MainFrame mainFrame) {
			
		int[] stats = statisticsAccess.fetchStats();

		if (stats != null) {
			if (mainFrame != null) {
				for (int i = 0; i < stats.length; i++) {
					mainFrame.fields[i].setText(" " + stats[i]);
				}
			}
		}
		else {
			if (mainFrame != null) {
				for (int i = 0; i < mainFrame.fields.length; i++) {
					mainFrame.fields[i].setText(" " + 0);
				}
			}
		}
		
	}



	public static AdminDAO getAdminDataHandler() {
		return adminDataHandler;
	}
	
	public static LoginFrame getLoginFrame() { 
		loginFrame = loginFrame == null ? new LoginFrame() : loginFrame;
		return loginFrame; 
	}
	public static CambiaCredenzialiFrame getCambiaCredenzialiFrame() {
		cambiaCredenzialiFrame = cambiaCredenzialiFrame == null ? new CambiaCredenzialiFrame() : cambiaCredenzialiFrame;
		return cambiaCredenzialiFrame; 
	}

	public static JFrame getActiveFrame () {
		return activeFrame;
	}
	public static void setActiveFrame (JFrame frame) {
		activeFrame = frame;
	}
	

	public static void setFilePath (String path) {
		credentialsPath = path;
	}

	public static void setStatisticsAccess(RTDBStatisticsAccess handler) {
		statisticsAccess = handler;
	}
	
}
