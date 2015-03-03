package com.headwire.testing.cq.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.headwire.testing.cq.factory.ToolsPage;

/**
 * Provides helper methods to perform actions on the tools page
 * 
 * @author <a href="mailto:dsamarjian@gmail.com">Diran Samarjian</a>
 */
public class ToolsPage60 extends WelcomePage60 implements ToolsPage {
		
	public ToolsPage60(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		
		
		if (!"AEM Tools".equals(driver.getTitle())) {
			throw new IllegalStateException("This is not the Tools page");
		}
	}
		
}
