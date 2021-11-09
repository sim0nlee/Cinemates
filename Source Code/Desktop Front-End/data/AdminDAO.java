package data;

public interface AdminDAO {

    int checkLoginType (String name, String password);

    boolean updateSuperadminCredentials(String newName, String newPassword);

    boolean addNewCredentials();

}
