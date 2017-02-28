package com.cqblueprints.testing.cq.pageobjects;

/**
 * Provides helper methods to perform actions on the login page
 * 
 * @author <a href="mailto:dsamarjian@gmail.com">Diran Samarjian</a>
 */
public interface LoginPage {
	
	/**
	 * Logs in using the provided credentials
	 * @param user   username
	 * @param pass   password
	 * @return       A new instance of WelcomePage
	 */
	public WelcomePage loginAs(String user, String pass);
}
