package com.cqblueprints.testing.cq.pageobjects.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Provides helper methods to perform actions on WCM author pages
 * in AEM 6.0
 * 
 * @author <a href="mailto:dsamarjian@gmail.com">Diran Samarjian</a>
 */
public class AuthorPage60 extends BaseAuthorPage {


	public AuthorPage60(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
		this.driver = driver;
		this.wait = wait;
	}

	public void selectInlineEditor(String type) {
		By by = By.xpath("//h2[contains(@class,'editor-selector-name') and text()='"+type+"']");
		wait.until(ExpectedConditions.presenceOfElementLocated(by));
		driver.findElement(by).click();
	}

}
