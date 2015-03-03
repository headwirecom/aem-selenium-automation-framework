package com.headwire.testing.cq.pageobjects;

import java.util.List;

import junit.framework.Assert;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.headwire.testing.cq.factory.PublishPage;

/**
 * Provides helper methods to perform actions on publish environment pages
 * 
 * @author <a href="mailto:dsamarjian@gmail.com">Diran Samarjian</a>
 */
public class PublishPage60 extends BasePage implements PublishPage {

	public PublishPage60(String publishUrl) {
		PageFactory.initElements(driver, this);
		driver = new FirefoxDriver();
		wait = new WebDriverWait(driver, 5);	
		driver.get(publishUrl+"?device=browser");
		if (!driver.getCurrentUrl().startsWith(publishUrl)) {
			driver.close();
			Assert.fail("Incorrect Publish Page - Expected: "+publishUrl+" Actual: "+driver.getCurrentUrl());
		}
	}

	public void validateContentByTag(By by, String text) {
		wait.until(ExpectedConditions.presenceOfElementLocated(by));
		List<WebElement> elements = driver.findElements(by);
		WebElement targetElement = null;
		for (WebElement el : elements) {
			if (el.getText().equals(text)) {
				targetElement = el;
				break;
			}
		}
		Assert.assertNotNull(text+" not found.", targetElement);
	}

	public void validateImageAttribute(String attribute, String text) {
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("img")));
			List<WebElement> elements = driver.findElements(By.tagName("img"));
			WebElement targetElement = null;
			for (WebElement el : elements) {
				if (el.getAttribute(attribute).equals(text)) {
					targetElement = el;
					break;
				}
			}
			Assert.assertNotNull(text+" "+attribute+" not found.", targetElement);
		} catch (Exception e) {
			Assert.fail("Failed to find image attribute "+attribute+": "+e);
		}

	}

	public void closeDriver() {
		driver.close();
	}

	public void isNot404(String newTitle) {
		String pageTitleXpath = "//div[@class='title']/h1";
		WebElement element = null;
		String titleText = "";
		try {
			element = driver.findElement(By.xpath(pageTitleXpath));
			titleText = element.getText();
		} catch (Exception e) {

		}
		Assert.assertTrue("Page returned 404 "+newTitle, titleText.toLowerCase().equals(newTitle.toLowerCase()));
	}

	public void is404(String newTitle) {
		String pageTitleXpath = "//div[@class='title']/h1";
		String titleText = "";
		WebElement element = null;
		try {
			element = driver.findElement(By.xpath(pageTitleXpath));
			titleText = element.getText();
		} catch (Exception e) {
			
		}
		Assert.assertFalse("Page exists "+newTitle, titleText.toLowerCase().equals(newTitle.toLowerCase()));
	}

	public void refresh() {
		driver.navigate().refresh();
	}

}
