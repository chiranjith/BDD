package mtaf.concordion.concordiontests;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.After;
import org.junit.Before;

import com.mphasis.automation.GlobalInit;
import com.mphasis.automation.MTAFCore;
import com.mphasis.automation.MTAFTestSetup;
import com.mphasis.automation.ApplicationInterface.DriverFunctions;

public class TestSetup extends GlobalInit{

	private DriverFunctions execEngine = MTAFTestSetup.getInstance().execEngine;
	public MTAFCore mtafSingleton = MTAFTestSetup.getInstance().mtaf;
	public MTAFCore mtaf = new MTAFCore();
	protected static String commonDataProperties = "CommonData.properties";
	static Configuration configuration;

	@Before
	public void StartBrowser() {

		try {

			configuration = new PropertiesConfiguration(commonDataProperties);
			execEngine.navigateToUrl(
					configuration.getString("application.url"), null, 0);
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@After
	public void tearDown() {

	}
}
