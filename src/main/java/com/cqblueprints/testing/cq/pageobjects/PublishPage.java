package com.cqblueprints.testing.cq.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Provides helper methods to perform actions on publish environment pages
 * 
 * @author <a href="mailto:dsamarjian@gmail.com">Diran Samarjian</a>
 */
public interface PublishPage {

	/**
	 * Validates text exists using an html tag
	 * @param by      Selenium selector for the element
	 * @param text    Text to validate
	 */
	public void validateContentByTag(By by, String text);

	/**
	 * Validates an image attribute
	 * 
	 * @param attribute  The attribute to test
	 * @param text       The expected value of the attribute
	 */
	public void validateImageAttribute(String attribute, String text);

	/**
	 * Shuts down the active publish driver
	 */
	public void closeDriver();

	/**
	 * Tests if the current page is not a 404 page
	 * @param newTitle Expected title of the page
	 */
	public void isNot404(String newTitle);

	/**
	 * Tests if the current page is a 404 page
	 * @param newTitle Expected title of the page
	 */
	public void is404(String newTitle);

	/**
	 * Tests if the current page does not return a 200 status code
	 * @return boolean true for a non-200 page, false otherwise
	 */
	public boolean is404();
	
	/**
	 * Refreshes the current page
	 */
	public void refresh();
	
	/**
	 * Extracts text from an html input field
	 * @param by The selenium selector of the element to extract text from
	 * @return String value of the input fields text
	 */
	public String getTextFromInput(By by);
	
	/**
	 * Extracts text from an html input field
	 * @param ele The element to extract text from
	 * @return String value of the input fields text
	 */
	public String getTextFromInput(WebElement ele);
	
	/**
	 * Waits for and finds a list of elements by their selenium selector
	 * @param by Selenium selector of the elements
	 * @return List of all the found elements
	 */
	public List<WebElement> getElements(By by);
	
	/**
	 * Extracts text from an element
	 * @param by The selenium selector of the element to extract text from
	 * @return String value of the elements text
	 */
	public String getTextFromElement(By by);
	
	/**
	 * Waits for an element and clicks it.&nbsp;Found by a selenium selector
	 * 
	 * @param by The selenium selector of the element
	 */
	public void clickBy(By by);
	
	/**
	 * Tests the breadcrumb of a page to validate it matches the content hierarchy
	 */
	public void validateBreadcrumbs();
	
	/**
	 * Tests the width and height of a component
	 * 
	 * @param parsysName Parent parsys of the element
	 * @param width      The expected width
	 * @param height     The expected height
	 */
	public void checkWidthAndHeightOfComponent(String parsysName, String width, String height);
	
	/**
	 * Validate an element exists by a selenium selector
	 *
	 * @param by selenium selector of the target element
	 */
	public void assertExists(By by);

	/**
	 * Validate an element does not exist by a selenium selector
	 *
	 * @param by selenium selector of the target element
	 */
	public void assertNotExists(By by);
	
	/**
	 * Validate an element has the specified text by a selenium selector
	 * 
	 * @param by selenium selector of the target element
	 * @param text the expected text value
	 */
	public void assertText(By by, String text);

	/**
	 * Assert that an element is visible by a selenium selector
	 *
	 * @param by selenium selector of the target element
	 * @param visible  true if element should be visible, false if hidden
	 */
	public void assertVisibility(By by, boolean visible);

	/**
	 * Validate a link created using the List component
	 * 
	 * @param s The link text to search for
	 */
	public void checkLinkByText(String s);
	
	/**
	 * Validate a link has the specified text by a selenium selector
	 * 
	 * @param link The link text to search for
	 */
	public void assertLinkText(String link);

	/**
	 * Switches to an iframe based on a selenium selector
	 * @param by The selector to use to locate the iframe
	 */
	public void switchToFrame(By by);
}
