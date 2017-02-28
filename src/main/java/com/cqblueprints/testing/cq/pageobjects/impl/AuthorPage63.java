package com.cqblueprints.testing.cq.pageobjects.impl;

import com.cqblueprints.testing.cq.pageobjects.AuthorPage;
import junit.framework.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

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
					Thread.sleep(300);
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
					ACTIONS.doubleClick(driver, el);
				}

			} catch (NoSuchElementException e) {
				Assert.fail("Failed to find component to drag and drop "
						+ componentName + ". Error: " + e.getMessage());
			}
			try {
				Thread.sleep(300);
				WebElement dialogFrame = driver.findElement(By.xpath("//coral-dialog-content/iframe"));
				driver.switchTo().frame(dialogFrame);
			} catch (Exception e) {
				// non-iframe dialog
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
										By.xpath("//coral-button-label[text()='Delete']"))
										.click();
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
				}
			}
		} catch (Exception e) {
			driver.switchTo().activeElement();
			By tabBy = By
					.xpath("//nav[@class='coral-TabPanel-navigation']/a[text()='"
							+ tabName + "']");
			wait.until(ExpectedConditions.visibilityOfElementLocated(tabBy));
			driver.findElement(tabBy).click();
		}
	}

	public void selectInlineEditor(String type) {
		try {
			By by = By
					.xpath("//div[contains(@class,'is-edited')]");
			WebElement el = driver.findElement(by);
			el.clear();
			el.sendKeys(type);
		} catch (Exception e) {
			By by = By.xpath("//*[@contenteditable]");
			WebElement el = driver.findElement(by);
			el.clear();
			el.sendKeys(type);
		}
	}

	public void closeInlineEditor() {
		try {
			clickBy(By.xpath("//button[@data-action='control#save']"));
		} catch (Exception e) {
			ACTIONS.hitEnter();
		}
	}
}
