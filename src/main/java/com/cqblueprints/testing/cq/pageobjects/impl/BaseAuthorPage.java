package com.cqblueprints.testing.cq.pageobjects.impl;

import com.cqblueprints.testing.cq.base.Constants.MouseAction;
import com.cqblueprints.testing.cq.base.TestEnvironment;
import com.cqblueprints.testing.cq.pageobjects.AuthorPage;
import junit.framework.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.cqblueprints.testing.cq.base.BaseActions.ACTIONS;

/**
 * Provides helper methods to perform actions on WCM author pages Version AEM
 * 5.6
 * 
 * @author <a href="mailto:dsamarjian@gmail.com">Diran Samarjian</a>
 */
public class BaseAuthorPage extends BasePage implements AuthorPage {

	public static final String AEM_EDITOR = "/editor.html";
	public static final String CLASSIC_EDITOR = "/cf#";

	// Classic UI
	@FindBy(className = "x-form-arrow-trigger")
	protected WebElement sidekickWorkflowDropdown;
	@FindBy(className = "cq-sidekick-tab-icon-workflow")
	protected WebElement sidekickWorkflowTab;
	@FindBy(className = "cq-sidekick-tab-icon-components")
	protected WebElement sidekickComponentsTab;
	@FindBy(xpath = "//div[@class='x-combo-list-inner']/div[@class='x-combo-list-item']")
	protected List<WebElement> sidekickAvailableWorkflows;
	@FindBy(xpath = "//button[text()='Start Workflow']")
	protected WebElement sidekickStartWorkflow;
	@FindBy(xpath = "//button[text()='Complete']")
	protected WebElement sidekickCompleteWorkflow;
	@FindBy(xpath = "//span[text()='Complete Work Item']")
	protected WebElement workflowCompleteDialog;
	@FindBy(name = "comment")
	protected WebElement workflowDialogComment;
	@FindBy(xpath = "//button[text()='OK']")
	protected WebElement workflowDialogOkButton;
	@FindBy(xpath = "//div[@class='x-combo-list-item']")
	protected WebElement sidekickWorkflowLabel;

	// New UI
	@FindBy(xpath = "//button[@title='Toggle Side Panel']")
	protected WebElement sidePanelToggle;
	@FindBy(className = "search")
	protected WebElement filterSearch;
	@FindBy(className = "componentfilter")
	protected WebElement componentFilter;
	@FindBy(className = "coral-TabPanel-tab")
	protected List<WebElement> sidePanelTabs;
	@FindBy(className = "card-component")
	protected List<WebElement> componentCards;
	@FindBy(className = "cq-droptarget")
	protected List<WebElement> dropTargets;
	@FindBy(xpath = "//i[contains(@class,'coral-Icon--addCircle')]")
	protected WebElement addItemButton;

	public BaseAuthorPage(WebDriver driver, WebDriverWait wait) {
		PageFactory.initElements(driver, this);
		this.driver = driver;
		this.wait = wait;
	}

	public void toggleSidePanel() {
		wait.until(ExpectedConditions.visibilityOf(sidePanelToggle));
		sidePanelToggle.click();
	}

	public boolean isClassic() {
		return !driver.getCurrentUrl().contains("/editor.html");
	}
	
	public AuthorPage selectSidePanelTab(String tabName) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By
					.xpath("//a[@class='coral-TabPanel-tab' and text()='"
							+ tabName + "']")));
		} catch (Exception e) {
			toggleSidePanel();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By
					.xpath("//a[@class='coral-TabPanel-tab' and text()='"
							+ tabName + "']")));
		}
		for (WebElement el : sidePanelTabs) {
			if (el.getText().equals(tabName)) {
				el.click();
				return new BaseAuthorPage(driver, wait);
			}
		}
		return this;
	}

	public void selectDialogTab(String tabName) {
		driver.switchTo().activeElement();
		By tabBy = By
				.xpath("//nav[@class='coral-TabPanel-navigation']/a[text()='"
						+ tabName + "']");
		wait.until(ExpectedConditions.visibilityOfElementLocated(tabBy));
		driver.findElement(tabBy).click();
	}

	public void selectDialogTabX(String tabName) {
		driver.switchTo().activeElement();
		sidePanelTabs = driver.findElements(By
				.xpath("//span[@class='x-tab-strip-inner']"));
		for (WebElement el : sidePanelTabs) {
			if (el.getText().equals(tabName)) {
				el.click();
			}
		}
	}

	public void fillInDialogFieldByName(String name, String text) {
		int maxRetries = 5;
		int retries = 0;
		String inputString = "";
		try {
			while (!inputString.equals(text) && retries < maxRetries) {
				By fieldBy = By.name(name);
				wait.until(ExpectedConditions.presenceOfElementLocated(fieldBy));
				List<WebElement> fields = driver.findElements(fieldBy);
				WebElement field = null;
				for (WebElement f : fields) {
					if (f.getAttribute("type") == null || !f.getAttribute("type").equals("hidden")) {
						field = f;
						break;
					}
				}
				wait.until(ExpectedConditions.visibilityOf(field));
				field.clear();
				field.sendKeys(text);
				field.sendKeys(Keys.BACK_SPACE);
				field.sendKeys(""+text.charAt(text.length()-1));
				retries++;
				Thread.sleep(500);
				inputString = field.getAttribute("value");
			}
			if (retries >= maxRetries) {
				Assert.fail("Failed to input text into field. Expected: "
						+ text + "    Actual: " + inputString);
			}

		} catch (Exception e) {
			Assert.fail("Failed to input " + text + " in dialog field named "
					+ name + ". Error message: " + e.getMessage());
		}

	}

	public void fillInRTE(String fieldName, String value) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By
				.name(fieldName)));
		WebElement rteFrame = driver.findElement(By.xpath("//textarea[@name='"
				+ fieldName + "']/following-sibling::div/iframe"));
		driver.switchTo().frame(rteFrame);
		driver.findElement(By.id("CQrte")).sendKeys(value);
		driver.switchTo().defaultContent();
	}

	public void switchToOldDialogIframe() {
		try {
			driver.switchTo().frame(
					driver.findElement(By
							.xpath("//div[@class='coral-Modal-body']/iframe")));
		} catch (Exception e) {
		}
	}

	public WebElement findElementByName(String name) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.name(name)));
		return driver.findElement(By.name(name));
	}

	public WebElement findElementBy(By by) {
		try {
			driver.switchTo().activeElement();
			wait.until(ExpectedConditions.presenceOfElementLocated(by));
			return driver.findElement(by);
		} catch (Exception e) {
			return null;
		}
		
	}

	public void closeInlineEditor() {
		clickBy(By.xpath("//button[@data-action='control#close']"));
	}

	public WebElement getParentOfElement(By by) {
		wait.until(ExpectedConditions.presenceOfElementLocated(by));
		WebElement element = driver.findElement(by);
		WebElement parent = element.findElement(By.xpath(".."));
		return parent;
	}

	public void dragComponentIntoParsys(String componentName, String parsysName) {
		if (!driver.getCurrentUrl().contains(AEM_EDITOR)) {
			By componentBy = By.xpath("//button[text()='" + componentName
					+ "']");
			wait.until(ExpectedConditions
					.visibilityOfElementLocated(componentBy));
			try {
				WebElement element = driver.findElement(componentBy);
				((Locatable) element).getCoordinates().inViewPort();
				WebElement parsys = driver
						.findElement(By.className(parsysName));
				ACTIONS.dragDrop(driver, wait, element, parsys);
				Thread.sleep(2000);
				parsys = driver.findElement(By.className(parsysName));
				parsys.findElement(By.xpath("//*"));
			} catch (Exception e) {
				Assert.fail("Unable to drag component: " + componentName
						+ " into parsys: " + parsysName+ " Error: "+e.getMessage());
			}
		} else {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By
					.className("card-component")));
			componentCards = driver
					.findElements(By.className("card-component"));
			WebElement element = null;
			WebElement elText = null;
			for (WebElement el : componentCards) {
				elText = el.findElement(By.tagName("h4"));
				if (elText.getText().equals(componentName)) {
					element = el;
					break;
				}
			}
			if (element == null) {
				Assert.fail(componentName + " not found in sidepanel.");
			}
			((Locatable) element).getCoordinates().inViewPort();
			WebElement parsys = null;
			dropTargets = driver.findElements(By
					.xpath("//*[@id='OverlayWrapper']/div")); // .findElements(By.xpath("//div[@class='cq-droptarget']"));
			for (WebElement el : dropTargets) {
				if (el.getAttribute("data-path") != null) {
					if (el.getAttribute("data-path").contains("/" + parsysName)) {
						parsys = el;
						break;
					}
				}
			}
			wait.until(ExpectedConditions.visibilityOf(elText));
			wait.until(ExpectedConditions.visibilityOf(parsys));

			ACTIONS.dragDrop(driver, wait, elText, parsys);
		}
	}

	public void dragAssetIntoParsys(String assetPath, String parsysName) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By
				.xpath("//article[@data-path='" + assetPath + "']")));
		List<WebElement> assetCards = driver.findElements(By
				.className("card-asset"));
		WebElement element = null;
		WebElement elText = null;
		for (WebElement el : assetCards) {
			String elPath = el.getAttribute("data-path");
			if (elPath.equals(assetPath)) {
				element = el;
				break;
			}
		}
		((Locatable) element).getCoordinates().inViewPort();
		WebElement parsys = null;
		wait.until(ExpectedConditions.presenceOfElementLocated(By
				.xpath("//div[contains(@data-path, '" + parsysName + "')]")));
		dropTargets = driver.findElements(By
				.xpath("//*[@id='OverlayWrapper']/div/div")); // .findElements(By.xpath("//div[@class='cq-droptarget']"));
		for (WebElement el : dropTargets) {
			if (el.getAttribute("data-path") != null) {
				if (el.getAttribute("data-path").endsWith(
						"/" + parsysName.toLowerCase().replace(" ", ""))) {
					parsys = el;
					break;
				}
			}
		}

		elText = element.findElement(By.tagName("div"));
		wait.until(ExpectedConditions.visibilityOf(elText));
		boolean dragSuccess = ACTIONS.dragDropAsset(driver, wait, element,
				parsys);
		Assert.assertTrue("Failed to drag and drop " + elText.getText(),
				dragSuccess);
	}

	public void typeInRTETable(String text, int row, int column) {
		driver.findElement(
				By.xpath("//body[@id='CQrte']/table/tbody/tr[" + row + "]/td["
						+ column + "]")).click();
		for (int i = 0; i < row * column; i++) {
			driver.switchTo().activeElement().sendKeys(Keys.TAB);
		}
		driver.switchTo().activeElement().sendKeys(text);
	}

	public void switchToRTEIframe() {
		driver.switchTo()
				.frame(driver.findElement(By
						.xpath("//textarea[@name='./tableData']/following-sibling::div/iframe")));
	}

	public void editText(String componentName) {
		driver.switchTo().activeElement();
		List<WebElement> componentTargets = driver.findElements(By
				.xpath("//*[@id='OverlayWrapper']/div/div"));
		for (WebElement el : componentTargets) {
			if (el.getAttribute("data-path") != null) {
				if (el.getAttribute("data-path").endsWith(
						"/" + componentName.toLowerCase().replace(" ", ""))) {
					el.click();
					el.click();
				}
			}
		}
	}

	public void type(String text) {
		driver.switchTo().activeElement().sendKeys(text);
	}

	public void selectAssetFinderDropdown(String option) {
		driver.findElement(
				By.xpath("//div[@id='assetfinder-filter']/div/span/button/span"))
				.click();
		List<WebElement> elements = driver
				.findElements(By
						.xpath("//select[@name='assetfilter_type_selector']/following-sibling::ul/li"));
		for (WebElement el : elements) {
			if (el.getText().equals(option)) {
				el.click();
				break;
			}
		}
	}

	public void checkFormLabel(String elementName, String label) {
		Assert.assertTrue(
				"Default Account Name label incorrect",
				driver.findElement(
						By.xpath("//label[@for='new_form_" + elementName + "']"))
						.getText().equals(label));
	}

	public void checkLabel(String elementName, String label) {
		Assert.assertTrue("Default Account Name label incorrect", driver
				.findElement(By.xpath("//label[@for='" + elementName + "']"))
				.getText().equals(label));
	}

	public void validateFields(Map<String, String> fields) {
		Iterator<Entry<String, String>> it = fields.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> pairs = it.next();
			checkFormLabel(pairs.getValue(), pairs.getKey());
			it.remove(); // avoids a ConcurrentModificationException
		}
	}

	public void editComponent(String componentName) {
		if (!driver.getCurrentUrl().contains(AEM_EDITOR)) {
			editComponentClassicUI(componentName);
		} else {
			try {
				driver.switchTo().activeElement();
				By by = By.xpath("//div[contains(@data-path,'/" + componentName
						+ "')]");
				wait.until(ExpectedConditions.presenceOfElementLocated(by));
				WebElement el = driver.findElement(by);
				try {
					el.click();
					List<WebElement> editBarOptions = driver.findElements(By
							.xpath("//*[@id='EditableToolbar']/button"));
					for (WebElement el2 : editBarOptions) {
						String actionAttrib = el2.getAttribute("data-action");
						if (actionAttrib != null) {
							if (actionAttrib.equals("EDIT")
									|| actionAttrib.equals("CONFIGURE")) {
								el2.click();
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
		}
	}

	public void editComponent(String componentName, MouseAction mouseAction) {
		try {
			driver.switchTo().activeElement();
			By by = By.xpath("//div[contains(@data-path,'/" + componentName
					+ "')]");
			wait.until(ExpectedConditions.presenceOfElementLocated(by));
			WebElement el = driver.findElement(by);
			switch (mouseAction) {
			case CONTEXTCLICK:
				el.click();
				List<WebElement> editBarOptions = driver.findElements(By
						.xpath("//*[@id='EditableToolbar']/button"));
				for (WebElement el2 : editBarOptions) {
					String actionAttrib = el2.getAttribute("data-action");
					if (actionAttrib != null) {
						if (actionAttrib.equals("EDIT")
								|| actionAttrib.equals("CONFIGURE")) {
							el2.click();
						}
					}
				}
				break;
			default:
				ACTIONS.doubleClick(driver, el);
				break;
			}
		} catch (Exception e) {
			Assert.fail("Failed to find component to drag and drop "
					+ componentName + ". Error: " + e.getMessage());
		}
	}

	public void editComponent(String componentName, int location) {
		if (driver.getCurrentUrl().contains(CLASSIC_EDITOR)) {
			editComponentClassicUI(componentName);
		} else {
			try {
				driver.switchTo().activeElement();
				By by = By.xpath("//div[contains(@data-path,'/" + componentName
						+ "')]");
				wait.until(ExpectedConditions.presenceOfElementLocated(by));
				int numberOfElements = driver.findElements(by).size();
				if (location > numberOfElements) {
					location = numberOfElements;
				}
				by = By.xpath("(//div[contains(@data-path,'/" + componentName
						+ "')])[" + location + "]");
				WebElement el = driver.findElement(by);
				try {
					if (componentName.equals("title")) {
						ACTIONS.doubleClick(driver, el);
					} else {
						el.click();
						List<WebElement> editBarOptions = driver
								.findElements(By
										.xpath("//*[@id='EditableToolbar']/button"));
						for (WebElement el2 : editBarOptions) {
							String actionAttrib = el2
									.getAttribute("data-action");
							if (actionAttrib != null) {
								if (actionAttrib.equals("EDIT")
										|| actionAttrib.equals("CONFIGURE")) {
									el2.click();
								}
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
		}
	}

	private void switchToClassicContent() {
		driver.switchTo().frame(driver.findElement(By.id("cq-cf-frame")));
	}

	protected void editComponentClassicUI(String componentName) {
		try {
			By editBy = By.xpath("//div[text()='" + componentName
					+ "']/../../td/table/tbody/tr/td/em/button[text()='Edit']");
			wait.until(ExpectedConditions.presenceOfElementLocated(editBy));
			WebElement editElement = driver.findElement(editBy);
			wait.until(ExpectedConditions.visibilityOf(editElement));
			editElement.click();
		} catch (Exception e) {
			WebElement component = driver.findElement(By
					.xpath("//div[contains(@class,'" + componentName + " ')]"));
			ACTIONS.doubleClick(driver, component);
		}
		wait.until(ExpectedConditions.visibilityOfElementLocated(By
				.className("x-window-mc")));
		driver.switchTo().activeElement();
	}

	public void selectInlineEditor(String type) {
		By by = By
				.xpath("//h2[contains(@class,'editor-selector-name') and text()='"
						+ type + "']");
		wait.until(ExpectedConditions.presenceOfElementLocated(by));
		driver.findElement(by).click();
	}

	public void deleteComponent(String componentName) {
		if (driver.getCurrentUrl().contains(CLASSIC_EDITOR)) {
			try {
				By deleteBy = By
						.xpath("//div[text()='"
								+ componentName
								+ "']/../../td/table/tbody/tr/td/em/button[text()='Delete']");
				wait.until(ExpectedConditions
						.presenceOfElementLocated(deleteBy));
				WebElement deleteElement = driver.findElement(deleteBy);
				wait.until(ExpectedConditions.visibilityOf(deleteElement));
				deleteElement.click();
			} catch (NoSuchElementException e) {
				WebElement component = driver.findElement(By
						.className(componentName));
				ACTIONS.contextClick(driver, component);
			}

		} else {
			try {
				selectDelete(componentName);
			} catch (NoSuchElementException e) {
				Assert.fail("Failed to delete component");
			} catch (StaleElementReferenceException e) {
				// Try again in case component triggered 
				try {
					Thread.sleep(500);
				} catch (Exception e2) {
					//swallow
				}
				selectDelete(componentName);
			}
		}
	}
	
	private void selectDelete(String componentName) {
		driver.switchTo().activeElement();
		List<WebElement> componentTargets = driver.findElements(By
				.xpath("//*[@id='OverlayWrapper']/div/div"));
		for (WebElement el : componentTargets) {
			if (el.getAttribute("data-path") != null) {
				if (el.getAttribute("data-path").endsWith(
						"/" + componentName.toLowerCase())) {
					el.click();
					List<WebElement> editBarOptions = driver
							.findElements(By
									.xpath("//*[@id='EditableToolbar']/button"));
					for (WebElement el2 : editBarOptions) {
						String actionAttrib = el2
								.getAttribute("data-action");
						if (actionAttrib != null) {
							if (actionAttrib.equals("DELETE")) {
								el2.click();
								driver.findElement(
										By.className("cq-deleteconfirm"))
										.click();
							}
						}
					}
					break;
				}
			}
		}
	}

	public void checkContentByClass(String className) {
		switchToContent();
		wait.until(ExpectedConditions.presenceOfElementLocated(By
				.className(className)));
		driver.switchTo().frame(driver.findElement(By.id("ContentFrame")));
		Assert.assertNotNull("Address component not found",
				driver.findElement(By.className(className)));
		driver.switchTo().defaultContent();
	}

	public void checkContentByXPath(String xpath) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
		driver.switchTo().frame(driver.findElement(By.id("ContentFrame")));
		Assert.assertNotNull("Address component not found",
				driver.findElement(By.className(xpath)));
		driver.switchTo().defaultContent();
	}

	public void validateDropdownValues(String[] options) {
		Select select = new Select(driver.findElement(By.name("dropdown")));
		List<WebElement> elements = select.getOptions();
		for (WebElement el : elements) {
			boolean hasOption = false;
			String optionText = el.getText();
			for (String s : options) {
				if (s.equals(optionText)) {
					hasOption = true;
				}
			}
			Assert.assertTrue(optionText + " is not a valid option", hasOption);
		}
	}

	public void switchToContent() {
		try {
		if (driver.getCurrentUrl().contains(AEM_EDITOR)) {
			driver.switchTo().frame(driver.findElement(By.id("ContentFrame")));
		} else {
			switchToClassicContent();
		}
		} catch (Exception e) {
			// No content frame
		}
	}

	public void confirmDialog() {
		if (!driver.getCurrentUrl().contains(AEM_EDITOR)) {
			confirmDialogOld();
			return;
		}
		try {
			List<WebElement> elements = driver.findElements(By
					.xpath("//div[@class='cq-dialog-header-actions']/button"));
			WebElement okButton = null;
			for (WebElement el : elements) {
				if (el.getAttribute("title").equals("Done")) {
					okButton = el;
					break;
				}
			}
			if (okButton == null) {
				okButton = driver.findElement(By
						.xpath("//button[@title='Done']"));
			}

			driver.switchTo().activeElement();
			okButton.click();
		} catch (Exception e) {
			Assert.fail("Dialog confirm button is not available.");
		}
	}

	protected void confirmDialogOld() {
		if (!driver.getCurrentUrl().contains(CLASSIC_EDITOR)) {
			driver.switchTo().defaultContent();
		} else {
			try {
				switchToClassicContent();
			} catch (Exception e) {
				driver.switchTo().defaultContent();
			}
		}
		driver.findElement(By.xpath("//*[text()='OK']")).click();
	}

	public void switchToDefaultContent() {
		driver.switchTo().defaultContent();
	}

	public AuthorPage startWorkflowFromSidekick(String workflowName) {
		driver.findElement(By.id("CQ")).click();
		wait.until(ExpectedConditions.visibilityOf(sidekickWorkflowTab));
		sidekickWorkflowTab.click();
		wait.until(ExpectedConditions.visibilityOf(sidekickWorkflowDropdown));
		sidekickWorkflowDropdown.click();
		for (WebElement workflow : sidekickAvailableWorkflows) {
			if (workflow.getText().equals(workflowName)) {
				workflow.click();
			}
		}
		sidekickStartWorkflow.click();
		wait.until(ExpectedConditions.visibilityOf(sidekickWorkflowTab));
		return new AuthorPage61(driver, wait);
	}

	public AuthorPage advanceWorkflowFromSidekick(String comment) {
		wait.until(ExpectedConditions.visibilityOf(sidekickWorkflowTab));
		sidekickWorkflowTab.click();
		wait.until(ExpectedConditions.visibilityOf(sidekickWorkflowDropdown));
		sidekickCompleteWorkflow.click();
		driver.switchTo().activeElement();
		wait.until(ExpectedConditions.visibilityOf(workflowDialogComment));
		workflowDialogComment.click();
		workflowDialogComment.sendKeys(comment);
		workflowDialogOkButton.click();
		// wait.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(sidekickWorkflowLabel)));
		return new AuthorPage61(driver, wait);
	}

	public AuthorPage advanceWorkflowFromSidekickNoRefresh(String comment) {
		wait.until(ExpectedConditions.visibilityOf(sidekickWorkflowTab));
		sidekickWorkflowTab.click();
		wait.until(ExpectedConditions.visibilityOf(sidekickWorkflowDropdown));
		sidekickCompleteWorkflow.click();
		driver.switchTo().activeElement();
		wait.until(ExpectedConditions.visibilityOf(workflowDialogComment));
		workflowDialogComment.sendKeys(comment);
		workflowDialogOkButton.click();
		return this;
	}

	public String getWorkflowLabel() {
		return sidekickWorkflowLabel.getText();
	}

	public void activatePage(String pagePath, TestEnvironment env) {
		try {
			ACTIONS.activatePage(pagePath.replace(".html", ""), env);
			Thread.sleep(6000);
		} catch (Exception e) {
			Assert.fail("Failed to activate page " + pagePath + ". Error: "
					+ e.getMessage());
		}
	}

	public void deactivatePage(String pagePath, TestEnvironment env) {
		try {
			ACTIONS.deactivatePage(pagePath.replace(".html", ""), env);
			Thread.sleep(6000);
		} catch (Exception e) {
			Assert.fail("Failed to deactivate page " + pagePath + ". Error: "
					+ e.getMessage());
		}
	}

	public void navigateToPublish(String publishUrl) {
		WebDriver publishDriver = new FirefoxDriver();
		publishDriver.get(publishUrl);

	}

	public void clickByXpath(String xpath) {
		driver.switchTo().activeElement();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
		driver.findElement(By.xpath(xpath)).click();
	}

	public void selectDropDown(String label, String option) {
		try {
			if (driver.getCurrentUrl().contains(AEM_EDITOR)) {
				driver.findElement(
						By.xpath("//label[text()='" + label
								+ "']/following-sibling::span/button")).click();
				List<WebElement> listElements = driver.findElements(By
						.xpath("//select/following-sibling::ul/li"));
				if (listElements.size() == 0) {
					Assert.fail("Could not find dropdown values.");
				}
				for (WebElement el : listElements) {
					if (el.getAttribute("data-value").equals(option)) {
						wait.until(ExpectedConditions.visibilityOf(el));
						el.click();
						break;
					}
				}
			} else {
				driver.findElement(
						By.xpath("//label[text()='"
								+ label
								+ "']/following-sibling::div/div/div/div/div/div/div/img"))
						.click();
				Thread.sleep(200);
				List<WebElement> listElements = driver
						.findElements(By
								.xpath("//div[@class='x-combo-list-inner']/div[contains(@class,'x-combo-list-item')]"));
				if (listElements.size() == 0) {
					Assert.fail("Could not find dropdown values.");
				}
				for (WebElement el : listElements) {
					if (el.getText().equals(option)) {
						wait.until(ExpectedConditions.visibilityOf(el));
						el.click();
						break;
					}
				}
			}
		} catch (Exception e) {
			Assert.fail("Unable to select dropdown: " + e.getMessage());
		}
	}

	public void moveMouse(int x, int y) {
		ACTIONS.moveMouse(x, y);
	}

	public void closeSuggestions() {
		ACTIONS.hitEscape();
	}

	public void followingSiblingInput(String label, String text) {
		String xpath = "//label[text()='" + label
				+ "']/following-sibling::span/input";
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
		driver.findElement(By.xpath(xpath)).sendKeys(text);
	}

	public void followingSiblingInput(String label, String text, String fallback) {
		String xpath = "//label[text()='" + label
				+ "']/following-sibling::span";
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(By
					.xpath(xpath)));
			driver.findElement(By.xpath(xpath)).sendKeys(text);
		} catch (Exception e) {
			xpath.replace(label, fallback);
			wait.until(ExpectedConditions.presenceOfElementLocated(By
					.xpath(xpath)));
			driver.findElement(By.xpath(xpath)).sendKeys(text);
		}

	}

	public void validatePageTitle(String newTitle) {
		switchToContent();
		String pageTitleXpath = "//div[@class='title']/h1";
		WebElement element = driver.findElement(By.xpath(pageTitleXpath));
		Assert.assertTrue("Page title does not match Expected: " + newTitle
				+ " Actual: " + element.getText(), element.getText()
				.toLowerCase().equals(newTitle.toLowerCase()));
	}

	public void validateMetaData(String name, String text) {
		try {
			switchToContent();
		} catch (Exception e) {
			// No content frame
		}
		WebElement element = driver.findElement(By.name(name));
		String elementText = element.getAttribute("content");
		Assert.assertTrue("Meta Field " + name + " does not match " + text,
				elementText.equals(text));
	}

	public AuthorPage addTabs(int numberOfTabs) {
		for (int i = 0; i < numberOfTabs; i++) {
			String curUrl = driver.getCurrentUrl();
			if (curUrl.contains(AEM_EDITOR)) {
				wait.until(ExpectedConditions
						.elementToBeClickable(addItemButton));
				addItemButton.click();
			} else {
				try {
					By okBy = By.xpath("//button[text()='+']");
					wait.until(ExpectedConditions
							.visibilityOfElementLocated(okBy));
					driver.findElement(okBy).click();
				} catch (Exception e) {
					By okBy = By
							.xpath("//button[contains(@class,'cq-multifield-add')]");
					wait.until(ExpectedConditions
							.visibilityOfElementLocated(okBy));
					driver.findElement(okBy).click();
				}
			}
		}
		return new BaseAuthorPage(driver, wait);
	}

	public AuthorPage fillInMultipleFields(String fieldName, String prefix) {
		int maxRetries = 5;
		
		List<WebElement> elements = driver.findElements(By.name(fieldName));
		for (int i = 0; i < elements.size(); i++) {
			try {
				int retries = 0;
				String inputString = "";
				WebElement targetElem = elements.get(i);
				while (!inputString.equals(prefix + i) && retries < maxRetries) {
					wait.until(ExpectedConditions.visibilityOf(targetElem));
					targetElem.clear();
					targetElem.sendKeys(prefix + i);
					retries++;
					Thread.sleep(500);
					inputString = getTextFromInput(targetElem);
				}
				if (retries >= maxRetries) {
					Assert.fail("Failed to input text into field. Expected: "
							+ prefix+i + "    Actual: " + inputString);
				}
			} catch (Exception e) {
				Assert.fail("Failed to type input in field: " + fieldName);
			}

		}
		return this;
	}

	public AuthorPage fillInMultipleFields(String fieldName, String[] values) {
		int maxRetries = 5;
		
		List<WebElement> elements = driver.findElements(By.name(fieldName));
		for (int i = 0; i < elements.size(); i++) {
			try {
				int retries = 0;
				String inputString = "";
				WebElement targetElem = elements.get(i);
				while (!inputString.equals(values[i]) && retries < maxRetries) {
					wait.until(ExpectedConditions.visibilityOf(targetElem));
					targetElem.clear();
					targetElem.sendKeys(values[i]);
					targetElem.sendKeys(Keys.BACK_SPACE);
					targetElem.sendKeys(""+values[i].charAt(values[i].length()-1));
					retries++;
					Thread.sleep(1000);
					inputString = getTextFromInput(targetElem);
				}
				if (retries >= maxRetries) {
					Assert.fail("Failed to input text into field. Expected: "
							+ values[i] + "    Actual: " + inputString);
				}
			} catch (Exception e) {
				Assert.fail("Failed to type input in field: " + fieldName);
			}

		}
		return this;
	}

	public void selectDropDownValue(String option) {
		String xpath = "//img[contains(@class,'x-form-arrow-trigger')]";
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
		WebElement ele = driver.findElement(By.xpath(xpath));
		ele.click();
		List<WebElement> elements = driver.findElements(By
				.xpath("//div[@class='x-combo-list-item']"));
		for (WebElement el : elements) {
			if (el.getText().equals(option)) {
				el.click();
				break;
			}
		}
	}

	public void refresh() {
		driver.navigate().refresh();
	}

	public void waitForRefresh() {
		try {
			Thread.sleep(3000);
		} catch (Exception e) {
			// swallow
		}
		if (isClassic()) {
			wait.until(ExpectedConditions.presenceOfElementLocated(By
					.id("cq-cf-frame")));
		} else {
			wait.until(ExpectedConditions.presenceOfElementLocated(By
				.id("ContentFrame")));
		}
	}

	public void increment(int numberOfTimes, int indexOfIncrementField) {
		List<WebElement> incrementors = driver.findElements(By
				.xpath("//button[@title='Increment']"));
		int incrementorsSize = incrementors.size();
		WebElement targetIncrementor = null;
		if (indexOfIncrementField < incrementors.size()) {
			targetIncrementor = incrementors.get(indexOfIncrementField);
		} else if (incrementorsSize > 0) {
			targetIncrementor = incrementors.get(incrementorsSize - 1);
		}
		try {
			for (int i = 0; i < numberOfTimes; i++) {
				targetIncrementor.click();
			}
		} catch (Exception e) {
			Assert.fail("No incrementors found.");
		}
	}

	public void decrement(int numberOfTimes, int indexOfIncrementField) {
		List<WebElement> decrementors = driver.findElements(By
				.xpath("//button[@title='Increment']"));
		int decrementorsSize = decrementors.size();
		WebElement targetDecrementor = null;
		if (indexOfIncrementField < decrementors.size()) {
			targetDecrementor = decrementors.get(indexOfIncrementField);
		} else if (decrementorsSize > 0) {
			targetDecrementor = decrementors.get(decrementorsSize - 1);
		}
		try {
			for (int i = 0; i < numberOfTimes; i++) {
				targetDecrementor.click();
			}
		} catch (Exception e) {
			Assert.fail("No incrementors found.");
		}
	}

	public void selectSidekickTab(String tabName) {
		By tabBy = By.xpath("//span[@class='x-panel-header-text' and text()='"
				+ tabName + "']");
		wait.until(ExpectedConditions.presenceOfElementLocated(tabBy));
		try {
			driver.findElement(
					By.xpath("//div[contains(@class,'x-panel-collapsed')]/div/span[@class='x-panel-header-text' and text()='"
							+ tabName + "']")).click();
		} catch (Exception e) {
			System.out.println("Failed to find panel");
		}

	}
}
