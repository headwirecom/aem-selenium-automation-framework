package com.cqblueprints.testing.cq.pageobjects.impl;

import com.cqblueprints.testing.cq.pageobjects.AuthorPage;
import junit.framework.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.cqblueprints.testing.cq.base.BaseActions.ACTIONS;

/**
 * Provides helper methods to perform actions on WCM author pages
 * in AEM 6.0
 * 
 * @author <a href="mailto:dsamarjian@gmail.com">Diran Samarjian</a>
 */
public class AuthorPage63 extends BaseAuthorPage {


	public AuthorPage63(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
		this.driver = driver;
		this.wait = wait;
	}

	public AuthorPage selectSidePanelTab(String tabName) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By
					.xpath("//coral-tab[@title='"
							+ tabName + "']/coral-icon")));
		} catch (Exception e) {
			toggleSidePanel();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By
					.xpath("//coral-tab[@title='"
							+ tabName + "']/coral-icon")));
		}
		driver.findElement(By.xpath("//coral-tab[@title='"+tabName+"']/coral-icon")).click();
		return this;
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
					.xpath("//coral-columnview-item[@data-title='"+componentName+"']")));
			WebElement element = driver.findElement(By.xpath("//coral-columnview-item[@data-title='"+componentName+"']"));

			if (element == null) {
				Assert.fail(componentName + " not found in sidepanel.");
			}
			((Locatable) element).getCoordinates().inViewPort();
			WebElement parEl = driver.findElement(By
					.xpath("//*[@id='OverlayWrapper']/div/div[contains(@data-path, '/"+parsysName+"')]")); // .findElements(By.xpath("//div[@class='cq-droptarget']"));
			System.out.println(parEl);
			//wait.until(ExpectedConditions.visibilityOf(parEl));

			ACTIONS.dragDrop(driver, wait, element, parEl);
		}
	}

	public void editComponent(String componentName) {
		System.out.println("Editing Component: " + componentName);
		if (!driver.getCurrentUrl().contains(AEM_EDITOR)) {
			editComponentClassicUI(componentName);
		} else {
			try {
				driver.switchTo().activeElement();
				By by = By.xpath("//div[contains(@data-path,'/" + componentName
						+ "')]");
				System.out.println("Begin waiting for visibility");
				wait.until(ExpectedConditions.visibilityOfElementLocated(by));
				System.out.println("Visible");
				WebElement el = driver.findElement(by);
				try {
					System.out.println("Sleep 300ms");
					Thread.sleep(300);
					System.out.println("Slept 300ms");
					el.click();
					List<WebElement> editBarOptions = driver.findElements(By
							.xpath("//*[@id='EditableToolbar']/button"));
					//wait.until(ExpectedConditions.visibilityOfAllElements(editBarOptions));
					for (WebElement el2 : editBarOptions) {
						String actionAttrib = el2.getAttribute("data-action");
						if (actionAttrib != null) {
							if (actionAttrib.equals("EDIT")
									|| actionAttrib.equals("CONFIGURE")) {
								el2.click();
								break;
							}
						}
					}
				} catch (Exception e) {
					System.out.println("Edit bar click failed, trying doubleclick");
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
				List<WebElement> elements = driver.findElements(By.xpath("//coral-dialog-content/iframe"));
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

	public void confirmDialog() {
		if (!driver.getCurrentUrl().contains(AEM_EDITOR)) {
			confirmDialogOld();
			return;
		}
		try {
			driver.switchTo().defaultContent();
			WebElement okButton = driver.findElement(By
						.xpath("//button[@title='Done']"));

			driver.switchTo().activeElement();
			okButton.click();
		} catch (Exception e) {
			Assert.fail("Dialog confirm button is not available."+ e.getMessage());
		}
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
				e.printStackTrace();
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
		System.out.println("Before switch to active element");
		driver.switchTo().activeElement();
		System.out.println("After switch to active element");
		List<WebElement> componentTargets = driver.findElements(By
				.xpath("//*[@id='OverlayWrapper']/div/div"));
		System.out.println("After finding component targets");
		for (WebElement el : componentTargets) {
			System.out.println("Start loop");
			if (el.getAttribute("data-path") != null) {
				System.out.println("In first if");
				if (el.getAttribute("data-path").endsWith(
						"/" + componentName.toLowerCase())) {
					System.out.println("In second if");
					el.click();
					System.out.println("After el.click()");
					List<WebElement> editBarOptions = driver
							.findElements(By
									.xpath("//*[@id='EditableToolbar']/button"));
					System.out.println("After find buttons");
					for (WebElement el2 : editBarOptions) {
						System.out.println("Start second loop");
						String actionAttrib = el2
								.getAttribute("data-action");
						if (actionAttrib != null) {
							System.out.println("In third if");
							if (actionAttrib.equals("DELETE")) {
								System.out.println("In fourth if");
								el2.click();
								System.out.println("After el2.click()");
								driver.switchTo().defaultContent();
								try
								{
									Thread.sleep(200);
								}
								catch(Exception e)
								{

								}
								driver.switchTo().activeElement();
								try
								{
									Thread.sleep(200);
								}
								catch(Exception e)
								{

								}
								System.out.println("After second switch to active element");
								WebElement deleteConfirmButton = driver.findElement(
										By.xpath("//coral-button-label[text()='Delete']"));
								System.out.println("Delete confirm button: " + deleteConfirmButton.getText());
								deleteConfirmButton.click();
							}
						}
					}
					break;
				}
			}
		}
		driver.switchTo().defaultContent();
	}

	public void fillInDialogFieldByName(String name, String text) {
		System.out.println("6-3 Filling in "+ name + " field with text " + text);
		int maxRetries = 5;
		int retries = 0;
		String inputString = "";
		try {
			while (!inputString.equals(text) && retries < maxRetries) {
				System.out.println("6-3 Try " + (retries + 1) + "/" + maxRetries);
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
				System.out.println("6-3 Sleeping 500ms");
				Thread.sleep(500);
				System.out.println("6-3 Slept 500ms");
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

	public AuthorPage addTabs(int numberOfTabs) {
		for (int i = 0; i < numberOfTabs; i++) {
			String curUrl = driver.getCurrentUrl();

				try {
					By okBy = By
							.xpath("//button[contains(@class,'cq-multifield-add')]");
					wait.until(ExpectedConditions
							.visibilityOfElementLocated(okBy));
					driver.findElement(okBy).click();
				} catch (Exception e) {
					By okBy = By.xpath("//button[text()='+']");
					wait.until(ExpectedConditions
							.visibilityOfElementLocated(okBy));
					driver.findElement(okBy).click();
				}
		}
		return new BaseAuthorPage(driver, wait);
	}

	public void selectDialogTab(String tabName) {
		try {
			driver.switchTo().activeElement();
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
		try
		{
			driver.switchTo().activeElement();
			By tabBy = By
					.xpath("//nav[@class='coral-TabPanel-navigation']/a[text()='"
							+ tabName + "']");
			wait.until(ExpectedConditions.visibilityOfElementLocated(tabBy));
			driver.findElement(tabBy).click();
		}
		catch(Exception e)
		{
			selectCoralDialogTab(tabName);
		}

	}

	private void selectCoralDialogTab(String tabName)
	{
		driver.switchTo().activeElement();
		By tabBy = By.xpath("//coral-tab-label[text()='" + tabName + "']");
		WebElement tab = wait.until(ExpectedConditions.visibilityOfElementLocated(tabBy));
		tab.click();
	}

	public void selectInlineEditor(String type) {
		try {
			By by = By
					.xpath("//div[contains(@class,'is-edited')]");
			WebElement el = driver.findElement(by);
			el.clear();
			el.sendKeys(type);
		} catch (Exception e) {
			switchToContent();
			By by = By.xpath("//*[@contenteditable]");
			WebElement el = driver.findElement(by);
			el.clear();
			el.sendKeys(type);
		}
	}

	public void closeInlineEditor() {
		ACTIONS.hitEnter();
	}
}
