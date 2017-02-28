package com.cqblueprints.testing;

import com.cqblueprints.testing.Constants.MouseAction;
import junit.framework.Assert;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class AuthorPageActions {

	private static final int MAX_RETRIES = 10;
	
	public static void dragComponentIntoParsys(WebDriver driver, WebDriverWait wait, String componentName, String parsysName) {
		List<WebElement> cards = driver.findElements(By.className("card-component"));
		for (WebElement card : cards) {
			if (card.getAttribute("data-group").equals(".hidden")) {
				continue;
			}
			wait.until(ExpectedConditions.visibilityOf(card));
			break;
		}
		
		List<WebElement> componentCards = driver
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
		List<WebElement> dropTargets = driver.findElements(By
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

		PageUtil.dragDrop(driver, wait, elText, parsys);
	}	
	
	public static void selectSidePanelTab(WebDriver driver, WebDriverWait wait, String tabName) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By
					.xpath("//a[@class='coral-TabPanel-tab' and text()='"
							+ tabName + "']")));
		} catch (Exception e) {
			AuthorPageActions.toggleSidePanel(driver, wait);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By
					.xpath("//a[@class='coral-TabPanel-tab' and text()='"
							+ tabName + "']")));
		}
		List<WebElement> sidePanelTabs = driver.findElements(By.className("coral-TabPanel-tab"));
		for (WebElement el : sidePanelTabs) {
			if (el.getText().equals(tabName)) {
				el.click();
			}
		}
	}
	
	public static void toggleSidePanel(WebDriver driver, WebDriverWait wait) {
		closePopover(driver, wait);
		WebElement sidePanelToggle = driver.findElement(By.xpath("//button[@title='Toggle Side Panel']"));
		wait.until(ExpectedConditions.visibilityOf(sidePanelToggle));
		sidePanelToggle.click();
	}
	
	public static void editComponent(WebDriver driver, WebDriverWait wait, String componentName) {
		closePopover(driver, wait);
		try {
			driver.switchTo().activeElement();
			By by = By.xpath("//div[contains(@data-path,'/" + componentName
					+ "')]");
			wait.until(ExpectedConditions.presenceOfElementLocated(by));
			WebElement el = driver.findElement(by);
			try {
				Thread.sleep(500); 
				el.click();
				el.click();
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
				PageUtil.doubleClick(driver, el);
			}
			

		} catch (NoSuchElementException e) {
			Assert.fail("Failed to find component to edit "
					+ componentName + ". Error: " + e.getMessage());
		}
	}
	
	public static void closePopover(WebDriver driver, WebDriverWait wait) {
		try {
			driver.findElement(By.xpath("//div[contains(@class,'cq-Tour')]"));
			By popoverBy = By.xpath("//button[contains(@class,'cq-Tour-skipTourButton')]");
			wait.until(ExpectedConditions.visibilityOfElementLocated(popoverBy));
			WebElement popEl = driver.findElement(popoverBy);
			popEl.click();
			int retries = 0;
			while (driver.findElement(By.id("skipPopover")).getAttribute("style").contains("display: none;") && retries < 5) {
				popEl.click();
				retries++;
			}
			By skipBy = By.xpath("//a[contains(@class,'cq-Tour-dontShowAgainButton')]");
			wait.until(ExpectedConditions.visibilityOfElementLocated(skipBy));
			driver.findElement(skipBy).click();
		} catch (Exception e) {
			// No popover
		}
	}
	
	public static void elementHighlight(WebDriver driver, WebElement element) {
		for (int i = 0; i < 2; i++) {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(
					"arguments[0].setAttribute('style', arguments[1]);",
					element, "color: red; border: 3px solid red;");
			js.executeScript(
					"arguments[0].setAttribute('style', arguments[1]);",
					element, "");
		}
	}
	
	public static void editComponent(WebDriver driver, WebDriverWait wait, String componentName, MouseAction mouseAction) {
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
				PageUtil.doubleClick(driver, el);
				break;
			}
			Thread.sleep(1000);
		} catch (Exception e) {
			Assert.fail("Failed to find component to edit "
					+ componentName + ". Error: " + e.getMessage());
		}
	}
	
	public static void input(WebDriver driver, WebDriverWait wait, String elementName, String text) {
		int maxRetries = 5;
		int retries = 0;
		String inputString = "";
		try {
			Thread.sleep(500);
			while (!inputString.equals(text) && retries < maxRetries) {
				By fieldBy = By.name(elementName);
				wait.until(ExpectedConditions.presenceOfElementLocated(fieldBy));
				List<WebElement> fields = driver.findElements(fieldBy);
				WebElement field = null;
				for (WebElement f : fields) {
					if (f.getAttribute("type") == null || !f.getAttribute("type").equals("hidden")) {
						field = f;
						break;
					} else if (f.getAttribute("type") != null && f.getAttribute("type").equals("hidden")) {
						WebElement rteEl = getRte(driver, wait, elementName);
						if (rteEl != null) {
							while (!inputString.equals(text) && retries < maxRetries) {
								Actions actions = new Actions(driver);
								actions.moveToElement(rteEl).moveByOffset(10, 10).perform();
								JavascriptExecutor jse = (JavascriptExecutor)driver;
								jse.executeScript("arguments[0].click();", rteEl);
								Thread.sleep(1500);
								inputString = rteEl.getText();
								rteEl.clear();
								if (!StringUtils.isEmpty(rteEl.getText())) {
									String selectall;
									String os = System.getProperty("os.name");
									if (os.equals("WINDOWS")){
									   selectall = Keys.chord(Keys.CONTROL, "a");
									}else{
									   selectall = Keys.chord(Keys.COMMAND, "a");
									}
									rteEl.sendKeys(selectall);
									rteEl.sendKeys(Keys.BACK_SPACE);
								}
								rteEl.sendKeys(text);
								inputString = rteEl.getText();
								System.out.println(inputString);
								retries++;
								rteEl = getRte(driver, wait, elementName);
							}
							Assert.assertEquals(inputString, text);
							return;
						}
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
					+ elementName + ". Error message: " + e.getMessage());
		}

	}
	
	public static void input(WebDriver driver, WebDriverWait wait, String elementName, String text, int num) {
		int maxRetries = 5;
		int retries = 0;
		String inputString = "";
		try {
			while (!inputString.equals(text) && retries < maxRetries) {
				By fieldBy = By.name(elementName);
				wait.until(ExpectedConditions.presenceOfElementLocated(fieldBy));
				List<WebElement> fields = driver.findElements(fieldBy);
				WebElement field = null;
				int i = 0;
				for (WebElement f : fields) {
					if (f.getAttribute("type") == null || !f.getAttribute("type").equals("hidden") && i >= num) {
						field = f;
						break;
					} else if (f.getAttribute("type") != null && f.getAttribute("type").equals("hidden")) {
						WebElement rteEl = getRte(driver, wait, elementName);
						if (rteEl != null) {
							while (!inputString.equals(text) && retries < maxRetries) {
								Actions actions = new Actions(driver);
								actions.moveToElement(rteEl).moveByOffset(10, 10).click().perform();
								Thread.sleep(1500);
								inputString = rteEl.getText();
								rteEl.clear();
								if (!StringUtils.isEmpty(rteEl.getText())) {
									String selectall;
									String os = System.getProperty("os.name");
									if (os.equals("WINDOWS")){
									   selectall = Keys.chord(Keys.CONTROL, "a");
									}else{
									   selectall = Keys.chord(Keys.COMMAND, "a");
									}
									rteEl.sendKeys(selectall);
									rteEl.sendKeys(Keys.BACK_SPACE);
								}
								rteEl.sendKeys(text);
								inputString = rteEl.getText();
								System.out.println(inputString);
								retries++;
								rteEl = getRte(driver, wait, elementName);
							}
							Assert.assertEquals(inputString, text);
							return;
						}
					}
					i++;
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
					+ elementName + ". Error message: " + e.getMessage());
		}

	}
	
	public static void input(WebDriver driver, WebDriverWait wait, By fieldBy, String text) {
		int maxRetries = 5;
		int retries = 0;
		String inputString = "";
		try {
			while (!inputString.equals(text) && retries < maxRetries) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(fieldBy));
				List<WebElement> fields = driver.findElements(fieldBy);
				WebElement field = null;
				for (WebElement f : fields) {
					System.out.println(f.getSize());
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
					+ fieldBy.toString() + ". Error message: " + e.getMessage());
		}
	}
	
	/**
	 * @param driver
	 * @param wait
	 * @param elementName
	 * @param text
	 */
	public static void select(WebDriver driver, WebDriverWait wait, String elementName, String text) {
		int i = 0;
		String elText = "";
		if (elementName != null) {
			while (i++ < MAX_RETRIES) {
				By by = By.name(elementName);
				wait.until(ExpectedConditions.visibilityOfElementLocated(by));
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {}
				driver.findElement(by).click();
				By selectBy = By.xpath("//select[@name='"+elementName+"']/following-sibling::ul/li[@data-value='"+text+"']");
				wait.until(ExpectedConditions.visibilityOfElementLocated(selectBy));
				JavascriptExecutor jse = (JavascriptExecutor)driver;
				jse.executeScript("arguments[0].click();", driver.findElement(selectBy));
				String valText = driver.findElement(By.xpath("//option[@value='"+text+"']")).getText();
				elText = driver.findElement(By.xpath("//select[@name='"+elementName+"']/preceding-sibling::button")).getText();
				if (elText.equals(valText)) {
					break;
				}
				try {
					Thread.sleep(300);
				} catch (Exception e) {
					// Swallow
				}
			}
		} else {
			By selectBy = By.xpath("//li[@data-value='"+text+"']");
			wait.until(ExpectedConditions.presenceOfElementLocated(selectBy));
			JavascriptExecutor executor = (JavascriptExecutor)driver;
			executor.executeScript("arguments[0].click();", driver.findElement(selectBy));
		}
	}
	
	private static WebElement getRte(WebDriver driver, WebDriverWait wait, String elementName) {
		try {
			WebElement rteFrame = driver.findElement(By.xpath(
					"//input[@name='"+elementName+"']/following-sibling::div[contains(@class,'coral-RichText-editable')]"
					));
			System.out.println("Found rte frame");
			System.out.println(rteFrame.getAttribute("data-config-path"));
			return rteFrame;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static void addField(WebDriver driver, WebDriverWait wait, String fieldName) {
		By by =  By.xpath("//label[text()='"+fieldName+"']/following-sibling::div[@data-init='multifield']/button[contains(@class,'coral-Multifield-add')]");
		wait.until(ExpectedConditions.presenceOfElementLocated(by));
		WebElement element = driver.findElement(by);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("var evt = document.createEvent('MouseEvents');" + "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);" + "arguments[0].dispatchEvent(evt);", element);
	}
	
	public static void confirmDialog(WebDriver driver, WebDriverWait wait) {
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
	
	public static ExpectedCondition<Boolean> elementHasStoppedMoving(final WebElement element) {
	    return new ExpectedCondition<Boolean>() {
	        public Boolean apply(WebDriver driver) {
	            Point initialLocation = ((Locatable) element).getCoordinates().inViewPort();
	            try {
					Thread.sleep(50);
				} catch (InterruptedException e) {}
	            Point finalLocation = ((Locatable) element).getCoordinates().inViewPort();
	            return initialLocation.equals(finalLocation);
	        }
	    };
	}
	
	public static void selectDialogTab(WebDriver driver, WebDriverWait wait, String title) {
		driver.switchTo().activeElement();
		By tabBy = By
				.xpath("//nav[@class='coral-TabPanel-navigation']/a[text()='"
						+ title + "']");
		wait.until(ExpectedConditions.visibilityOfElementLocated(tabBy));
		jsClick(driver.findElement(tabBy), driver);
	}
	
	public static void jsClick(WebElement element, WebDriver driver) {
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click();", element);
	}
}

