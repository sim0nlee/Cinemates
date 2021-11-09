package Test;

import GUI.LoginFrame;
import control.AuthHandler;
import control.Controller;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VerifyCredentials_Test {

	private static final String TEST_FILE_PATH = "C://test.txt";

	private static final String FIRST_LOGIN_CREDENTIALS = "admin password 0";
	private static final String[] FIRST_LOGIN_CREDENTIALS_INPUT = new String[]{"admin", "password"};

	private static final String SUPERADMIN_LOGIN_CREDENTIALS = "newname newpassword 1";
	private static final String[] SUPERADMIN_LOGIN_CREDENTIALS_INPUT = new String[]{"newname", "newpassword"};

	private static final String NORMAL_LOGIN_CREDENTIALS = "name password";
	private static final String[] NORMAL_LOGIN_CREDENTIALS_INPUT = new String[]{"name", "password"};

	private static final String FAILED_LOGIN_CREDENTIALS = "name password";
	private static final String[] FAILED_LOGIN_CREDENTIALS_INPUT = new String[]{"name", "password1", "name1", "password"};


	@Test
	//4A
	void testVerifyCredentialsFromFileFirstLogin () {

		try (FileWriter writer = new FileWriter(TEST_FILE_PATH, false)) {
			writer.write(FIRST_LOGIN_CREDENTIALS);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Controller.setFilePath(TEST_FILE_PATH);
		int result = AuthHandler.verifyCredentialsFromFile(FIRST_LOGIN_CREDENTIALS_INPUT[0],
														   FIRST_LOGIN_CREDENTIALS_INPUT[1]);
		assertEquals(AuthHandler.FIRST_LOGIN, result);

	}

	@Test
	//4B
	void testVerifyCredentialsFromFileSuperadminLogin () {

		try (FileWriter writer = new FileWriter(TEST_FILE_PATH)) {
			writer.write(SUPERADMIN_LOGIN_CREDENTIALS);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Controller.setFilePath(TEST_FILE_PATH);
		int result = AuthHandler.verifyCredentialsFromFile(SUPERADMIN_LOGIN_CREDENTIALS_INPUT[0],
				                                           SUPERADMIN_LOGIN_CREDENTIALS_INPUT[1]);
		assertEquals(AuthHandler.SUPERADMIN_LOGIN, result);

	}

	@Test
	//4C
	void testVerifyCredentialsFromFileNormalLogin () {

		try (FileWriter writer = new FileWriter(TEST_FILE_PATH);) {
			writer.write(NORMAL_LOGIN_CREDENTIALS);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Controller.setFilePath(TEST_FILE_PATH);
		int result = AuthHandler.verifyCredentialsFromFile(NORMAL_LOGIN_CREDENTIALS_INPUT[0],
														   NORMAL_LOGIN_CREDENTIALS_INPUT[1]);
		assertEquals(AuthHandler.NORMAL_LOGIN, result);

	}

	@Test
	//4D
	void testVerifyCredentialsFromFileFailedLogin () {

		try (FileWriter writer = new FileWriter(TEST_FILE_PATH);) {
			writer.write(FAILED_LOGIN_CREDENTIALS);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Controller.setFilePath(TEST_FILE_PATH);
		int result;

		result = AuthHandler.verifyCredentialsFromFile(FAILED_LOGIN_CREDENTIALS_INPUT[0],
				                                       FAILED_LOGIN_CREDENTIALS_INPUT[1]);
		assertEquals(AuthHandler.WRONG_CREDENTIALS, result);

		result = AuthHandler.verifyCredentialsFromFile(FAILED_LOGIN_CREDENTIALS_INPUT[2],
													   FAILED_LOGIN_CREDENTIALS_INPUT[3]);
		assertEquals(AuthHandler.WRONG_CREDENTIALS, result);

	}

}
