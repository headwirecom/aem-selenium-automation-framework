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
 * AEM Version 6.2
 * 
 * @author <a href="mailto:dsamarjian@gmail.com">Diran Samarjian</a>
 */
public class SiteAdminPage62 extends SiteAdminPage60 implements SiteAdminPage {

	public static final Logger LOG = LoggerFactory.getLogger(SiteAdminPage62.class);

	public void openSitesPage() {
		LOG.info("Opening sites page");
		for(int i = 0; i < 5; i++)
		{
			try
			{
				driver.findElement(By.xpath("//a[@is='coral-shell-homeanchor']")).click();
				WebElement sitesButton = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[text()='Sites']"))));
				sitesButton.click();
				break;
			}
			catch(Exception e)
			{
				LOG.warn("Error clicking sites link, attempt " + i, e);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					LOG.error("Interrupted Exception", e1);
				}
			}
		}
		LOG.info("Opened sites page");
	}

	public SiteAdminPage62(WebDriver driver, WebDriverWait wait) {
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

		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//img[@src='" + thumbnailPath + "']"))))
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
				driver.findElement(By.xpath("//coral-columnview-item-content[@title='" + title + "']")));
	}

	public SiteAdminPage navigateToPage(String path) {
		String[] parts = path.split("/");
		StringBuilder pathBuilder = new StringBuilder();
		for (String part : parts) {
			if(part.equals(""))
			{
				continue;
			}
			if (part.equals("content")) {
				pathBuilder.append("/content");
				continue;
			}
			pathBuilder.append("/" + part);
			WebElement page = wait.until(ExpectedConditions.visibilityOf(
					driver.findElement(By.xpath("//coral-columnview-item[@data-foundation-collection-item-id='" +
							pathBuilder.toString() + "']/coral-columnview-item-content"))
			));
			page.click();
		}

		return new SiteAdminPage61(driver, wait);
	}

}
