package com.cqblueprints.testing.cq.pageobjects.impl;

import com.cqblueprints.testing.cq.pageobjects.LoginPage;
import com.cqblueprints.testing.cq.pageobjects.WelcomePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Provides helper methods to perform actions on the login page
 * AEM Version 6.0
 * @author <a href="mailto:dsamarjian@gmail.com">Diran Samarjian</a>
 */
public class LoginPage60 implements LoginPage {
	private final WebDriver driver;
	private WebDriverWait wait;

	private WebElement username;
	private WebElement password;

	@FindBy(xpath = "//button[text()='Sign In']")
	private WebElement signIn;
	
	public LoginPage60(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;

		PageFactory.initElements(driver, this);
		
		if (!"AEM Sign In".equals(driver.getTitle())) {
			throw new IllegalStateException("This is not the login page");
		}
	}
	
	public WelcomePage loginAs(String user, String pass) {
		username.sendKeys(user);
		password.sendKeys(pass);
		signIn.click();
		WelcomePage60 welcomePage = new WelcomePage60(driver, wait);
		return welcomePage;
	}
}
