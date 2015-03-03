package com.headwire.testing.cq.base;

import static com.headwire.testing.cq.base.BaseActions.ACTIONS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.http.client.ClientProtocolException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.headwire.testing.cq.factory.AuthorPage;
import com.headwire.testing.cq.factory.FactoryProducer;
import com.headwire.testing.cq.pageobjects.SiteAdminPage60;

public class DefaultBootstrapComponentBase extends TestBase {

	protected static final String TEST_PAGE_PARENT = "/content/bootstrap_demo/en/";
	protected static final String TEST_PAGE_NAME = "testpage";
	protected static final String TEST_PAGE_TEMPLATE = "/etc/designs/bootstrap/templates/oneColumnPage";
	protected static final String TEST_PAGE = TEST_PAGE_PARENT + TEST_PAGE_NAME
			+ ".html";
	protected static final String INBOX_URI = "/inbox";
	protected static final String AEM_6_EDITOR = "/editor.html";

	public static final String COMPONENTS_TAB_NAME = "Components";

	@Before
	public void startDriver() throws ClientProtocolException, IOException {
		ACTIONS.createPage(TEST_PAGE_NAME, TEST_PAGE_PARENT,
				TEST_PAGE_TEMPLATE, environment);
		driver.get(environment.getAuthorUrl() + AEM_6_EDITOR + TEST_PAGE);
		wait.until(ExpectedConditions.presenceOfElementLocated(By
				.id("ContentFrame")));
	}

	@Test
	public void testFooterLinks() throws Exception {
		AuthorPage authorPage = FactoryProducer.getPageFactory().getAuthorPage(driver, wait, environment.getVersion());
		authorPage.editComponent("footer");
		authorPage.switchToOldDialogIframe();
		String rootPage = authorPage.getTextFromInput(By.name("./navRoot"));

		driver.get(environment.getAuthorUrl() + "/sites.html" + rootPage);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'foundation-collection-container')]")));
		SiteAdminPage60 adminPage = new SiteAdminPage60(driver, wait);
		List<WebElement> pages = adminPage.getElements(By.xpath("//article[@itemprop='item']"));
		List<String> pageUris = new ArrayList<String>();
		for (WebElement page : pages) {
			String uri = page.getAttribute("data-path");
			pageUris.add(uri);
		}
		
		driver.get(environment.getAuthorUrl() + AEM_6_EDITOR + TEST_PAGE);
		authorPage = FactoryProducer.getPageFactory().getAuthorPage(driver, wait, environment.getVersion());
		authorPage.switchToContent();
		List<WebElement> navigationLinks = authorPage.getElements(By
				.xpath("//ul[@class='nav nav-pills']/li[@role='presentation']/a"));

		System.out.println("Page count: "+pages.size());
		for (WebElement link : navigationLinks) {
			String linkHref = link.getAttribute("href");
			System.out.println("link"+linkHref);
			boolean foundLink = false;
			for (String uri : pageUris) {
				if (uri != null) {
					if (linkHref != null & linkHref.endsWith(uri + ".html")) {
						foundLink = true;
						break;
					}
				}
			}
			Assert.assertTrue(linkHref + " link not found", foundLink);
		}

	}

	@After
	public void closeDriver() throws ClientProtocolException, IOException {
		ACTIONS.deletePage(TEST_PAGE.replace(".html", ""), environment);
	}
}
