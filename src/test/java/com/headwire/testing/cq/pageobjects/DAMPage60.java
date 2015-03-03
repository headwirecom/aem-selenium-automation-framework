package com.headwire.testing.cq.pageobjects;

import java.util.List;

import junit.framework.Assert;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.headwire.testing.cq.factory.DAMPage;

/**
 * Provides helper methods to perform actions on DAM assets
 * Version 6.0
 * 
 * @author <a href="mailto:dsamarjian@gmail.com">Diran Samarjian</a>
 */
public class DAMPage60 implements DAMPage {
	
	private WebDriver driver;
	private WebDriverWait wait;

	@FindBy(xpath = "//article[@data-type='asset']")
	private List<WebElement> damAssets;
	@FindBy(xpath = "//article[@data-type='directory']")
	private List<WebElement> damFolders;
	@FindBy(xpath = "//button[@title='Copy']")
	private WebElement copyPageButton;	
	@FindBy(xpath = "//button[@title='Timeline']")
	private WebElement timelineButton;
	@FindBy(xpath = "//button[@title='References']")
	private WebElement referencesButton;
	@FindBy(xpath = "//section[@data-type='siteReference' and contains(@class, 'siteReference')]")
	private WebElement siteReferencesSection;
	@FindBy(xpath = "//span[@class='granite-references-title']/span")
	private List<WebElement> siteReferences;
	@FindBy(xpath = "//button[@title='Move']")
	private WebElement movePageButton;
	@FindBy(xpath = "//a[@title='Publish']")
	private WebElement publishPageButton;
	@FindBy(xpath = "//button[@title='Enter Selection']")
	private WebElement enterSelectionButton;
	@FindBy(xpath = "//section[@class='cq-common-admin-timeline-event']/div")
	private List<WebElement> timelineComments;

	public DAMPage60(WebDriver driver, WebDriverWait wait) {
		PageFactory.initElements(driver, this);
		this.driver = driver;
		this.wait = wait;				
	}

	public DAMPage viewTimeline(String imagePath) {
		timelineButton.click();
		for (WebElement asset : damAssets) {
			if (asset!=null) {
				if (asset.getAttribute("data-path").endsWith(imagePath)) {
					asset.click();
					break;
				}
			}
		}
		return new DAMPage60(driver, wait);
	}

	public DAMPage verifyTimelineComment(String comment) {
		boolean commentExists = false;
		for (WebElement timelineComment : timelineComments) {
			if (timelineComment != null) {
				if (timelineComment.getText().equals(comment)) {
					commentExists = true;
					break;
				}
			}
		}
		Assert.assertTrue("Timeline comment not found", commentExists);
		return this;
	}
	
	public DAMPage viewReferences(String imagePath) {
		referencesButton.click();
		for (WebElement asset : damAssets) {
			if (asset!=null) {
				if (asset.getAttribute("data-path").endsWith(imagePath)) {
					asset.click();
					break;
				}
			}
		}
		return new DAMPage60(driver, wait);
	}
	
	public DAMPage openSiteReferences() {
		siteReferencesSection.click();
		return new DAMPage60(driver, wait);
	}
	
	public DAMPage verifyReferences(String[] references) {
		int matchesFound = 0;
		for (WebElement siteReference : siteReferences) {
			if (siteReference != null) {
				for (String reference : references) {
					if (reference.equals(siteReference.getText())) {
						matchesFound++;
						break;
					}
				}
			}
		}
		Assert.assertTrue("Incorrect amount of references found for selected asset. "
							+"Expected: "+references.length +" Actual: "+matchesFound, references.length == matchesFound);
		return this;
	}

}
