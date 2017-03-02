package com.cqblueprints.testing.cq.pageobjects.impl;

import com.cqblueprints.testing.cq.pageobjects.SiteAdminPage;
import junit.framework.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static com.cqblueprints.testing.cq.base.BaseActions.ACTIONS;

/**
 * Provides helper methods to perform actions on the site admin page
 * AEM Version 6.0
 * 
 * @author <a href="mailto:dsamarjian@gmail.com">Diran Samarjian</a>
 */
public class SiteAdminPage60 extends BasePage implements SiteAdminPage {

	@FindBy(className = "card-page")
	private List<WebElement> pages;
	@FindBy(className = "icon-edit")
	private WebElement editPage;
	@FindBy(className = "foundation-admin-properties-activator")
	private WebElement editProperties;
	@FindBy(className = "icon-copy")
	private WebElement copyPageButton;
	@FindBy(className = "icon-move")
	private WebElement movePageButton;
	@FindBy(className = "icon-globe")
	private WebElement publishPageButton;
	@FindBy(xpath = "//article[class='foundation-collection-item card-page']")
	private List<WebElement> pageCards;

	public List<WebElement> getPageCards() {
		return pageCards;
	}

	public SiteAdminPage60(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
		PageFactory.initElements(driver, this);
	}

	public SiteAdminPage navigateToPage(String path) {
		if (driver.getCurrentUrl().contains("/siteadmin#")) {
			for (WebElement page : pages) {
				String pageLink = page.getAttribute("data-path");
				if (path.equals(pageLink)) {
					boolean mouseOver = ACTIONS.mouseOver(driver, page);
					if (mouseOver) {
						editPage.click();
					} else {
						Assert.fail("Failed to mouseover page.");
					}
				} else if (path.contains(pageLink)) {
					page.click();
				}
			}

			Assert.fail("Couldn't find page: " + path);
		} else {
			String[] parts = path.split("/");
			for (String part : parts) {
				if (part.equals("content")) {
					continue;
				}
				driver.findElement(By.xpath("//div[text()[normalize-space() = '"+part+"']]")).click();
			}
		}
		return new SiteAdminPage60(driver, wait);
	}

	public void validatePageTitle(String pageTitle) {
		Assert.assertNotNull("Page title " + pageTitle + " not found", driver
				.findElement(By.xpath("//h4[@itemprop='title' and text()='"
						+ pageTitle + "']")));
	}

	public void validatePagePropTitle(String pageTitle) {
		Assert.assertNotNull(
				"Page title " + pageTitle + " not found",
				driver.findElement(By.xpath("//span[text()='" + pageTitle
						+ "']")));
	}

	public void clickSiteAdminButton(String title) {
		try {
			driver.findElement(By.xpath("//button/span[text()='More']"))
					.click();
		} catch (Exception e) {
			// No more button
		}
		try {
			driver.findElement(By.xpath("//button[@title='" + title + "']"))
					.click();
		} catch (Exception e) {
			driver.findElement(
					By.xpath("//button/span[text()='" + title + "']")).click();
		}
	}

	public void clickSiteAdminButton(String title, String fallback) {
		try {
			driver.findElement(By.xpath("//button[@title='" + title + "']"))
					.click();
		} catch (Exception e) {
			driver.findElement(By.xpath("//button[@title='" + fallback + "']"))
					.click();
		}
	}

	public void clickSiteAdminLink(String title) {
		try {
			driver.findElement(By.xpath("//button/span[text()='More']"))
					.click();
		} catch (Exception e) {
			// No more button
		}
		try {
			driver.findElement(By.xpath("//a[@title='" + title + "']")).click();
		} catch (Exception e) {
			driver.findElement(By.xpath("//button[@title='" + title + "']"))
					.click();
		}
	}

	public void selectDropdownItem(String text) {
		List<WebElement> elements = driver.findElements(By
				.xpath("//div[@class='endor-List']/a"));
		for (WebElement el : elements) {
			if (el.getText().equals(text)) {
				el.click();
				break;
			}
		}
	}

	public void createPageWithTemplate(String thumbnailPath, String title) {
		driver.findElement(By.xpath("//img[@src='" + thumbnailPath + "']"))
				.click();
		try {
			clickButtonByText("Next");
		} catch (Exception e) {
			driver.findElement(By.xpath("//button[@data-action='next']"))
					.click();
		}
		try {
			driver.findElement(By.name("pageTitle")).sendKeys(title);
		} catch (Exception e) {
			driver.findElement(By.name("./jcr:title")).sendKeys(title);
		}
		clickButtonByText("Create");
		try {
			driver.findElement(By.xpath("//a[text()='Done']")).click();
		} catch (Exception e) {
			driver.findElement(By.xpath("//button[text()='Done']")).click();
		}
		Assert.assertNotNull("Page failed to create",
				driver.findElement(By.xpath("//h4[text()='" + title + "']")));
	}

	public void clickButtonByText(String text) {
		try {
			driver.findElement(By.xpath("//button[text()='" + text + "']"))
					.click();
		} catch (Exception e) {
			driver.findElement(By.xpath("//button/span[text()='" + text + "']"))
					.click();
		}
	}

	public void fillInDialogFieldByName(String name, String text) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.name(name)));
		driver.findElement(By.name(name)).sendKeys(text);
	}

	public void navigateToCurrentPage(String page) {
		driver.get(page);
	}

	public SiteAdminPage60 clickSiteAdminPage(String xpath) {
		driver.findElement(By.xpath(xpath)).click();
		return this;
	}

	public void validatePageDeleted(String xpath) {
		WebElement el = null;
		try {
			el = driver.findElement(By.xpath(xpath));
		} catch (NoSuchElementException e) {
			// Page should not be found
		}
		Assert.assertNull("Page was not deleted", el);
	}

	public void renamePage(String newTitle) {
		WebElement renameField = driver.findElement(
				By.xpath("//input[contains(@class, 'rename-item-name')]"));
		renameField.clear();
		renameField.sendKeys(newTitle);
	}

	public void validateUserSettings() {
		Assert.assertNotNull("Language selection not available",
				driver.findElement(By.name("language")));
		Assert.assertNotNull("Window Mode selection not available",
				driver.findElement(By.name("winMode")));
		Assert.assertNotNull("Authoring Mode selection not available",
				driver.findElement(By.name("authoringMode")));
	}

	public void validateSidepanelLinks() {
		String[] sidepanelLinks = { "Projects", "Sites", "Apps",
				"Publications", "Forms", "Assets", "Communities", "Commerce",
				"Tools" };
		for (String s : sidepanelLinks) {
			WebElement el = null;
			el = driver.findElement(By.xpath("//nav/a[text()='" + s + "']"));
			Assert.assertNotNull("Failed to find link " + s, el);
		}
	}

	public void search(String text) {
		clickSiteAdminButton("Search");
		WebElement searchBox = driver.findElement(By.name("fulltext"));
		searchBox.sendKeys(text);
		searchBox.sendKeys(Keys.RETURN);
	}

	public void validateNumberOfSearchResults(int number) {
		List<WebElement> elements = driver.findElements(By
				.xpath("//div[@class='grid-3']/article"));
		Assert.assertTrue(
				"Incorrect number of search results: " + elements.size()
						+ " Expected: " + number, elements.size() == number);
	}

	public void activatePage() {
		clickSiteAdminLink("Publish");
		try {
			driver.findElement(By.xpath("//div/a[text()='Publish']")).click();
		} catch (Exception e) {
			driver.findElement(By.xpath("//button[text()='Publish']")).click();
		}
		try {
			driver.findElement(By.xpath("//button[text()='Publish']")).click();
		} catch (Exception e) {
			// No assets need to be activated
		}

	}

	public void deactivatePage() {
		clickSiteAdminLink("Unpublish");
		driver.findElement(By.xpath("//div/a[text()='Unpublish']")).click();
	}

	public void openSitesPage() {
		driver.findElement(By.xpath("//div[text()='Sites']")).click();
	}
}
