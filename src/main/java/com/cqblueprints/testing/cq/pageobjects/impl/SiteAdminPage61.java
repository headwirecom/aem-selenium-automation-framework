package com.cqblueprints.testing.cq.pageobjects.impl;

import com.cqblueprints.testing.cq.pageobjects.SiteAdminPage;
import junit.framework.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.cqblueprints.testing.cq.base.BaseActions.ACTIONS;

/**
 * Provides helper methods to perform actions on the site admin page
 * AEM Version 6.1
 * 
 * @author <a href="mailto:dsamarjian@gmail.com">Diran Samarjian</a>
 */
public class SiteAdminPage61 extends SiteAdminPage60 implements SiteAdminPage {

	public static final Logger LOG = LoggerFactory.getLogger(SiteAdminPage61.class);

	@FindBy(className = "card-page")
	private List<WebElement> pages;
	@FindBy(className = "icon-edit")
	private WebElement editPage;

	public SiteAdminPage61(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	}

	public void openSitesPage() {
		LOG.info("Opening sites page");
		for(int i = 0; i < 5; i++)
		{
			try
			{
				driver.findElement(By.xpath("//a[@href='/sites.html/content']")).click();
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

	public void cycleToColumnView()
	{
		try
		{
			Thread.sleep(1000);
		}
		catch(Exception e)
		{

		}
		List<WebElement> cycleButtons = driver.findElements(By.xpath("//button[contains(@class, 'coral-CycleButton-button')]"));
		for(WebElement cycleButton : cycleButtons)
		{
			if(cycleButton.isDisplayed() && cycleButton.getAttribute("title").equals("Column View"))
			{
				//Mode is correct
				return;
			}

			if(cycleButton.isDisplayed())
			{
				cycleButton.click();
				cycleToColumnView();
			}
		}
	}

	public SiteAdminPage navigateToPage(String path) {
		cycleToColumnView();
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
			driver.findElement(By.xpath("//a[@data-path = '"+pathBuilder.toString()+"']")).click();
		}


//		for (WebElement page : pages) {
//			String pageLink = page.getAttribute("data-path");
//			if (path.contains(pageLink)) {
//				page.click();
//			}
//		}

		return new SiteAdminPage61(driver, wait);
	}

	public void createPageWithTemplate(String thumbnailPath, String title) {

		boolean createClicked = false;
		for(int i = 0; i < 5; i++)
		{
			try
			{
				driver.findElement(By.xpath("//a[contains(@class, 'cq-siteadmin-admin-actions-create-activator')]/span[text()='Create']"))
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

		driver.findElement(By.xpath("//a[contains(@class, 'cq-siteadmin-admin-createpage-at-activator') and text()='Create Page']")).click();

		driver.findElement(By.xpath("//img[@src='" + thumbnailPath + "']"))
				.click();
//		try {
//			clickButtonByText("Next");
//		} catch (Exception e) {
//
//		}

		driver.findElement(By.xpath("//button[@data-action='next' and not(contains(@class, 'hidden'))]"))
				.click();

//		try {
//			driver.findElement(By.name("pageTitle")).sendKeys(title);
//		} catch (Exception e) {
//
//		}

		driver.findElement(By.name("./jcr:title")).sendKeys(title);

		driver.findElement(By.xpath("//button[text()='Create' and not(contains(@class, 'hidden'))]"))
				.click();
		driver.findElement(By.xpath("//button[text()='Done']")).click();
		Assert.assertNotNull("Page failed to create",
				driver.findElement(By.xpath("//div[@title='" + title + "']")));
	}

}
