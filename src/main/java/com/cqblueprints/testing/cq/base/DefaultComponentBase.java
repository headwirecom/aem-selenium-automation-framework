package com.cqblueprints.testing.cq.base;

import org.apache.http.client.ClientProtocolException;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;

import static com.cqblueprints.testing.cq.base.BaseActions.ACTIONS;

public class DefaultComponentBase extends TestBase {
	
	protected static final String TEST_PAGE_PARENT = "/content/geometrixx/en/";
	protected static final String TEST_PAGE_NAME = "testpage";
	protected static final String TEST_PAGE_TEMPLATE = "/apps/geometrixx/templates/contentpage";
	protected static final String TEST_PAGE = TEST_PAGE_PARENT+TEST_PAGE_NAME+".html"; 	
	protected static final String INBOX_URI = "/inbox";
	protected static final String AEM_6_EDITOR = "/editor.html";
	
	public static final String COMPONENTS_TAB_NAME = "Components";
	
	@Before
	public void startDriver() throws ClientProtocolException, IOException {
		ACTIONS.createPage(TEST_PAGE_NAME, TEST_PAGE_PARENT, TEST_PAGE_TEMPLATE, environment);
		if (environment.getVersion().startsWith("5.")) {
			driver.get(environment.getAuthorUrl()+TEST_PAGE);
		} else {
			driver.get(environment.getAuthorUrl()+AEM_6_EDITOR+TEST_PAGE);
		}
	}
	
	@After
	public void closeDriver() throws ClientProtocolException, IOException {
		ACTIONS.deletePage(TEST_PAGE.replace(".html", ""), environment);
		driver.quit();
	}	
}
