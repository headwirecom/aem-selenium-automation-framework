package com.headwire.testing.cq.pageobjects;

import static com.headwire.testing.cq.base.BaseActions.ACTIONS;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import junit.framework.Assert;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.headwire.testing.cq.base.TestEnvironment;
import com.headwire.testing.cq.factory.AuthorPage;

/**
 * Provides helper methods to perform actions on WCM author pages
 * Version AEM 5.6
 * 
 * @author <a href="mailto:dsamarjian@gmail.com">Diran Samarjian</a>
 */
public class AuthorPage60 extends BasePage implements AuthorPage{

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
	@FindBy(xpath = "//div[@id='Content']/div/nav/div/button[@title='Toggle Side Panel']") //[contains(@class, 'toggle-sidepanel')]")     //(xpath = "//div[@class='header-main-left']/button") //[@class='toggle-sidepanel']")
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
	@FindBy(xpath = "//span[text()='Add Item']")
	protected WebElement addItemButton;


	public AuthorPage60(WebDriver driver, WebDriverWait wait) {
		PageFactory.initElements(driver, this);
		this.driver = driver;
		this.wait = wait;
	}

	public void toggleSidePanel() {
		wait.until(ExpectedConditions.visibilityOf(sidePanelToggle));
		sidePanelToggle.click();
	}

	public AuthorPage selectSidePanelTab(String tabName) {
		wait.until(ExpectedConditions.visibilityOfAllElements(sidePanelTabs));
		for (WebElement el : sidePanelTabs) {
			if (el.getText().equals(tabName)) {
				el.click();
				return new AuthorPage61(driver, wait);
			}
		}
		return this;
	}

	public void selectDialogTab(String tabName) {
		driver.switchTo().activeElement();
		List<WebElement> dialogTabs = driver.findElements(By.xpath("//nav[@class='coral-TabPanel-navigation']/a"));
		for (WebElement el : dialogTabs) {
			if (el.getText().toLowerCase().equals(tabName.toLowerCase())) {
				el.click();
			}
		}
	}

	public void selectDialogTabX(String tabName) {
		driver.switchTo().activeElement();
		sidePanelTabs = driver.findElements(By.xpath("//span[@class='x-tab-strip-inner']"));
		for (WebElement el : sidePanelTabs) {
			if (el.getText().equals(tabName)) {
				el.click();
			}
		}
	}

	public void fillInDialogFieldByName(String name, String text) {
		try { 
			wait.until(ExpectedConditions.presenceOfElementLocated(By.name(name)));
			WebElement field = driver.findElement(By.name(name));
			wait.until(ExpectedConditions.visibilityOf(field));
			field.sendKeys(text);
		} catch (Exception e) {
			Assert.fail("Failed to input "+text+" in dialog field named "+name+". Error message: "+e.getMessage());
		}

	}

	public void switchToOldDialogIframe() {
		try {
			driver.switchTo().frame(driver.findElement(By.xpath("//div[@class='coral-Modal-body']/iframe")));
		} catch (Exception e) {
		}
	}

	public WebElement findElementByName(String name) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.name(name)));
		return driver.findElement(By.name(name));
	}

	public WebElement findElementBy(By by) {
		driver.switchTo().activeElement();
		wait.until(ExpectedConditions.presenceOfElementLocated(by));
		return driver.findElement(by);
	}

	public WebElement getParentOfElement(By by) {
		wait.until(ExpectedConditions.presenceOfElementLocated(by));
		WebElement element = driver.findElement(by);
		WebElement parent = element.findElement(By.xpath(".."));
		return parent;
	}

	public void dragComponentIntoParsys(String componentName, String parsysName) {
		if (driver.getCurrentUrl().contains(CLASSIC_EDITOR)) {
			List<WebElement> elements = driver.findElements(By.className("x-btn-mc"));
			WebElement element = null;
			for (WebElement el : elements) {
				if (el.getText() == componentName) {
					element = el;
				}
			}
			((Locatable) element).getCoordinates().inViewPort();
			WebElement parsys = driver.findElement(By.className(parsysName));
			ACTIONS.dragDrop(driver, wait, element, parsys);   
		} else {
			componentCards = driver.findElements(By.className("card-component")); 
			WebElement element = null;
			WebElement elText = null;
			for (WebElement el : componentCards) {
				elText = el.findElement(By.tagName("h4"));
				if (elText.getText().equals(componentName)) {
					element = el;
					break;
				}
			}
			((Locatable) element).getCoordinates().inViewPort();
			WebElement parsys = null;
			dropTargets = driver.findElements(By.xpath("//*[@id='OverlayWrapper']/div")); //.findElements(By.xpath("//div[@class='cq-droptarget']"));
			for (WebElement el : dropTargets) {
				if (el.getAttribute("data-path") != null) {
					if (el.getAttribute("data-path").contains("/"+parsysName)) {
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
		List<WebElement> assetCards = driver.findElements(By.className("card-asset")); 
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
		dropTargets = driver.findElements(By.xpath("//*[@id='OverlayWrapper']/div/div")); //.findElements(By.xpath("//div[@class='cq-droptarget']"));
		for (WebElement el : dropTargets) {
			if (el.getAttribute("data-path") != null) {
				if (el.getAttribute("data-path").endsWith("/"+parsysName.toLowerCase().replace(" ", ""))) {
					parsys = el;
					break;
				}
			}
		}

		elText = element.findElement(By.tagName("div"));
		wait.until(ExpectedConditions.visibilityOf(elText));
		wait.until(ExpectedConditions.visibilityOf(parsys));
		boolean dragSuccess = ACTIONS.dragDropAsset(driver, wait, element, parsys);
		Assert.assertTrue("Failed to drag and drop "+elText.getText(), dragSuccess);
	}

	public void typeInRTETable(String text, int row, int column) {
		driver.findElement(By.xpath("//body[@id='CQrte']/table/tbody/tr["+row+"]/td["+column+"]")).click();
		for (int i=0; i<row*column;i++) {
			driver.switchTo().activeElement().sendKeys(Keys.TAB);
		}
		driver.switchTo().activeElement().sendKeys(text);
	}

	public void switchToRTEIframe() {
		driver.switchTo().frame(driver.findElement(By.xpath("//textarea[@name='./tableData']/following-sibling::div/iframe")));
	}

	public void editText(String componentName) {
		driver.switchTo().activeElement();
		List<WebElement> componentTargets = driver.findElements(By.xpath("//*[@id='OverlayWrapper']/div/div"));
		for (WebElement el : componentTargets) {
			if (el.getAttribute("data-path") != null) {
				if (el.getAttribute("data-path").endsWith("/"+componentName.toLowerCase().replace(" ", ""))) {
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
		driver.findElement(By.xpath("//div[@id='assetfinder-filter']/div/span/button/span")).click();
		List<WebElement> elements = driver.findElements(By.xpath("//select[@name='assetfilter_type_selector']/following-sibling::ul/li"));
		for (WebElement el: elements) {
			if (el.getText().equals(option)) {
				el.click();
				break;
			}
		}
	}

	public void checkFormLabel(String elementName, String label) {
		Assert.assertTrue("Default Account Name label incorrect", driver.findElement(By.xpath("//label[@for='new_form_"+elementName+"']")).getText().equals(label));
	}

	public void checkLabel(String elementName, String label) {
		Assert.assertTrue("Default Account Name label incorrect", driver.findElement(By.xpath("//label[@for='"+elementName+"']")).getText().equals(label));
	}

	public void validateFields(Map<String, String> fields) {
		Iterator<Entry<String, String>> it = fields.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> pairs = it.next();
			checkFormLabel(pairs.getValue(), pairs.getKey());
			it.remove(); // avoids a ConcurrentModificationException
		}
	}

	public void editComponent(String componentName) {
		if (driver.getCurrentUrl().contains(CLASSIC_EDITOR)) {
			try {
				By editBy = By.xpath("//div[text()='"+componentName+"']/../../td/table/tbody/tr/td/em/button[text()='Edit']");
				wait.until(ExpectedConditions.presenceOfElementLocated(editBy));
				WebElement editElement = driver.findElement(editBy);
				wait.until(ExpectedConditions.visibilityOf(editElement));
				editElement.click();
			} catch (NoSuchElementException e) {
				WebElement component = driver.findElement(By.className(componentName));
				ACTIONS.doubleClick(driver, component);
			}
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("x-window-mc")));
			driver.switchTo().activeElement();
		} else {
			try {
				driver.switchTo().activeElement();
				By by = By.xpath("//div[contains(@data-path,'/"+componentName+"')]");
				wait.until(ExpectedConditions.presenceOfElementLocated(by));
				WebElement el = driver.findElement(by);
				try {
					el.click();
					el.click();
					el.click();
					List<WebElement> editBarOptions = driver.findElements(By.xpath("//*[@id='EditableToolbar']/button"));
					for (WebElement el2 : editBarOptions) {
						String actionAttrib = el2.getAttribute("data-action");
						if (actionAttrib != null) {
							if (actionAttrib.equals("EDIT") || actionAttrib.equals("CONFIGURE")) {
								el2.click();
							}
						}
					}
				} catch (Exception e) {
					ACTIONS.doubleClick(driver, el);
				}
			} catch (NoSuchElementException e) {
				Assert.fail("Failed to find component to drag and drop "+componentName+". Error: "+e.getMessage());
			}
		}
	}

	public void selectInlineEditor(String type) {
		By by = By.xpath("//h2[contains(@class,'editor-selector-name') and text()='"+type+"']");
		wait.until(ExpectedConditions.presenceOfElementLocated(by));
		driver.findElement(by).click();
	}

	public void deleteComponent(String componentName) {
		if (driver.getCurrentUrl().contains(CLASSIC_EDITOR)) {
			try {
				By deleteBy = By.xpath("//div[text()='"+componentName+"']/../../td/table/tbody/tr/td/em/button[text()='Delete']");
				wait.until(ExpectedConditions.presenceOfElementLocated(deleteBy));
				WebElement deleteElement = driver.findElement(deleteBy);
				wait.until(ExpectedConditions.visibilityOf(deleteElement));
				deleteElement.click();
			} catch (NoSuchElementException e) {
				WebElement component = driver.findElement(By.className(componentName));
				ACTIONS.contextClick(driver, component);
			}

		} else {
			try {
				driver.switchTo().activeElement();
				List<WebElement> componentTargets = driver.findElements(By.xpath("//*[@id='OverlayWrapper']/div/div")); 
				for (WebElement el : componentTargets) {
					if (el.getAttribute("data-path") != null) {
						if (el.getAttribute("data-path").endsWith("/"+componentName.toLowerCase())) {
							el.click();
							List<WebElement> editBarOptions = driver.findElements(By.xpath("//*[@id='EditableToolbar']/button"));
							for (WebElement el2 : editBarOptions) {
								String actionAttrib = el2.getAttribute("data-action");
								if (actionAttrib != null) {
									if (actionAttrib.equals("DELETE")) {
										el2.click();
										driver.findElement(By.className("cq-deleteconfirm")).click();
									}
								}
							}
							break;
						}
					}
				}
			} catch (NoSuchElementException e) {
				Assert.fail("Failed to delete component");
			}	
		}
	}

	public void checkContentByClass(String className) {
		switchToContent();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.className(className)));
		driver.switchTo().frame(driver.findElement(By.id("ContentFrame")));
		Assert.assertNotNull("Address component not found", driver.findElement(By.className(className)));
		driver.switchTo().defaultContent();
	}

	public void checkContentByXPath(String xpath) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
		driver.switchTo().frame(driver.findElement(By.id("ContentFrame")));
		Assert.assertNotNull("Address component not found", driver.findElement(By.className(xpath)));
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
			Assert.assertTrue(optionText+ " is not a valid option", hasOption);
		}
	}

	public void switchToContent() {
		driver.switchTo().frame(driver.findElement(By.id("ContentFrame")));
	}

	public void confirmDialog() {
		try {
			List<WebElement> elements = driver.findElements(By.xpath("//div[@class='cq-dialog-header-actions']/button"));
			WebElement okButton = null;
			for (WebElement el : elements) {
				if (el.getAttribute("title").equals("Done")) {
					okButton = el;
					break;
				}
			}
			if (okButton == null) {
				okButton = driver.findElement(By.xpath("//button[@title='Done']"));
			}

			driver.switchTo().activeElement();
			okButton.click();
		} catch (Exception e) {
			Assert.fail("Dialog confirm button is not available.");
		}
	}

	public void confirmDialogOld() {
		driver.switchTo().defaultContent();
		driver.findElement(By.xpath("//button[text()='OK']")).click();
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
		//wait.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(sidekickWorkflowLabel)));
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
			Assert.fail("Failed to activate page "+pagePath+". Error: "+e.getMessage());
		}
	}

	public void deactivatePage(String pagePath, TestEnvironment env) {
		try {
			ACTIONS.deactivatePage(pagePath.replace(".html", ""), env);
			Thread.sleep(6000);
		} catch (Exception e) {
			Assert.fail("Failed to deactivate page "+pagePath+". Error: "+e.getMessage());
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
		driver.findElement(By.xpath("//label[text()='"+label+"']/following-sibling::span/button")).click();

		List<WebElement> listElements = driver.findElements(By.xpath("//select/following-sibling::ul/li"));
		for (WebElement el : listElements) {
			if (el.getAttribute("data-value").equals(option)) {
				wait.until(ExpectedConditions.visibilityOf(el));
				el.click();
				break;
			}
		}
	}

	public void moveMouse(int x, int y) {
		ACTIONS.moveMouse(x, y);
	}

	public void closeSuggestions() {
		ACTIONS.hitEscape();
	}

	public void followingSiblingInput(String label, String text) {
		String xpath = "//label[text()='"+label+"']/following-sibling::span/input";
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
		driver.findElement(By.xpath(xpath)).sendKeys(text);
	}

	public void followingSiblingInput(String label, String text, String fallback) {
		String xpath = "//label[text()='"+label+"']/following-sibling::span";
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
			driver.findElement(By.xpath(xpath)).sendKeys(text);
		} catch (Exception e) {
			xpath.replace(label, fallback);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
			driver.findElement(By.xpath(xpath)).sendKeys(text);
		}

	}

	public void validatePageTitle(String newTitle) {
		switchToContent();
		String pageTitleXpath = "//div[@class='title']/h1";
		WebElement element = driver.findElement(By.xpath(pageTitleXpath));
		Assert.assertTrue("Page title does not match Expected: "+newTitle+" Actual: "+element.getText(), element.getText().toLowerCase().equals(newTitle.toLowerCase()));
	}

	public void validateMetaData(String name, String text) {
		try {
			switchToContent();
		} catch (Exception e) {
			// No content frame
		}
		WebElement element = driver.findElement(By.name(name));
		String elementText = element.getAttribute("content");
		Assert.assertTrue("Meta Field "+name+" does not match "+text, elementText.equals(text));
	}

	public AuthorPage addTabs(int numberOfTabs) {
		for (int i=0; i<numberOfTabs; i++) {
			addItemButton.click();
		}
		return new AuthorPage61(driver, wait);
	}

	public AuthorPage fillInMultipleFields(String fieldName, String prefix) {
		List<WebElement> elements = driver.findElements(By.name(fieldName));
		for (int i=0; i<elements.size(); i++) {
			elements.get(i).sendKeys(prefix+i);
		}
		return this;
	}

	public void selectDropDownValue(String option) {
		String xpath = "//img[contains(@class,'x-form-arrow-trigger')]";
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
		WebElement ele = driver.findElement(By.xpath(xpath));
		ele.click();
		List<WebElement> elements = driver.findElements(By.xpath("//div[@class='x-combo-list-item']"));
		for (WebElement el : elements) {
			if (el.getText().equals(option)) {
				el.click();
				break;
			}
		}
	}

}
