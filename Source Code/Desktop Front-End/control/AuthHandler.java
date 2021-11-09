package control;

import GUI.LoginFrame;
import GUI.MainFrame;

import javax.swing.*;

public class AuthHandler {

    public static final int FIRST_LOGIN = 0;
    public static final int NORMAL_LOGIN = 1;
    public static final int WRONG_CREDENTIALS = 2;
    public static final int SUPERADMIN_LOGIN = 3;

    private static final String FIRST_LOGIN_MESSAGE = "            Primo accesso rilevato.      " +
                                                      "\nModifica le credenziali da super admin.";

    public static int login (LoginFrame frame, String adminName, String adminPassword) {

        int loginResult = AuthHandler.verifyCredentialsFromFile(adminName, adminPassword);

        if (loginResult == FIRST_LOGIN) {
            JOptionPane.showMessageDialog(frame,
                                        FIRST_LOGIN_MESSAGE,
                                        "Primo accesso",
                                        JOptionPane.INFORMATION_MESSAGE);
            frame.setVisible(false);
            Controller.getCambiaCredenzialiFrame().setVisible(true);
        }
        else if (loginResult == SUPERADMIN_LOGIN) {
            frame.setVisible(false);
            MainFrame mainFrame = new MainFrame(false);
            mainFrame.setVisible(true);
            mainFrame.setTitle(mainFrame.getTitle() + " - Admin: " + adminName);
        }
        else if (loginResult == NORMAL_LOGIN) {
            frame.setVisible(false);
            MainFrame mainFrame = new MainFrame(false);
            mainFrame.setVisible(true);
            mainFrame.setTitle(mainFrame.getTitle() + " - Admin: " + adminName);
        }
        else if (loginResult == WRONG_CREDENTIALS){
            JOptionPane.showMessageDialog(frame,
                    "Credenziali non valide",
                    "Accesso negato",
                    JOptionPane.ERROR_MESSAGE);
        }
        else {
            JOptionPane.showMessageDialog(frame,
                    "Errore nella lettura delle credenziali.",
                    "Accesso negato",
                    JOptionPane.ERROR_MESSAGE);
        }

        return loginResult;

    }
    public static int verifyCredentialsFromFile(String adminName, String adminPassword) {
        return Controller.getAdminDataHandler().checkLoginType(adminName, adminPassword);
    }

    public static boolean updateSuperadminCredentialsOnFile(String newName, String newPassword) {
        return Controller.getAdminDataHandler().updateSuperadminCredentials(newName, newPassword);
    }

    public static boolean addNewCredentialsToFile() {
        return Controller.getAdminDataHandler().addNewCredentials();
    }

}
