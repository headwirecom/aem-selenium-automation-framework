package com.headwire.testing.cq.factory;

/**
 * Provides helper methods to perform actions on the welcome page
 * 
 * @author <a href="mailto:dsamarjian@gmail.com">Diran Samarjian</a>
 */
public interface WelcomePage {
	/**
	 * Navigate to the site admin page
	 * @return New instance of SiteAdminPage
	 */
	public SiteAdminPage openSiteAdmin();
}
