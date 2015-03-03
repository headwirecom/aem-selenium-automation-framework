package com.headwire.testing.cq.pageobjects;

import java.util.List;

import junit.framework.Assert;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.headwire.testing.cq.factory.InboxPage;

import static com.headwire.testing.cq.base.BaseActions.ACTIONS;

/**
 * Provides helper methods to perform actions on the inbox page
 * AEM Version 6.0
 * 
 * @author <a href="mailto:dsamarjian@gmail.com">Diran Samarjian</a>
 */
public class InboxPage60 implements InboxPage {
	private WebDriver driver;
	private WebDriverWait wait;
	
	@FindBy(className = "x-grid3-td-4")
	private List<WebElement> inboxItems;
	
	@FindBy(xpath = "//span[@class='x-menu-item-text' and text()='Complete']")
	private WebElement completeButton;
	
	@FindBy(tagName = "textarea")
	private WebElement commentArea;
	
	@FindBy(xpath = "//button[text()='OK']")
	private WebElement workflowDialogOkButton;
	
	public InboxPage60(WebDriver driver, WebDriverWait wait) {
		PageFactory.initElements(driver, this);
		this.driver = driver;
		this.wait = wait;
		
		if (!"AEM Workflow Inbox".equals(driver.getTitle())) {
			throw new IllegalStateException("This is not the Inbox page");
		}
	}
	
	public InboxPage advanceWorkflowThroughInbox(String pageTitle, String comment) {
		By workflowItemBy = By.xpath("//div[@class='payload-summary']/div/a[text()='"+pageTitle+"']");
		wait.until(ExpectedConditions.visibilityOfElementLocated(workflowItemBy));
		WebElement workflowItem = driver.findElement(workflowItemBy);
		ACTIONS.contextClick(driver, workflowItem);
		wait.until(ExpectedConditions.visibilityOf(completeButton));
		completeButton = driver.findElement(By.xpath("//span[@class='x-menu-item-text' and text()='Complete']"));
		completeButton.click();
		driver.switchTo().activeElement();
		commentArea = driver.findElement(By.tagName("textarea"));
		commentArea.sendKeys(comment);
		wait.until(ExpectedConditions.visibilityOf(workflowDialogOkButton));
		workflowDialogOkButton.click();
		return new InboxPage60(driver, wait);
	}
	
	public void validateStatus(String status) {
		By statusBy = By.xpath("//tbody/tr/td/div/div[text()='"+status+"']");
		WebElement el = null;
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(statusBy));
			el = driver.findElement(statusBy);
		} catch(Exception e) {
			
		}
		Assert.assertNotNull("Status: "+status+" not found.", el);
	}
	
	public void validateComment(String comment) {
		By statusBy = By.xpath("//tbody/tr/td/div/div[text()='"+comment+"']");
		WebElement el = null;
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(statusBy));
			el = driver.findElement(statusBy);
		} catch(Exception e) {
			
		}
		Assert.assertNotNull("Comment: "+comment+" not found.", el);
	}
	
}
