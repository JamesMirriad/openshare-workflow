package com.mirriad.util.properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
/**
 * loads a properties file according to the system specified properties.location path and given file name
 * @author james.mcilroy
 *
 */
public class PropertiesLoader {


	private static final Logger logger = Logger.getLogger(PropertiesLoader.class);
	
	private Properties props;
	
	/**
	 * conststructor to create properties loaded of the system specified properties path
	 * as specified by the "properties.location"
	 * @param propFileName
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public PropertiesLoader(String propFileName) throws FileNotFoundException, IOException {
		String propFileLocation = System.getProperty("properties.location") + "/" + propFileName;
		logger.info("loading properties from: " + propFileLocation);
		props = new Properties();
		props.load(new FileInputStream(propFileLocation));
	}

	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

	/**
	 * get a property.
	 * @param propertyName
	 * @return
	 */
	public String getProperty(String propertyName) {
		if(this.props!=null){
			return props.getProperty(propertyName);
		}
		return null;
	}
}
