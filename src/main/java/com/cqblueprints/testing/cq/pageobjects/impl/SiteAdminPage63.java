package com.cqblueprints.testing.cq.pageobjects.impl;

import com.cqblueprints.testing.cq.pageobjects.SiteAdminPage;
import junit.framework.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Provides helper methods to perform actions on the site admin page
 * AEM Version 6.0
 * 
 * @author <a href="mailto:dsamarjian@gmail.com">Diran Samarjian</a>
 */
public class SiteAdminPage63 extends SiteAdminPage60 implements SiteAdminPage {

	public static final Logger LOG = LoggerFactory.getLogger(SiteAdminPage63.class);

	public SiteAdminPage63(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	}

	public void clickSiteAdminButton(String title) {
		try {
			driver.findElement(By.xpath("//coral-button-label[text()='More']"))
					.click();
		} catch (Exception e) {
			// No more button
		}
		try {
			driver.findElement(By.xpath("//coral-button-label[text()='" + title + "']"))
					.click();
		} catch (Exception e) {
			driver.findElement(
					By.xpath("//button[text()='" + title + "']")).click();
		}
	}

	public void selectDropdownItem(String text) {
		List<WebElement> elements = driver.findElements(By
				.xpath("//coral-list-item-content"));
		for (WebElement el : elements) {
			if (el.getText().equals(text)) {
				wait.until(ExpectedConditions.visibilityOf(el));
				el.click();
				break;
			}
		}
	}

	public void createPageWithTemplate(String thumbnailPath, String title) {

		boolean createClicked = false;
		for(int i = 0; i < 5; i++)
		{
			try
			{
				driver.findElement(By.xpath("//coral-button-label[text()='Create']"))
						.click();
				createClicked = true;
				break;
			}
			catch(Exception e)
			{
				LOG.warn("Exception trying to click 'Create' button, try " + i, e);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					LOG.error("InterruptedException while trying to sleep", e1);
				}
			}
		}

		if(!createClicked)
		{
			LOG.error("Could not click create.");
			return;
		}

		driver.findElement(By.xpath("//coral-list-item-content[text()='Page']")).click();

		driver.findElement(By.xpath("//img[@src='" + thumbnailPath + "']"))
				.click();
//		try {
//			clickButtonByText("Next");
//		} catch (Exception e) {
//
//		}

		driver.findElement(By.xpath("//button[@coral-wizardview-next]"))
				.click();

//		try {
//			driver.findElement(By.name("pageTitle")).sendKeys(title);
//		} catch (Exception e) {
//
//		}

		driver.findElement(By.name("./jcr:title")).sendKeys(title);

		driver.findElement(By.xpath("//coral-button-label[text()='Create']"))
				.click();
		try {
			driver.findElement(By.xpath("//coral-button-label[text()='Done']")).click();
		} catch (Exception e) {
			driver.findElement(By.xpath("//button[text()='Done']")).click();
		}
		Assert.assertNotNull("Page failed to create",
				driver.findElement(By.xpath("//div[@title='" + title + "']")));
	}

}
