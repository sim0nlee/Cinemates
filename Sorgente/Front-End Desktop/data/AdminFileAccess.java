package data;

import GUI.LoginFrame;
import control.AuthHandler;
import control.Controller;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

public class AdminFileAccess implements AdminDAO {

    @Override
    public int checkLoginType (String adminName, String adminPassword) {
        adminName = adminName.trim();
        try (RandomAccessFile file = new RandomAccessFile(new File(Controller.credentialsPath), "r")) {
            String[] credentials;
            String line;
            while ((line = file.readLine()) != null) {
                credentials = line.split(" ");
                if (adminName.equals(credentials[0]) && adminPassword.equals(credentials[1])) {
                    if (credentials.length == 3) {
                        if (credentials[2].equals("0"))
                            return AuthHandler.FIRST_LOGIN;
                        else
                            return AuthHandler.SUPERADMIN_LOGIN;
                    }
                    else
                        return AuthHandler.NORMAL_LOGIN;
                }
            }
            return AuthHandler.WRONG_CREDENTIALS;
        }
        catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean updateSuperadminCredentials(String newName, String newPassword) {
        newName = newName.trim();
        try (RandomAccessFile file = new RandomAccessFile(new File(Controller.credentialsPath), "rw")) {
            file.setLength(0);
            file.writeBytes(newName + " " + newPassword + " 1\n");
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addNewCredentials() {
        try (RandomAccessFile file = new RandomAccessFile(new File(Controller.credentialsPath), "rw")) {
            String newAdmin = randomAlphanumericString();
            String newPassword = randomAlphanumericString();
            file.seek(file.length());
            file.writeBytes(newAdmin + " " + newPassword + '\n');
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String randomAlphanumericString () {
        return UUID.randomUUID().toString().substring(0, 8);
    }

}
