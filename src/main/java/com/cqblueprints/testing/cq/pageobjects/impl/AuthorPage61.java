package com.cqblueprints.testing.cq.pageobjects.impl;

import com.cqblueprints.testing.cq.pageobjects.AuthorPage;
import junit.framework.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.cqblueprints.testing.cq.base.BaseActions.ACTIONS;

/**
 * Provides helper methods to perform actions on WCM author pages
 * in AEM 6.0
 * 
 * @author <a href="mailto:dsamarjian@gmail.com">Diran Samarjian</a>
 */
public class AuthorPage61 extends BaseAuthorPage {

	private static final Logger LOG = LoggerFactory.getLogger(AuthorPage61.class);

	@FindBy(xpath = "//button[@title='Toggle Side Panel' and contains(@class, 'js-editor-SidePanel-toggle')]")
	protected WebElement sidePanelToggle;

	public AuthorPage61(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
		this.driver = driver;
		this.wait = wait;
	}

//	public void selectInlineEditor(String type) {
//		By by = By.xpath("//h2[contains(@class,'editor-selector-name') and text()='"+type+"']");
//		wait.until(ExpectedConditions.presenceOfElementLocated(by));
//		driver.findElement(by).click();
//	}

	public void selectInlineEditor(String type) {
			LOG.info("Finding inline editor by //*[@contenteditable]");
			switchToContent();
			By by = By.xpath("//*[@contenteditable]");
			WebElement el = driver.findElement(by);
			LOG.info("Found editor");
			el.clear();
			el.sendKeys(type);
	}

	public void toggleSidePanel() {
		wait.until(ExpectedConditions.visibilityOf(sidePanelToggle));
		//sidePanelToggle.click();\
		sidePanelToggle.sendKeys(Keys.RETURN);
	}

	public void editComponent(String componentName) {
		if (!driver.getCurrentUrl().contains(AEM_EDITOR)) {
			LOG.info("Using classic UI to edit");
			editComponentClassicUI(componentName);
		} else {
			try {
				LOG.info("Switching to active element");
				driver.switchTo().activeElement();
				LOG.info("Switched to active element");
				By by = By.xpath("//div[contains(@data-path,'/" + componentName
						+ "')]");
				LOG.info("Waiting until editable div is visible");
				wait.until(ExpectedConditions.presenceOfElementLocated(by));
				LOG.info("Editable div is visible");
				WebElement el = driver.findElement(by);
				LOG.info("Found editable div element");
				try {
					Thread.sleep(300);
					el.click();
					LOG.info("Clicked editable div");
					List<WebElement> editBarOptions = driver.findElements(By
							.xpath("//*[@id='EditableToolbar']/button"));
					LOG.info("Got edit bar options");
					for (WebElement el2 : editBarOptions) {
						String actionAttrib = el2.getAttribute("data-action");
						LOG.info("Looking at button with action: {}", actionAttrib);
						if (actionAttrib != null) {
							if (actionAttrib.equals("EDIT")
									|| actionAttrib.equals("CONFIGURE")) {
								LOG.info("Found {} action, clicking", actionAttrib);
								el2.click();
								LOG.info("Clicked {} action", actionAttrib);
								break;
							}
						}
					}
				} catch (Exception e) {
					ACTIONS.doubleClick(driver, el);
				}

			} catch (NoSuchElementException e) {
				Assert.fail("Failed to find component to drag and drop "
						+ componentName + ". Error: " + e.getMessage());
			}
			try {
				System.out.println("Second sleep 300ms");
				Thread.sleep(300);
				System.out.println("End second sleep 300ms");
				//WebElement dialogFrame = driver.findElement(By.xpath("//coral-dialog-content/iframe"));
				WebElement dialogFrame = null;
				driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
				List<WebElement> elements = driver.findElements(By.xpath("//div[@class='coral-Modal-body']/iframe"));
				if(elements == null || elements.isEmpty())
				{
					System.out.println("Dialog frame not found");
				}
				else
				{
					dialogFrame = elements.get(0);
					driver.switchTo().frame(dialogFrame);
					System.out.println("Switched to dialog frame");
				}

			} catch (Exception e) {
				// non-iframe dialog
				e.printStackTrace();
			}
		}
	}

	public void closeInlineEditor()
	{
		ACTIONS.hitEnter();
	}

	public AuthorPage selectSidePanelTab(String tabName) {
		try {
			LOG.info("Looking for side panel tab: {}", tabName);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By
					.xpath("//a[contains(@class,'coral-TabPanel-tab') and text()='" + tabName + "']")));
		} catch (Exception e) {
			LOG.info("Couldn't find side panel tab: {}, toggling side panel and trying again", tabName);
			toggleSidePanel();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By
					.xpath("//a[contains(@class,'coral-TabPanel-tab') and text()='" + tabName + "']")));
		}

		for (WebElement el : sidePanelTabs) {
			if (el.getText().equals(tabName)) {
				if(!el.getAttribute("class").contains("is-active"))
				{
					el.click();
				}
				return new BaseAuthorPage(driver, wait);
			}
		}
		return this;
	}

	public void fillInDialogFieldByName(String name, String text) {
		LOG.info("6-1 Filling in {} field with text {}", name, text);
		int maxRetries = 5;
		int retries = 0;
		String inputString = "";
		try {
			while (!inputString.equals(text) && retries < maxRetries) {
				LOG.info("6-1 Try {}/{}",(retries + 1),maxRetries);
				//By fieldBy = By.name(name);
				By fieldBy = By.xpath("//input[@name='" + name + "']");
				wait.until(ExpectedConditions.presenceOfElementLocated(fieldBy));
				LOG.info("6-1 I think the field is present?");
				List<WebElement> fields = driver.findElements(fieldBy);
				LOG.info("6-1 We found everything matching that field?");
				WebElement field = null;
				for (WebElement f : fields) {
					if (f.getAttribute("type") == null || !f.getAttribute("type").equals("hidden")) {
						field = f;
						break;
					}
				}
				wait.until(ExpectedConditions.visibilityOf(field));
				LOG.info("6-1 just waited for field visibility");
				try {
					field.clear();
				} catch (InvalidElementStateException e2) {
					field = field.findElement(By.tagName("input"));
					field.clear();
				}
				field.sendKeys(text);
				field.sendKeys(Keys.BACK_SPACE);
				field.sendKeys("" + text.charAt(text.length() - 1));
				retries++;
				LOG.info("6-1 Sleeping 500ms");
				Thread.sleep(500);
				LOG.info("6-1 Slept 500ms");
				inputString = field.getAttribute("value");
			}
			if (retries >= maxRetries) {
				Assert.fail("Failed to input text into field. Expected: "
						+ text + "    Actual: " + inputString);
			}
		} catch (Exception e) {
			LOG.error("Input failure", e);
			Assert.fail("Failed to input " + text + " in dialog field named "
					+ name + ". Error message: " + e.getMessage());
		}
	}

	public void selectDialogTab(String tabName) {
		try {
			//driver.switchTo().activeElement();
			sidePanelTabs = driver.findElements(By
					.xpath("//span[@class='x-tab-strip-inner']"));
			for (WebElement el : sidePanelTabs) {
				if (el.getText().equals(tabName)) {
					el.click();
					return;
				}
			}
			selectGraniteDialogTab(tabName);
		} catch (Exception e) {
			selectGraniteDialogTab(tabName);
		}
	}

	private void selectGraniteDialogTab(String tabName)
	{
		driver.switchTo().activeElement();
		By tabBy = By
				.xpath("//nav[@class='coral-TabPanel-navigation']/a[text()='"
						+ tabName + "']");
		wait.until(ExpectedConditions.visibilityOfElementLocated(tabBy));
		driver.findElement(tabBy).click();
	}


	public void confirmDialog() {
		if (!driver.getCurrentUrl().contains(AEM_EDITOR)) {
			confirmDialogOld();
			return;
		}
		try {
			driver.switchTo().defaultContent();
			WebElement okButton = driver.findElement(By
					.xpath("//button[text()='OK']"));

			driver.switchTo().activeElement();
			okButton.click();
		} catch (Exception e) {
			try
			{
				WebElement checkButton = driver.findElement(By.xpath("//i[@class='coral-Icon coral-Icon--check']"));
				checkButton.click();
			}
			catch (Exception e1)
			{
				Assert.fail("Dialog confirm button is not available."+ e.getMessage());
			}
		}
	}

}
