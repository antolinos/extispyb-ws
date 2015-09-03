package extispyb.core.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ExiSaxsProperties {
	private static ExiSaxsProperties instance = null;
	private Properties properties;

	protected ExiSaxsProperties() {
		this.properties = new Properties();
		String propFileName = "exisaxs.properties";
		InputStream input = getClass().getClassLoader().getResourceAsStream(propFileName);
		try {
			this.properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ExiSaxsProperties getInstance() {
		if (instance == null) {
			instance = new ExiSaxsProperties();
		}
		return instance;
	}

	public Properties getProperties() {
		return properties;
	}
}
