import com.cqblueprints.testing.cq.base.TestBase;
import com.cqblueprints.testing.cq.factory.FactoryProducer;
import com.cqblueprints.testing.cq.pageobjects.SiteAdminPage;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by headwire on 4/12/2017.
 */
public class SiteAdminTest extends TestBase {

    private static final Logger LOG = LoggerFactory.getLogger(SiteAdminTest.class);

    public static final String TEST_PAGE_NAME = "test-page-01";
    public static final String THUMBNAIL_PATH = "/apps/aem-selenium-basic-project/templates/basic/thumbnail.png";
    public static final String PROJECT_CONTENT_PATH = "/content/aem-selenium-basic-project";

    /**
     * Tests the following methods of SiteAdminPage interface:
     * <ul>
     *     <li>openSitesPage</li>
     *     <li>navigateToPage</li>
     *     <li>createPageWithTemplate</li>
     * </ul>
     */
    public static void testPageCreation()
    {
        LOG.info("Beginning page creation test");
        SiteAdminPage siteAdminPage = FactoryProducer.getPageFactory().getSiteAdminPage(driver, wait, environment.getVersion());
        siteAdminPage.openSitesPage();
        siteAdminPage.navigateToPage(PROJECT_CONTENT_PATH);
        siteAdminPage.createPageWithTemplate(THUMBNAIL_PATH, TEST_PAGE_NAME);
        driver.get(environment.getAuthorUrl()+"/editor.html"+PROJECT_CONTENT_PATH+"/"+TEST_PAGE_NAME+".html");
        LOG.info("Completed page creation test");
    }
}
