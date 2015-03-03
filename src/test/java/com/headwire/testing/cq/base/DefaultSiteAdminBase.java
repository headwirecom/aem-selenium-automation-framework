package com.headwire.testing.cq.base;

import static com.headwire.testing.cq.base.BaseActions.ACTIONS;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.junit.After;
import org.junit.Before;

import com.headwire.testing.cq.factory.FactoryProducer;
import com.headwire.testing.cq.factory.SiteAdminPage;

public class DefaultSiteAdminBase extends TestBase {
	public static SiteAdminPage siteAdminPage;
	protected static final String TEST_PAGE_PARENT = "/content/geometrixx/en/";
	protected static final String TEST_PAGE_NAME = "testpage";
	protected static final String TEST_PAGE_TEMPLATE = "/apps/geometrixx/templates/contentpage";
	protected static final String TEST_PAGE = TEST_PAGE_PARENT+TEST_PAGE_NAME+".html"; 			//"/content/geometrixx/en/events/dsc.html";
	protected static final String INBOX_URI = "/inbox";
	protected static final String AEM_6_EDITOR = "/editor.html";
	
	public static final String COMPONENTS_TAB_NAME = "Components";
	
	@Before
	public void startDriver() throws ClientProtocolException, IOException {
		ACTIONS.createPage(TEST_PAGE_NAME, TEST_PAGE_PARENT, TEST_PAGE_TEMPLATE, environment);
		driver.get(environment.getAuthorUrl()+"/sites.html"+TEST_PAGE_PARENT.substring(0, TEST_PAGE_PARENT.length()-1));
		siteAdminPage = FactoryProducer.getPageFactory().getSiteAdminPage(driver, wait, environment.getVersion());
	}
	
	
	@After
	public void closeDriver() throws ClientProtocolException, IOException {
		ACTIONS.deletePage(TEST_PAGE.replace(".html", ""), environment);
	}	
}
